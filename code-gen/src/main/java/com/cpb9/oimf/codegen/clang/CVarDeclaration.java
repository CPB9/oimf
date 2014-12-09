package com.cpb9.oimf.codegen.clang;

import com.google.common.base.Optional;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * @author Artem Shein
 */
public class CVarDeclaration implements CSemicolonEndedStatement
{
	private final CType type;
	private final CVar var;
	@NotNull
	private final Optional<CConstDeclValue> value;

	public CVarDeclaration(@NotNull CType type, @NotNull CVar var)
	{
		this.type = type;
		this.var = var;
		this.value = Optional.absent();
	}

	public CVarDeclaration(@NotNull CType type, @NotNull CVar var, @NotNull CConstDeclValue value)
	{
		this.type = type;
		this.var = var;
		this.value = Optional.of(value);
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config)
			throws IOException
	{
		type.generate(appendable, config);
		appendable.append(' ');
		var.generate(appendable, config);
		if (value.isPresent())
		{
			appendable.append(" = ");
			value.get().generate(appendable, config);
		}
	}
}
