package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfMethodArgument
{
	@NotNull
	private final ImmutableOimfTraitApplication type;
	@NotNull
	private final String name;

	public ImmutableOimfMethodArgument(@NotNull ImmutableOimfTraitApplication type, @NotNull String name)
	{
		this.type = type;
		this.name = name;
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
		if (o == null || !(o instanceof ImmutableOimfMethodArgument))
		{
			return false;
		}
		ImmutableOimfMethodArgument other = (ImmutableOimfMethodArgument) o;
		return this == other || (Objects.equals(type, other.type) && Objects.equals(name, other.name));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(type, name);
	}
}
