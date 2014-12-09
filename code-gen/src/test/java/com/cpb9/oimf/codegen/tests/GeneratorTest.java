package com.cpb9.oimf.codegen.tests;

import com.cpb9.oimf.codegen.clang.CGeneratorConfiguration;
import com.cpb9.oimf.codegen.sql.OimfSqlGenerator;
import com.cpb9.oimf.codegen.sql.OimfSqlGeneratorConfiguration;
import com.cpb9.oimf.codegen.yaml.OimfYamlGenerator;
import com.cpb9.oimf.codegen.yaml.OimfYamlGeneratorConfiguration;
import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.ParserUtils;
import com.cpb9.oimf.parser.TransformationResult;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public class GeneratorTest
{
	private static final Logger LOG = LoggerFactory.getLogger(GeneratorTest.class);

	public void testParsing() throws IOException
	{
		TransformationResult<Set<ImmutableOimfTrait>>
				result = ParserUtils.transformFiles(ParserUtils.parseResource("Test.oimf"), ParserUtils.parseResource("Test2.oimf"));
		CGeneratorConfiguration config = new CGeneratorConfiguration();
		config.getNamespaceAliases().put("com.cpb9.test", "test");
//        new OimfCGenerator(config, transformResult.getValue()).generate();
	}



	@Test
	public void testOimfYaml() throws IOException
	{
		TransformationResult<Set<ImmutableOimfTrait>> result = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"));
		OimfYamlGeneratorConfiguration config = new OimfYamlGeneratorConfiguration();
		new OimfYamlGenerator(config, result.getValue()).generate();
	}

	@Test
	public void testOimfSql() throws IOException, SQLException
	{
		File outputFile = new File("oimf.sqlite3");

		createSqliteDatabaseSchema(outputFile);

		TransformationResult<Set<ImmutableOimfTrait>> result = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"));
		OimfSqlGeneratorConfiguration config = new OimfSqlGeneratorConfiguration();
		config.setOutputFile(outputFile);
		new OimfSqlGenerator(config, result.getValue()).generate();
	}

	@Test
	public void testOimfRuntimeSql() throws IOException, SQLException
	{
		File outputFile = new File("oimf_runtime.sqlite3");

		createSqliteDatabaseSchema(outputFile);

		TransformationResult<Set<ImmutableOimfTrait>> result = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"),
				ParserUtils.parseResource("OimfRuntime.oimf"), ParserUtils.parseResource("Core.oimf"));
		OimfSqlGeneratorConfiguration config = new OimfSqlGeneratorConfiguration();
		config.setOutputFile(outputFile);
		new OimfSqlGenerator(config, result.getValue()).generate();
	}

	private void createSqliteDatabaseSchema(@NotNull File outputFile) throws SQLException, IOException
	{
		try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + outputFile.getAbsolutePath()))
		{
			connection.setAutoCommit(false);
			for (String statementSql : Resources.toString(Resources.getResource("Oimf.sql"), Charsets.UTF_8)
					.split(Pattern.quote(";")))
			{
				LOG.debug("Executing: {}", statementSql);
				if (!statementSql.trim().isEmpty())
				{
					connection.createStatement().execute(statementSql);
				}
			}
			connection.commit();
		}
	}
}
