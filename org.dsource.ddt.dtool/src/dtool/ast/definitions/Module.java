package dtool.ast.definitions;

import static dtool.util.NewUtils.assertCast;
import static melnorme.utilbox.core.Assert.AssertNamespace.assertNotNull;
import dtool.ast.ASTCodePrinter;
import dtool.ast.ASTNode;
import dtool.ast.ASTNodeTypes;
import dtool.ast.IASTVisitor;
import dtool.ast.references.RefModule;
import dtool.parser.BaseLexElement;
import dtool.parser.IToken;
import dtool.parser.Token;
import dtool.resolver.CommonDefUnitSearch;
import dtool.resolver.IScopeNode;
import dtool.resolver.ReferenceResolver;
import dtool.util.ArrayView;
import dtool.util.NewUtils;

/**
 * D Module. 
 * The top-level AST class, has no parent, is the first and main node of every compilation unit.
 */
public class Module extends DefUnit implements IScopeNode {
	
	public static class ModuleDefSymbol extends DefSymbol {
		
		protected Module module;
		
		public ModuleDefSymbol(String id) {
			super(id);
		}
		
		@Override
		protected ASTNode getParent_Concrete() {
			return assertCast(parent, DeclarationModule.class);
		}
		
		@Override
		public DefUnit getDefUnit() {
			return module;
		}
	}
	
	public static class DeclarationModule extends ASTNode {
		
		public final Token[] comments;
		public final ArrayView<IToken> packageList;
		public final String[] packages; // Old API
		public final DefSymbol moduleName; 
		
		public DeclarationModule(Token[] comments, ArrayView<IToken> packageList, BaseLexElement moduleDefUnit) {
			this.comments = comments;
			this.packageList = assertNotNull(packageList);
			this.packages = RefModule.tokenArrayToStringArray(packageList);
			this.moduleName = new ModuleDefSymbol(moduleDefUnit.getSourceValue());
			this.moduleName.setSourceRange(moduleDefUnit.getSourceRange());
			this.moduleName.setParsedStatus();
			parentize(moduleName);
		}
		
		public ModuleDefSymbol getModuleSymbol() {
			return (ModuleDefSymbol) moduleName;
		}
		
		@Override
		public ASTNodeTypes getNodeType() {
			return ASTNodeTypes.DECLARATION_MODULE;
		}
		
		@Override
		public void visitChildren(IASTVisitor visitor) {
			//TreeVisitor.acceptChildren(visitor, packages);
			acceptVisitor(visitor, moduleName);
		}
		
		@Override
		public void toStringAsCode(ASTCodePrinter cp) {
			cp.append("module ");
			cp.appendTokenList(packageList, ".", true);
			cp.append(moduleName.name);
			cp.append(";");
		}
		
	}
	
	public static Module createModuleNoModuleDecl(String moduleName, ArrayView<ASTNode> members) {
		ModuleDefSymbol defSymbol = new ModuleDefSymbol(moduleName);
		return new Module(defSymbol, null, members);
	}
	
	public final DeclarationModule md;
	public final ArrayView<ASTNode> members;
	
	public Module(ModuleDefSymbol defSymbol, DeclarationModule md, ArrayView<ASTNode> members) {
		super(defSymbol, false);
		defSymbol.module = this;
		this.md = parentize(md);
		this.members = parentize(members);
		assertNotNull(members);
	}
	
	@Override
	public ASTNodeTypes getNodeType() {
		return ASTNodeTypes.MODULE;
	}
	
	@Override
	public void visitChildren(IASTVisitor visitor) {
		acceptVisitor(visitor, md);
		acceptVisitor(visitor, members);
	}
	
	@Override
	public void toStringAsCode(ASTCodePrinter cp) {
		cp.append(md, cp.ST_SEP);
		cp.appendList(members, cp.ST_SEP);
	}
	
	@Override
	public EArcheType getArcheType() {
		return EArcheType.Module;
	}
	
	@Override
	public Token[] getDocComments() {
		if(md != null) {
			return md.comments;
		}
		return null;
	}
	
	@Override
	public String getFullyQualifiedName() {
		ASTCodePrinter cp = new ASTCodePrinter();
		if(md != null) {
			cp.appendTokenList(md.packageList, ".", true);
		}
		cp.append(getName());
		return cp.toString();
	}
	
	@Override
	public String getModuleFullyQualifiedName() {
		return getFullyQualifiedName();
	}
	
	public String[] getDeclaredPackages() {
		if(md != null) {
			return md.packages;
		}
		return NewUtils.EMPTY_STRING_ARRAY;
	}
	
	@Override
	public void resolveSearchInMembersScope(CommonDefUnitSearch search) {
		ReferenceResolver.resolveSearchInScope(search, this);
	}
	
	@Override
	public void resolveSearchInScope(CommonDefUnitSearch search) {
		ReferenceResolver.findInNodeList(search, members, false);
	}
	
}