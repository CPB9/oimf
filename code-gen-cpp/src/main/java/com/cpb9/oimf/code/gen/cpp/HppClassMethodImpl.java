package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public class HppClassMethodImpl implements HppClassMethod
{
	@NotNull
	private final String name;
	@NotNull
	private final HppType returnType;
	@NotNull
	private final List<HppFuncArgument> arguments;
	@NotNull
	private final HppClassMethodVisibility visibility;
	@NotNull
	private final List<HppElement> elements;
	private final boolean isVirtual;

	public HppClassMethodImpl(@NotNull String name, @NotNull HppType returnType, @NotNull ArrayList<HppFuncArgument> arguments,
	                          @NotNull HppClassMethodVisibility visibility, boolean isVirtual,
	                          @NotNull ArrayList<HppElement> elements)
	{
		this.name = name;
		this.returnType = returnType;
		this.arguments = arguments;
		this.visibility = visibility;
		this.isVirtual = isVirtual;
		this.elements = elements;
	}

	@NotNull
	@Override
	public String getName()
	{
		return name;
	}

	@NotNull
	@Override
	public HppType getReturnType()
	{
		return returnType;
	}

	@NotNull
	@Override
	public List<HppFuncArgument> getArgs()
	{
		return arguments;
	}

	@Override
	public HppClassMethodVisibility getVisibility()
	{
		return visibility;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CppGeneratorConfiguration config)
			throws IOException
	{
		if (isVirtual)
		{
			appendable.append("virtual ");
		}
		returnType.generate(appendable, config);
		if (returnType != HppSimpleType.OMMITED_TYPE)
		{
			appendable.append(" ");
		}
		appendable.append(name).append("(");
		boolean isFirst = true;
		for (HppFuncArgument arg : arguments)
		{
			if (isFirst)
			{
				isFirst = false;
			}
			else
			{
				appendable.append(", ");
			}
			arg.getType().generate(appendable, config);
			appendable.append(" ").append(arg.getName());
		}
		appendable.append(") {");
		if (!elements.isEmpty())
		{
			config.eol();
			config.incIndent();
			for (HppElement element : elements)
			{
				config.indent();
				element.generate(appendable, config);
				config.eol();
			}
			config.decIndent();
			config.indent();
		}
		appendable.append("}");
	}
}
