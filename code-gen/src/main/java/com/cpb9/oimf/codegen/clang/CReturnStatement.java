package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CReturnStatement implements CSemicolonEndedStatement
{
	@Nullable
	private final CExpression expression;

	public CReturnStatement(@Nullable CExpression expression)
	{
		this.expression = expression;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("return");
		if (expression != null)
		{
			appendable.append(' ');
			expression.generate(appendable, config);
		}
	}
}
