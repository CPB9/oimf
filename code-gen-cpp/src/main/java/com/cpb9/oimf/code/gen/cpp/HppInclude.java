package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class HppInclude implements HppElement
{
	private final String file;

	public HppInclude(String file)
	{
		this.file = file;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		appendable.append("#include<").append(file).append(">");
		config.eol();
	}
}
