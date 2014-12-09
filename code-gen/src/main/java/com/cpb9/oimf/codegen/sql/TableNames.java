package com.cpb9.oimf.codegen.sql;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

/**
 * @author Artem Shein
 */
public enum TableNames
{
	TRAIT("trait"), TRAIT_ARGUMENT("trait_argument"), TRAIT_FIELD("trait_field"), TRAIT_APPLICATION("trait_application"),
	TRAIT_APPLICATION_ARGUMENT("trait_application_argument"), TRAIT_EXTEND("trait_extend"), TRAIT_METHOD("trait_method"),
	TRAIT_METHOD_ARGUMENT("trait_method_argument");

	@NotNull
	private final String name;

	@NotNull
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return getName();
	}

	TableNames(@NotNull String name)
	{
		this.name = name;
	}
}
