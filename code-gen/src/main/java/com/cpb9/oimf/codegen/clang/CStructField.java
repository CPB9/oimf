package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CStructField implements Generatable<CGenetratorConfig>
{
	@NotNull
	private final String name;
	@NotNull
	private final SimpleCType type;

	public CStructField(@NotNull String name, @NotNull SimpleCType type)
	{
		this.name = name;
		this.type = type;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		type.generate(appendable, config);
		appendable.append(" ").append(name).append(';');
	}
}
