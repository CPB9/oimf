package com.cpb9.oimf.codegen.clang;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CFuncImplementation implements CLangElement
{
	private final CType returnType;
	private final String name;
	private final List<CFuncArgument> args;
	private final ArrayList<CStatement> statements;

	public CFuncImplementation(CType returnType, String name, List<CFuncArgument> args, List<CStatement> statements)
	{
		this.returnType = returnType;
		this.name = name;
		this.args = args;
		this.statements = Lists.newArrayList(statements);
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		returnType.generate(appendable, config);
		appendable.append(' ').append(name).append('(');
		boolean isFirst = true;
		for (CFuncArgument arg : args)
		{
			if (isFirst)
				isFirst = false;
			else
				appendable.append(", ");
			arg.getType().generate(appendable, config);
			appendable.append(' ').append(arg.getName());
		}
		appendable.append(") {");
		config.appendEol(appendable);
		config.incCurrentIndent();
		for (CStatement statement : statements)
		{
			config.makeIndent(appendable);
			statement.generate(appendable, config);
			if (statement instanceof CSemicolonEndedStatement)
			{
				appendable.append(';');
			}
			config.appendEol(appendable);
		}
		config.decCurrentIndent();
		appendable.append('}');
		config.appendEol(appendable);
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	@NotNull
	public CType getReturnType()
	{
		return returnType;
	}

	@NotNull
	public List<CFuncArgument> getArgs()
	{
		return args;
	}

	@NotNull
	public List<CStatement> getStatements()
	{
		return statements;
	}
}
