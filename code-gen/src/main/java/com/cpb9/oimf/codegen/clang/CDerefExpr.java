package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CDerefExpr implements CExpression
{
	private final CVar var;

	public CDerefExpr(@NotNull CVar var)
	{
		this.var = var;
	}


	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		appendable.append("(*");
		var.generate(appendable, config);
		appendable.append(')');
	}
}
