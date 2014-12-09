package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CLtExpr implements CExpression
{
	private final CExpression leftExpr;
	private final CExpression rightExpr;

	public CLtExpr(CExpression leftExpr, CExpression rightExpr)
	{
		this.leftExpr = leftExpr;
		this.rightExpr = rightExpr;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		leftExpr.generate(appendable, config);
		appendable.append(" < ");
		rightExpr.generate(appendable, config);
	}
}
