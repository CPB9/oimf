package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by metadeus on 21.10.14.
 */
public class CPointerType implements CType
{
	private final CType baseType;

	public CPointerType(CType baseType)
	{
		this.baseType = baseType;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		baseType.generate(appendable, config);
		appendable.append("*");
	}
}
