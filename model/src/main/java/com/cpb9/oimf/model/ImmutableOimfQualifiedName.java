package com.cpb9.oimf.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public class ImmutableOimfQualifiedName
{
	@NotNull
	private final ImmutableList<String> parts;

	public ImmutableOimfQualifiedName(@NotNull String[] parts)
	{
		this.parts = ImmutableList.copyOf(parts);
	}

	public ImmutableOimfQualifiedName(@NotNull ImmutableList<String> parts)
	{
		this.parts = parts;
	}

	@NotNull
	public static ImmutableOimfQualifiedName createFromString(@NotNull String fullName)
	{
		return new ImmutableOimfQualifiedName(Preconditions.checkNotNull(fullName, "fullName").split(Pattern.quote(".")));
	}

	@NotNull
	public ImmutableList<String> getParts()
	{
		return parts;
	}

	public ImmutableOimfQualifiedName newAppend(@NotNull String part)
	{
		return new ImmutableOimfQualifiedName(ImmutableList.<String>builder().addAll(parts).add(part).build());
	}

	@Override
	@NotNull
	public String toString()
	{
		return StringUtils.join(parts, ".");
	}

	public boolean isEmpty()
	{
		return parts.isEmpty();
	}

	@NotNull
	public ImmutableOimfQualifiedName newDropLast()
	{
		return isEmpty() ? this : new ImmutableOimfQualifiedName(parts.subList(0, parts.size() - 1));
	}

	@NotNull
	public Optional<String> getLast()
	{
		return Optional.fromNullable(parts.isEmpty() ? null : parts.get(parts.size() - 1));
	}

	@Override
	public boolean equals(Object o)
	{
		return !(o == null || !(o instanceof ImmutableOimfQualifiedName))
				&& (this == o || Objects.equal(parts, ((ImmutableOimfQualifiedName) o).parts));
	}

	@Override
	public int hashCode()
	{
		return parts.hashCode();
	}

	public int size()
	{
		return parts.size();
	}

}
