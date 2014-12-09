package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CComment implements CStatement
{
	@NotNull
	protected final String text;

	public CComment(@NotNull String text)
	{
		this.text = text;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("/* ").append(text).append(" */");
	}
}
