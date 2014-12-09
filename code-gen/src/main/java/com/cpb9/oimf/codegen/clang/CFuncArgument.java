package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class CFuncArgument
{
	@NotNull
	private final String name;
	@NotNull
	private final CType type;

	public CFuncArgument(@NotNull String name, @NotNull CType type)
	{
		this.name = name;
		this.type = type;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	@NotNull
	public CType getType()
	{
		return type;
	}
}
