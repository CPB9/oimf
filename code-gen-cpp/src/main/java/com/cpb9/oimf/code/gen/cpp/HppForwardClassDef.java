package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppForwardClassDef implements HppElement
{
	@NotNull
	private final String name;

	public HppForwardClassDef(@NotNull String name)
	{
		this.name = name;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration cppGeneratorConfiguration)
			throws IOException
	{
		appendable.append("class ").append(name).append(";");
	}
}
