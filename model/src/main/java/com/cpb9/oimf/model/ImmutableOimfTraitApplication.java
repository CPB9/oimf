package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfTraitApplication
{
	@NotNull
	private final ImmutableOimfQualifiedName name;
	@NotNull
	private final ImmutableList<ImmutableOimfTraitApplication> arguments;

	public ImmutableOimfTraitApplication(@NotNull ImmutableOimfQualifiedName name,
										 @NotNull ImmutableList<ImmutableOimfTraitApplication> arguments)
	{
		this.name = name;
		this.arguments = arguments;
	}

	@NotNull
	public ImmutableList<ImmutableOimfTraitApplication> getArguments()
	{
		return arguments;
	}

	@NotNull
	public ImmutableOimfQualifiedName getName()
	{
		return name;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("name", name).add("arguments", arguments).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfTraitApplication))
		{
			return false;
		}
		ImmutableOimfTraitApplication other = (ImmutableOimfTraitApplication) o;
		return this == other || (Objects.equals(name, other.name) && Objects.equals(arguments, other.arguments));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, arguments);
	}
}
