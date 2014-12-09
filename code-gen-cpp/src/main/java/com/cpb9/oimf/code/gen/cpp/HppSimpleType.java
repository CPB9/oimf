package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppSimpleType implements HppType
{
	public static HppSimpleType OMMITED_TYPE = new HppSimpleType("NO_RETURN_TYPE");
	private final String name;

	public HppSimpleType(@NotNull String name)
	{
		this.name = name;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration cppGeneratorConfiguration)
			throws IOException
	{
		if (this != OMMITED_TYPE)
		{
			appendable.append(name);
		}
	}
}
