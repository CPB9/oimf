package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CTypedef implements CSemicolonEndedStatement
{
	private final CType originalType;
	private final CType newName;

	public CTypedef(CType originalType, CType newName)
	{
		this.originalType = originalType;
		this.newName = newName;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("typedef ");
		originalType.generate(appendable, config);
		appendable.append(" ");
		newName.generate(appendable, config);
	}
}
