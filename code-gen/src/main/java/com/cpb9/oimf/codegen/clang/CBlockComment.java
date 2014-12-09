package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CBlockComment extends CComment
{
	public CBlockComment(String text)
	{
		super(text);
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("/* ").append(text).append(" */");
	}
}
