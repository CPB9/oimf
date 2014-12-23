import com.cpb9.oimf.code.gen.cpp.OimfCppGenerator;
import com.cpb9.oimf.code.gen.cpp.OimfCppGeneratorConfiguration;
import com.cpb9.oimf.code.gen.cpp.app.OimfSerializationGenerator;
import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.ParserUtils;
import com.cpb9.oimf.parser.TransformationResult;
import org.junit.Test;

import java.io.IOException;
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
	public void testOimfRuntime() throws IOException
	{
		OimfSerializationGenerator.main(new String[]{});
	}
}
