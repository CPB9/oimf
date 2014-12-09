package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CIfElseStatement implements CStatement
{
	@NotNull
	private final CExpression testExpr;
	@NotNull
	private final List<CStatement> ifStatements;
	@NotNull
	private final List<CStatement> elseStatements;

	public CIfElseStatement(@NotNull CExpression testExpr, @NotNull List<CStatement> ifStatements,
	                        @NotNull List<CStatement> elseStatements)
	{
		this.testExpr = testExpr;
		this.ifStatements = ifStatements;
		this.elseStatements = elseStatements;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("if (");
		testExpr.generate(appendable, config);
		appendable.append(") {");
		config.appendEol(appendable);
		config.incCurrentIndent();
		for (CStatement statement : ifStatements)
		{
			config.makeIndent(appendable);
			statement.generate(appendable, config);
			appendable.append(';');
			config.appendEol(appendable);
		}
		config.decCurrentIndent();
		config.makeIndent(appendable);
		if (!elseStatements.isEmpty())
		{
			appendable.append("} else {");
			config.appendEol(appendable);
			config.incCurrentIndent();
			for (CStatement statement : elseStatements)
			{
				statement.generate(appendable, config);
				if (statement instanceof CSemicolonEndedStatement)
				{
					appendable.append(';');
				}
				config.appendEol(appendable);
			}
			config.decCurrentIndent();
			config.makeIndent(appendable);
		}
		appendable.append('}');
	}
}
