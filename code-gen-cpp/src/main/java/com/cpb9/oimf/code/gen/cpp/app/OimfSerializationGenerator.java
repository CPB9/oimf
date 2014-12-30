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
					put("oimf.serialization.I8", "QtGlobal/qint8");
					put("oimf.serialization.U8", "QtGlobal/quint8");
					put("oimf.serialization.I16", "QtGlobal/qint16");
					put("oimf.serialization.U16", "QtGlobal/quint16");
					put("oimf.serialization.I32", "QtGlobal/qint32");
					put("oimf.serialization.U32", "QtGlobal/quint32");
					put("oimf.serialization.I64", "QtGlobal/qint64");
					put("oimf.serialization.U64", "QtGlobal/quint64");
					put("oimf.serialization.F32", "float");
					put("oimf.serialization.F64", "double");
					put("oimf.serialization.Variant", "QVariant/QVariant");

					put("oimf.AsciiChar", "char");
					put("oimf.AsciiString", "QString/QString");
					put("oimf.Boolean", "bool");
					put("oimf.Size", "cstddef/std::size_t");
					put("oimf.Unit", "void");
					put("oimf.Value", "QVariant/QVariant");
				}};

			mappings.putAll(config.getTraitMappings());
			config.setTraitMappings(mappings);
			config.setOwnershipStyle(OimfCppGeneratorConfiguration.OwnershipStyle.VALUE);
			config.setInheritanceStyle(OimfCppGeneratorConfiguration.InheritanceStyle.USE_DERIVED_CLASSES);

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
