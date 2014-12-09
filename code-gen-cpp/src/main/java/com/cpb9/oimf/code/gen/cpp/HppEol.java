package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppEol implements HppElement
{
	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		config.eol();
	}
}
