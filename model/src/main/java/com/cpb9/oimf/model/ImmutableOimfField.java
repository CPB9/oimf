package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfField extends ImmutableOimfAnnotated
{
	@NotNull
	private final ImmutableOimfTraitApplication type;
	@NotNull
	private final String name;
	@NotNull
	private final Optional<ImmutableOimfValue> defaultValue;

	public ImmutableOimfField(@NotNull ImmutableList<ImmutableOimfAnnotation> annotations, @NotNull String name, @NotNull ImmutableOimfTraitApplication type, @NotNull Optional<ImmutableOimfValue> defaultValue)
	{
		super(annotations);
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
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

	@NotNull
	public Optional<ImmutableOimfValue> getDefaultValue()
	{
		return defaultValue;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("super", super.toString()).add("name", name).add("type", type).add("defaultValue", defaultValue).toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfField))
		{
			return false;
		}
		ImmutableOimfField other = (ImmutableOimfField)o;
		return this == other || (super.equals(other) && Objects.equals(name, other.name) && Objects.equals(type, other.type));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(super.hashCode(), name, type);
	}
}
