package com.cpb9.oimf.code.gen.cpp;

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
public class OimfCppGeneratorConfiguration
{

	public static enum OwnershipStyle { QT_SHARED_POINTER, POINTER }

	@NotNull
	@Option(name = "--hpp-prologue", metaVar = "STR", usage = "Prologue for .hpp files")
	private String hppPrologue = "// File is generated automatically. DO NOT EDIT\n";

	@NotNull
	@Option(name = "--cpp-prologue", metaVar = "STR", usage = "Prologue for .cpp files")
	private String cppPrologue = "// File is generated automatically. DO NOT EDIT\n";

	@NotNull
	@Option(name = "--hpp-epilogue", metaVar = "STR", usage = "Epilogue for .hpp files")
	private String hppEpilogue = "";

	@NotNull
	@Option(name = "--cpp-epilogue", metaVar = "STR", usage = "Epilogue for .cpp files")
	private String cppEpilogue = "";

	@NotNull
	@Argument(metaVar = "OUTPUT_PATH", usage = "output path")
	private File outputPath = new File(System.getProperty("user.dir"));

	@NotNull
	@Option(name = "--ownership-style", required = true, usage = "Use specified ownership style", metaVar = "OWNERSHIP_STYLE")
	private OwnershipStyle ownershipStyle = OwnershipStyle.POINTER;

	@Option(name = "-a", aliases = {"--namespace-aliases"}, handler = MapOptionHandler.class, usage = "Namespace aliases")
	Map<String, String> namespaceAliases = new HashMap<>();

	@Option(name = "-m", aliases = {"--trait-aliases"}, handler = MapOptionHandler.class, usage = "Map trait to name")
	Map<String, String> traitMappings = new HashMap<>();

	@NotNull
	public File getOutputPath()
	{
		return outputPath;
	}

	public void setOutputPath(@NotNull File outputPath)
	{
		this.outputPath = outputPath;
	}

	@NotNull
	public String getHppPrologue()
	{
		return hppPrologue;
	}

	@NotNull
	public String getCppPrologue()
	{
		return cppPrologue;
	}

	@NotNull
	public String getHppEpilogue()
	{
		return hppEpilogue;
	}

	@NotNull
	public String getCppEpilogue()
	{
		return cppEpilogue;
	}

	public void setOwnershipStyle(@NotNull OwnershipStyle ownershipStyle)
	{
		this.ownershipStyle = ownershipStyle;
	}

	@NotNull
	public OwnershipStyle getOwnershipStyle()
	{
		return ownershipStyle;
	}

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
