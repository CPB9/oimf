package com.cpb9.oimf.codegen.clang;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Artem Shein
 */
public abstract class AbstractFile<T extends HLangElement> implements HLangElement
{
	private List<T> contents = new ArrayList<>();

	public AbstractFile()
	{
	}

	@Override
	public void generate(@NotNull Appendable appendable, @NotNull CGenetratorConfig config) throws IOException
	{
		for (T element : contents)
		{
			element.generate(appendable, config);
			if (element instanceof CSemicolonEndedStatement)
			{
				appendable.append(';');
			}
		}
	}

	public void append(@NotNull List<? extends T> elements)
	{
		this.contents.addAll(elements);
	}

	public void append(@NotNull T element)
	{
		this.contents.add(element);
	}
}
