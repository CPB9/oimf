package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CSetExpr implements CSemicolonEndedStatement
{
	@NotNull
	private final CExpression left;
	@NotNull
	private final CExpression right;

	public CSetExpr(@NotNull CExpression left, @NotNull CExpression right)
	{
		this.left = left;
		this.right = right;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		left.generate(appendable, config);
		appendable.append(" = ");
		right.generate(appendable, config);
	}
}
