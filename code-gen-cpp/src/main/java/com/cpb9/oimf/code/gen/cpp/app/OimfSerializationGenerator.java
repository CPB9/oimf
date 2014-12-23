package com.cpb9.oimf.code.gen.cpp.app;

import com.cpb9.oimf.code.gen.cpp.OimfCppGenerator;
import com.cpb9.oimf.code.gen.cpp.OimfCppGeneratorConfiguration;
import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.ParserUtils;
import com.cpb9.oimf.parser.TransformationResult;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class OimfSerializationGenerator
{
	public static void main(String[] args)
	{
		OimfCppGeneratorConfiguration config = new OimfCppGeneratorConfiguration();
		CmdLineParser parser = new CmdLineParser(config);
		try
		{
			parser.parseArgument(args);
			HashMap<String, String> mappings = new HashMap<String, String>() {{
					put("com.cpb9.core.I8", "QtGlobal/qint8");
					put("com.cpb9.core.U8", "QtGlobal/quint8");
					put("com.cpb9.core.I16", "QtGlobal/qint16");
					put("com.cpb9.core.U16", "QtGlobal/quint16");
					put("com.cpb9.core.I32", "QtGlobal/qint32");
					put("com.cpb9.core.U32", "QtGlobal/quint32");
					put("com.cpb9.core.I64", "QtGlobal/qint64");
					put("com.cpb9.core.U64", "QtGlobal/quint64");
					put("com.cpb9.core.F32", "float");
					put("com.cpb9.core.F64", "double");
					put("com.cpb9.core.Variant", "QVariant/QVariant");

					put("oimf.AsciiChar", "char");
					put("oimf.Boolean", "bool");
					put("oimf.Size", "cstddef/std::size_t");
					put("oimf.Unit", "void");
				}};

			mappings.putAll(config.getTraitMappings());
			config.setTraitMappings(mappings);

			TransformationResult<Set<ImmutableOimfTrait>>
					result = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"),
					ParserUtils.parseResource("Serialization.oimf"));

			new OimfCppGenerator(config, result.getValue()).generate();
		}
		catch (CmdLineException e)
		{
			e.printStackTrace();
			parser.printUsage(System.out);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
