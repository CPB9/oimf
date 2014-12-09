package com.cpb9.oimf.code.gen.cpp;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public class HppNamespaceDef implements HppElement
{
	private final List<? extends HppElement> elements;
	private final String name;

	public HppNamespaceDef(@NotNull String name, @NotNull List<? extends HppElement> elements)
	{
		this.name = name;
		this.elements = elements;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		String[] names = name.split(Pattern.quote("::"));
		boolean isFirst = true;
		for (String namePart : names)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				appendable.append(" ");
			}
			appendable.append("namespace ").append(namePart).append(" {");
		}
		config.eol();
		config.eol();
		for (HppElement element : elements)
		{
			element.generate(appendable, config);
		}
		config.eol();
		appendable.append(StringUtils.repeat("}", names.length));
		config.eol();
	}
}
