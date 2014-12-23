package com.cpb9.oimf.parser.model;

import com.cpb9.oimf.model.ImmutableOimfValue;
import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public class Field extends Annotated
{
	@NotNull
	private final String name;
	@NotNull
	private final TraitApplication type;
	@NotNull
	private final Optional<ImmutableOimfValue> defaultValue;

	public Field(@NotNull List<Annotation> annotations, @NotNull String name, @NotNull TraitApplication type, @NotNull Optional<ImmutableOimfValue> defaultValue)
	{
		super(annotations);
		this.name = name;
		this.type = type;
		this.defaultValue = defaultValue;
	}

	@NotNull
	public String getName()
	{
		return name;
	}

	@NotNull
	public TraitApplication getType()
	{
		return type;
	}

	@NotNull
	public Optional<ImmutableOimfValue> getDefaultValue()
	{
		return defaultValue;
	}
}
