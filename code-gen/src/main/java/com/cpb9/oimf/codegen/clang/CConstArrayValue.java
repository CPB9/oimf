package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CConstArrayValue implements CConstDeclValue
{
	@NotNull
	private final List<CConstDeclValue> values;

	public CConstArrayValue(@NotNull List<CConstDeclValue> values)
	{
		this.values = values;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		appendable.append('{');
		boolean isFirst = true;
		for (CConstDeclValue value : values)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				appendable.append(", ");
			}
			value.generate(appendable, config);
		}
		appendable.append('}');
	}
}
