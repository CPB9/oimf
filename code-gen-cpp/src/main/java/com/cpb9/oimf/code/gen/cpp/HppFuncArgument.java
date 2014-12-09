package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppFuncArgument implements Generatable<CppGeneratorConfiguration>
{
	private final String name;
	private final HppType type;

	public HppFuncArgument(@NotNull String name, @NotNull HppType type)
	{
		this.name = name;
		this.type = type;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		type.generate(appendable, config);
		appendable.append(" ").append(name);
	}

	public String getName()
	{
		return name;
	}

	public HppType getType()
	{
		return type;
	}
}
