package com.cpb9.oimf.codegen;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shein
 */
public abstract class OimfGeneratorConfig
{
	@Option(name = "-a", aliases = {"--namespace-aliases"}, handler = MapOptionHandler.class, usage = "Namespace aliases")
	Map<String, String> namespaceAliases = new HashMap<>();

	@Option(name = "-m", aliases = {"--trait-aliases"}, handler = MapOptionHandler.class, usage = "Map trait to name")
	Map<String, String> traitMappings = new HashMap<>();

	@NotNull
	public Map<String, String> getNamespaceAliases()
	{
		return namespaceAliases;
	}

	public void setTraitMappings(@NotNull Map<String, String> traitMappings)
	{
		this.traitMappings = traitMappings;
	}

	@NotNull
	public Map<String, String> getTraitMappings()
	{
		return traitMappings;
	}
}
