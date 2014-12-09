package com.cpb9.oimf.codegen;

/**
 * @author Artem Shein
 */
public class CLangInterfacesCodeGen
{
	public static void main(String[] args)
	{
//		final ParsingResult<File> result;
//		try
//		{
//			result = new TracingParseRunner<File>(Parboiled.createParser(OimfParser.class).File())
//					.run(Resources.toString(Resources.getResource("Clang.oimf"), Charsets.UTF_8));
//			Preconditions.checkState(result.matched);
//			final ParsingResult<File> result2 = new TracingParseRunner<File>(Parboiled.createParser(OimfParser.class).File())
//					.run(Resources.toString(Resources.getResource("Core.oimf"), Charsets.UTF_8));
//			Preconditions.checkState(result2.matched);
//			System.out.println(result.resultValue);
//			System.out.println(result2.resultValue);
//			TransformationResult<Set<ImmutableOimfTrait>> transformResult = new FileModel2OimfModelTransformator()
//					.transform(Arrays.asList(result.resultValue, result2.resultValue));
//			Preconditions.checkState(transformResult.isSuccess());
//			System.out.println(transformResult);
//			OimfJavaGeneratorConfiguration config = new OimfJavaGeneratorConfiguration();
//			config.getNamespaceAliases().put("com.cpb9.test", "test");
//			config.setOutputPath(new java.io.File(System.getProperty("user.dir")));
////			new OimfJavaGenerator(config, transformResult.getValue()).generate();
//		}
//		catch (IOException e)
//		{
//			e.printStackTrace();
//		}
	}
}
