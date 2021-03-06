package dtool.resolver;

import static melnorme.utilbox.core.Assert.AssertNamespace.assertFail;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import melnorme.utilbox.misc.StringUtil;
import dtool.DeeNamingRules;
import dtool.ast.definitions.Module;
import dtool.parser.DeeParser;
import dtool.parser.DeeParserResult;
import dtool.resolver.BaseResolverSourceTests.ITestsModuleResolver;
import dtool.tests.DToolBaseTest;

public final class TestsSimpleModuleResolver implements ITestsModuleResolver {
	
	protected File projectFolder;
	protected Map<String, DeeParserResult> modules = new HashMap<>();
	protected String extraModuleName;
	protected DeeParserResult extraModuleResult;
	
	public TestsSimpleModuleResolver(File projectFolder) {
		this.projectFolder = projectFolder;
		
		initModules(projectFolder, "");
	}
	
	public void setExtraModule(String extraModuleName, DeeParserResult extraModuleResult) {
		this.extraModuleName = extraModuleName;
		this.extraModuleResult = extraModuleResult;
	}
	
	@Override
	public void cleanupChanges() {
		extraModuleName = null;
		extraModuleResult = null;
	}
	
	public void initModules(File projectFolder, String namePrefix) {
		File[] children = projectFolder.listFiles();
		assertNotNull(children);
		for (File child : children) {
			if(child.isDirectory()) {
				String packageName = child.getName();
				assertTrue(DeeNamingRules.isValidDIdentifier(packageName));
				initModules(child, namePrefix + packageName + ".");
			} else if(child.getName().endsWith(".d")) {
				String moduleName = child.getName().replaceFirst(".d$", "");
				assertTrue(DeeNamingRules.isValidDIdentifier(moduleName));
				String source = DToolBaseTest.readStringFromFileUnchecked(child);
				DeeParserResult parseResult = DeeParser.parseSource(source, moduleName);
				modules.put(namePrefix + moduleName, parseResult);
			} else {
				assertFail();
			}
		}
	}
	
	@Override
	public String[] findModules(String fqNamePrefix) throws Exception {
		ArrayList<String> matchedModules = new ArrayList<>();
		Set<String> nameEntries = new HashSet<>(modules.keySet());
		if(extraModuleName != null) {
			nameEntries.add(extraModuleName);
		}
		
		for (String moduleName : nameEntries) {
			if(moduleName.startsWith(fqNamePrefix)) {
				matchedModules.add(moduleName);
			}
		}
		return matchedModules.toArray(new String[0]);
	}
	
	@Override
	public Module findModule(String[] packages, String module) throws Exception {
		String fullName = StringUtil.collToString(packages, ".");
		if(packages.length > 0) {
			fullName += ".";
		}
		fullName += module;
		return findModule(fullName);
	}
	
	public Module findModule(String fullName) {
		if(extraModuleName != null && fullName.equals(extraModuleName)) {
			return extraModuleResult.module;
		}
		DeeParserResult moduleEntry = modules.get(fullName);
		return moduleEntry == null ? null : moduleEntry.module;
	}
	
}