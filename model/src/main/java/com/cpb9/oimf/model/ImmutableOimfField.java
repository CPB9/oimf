package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfField
{
	@NotNull
	private final ImmutableOimfTraitApplication type;
	@NotNull
	private final String name;

	public ImmutableOimfField(@NotNull String name, @NotNull ImmutableOimfTraitApplication type)
	{
		this.name = name;
		this.type = type;
	}

	@NotNull
	public ImmutableOimfTraitApplication getType()
	{
		return type;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("name", name).add("type", type).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfField))
		{
			return false;
		}
		ImmutableOimfField other = (ImmutableOimfField)o;
		return this == other || (Objects.equals(name, other.name) && Objects.equals(type, other.type));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, type);
	}
}
