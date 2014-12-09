package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppUsing implements HppElement
{
	private final String name;

	public HppUsing(@NotNull String name)
	{
		this.name = name;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration cppGeneratorConfiguration)
			throws IOException
	{
		appendable.append("using ").append(name).append(";");
	}
}
