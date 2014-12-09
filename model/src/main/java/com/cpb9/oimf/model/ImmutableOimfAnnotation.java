package com.cpb9.oimf.model;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfAnnotation
{
	@NotNull
	private final String name;
	@NotNull
	private final Optional<Object> value;

	public ImmutableOimfAnnotation(@NotNull String name, @NotNull Optional<Object> value)
	{
		this.name = name;
		this.value = value;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	@Nullable
	public Optional<Object> getValue()
	{
		return value;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfAnnotation))
		{
			return false;
		}
		ImmutableOimfAnnotation other = (ImmutableOimfAnnotation) o;
		return this == other || (Objects.equals(name, other.name) && Objects.equals(value, other.value));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(name, value);
	}
}
