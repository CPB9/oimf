package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CInclude implements CLangElement
{
	private final String fileName;

	public CInclude(@NotNull String fileName)
	{
		this.fileName = fileName;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("#include \"").append(fileName).append("\"\n");
	}
}
