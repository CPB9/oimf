package com.cpb9.oimf.model;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author Artem Shein
 */
public class ImmutableOimfTrait extends ImmutableOimfAnnotated
{
	@NotNull
	private final ImmutableList<ImmutableOimfMethod> methods;
	@NotNull
	private final ImmutableList<ImmutableOimfField> fields;
	@NotNull
	private final ImmutableOimfQualifiedName guid;
	private final ImmutableList<ImmutableOimfTraitApplication> anExtends;
	private final ImmutableList<String> arguments;

	public ImmutableOimfTrait(@NotNull ImmutableMap<String, ImmutableOimfAnnotation> annotations,
							  @NotNull ImmutableOimfQualifiedName guid,
							  ImmutableList<String> arguments, @NotNull ImmutableList<ImmutableOimfTraitApplication> anExtends,
							  @NotNull ImmutableList<ImmutableOimfField> fields,
							  @NotNull ImmutableList<ImmutableOimfMethod> methods)
	{
        super(annotations);
		this.guid = guid;
		this.arguments = arguments;
		this.anExtends = anExtends;
		this.fields = fields;
		this.methods = methods;
	}

	@NotNull
	public ImmutableList<ImmutableOimfMethod> getMethods()
	{
		return methods;
	}

	@NotNull
	public ImmutableList<ImmutableOimfField> getFields()
	{
		return fields;
	}

	@NotNull
	public ImmutableList<ImmutableOimfTraitApplication> getExtends()
	{
		return anExtends;
	}

	@NotNull
	public ImmutableOimfQualifiedName getGuid()
	{
		return guid;
	}

	@NotNull
	public ImmutableList<String> getArguments()
	{
		return arguments;
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == null || !(o instanceof ImmutableOimfTrait))
		{
			return false;
		}
		ImmutableOimfTrait other = (ImmutableOimfTrait) o;
		return this == other || (super.equals(other) && Objects.equals(guid, other.guid)
				&& Objects.equals(anExtends, other.anExtends) && Objects.equals(arguments, other.arguments)
				&& Objects.equals(fields, other.fields)
				&& Objects.equals(methods, other.methods));
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(annotations, guid, anExtends, arguments, fields, methods);
	}

	@NotNull
	@Override
	public String toString()
	{
		return MoreObjects.toStringHelper(this).add("annotations", annotations).add("extends", anExtends)
				.add("arguments", arguments).add("fields", fields).add("methods", methods).toString();
	}
}
