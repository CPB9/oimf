package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppPragma implements HppElement
{
	private final String value;

	public HppPragma(@NotNull String value)
	{
		this.value = value;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		appendable.append("#pragma ").append(value);
		config.eol();
	}
}
