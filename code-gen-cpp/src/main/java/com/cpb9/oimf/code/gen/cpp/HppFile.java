package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class HppFile implements Generatable<CppGeneratorConfiguration>
{
	private List<HppElement> elements = new ArrayList<>();

	public HppFile()
	{
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		for (HppElement element : elements)
		{
			element.generate(appendable, config);
		}
	}

	@NotNull
	public HppFile append(@NotNull HppElement element)
	{
		elements.add(element);
		return this;
	}
}
