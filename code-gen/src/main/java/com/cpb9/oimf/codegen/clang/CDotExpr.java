package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CDotExpr implements CExpression
{
	private final CExpression from;
	private final String fieldName;

	public CDotExpr(@NotNull CExpression from, @NotNull String fieldName)
	{
		this.from = from;
		this.fieldName = fieldName;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		from.generate(appendable, config);
		appendable.append('.').append(fieldName);
	}
}
