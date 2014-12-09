package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CVar implements CExpression
{
	private final String name;

	public CVar(String name)
	{
		this.name = name;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append(name);
	}
}
