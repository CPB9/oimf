package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppComment implements HppElement
{
	private final String text;

	public HppComment(@NotNull String text)
	{
		this.text = text;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration cppGeneratorConfiguration)
			throws IOException
	{
		appendable.append("// ").append(text);
	}
}
