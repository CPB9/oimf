package com.cpb9.oimf.codegen.clang;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public interface Generatable<GeneratorConfig>
{
	public void generate(@NotNull Appendable appendable, @NotNull GeneratorConfig config) throws IOException;
}
