package com.cpb9.oimf.code.gen.cpp;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class HppClassMethodDecl implements HppClassMethod
{
	private final String name;
	private final HppType returnType;
	private final List<HppFuncArgument> args;
	private final HppClassMethodVisibility visibility;
	private final boolean isVirtual;
	private final boolean isPure;

	public HppClassMethodDecl(@NotNull String name, @NotNull HppType returnType,
	                          @NotNull List<HppFuncArgument> args, @NotNull HppClassMethodVisibility visibility)
	{
		this(name, returnType, args, visibility, false, false);
	}

	public HppClassMethodDecl(@NotNull String name, @NotNull HppType returnType,
	                          @NotNull List<HppFuncArgument> args, @NotNull HppClassMethodVisibility visibility,
	                          boolean isVirtual, boolean isPure)
	{
		this.name = name;
		this.returnType = returnType;
		this.args = args;
		this.visibility = visibility;
		Preconditions.checkArgument(!isPure || isVirtual, "Pure method must be virtual also");
		this.isVirtual = isVirtual;
		this.isPure = isPure;
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
		for (HppFuncArgument arg : args)
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
		appendable.append(")");
		if (isPure)
		{
			appendable.append(" = 0");
		}
		appendable.append(";");
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
		return args;
	}

	@Override
	public HppClassMethodVisibility getVisibility()
	{
		return visibility;
	}
}
