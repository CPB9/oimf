package com.cpb9.oimf.codegen.java;

import com.cpb9.oimf.codegen.OimfGeneratorConfig;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.MapOptionHandler;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Artem Shein
 */
public class OimfJavaGeneratorConfiguration extends OimfGeneratorConfig
{
	@Option(name = "-a", aliases = {"--namespace-aliases"}, handler = MapOptionHandler.class, usage = "Namespace aliases")
	Map<String, String> namespaceAliases = new HashMap<>();

	@NotNull
	public Map<String, String> getNamespaceAliases()
	{
		return namespaceAliases;
	}

	@Argument(metaVar = "OUTPUT_PATH", required = true, usage = "output path")
	File outputPath;

	public File getOutputPath()
	{
		return outputPath;
	}

	public void setOutputPath(File outputPath)
	{
		this.outputPath = outputPath;
	}
}
