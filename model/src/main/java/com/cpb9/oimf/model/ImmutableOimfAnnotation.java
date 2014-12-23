package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
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
	private final ImmutableOimfTraitApplication traitApplication;
	@NotNull
	private final Optional<ImmutableOimfValue> value;

	public ImmutableOimfAnnotation(@NotNull ImmutableOimfTraitApplication traitApplication, @NotNull Optional<ImmutableOimfValue> value)
	{
		this.traitApplication = traitApplication;
		this.value = value;
	}

	@NotNull
	public ImmutableOimfTraitApplication getTraitApplication()
	{
		return traitApplication;
	}

	@Nullable
	public Optional<ImmutableOimfValue> getValue()
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
		return this == other || (Objects.equals(traitApplication, other.traitApplication) && Objects.equals(value, other.value));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(traitApplication, value);
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("traitApplication", traitApplication).add("value", value).toString();
	}
}
