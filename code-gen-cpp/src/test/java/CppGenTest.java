import com.cpb9.oimf.code.gen.cpp.OimfCppGenerator;
import com.cpb9.oimf.code.gen.cpp.OimfCppGeneratorConfiguration;
import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.ParserUtils;
import com.cpb9.oimf.parser.TransformationResult;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class CppGenTest
{
	@Test
	public void testListTest() throws IOException
	{
		TransformationResult<Set<ImmutableOimfTrait>>
				result = ParserUtils.transformFiles(ParserUtils.parseResource("List_test.oimf"));
		OimfCppGeneratorConfiguration config = new OimfCppGeneratorConfiguration();
		config.setOutputPath(new java.io.File("oimf-gen"));
		new OimfCppGenerator(config, result.getValue()).generate();
	}

	@Test
	public void testOimf() throws IOException
	{
		TransformationResult<Set<ImmutableOimfTrait>>
				result = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"),
				ParserUtils.parseResource("OimfRuntime.oimf"), ParserUtils.parseResource("Core.oimf"));
		OimfCppGeneratorConfiguration config = new OimfCppGeneratorConfiguration();
		config.setOutputPath(new java.io.File("oimf-cpp-gen/"));
		config.setTraitMappings(new HashMap<String, String>()
		{{
				put("com.cpb9.core.I8", "QtGlobal/qint8");
				put("com.cpb9.core.U8", "QtGlobal/quint8");
				put("com.cpb9.core.I16", "QtGlobal/qint16");
				put("com.cpb9.core.U16", "QtGlobal/quint16");
				put("com.cpb9.core.I32", "QtGlobal/qint32");
				put("com.cpb9.core.U32", "QtGlobal/quint32");
				put("com.cpb9.core.I64", "QtGlobal/qint64");
				put("com.cpb9.core.U64", "QtGlobal/quint64");
				put("com.cpb9.core.F32", "stdint.h/float32_t");
				put("com.cpb9.core.F64", "stdint.h/float64_t");
				put("com.cpb9.core.Variant", "QVariant/QVariant");

				put("oimf.Char", "char");
				put("oimf.Boolean", "bool");
				put("oimf.Size", "cstddef/std::size_t");
				put("oimf.Unit", "void");
				put("oimf.AnsiString", "QString/QString");
			}});
		config.setOwnershipStyle(OimfCppGeneratorConfiguration.OwnershipStyle.POINTER);
		new OimfCppGenerator(config, result.getValue()).generate();
	}
}
