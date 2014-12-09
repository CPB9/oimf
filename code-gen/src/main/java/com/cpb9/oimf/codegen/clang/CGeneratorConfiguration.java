package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shein
 */
public class CGeneratorConfiguration
{
	@Option(name = "-a", aliases = {"--namespace-aliases"}, handler = MapOptionHandler.class, usage = "Namespace aliases")
	Map<String, String> namespaceAliases = new HashMap<>();

	@NotNull
	public Map<String, String> getNamespaceAliases()
	{
		return namespaceAliases;
	}
}
