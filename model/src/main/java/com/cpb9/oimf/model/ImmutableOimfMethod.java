package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import org.jetbrains.annotations.NotNull;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;

/**
 * @author Artem Shein
 */
public class ImmutableOimfMethod
{
	private final String name;
	private final ImmutableOimfTraitApplication returnType;
	private final ImmutableList<ImmutableOimfMethodArgument> arguments;

	public ImmutableOimfMethod(@NotNull String name, @NotNull ImmutableOimfTraitApplication returnType,
							   @NotNull ImmutableList<ImmutableOimfMethodArgument> arguments)
	{
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
	}

	public String getName()
	{
		return name;
	}

	@NotNull
	public ImmutableList<ImmutableOimfMethodArgument> getArguments()
	{
		return arguments;
	}

	@NotNull
	public ImmutableOimfTraitApplication getReturnType()
	{
		return returnType;
	}

	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("name", name).add("returnType", returnType).add("arguments", arguments)
				.toString();
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfMethod))
		{
			return false;
		}
		ImmutableOimfMethod other = (ImmutableOimfMethod) o;
		return this == other || (Objects.equal(name, other.name) && Objects.equal(returnType, other.returnType)
				&& Objects.equal(arguments, other.arguments));
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(name, returnType, arguments);
	}
}
