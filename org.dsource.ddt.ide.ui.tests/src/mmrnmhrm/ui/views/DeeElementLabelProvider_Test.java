package mmrnmhrm.ui.views;

import mmrnmhrm.tests.ui.BaseDeeUITest;

import org.junit.Test;

import dtool.ast.declarations.ModuleProxy;
import dtool.ast.declarations.PackageNamespace;
import dtool.ast.definitions.INamedElement;
import dtool.resolver.DefUnitSearch;
import dtool.resolver.LanguageIntrinsics;
import dtool.resolver.ReferenceResolver;
import dtool.resolver.api.NullModuleResolver;

public class DeeElementLabelProvider_Test extends BaseDeeUITest {
	
	@Test
	public void testBasic() throws Exception { testBasic$(); }
	public void testBasic$() throws Exception {
		
		INamedElement defElement;
		defElement = new ModuleProxy("foo", null);
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "foo");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "foo");
		
		defElement = new ModuleProxy("pack.mod", null);
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "pack.mod");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "mod");
		
		defElement = new ModuleProxy("pack.sub.mod", null);
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "pack.sub.mod");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "mod");
		
		
		defElement = PackageNamespace.createPartialDefUnits(array("pack"), new ModuleProxy("modA", null));
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "pack");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "pack");

		defElement = PackageNamespace.createPartialDefUnits(array("pack", "sub"), new ModuleProxy("modA", null));
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "pack");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "pack");
		
		
		DefUnitSearch search = new DefUnitSearch("int", null, -1, true, new NullModuleResolver());
		ReferenceResolver.findDefUnitInScope(LanguageIntrinsics.d_2_063_intrinsics.primitivesScope, search);
		defElement = search.getMatchedElements().iterator().next();
		
		assertEquals(DeeElementLabelProvider.getLabelForHoverSignature(defElement), "int");
		assertEquals(DeeElementLabelProvider.getLabelForContentAssistPopup(defElement), "int");
		
	}
	
}