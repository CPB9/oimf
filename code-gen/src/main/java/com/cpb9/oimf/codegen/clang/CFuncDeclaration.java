package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CFuncDeclaration implements HLangElement
{
	private final CType returnType;
	private final String name;
	private final List<CFuncArgument> args;

	public CFuncDeclaration(@NotNull SimpleCType returnType, @NotNull String name, @NotNull List<CFuncArgument> args)
	{
		this.returnType = returnType;
		this.name = name;
		this.args = args;
	}

	public CFuncDeclaration(@NotNull CFuncImplementation func)
	{
		this.returnType = func.getReturnType();
		this.name = func.getName();
		this.args = func.getArgs();
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
		appendable.append(");");
	}
}
