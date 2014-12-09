package com.cpb9.oimf.code.gen.cpp;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * @author Artem Shein
 */
public interface HppClassMethod extends Generatable<CppGeneratorConfiguration>
{
	@NotNull
	String getName();

	@NotNull
	HppType getReturnType();

	@NotNull
	List<HppFuncArgument> getArgs();

	public HppClassMethodVisibility getVisibility();
}
