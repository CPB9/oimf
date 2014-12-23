package com.cpb9.oimf.codegen.java;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class JavaGeneratorConfiguration
{
	private static final char EOL = '\n';

	public void appendEol(@NotNull Appendable appendable) throws IOException
	{
		appendable.append(EOL);
	}
}
