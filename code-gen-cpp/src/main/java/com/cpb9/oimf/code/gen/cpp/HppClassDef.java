package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;
import org.parboiled.common.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class HppClassDef implements HppElement
{
	private final String name;
	private final List<String> extendsClassNames;
	private final List<HppClassMethod> methods;

	public HppClassDef(String name, List<String> extendsClassNames, List<HppClassMethod> methodDecls)
	{
		this.name = name;
		this.extendsClassNames = extendsClassNames;
		this.methods = methodDecls;
	}

	public HppClassDef(@NotNull String name)
	{
		this(name, new ArrayList<String>(), new ArrayList<HppClassMethod>());
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		appendable.append("class ").append(name);
		if (!extendsClassNames.isEmpty())
		{
			appendable.append(" : public ");
			appendable.append(StringUtils.join(extendsClassNames, ", public "));
		}
		appendable.append(" {");
		config.eol();
		if (!methods.isEmpty())
		{
			generateMethods(HppClassMethodVisibility.PUBLIC, "public", appendable, config);
			generateMethods(HppClassMethodVisibility.PROTECTED, "protected", appendable, config);
			generateMethods(HppClassMethodVisibility.PRIVATE, "private", appendable, config);
		}
		appendable.append("};");
		config.eol();
	}

	private void generateMethods(HppClassMethodVisibility visibility, String declaration, Appendable appendable,
	                             CppGeneratorConfiguration config) throws IOException
	{
		List<HppClassMethod> filteredMethods = getVisibilityMethods(visibility);
		if (!filteredMethods.isEmpty())
		{
			appendable.append(declaration).append(":");
			config.eol();
			config.incIndent();
			for (HppClassMethod method : filteredMethods)
			{
				config.indent();
				method.generate(appendable, config);
				config.eol();
			}
			config.decIndent();
		}
	}

	private List<HppClassMethod> getVisibilityMethods(@NotNull HppClassMethodVisibility visibility)
	{
		List<HppClassMethod> filteredMethods = new ArrayList<>();
		for(HppClassMethod method : methods)
		{
			if (method.getVisibility().equals(visibility))
			{
				filteredMethods.add(method);
			}
		}
		return filteredMethods;
	}
}
