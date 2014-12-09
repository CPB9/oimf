package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CGenetratorConfig
{
	private String indent = "    ";
	private int currentIndent = 0;

	public int getCurrentIndent()
	{
		return currentIndent;
	}

	public void incCurrentIndent()
	{
		currentIndent++;
	}

	public void decCurrentIndent()
	{
		currentIndent--;
	}

	public void makeIndent(@NotNull Appendable appendable) throws IOException
	{
		for (int i = 0; i < currentIndent ; i++)
		{
			appendable.append(indent);
		}
	}

	public void appendEol(@NotNull Appendable appendable) throws IOException
	{
		appendable.append('\n');
	}
}
