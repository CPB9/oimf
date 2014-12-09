package com.cpb9.oimf.codegen.clang;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * @author Artem Shein
 */
public class CStruct implements CType
{
	@NotNull
	private final Optional<String> name;
	@NotNull
	private final List<CStructField> fields;

	public CStruct(@Nullable String name, @NotNull List<CStructField> fields)
	{
		this.name = Optional.fromNullable(name);
		this.fields = fields;
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		appendable.append("struct ");
		if (name.isPresent())
		{
			appendable.append(name.get());
		}
		appendable.append('{');
		for (CStructField field : fields)
		{
			appendable.append("\n    ");
			field.generate(appendable, config);
		}
		appendable.append("\n}");
	}
}
