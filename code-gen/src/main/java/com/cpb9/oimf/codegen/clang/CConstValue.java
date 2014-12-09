package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CConstValue<T> implements CConstDeclValue, CExpression
{
	private final T value;

	public CConstValue(T value)
	{
		this.value = value;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append(value.toString());
	}
}
