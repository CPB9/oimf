package com.cpb9.oimf.code.gen.cpp;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import org.parboiled.common.StringUtils;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CppGeneratorConfiguration
{
	public static char EOL = '\n';
	private char indentChar = '\t';
	private int indent = 0;
	private Appendable appendable;

	public Appendable getAppendable()
	{
		return appendable;
	}

	public void setAppendable(Appendable appendable)
	{
		this.appendable = appendable;
	}

	public void eol() throws IOException
	{
		Preconditions.checkNotNull(appendable).append(EOL);
	}

	public void incIndent()
	{
		indent++;
	}

	public void indent() throws IOException
	{
		Preconditions.checkNotNull(appendable).append(StringUtils.repeat(indentChar, indent));
	}

	public void decIndent()
	{
		indent--;
	}
}
