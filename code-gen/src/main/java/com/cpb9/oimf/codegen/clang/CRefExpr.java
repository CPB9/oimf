package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CRefExpr implements CExpression
{
	private final CExpression expression;

	public CRefExpr(@NotNull CExpression expression)
	{
		this.expression = expression;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		appendable.append('&');
		expression.generate(appendable, config);
	}
}
