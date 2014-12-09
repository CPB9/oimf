package com.cpb9.oimf.codegen.sql;

import com.cpb9.oimf.codegen.CodeGenerationException;
import com.cpb9.oimf.model.*;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class OimfSqlGenerator
{
	@NotNull
	private final OimfSqlGeneratorConfiguration config;
	@NotNull
	private final Set<ImmutableOimfTrait> traits;

	public OimfSqlGenerator(@NotNull OimfSqlGeneratorConfiguration config, @NotNull Set<ImmutableOimfTrait> traits)
	{
		this.config = config;
		this.traits = traits;
	}

	public void generate()
	{
		try(Connection connection = DriverManager.getConnection("jdbc:sqlite:" + config.getOutputFile().getAbsolutePath()))
		{
			connection.setAutoCommit(false);
			for (ImmutableOimfTrait trait : traits)
			{
				generateTraitHeader(trait, connection);
			}
			Set<ImmutableOimfTraitApplication> traitApplications = new HashSet<>();
			collectTraitApplications(traits, traitApplications);
			Map<ImmutableOimfTraitApplication, Long> rowIdByTraitApplication = createTraitApplicationHeaders(traitApplications, connection);
			insertTraitApplicationArguments(rowIdByTraitApplication, connection);
			generateTraitInternals(traits, rowIdByTraitApplication, connection);
			connection.commit();
		}
		catch (SQLException | IOException e)
		{
			throw new CodeGenerationException(e);
		}
	}

	private void insertTraitApplicationArguments(
			@NotNull Map<ImmutableOimfTraitApplication, Long> rowIdByTraitApplication, @NotNull Connection connection) throws SQLException
	{
		for (Map.Entry<ImmutableOimfTraitApplication, Long> entry : rowIdByTraitApplication.entrySet())
		{
			PreparedStatement insertArg = connection.prepareStatement(
					String.format("INSERT INTO %s (trait_application_id, argument_index, argument) VALUES (?, ?, ?)",
							TableNames.TRAIT_APPLICATION_ARGUMENT));
			ImmutableOimfTraitApplication traitApplication = entry.getKey();
			long index = 0;
			for (ImmutableOimfTraitApplication argument : traitApplication.getArguments())
			{
				insertArg.setLong(1, entry.getValue());
				insertArg.setLong(2, index++);
				insertArg.setLong(3, rowIdByTraitApplication.get(argument));
				insertArg.execute();
			}
		}
	}

	private Map<ImmutableOimfTraitApplication, Long> createTraitApplicationHeaders(
			@NotNull Set<ImmutableOimfTraitApplication> traitApplications, @NotNull Connection connection)
			throws SQLException
	{
		Map<ImmutableOimfTraitApplication, Long> result = new HashMap<>();
		PreparedStatement insertTraitApp = connection
				.prepareStatement(String.format("INSERT INTO %s (name) VALUES (?)", TableNames.TRAIT_APPLICATION));
		for (ImmutableOimfTraitApplication traitApplication : traitApplications)
		{
			insertTraitApp.setString(1, traitApplication.getName().toString());
			insertTraitApp.execute();
			result.put(traitApplication, insertTraitApp.getGeneratedKeys().getLong(1));
		}
		return result;
	}

	private static void generateTraitInternals(@NotNull Set<ImmutableOimfTrait> traits,
	                                           @NotNull Map<ImmutableOimfTraitApplication, Long> rowIdByTraitApplication,
	                                           @NotNull Connection connection)
			throws IOException, SQLException
	{
		Statement selectTraitId = connection.createStatement();
		for (ImmutableOimfTrait trait : traits)
		{
			Long traitId = selectTraitId.executeQuery(
					String.format("SELECT id FROM %s WHERE guid = \"%s\"", TableNames.TRAIT,
							trait.getGuid().toString())).getLong(1);
			PreparedStatement insertExtend = connection.prepareStatement(
					String.format("INSERT INTO %s (trait_id, extend_index, trait_application_id) VALUES (?, ?, ?)", TableNames.TRAIT_EXTEND));
			long index = 0;
			for (ImmutableOimfTraitApplication extend : trait.getExtends())
			{
				insertExtend.setLong(1, traitId);
				insertExtend.setLong(2, index++);
				insertExtend.setLong(3, rowIdByTraitApplication.get(extend));
				insertExtend.execute();
			}
			PreparedStatement insertField = connection.prepareStatement(
					String.format("INSERT INTO %s (trait_id, field_index, type, name) VALUES (?, ?, ?, ?)",
							TableNames.TRAIT_FIELD));
			index = 0;
			for (ImmutableOimfField field : trait.getFields())
			{
				insertField.setLong(1, traitId);
				insertField.setLong(2, index++);
				insertField.setLong(3, rowIdByTraitApplication.get(field.getType()));
				insertField.setString(4, field.getName());
				insertField.execute();
			}
			PreparedStatement insertMethod = connection.prepareStatement(
					String.format("INSERT INTO %s (trait_id, method_index, return_type, name) VALUES (?, ?, ?, ?)", TableNames.TRAIT_METHOD));
			index = 0;
			for (ImmutableOimfMethod method : trait.getMethods())
			{
				insertMethod.setLong(1, traitId);
				insertMethod.setLong(2, index++);
				insertMethod.setLong(3, rowIdByTraitApplication.get(method.getReturnType()));
				insertMethod.setString(4, method.getName());
				insertMethod.execute();
				Long methodId = insertMethod.getGeneratedKeys().getLong(1);
				PreparedStatement insertArgument = connection.prepareStatement(
						String.format("INSERT INTO %s (trait_method_id, argument_index, type, name) VALUES (%s, ?, ?, ?)",
								TableNames.TRAIT_METHOD_ARGUMENT, methodId));
				long argIndex = 0;
				for (ImmutableOimfMethodArgument argument : method.getArguments())
				{
					insertArgument.setLong(1, rowIdByTraitApplication.get(argument.getType()));
					insertArgument.setLong(2, argIndex++);
					insertArgument.setString(3, argument.getName());
					insertArgument.execute();
				}
			}
		}
	}

	private void generateTraitHeader(@NotNull ImmutableOimfTrait trait, @NotNull Connection connection)
			throws IOException, SQLException
	{
		Statement insertTrait = connection.createStatement();
		insertTrait.execute(String.format("INSERT INTO %s (guid) VALUES (\"%s\")", TableNames.TRAIT,
				trait.getGuid().toString()));
		Long traitId = insertTrait.getGeneratedKeys().getLong(1);

		long index = 0;
		PreparedStatement insertTraitArg = connection.prepareStatement(
				String.format("INSERT INTO %s (trait_id, argument_index, name) VALUES (%s, ?, ?)",
						TableNames.TRAIT_ARGUMENT, traitId));
		for (String argument : trait.getArguments())
		{
			insertTraitArg.setLong(1, index++);
			insertTraitArg.setString(2, argument);
			insertTraitArg.execute();
		}
	}

	@NotNull
	private void collectTraitApplications(@NotNull Set<ImmutableOimfTrait> traits, @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		for (ImmutableOimfTrait trait : traits)
		{
			collectTraitApplications(trait, traitApplications);
		}
	}

	@NotNull
	private void collectTraitApplications(@NotNull ImmutableOimfTrait trait, @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		collectTraitApplications(trait.getExtends(), traitApplications);
		for (ImmutableOimfField field : trait.getFields())
		{
			collectTraitApplications(field.getType(), traitApplications);
		}
		for (ImmutableOimfMethod method : trait.getMethods())
		{
			collectTraitApplications(method.getReturnType(), traitApplications);
			for (ImmutableOimfMethodArgument argument : method.getArguments())
			{
				collectTraitApplications(argument.getType(), traitApplications);
			}
		}
	}

	private void collectTraitApplications(@NotNull ImmutableOimfTraitApplication traitApplication,
	                                      @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		traitApplications.add(traitApplication);
		collectTraitApplications(traitApplication.getArguments(), traitApplications);
	}

	private void collectTraitApplications(@NotNull ImmutableList<ImmutableOimfTraitApplication> traitApplicationsList,
	                                      @NotNull Set<ImmutableOimfTraitApplication> traitApplications)
	{
		for (ImmutableOimfTraitApplication traitApplication : traitApplicationsList)
		{
			collectTraitApplications(traitApplication, traitApplications);
		}
	}
}
