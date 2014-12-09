package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class HppTemplateType implements HppType
{
	@NotNull
	private final String typeName;
	@NotNull
	private final List<? extends HppType> templateTypes;

	public HppTemplateType(@NotNull String typeName, @NotNull List<? extends HppType> templateTypes)
	{
		this.typeName = typeName;
		this.templateTypes = templateTypes;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		appendable.append(typeName).append("<");
		boolean isFirst = true;
		for (HppType templateType : templateTypes)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				appendable.append(", ");
			}
			templateType.generate(appendable, config);
		}
		appendable.append(">");
	}
}
