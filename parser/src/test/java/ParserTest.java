import com.cpb9.oimf.model.ImmutableOimfTrait;
import com.cpb9.oimf.parser.ParserUtils;
import com.cpb9.oimf.parser.TransformationResult;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Set;

/**
 * @author Artem Shein
 */
public class ParserTest
{
	@Test
	public void testOimf() throws IOException
	{
		TransformationResult<Set<ImmutableOimfTrait>>
				transformationResult = ParserUtils.transformFiles(ParserUtils.parseResource("Oimf.oimf"));
		boolean isTraitFound = false;
		for (ImmutableOimfTrait trait : transformationResult.getValue())
		{
			if ("oimf.Trait".equals(trait.getGuid().toString()))
			{
				isTraitFound = true;
				Assert.assertEquals("must have 5 fields", 5, trait.getFields().size());
				Assert.assertFalse("field name must not be empty", trait.getFields().get(0).getName().isEmpty());
			}
		}
		Assert.assertTrue("Trait oimf.Trait not found", isTraitFound);
	}


}
