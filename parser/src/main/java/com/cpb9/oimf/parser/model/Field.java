package com.cpb9.oimf.parser.model;

import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public class Field
{
	@NotNull
	private final String name;
	@NotNull
	private final TraitApplication type;

	public Field(@NotNull String name, @NotNull TraitApplication type)
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
	public TraitApplication getType()
	{
		return type;
	}
}
