package com.cpb9.oimf.codegen.clang;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CCallFunc implements CExpression, CSemicolonEndedStatement
{
	@NotNull
	private final String name;
	@NotNull
	private final List<CExpression> args;

	public CCallFunc(@NotNull String name, @NotNull List<CExpression> args)
	{
		this.name = name;
		this.args = args;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append(name).append('(');
		boolean isFirst = true;
		for (CExpression expr : args)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				appendable.append(", ");
			}
			expr.generate(appendable, config);
		}
		appendable.append(')');
	}
}
