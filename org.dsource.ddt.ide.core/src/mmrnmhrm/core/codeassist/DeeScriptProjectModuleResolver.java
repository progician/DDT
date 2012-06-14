package mmrnmhrm.core.codeassist;

import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ModelException;

import dtool.ast.definitions.Module;
import mmrnmhrm.core.parser.DLTKModuleResolver;

public class DeeScriptProjectModuleResolver extends DLTKModuleResolver {
	
	protected final IScriptProject scriptProject;

	public DeeScriptProjectModuleResolver(IScriptProject scriptProject) {
		this.scriptProject = scriptProject;
	}
	
	@Override
	protected Module findModule(String[] packages, String modName, IScriptProject deeproj) throws ModelException {
		return super.findModule(packages, modName, this.scriptProject);
	}
	
	@Override
	protected String[] findModules(String fqNamePrefix, IScriptProject scriptProject) throws ModelException {
		return super.findModules(fqNamePrefix, this.scriptProject);
	}
	
}