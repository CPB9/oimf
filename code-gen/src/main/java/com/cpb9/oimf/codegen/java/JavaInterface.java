package com.cpb9.oimf.codegen.java;

import com.cpb9.oimf.codegen.clang.Generatable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class JavaInterface implements Generatable<JavaGeneratorConfiguration>
{
	private final String _package;
	private final String name;

	public JavaInterface(String _package, String name)
	{
		this._package = _package;
		this.name = name;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull JavaGeneratorConfiguration config)
			throws IOException
	{
		appendable.append("package ").append(_package).append(";");
		config.appendEol(appendable);
		config.appendEol(appendable);
		appendable.append("interface ").append(name).append(" {");
		config.appendEol(appendable);
		appendable.append("}");
		config.appendEol(appendable);
	}
}
