package com.cpb9.oimf.parser;

import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.model.File;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import org.jetbrains.annotations.NotNull;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.TracingParseRunner;
import org.parboiled.support.ParsingResult;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

/**
 * @author Artem Shein
 */
public abstract class ParserUtils
{
	@NotNull
	public static ParsingResult<File> tryParseResource(@NotNull String resourceName) throws IOException
	{
		Rule fileRule = Parboiled.createParser(OimfParser.class).File();
		TracingParseRunner<File> parseRunner = new TracingParseRunner<>(fileRule);
		final ParsingResult<File> result = parseRunner
				.run(Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8));
		return result;
	}

	@NotNull
	public static TransformationResult<Set<ImmutableOimfTrait>> tryTransformFiles(@NotNull File... files)
	{
		TransformationResult<Set<ImmutableOimfTrait>> transformResult = new FileModel2OimfModelTransformator()
				.transform(Arrays.asList(files));
		return transformResult;
	}

	@NotNull
	public static File parseResource(@NotNull String resourceName) throws IOException
	{
		Rule fileRule = Parboiled.createParser(OimfParser.class).File();
		TracingParseRunner<File> parseRunner = new TracingParseRunner<>(fileRule);
		final ParsingResult<File> result = parseRunner
				.run(Resources.toString(Resources.getResource(resourceName), Charsets.UTF_8));
		Preconditions.checkState(result.matched, "parse error");
		return result.resultValue;
	}

	@NotNull
	public static TransformationResult<Set<ImmutableOimfTrait>> transformFiles(@NotNull File... files)
	{
		TransformationResult<Set<ImmutableOimfTrait>> transformResult = new FileModel2OimfModelTransformator()
				.transform(Arrays.asList(files));
		Preconditions.checkState(transformResult.isSuccess(), "transformation errors: '%s'", transformResult.getErrors());
		return transformResult;
	}
}
