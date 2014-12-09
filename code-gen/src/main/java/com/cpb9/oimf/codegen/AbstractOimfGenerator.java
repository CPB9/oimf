package com.cpb9.oimf.codegen;

import com.cpb9.oimf.model.ImmutableOimfQualifiedName;
import com.cpb9.oimf.model.ImmutableOimfTrait;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Artem Shein
 */
public abstract class AbstractOimfGenerator<T extends OimfGeneratorConfig>
{
	@NotNull
	protected final T config;
	@NotNull
	protected final Set<ImmutableOimfTrait> traits;
	@NotNull
	protected final Map<ImmutableOimfQualifiedName, ImmutableOimfTrait> traitByName = new HashMap<>();

	public AbstractOimfGenerator(@NotNull T config, @NotNull Set<ImmutableOimfTrait> traits)
	{
		this.config = config;
		this.traits = traits;

		for (ImmutableOimfTrait trait : traits)
		{
			traitByName.put(trait.getGuid(), trait);
		}
	}
}
