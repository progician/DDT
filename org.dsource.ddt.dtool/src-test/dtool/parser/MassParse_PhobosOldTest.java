package dtool.parser;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class MassParse_PhobosOldTest extends MassParse__CommonTest {
	
	@Parameters(name="{index}: {0}")
	public static Collection<Object[]> filesToParse() throws IOException {
		return getTestFilesFromFolderAsParameterList(getCommonResource(TESTSRC_PHOBOS1_OLD), true);
	}
	
	public MassParse_PhobosOldTest(String testDescription, File file) {
		super(testDescription, file);
	}
	
	@Override
	protected boolean failOnSyntaxErrors() {
		// allow syntax errors, because this is a D1 source.
		return false;
	}
	
}
