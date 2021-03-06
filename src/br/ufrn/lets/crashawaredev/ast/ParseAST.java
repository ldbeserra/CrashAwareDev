package br.ufrn.lets.crashawaredev.ast;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.FieldRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.MethodRepresentation;

public class ParseAST {

	public static CompilationUnit parse(ICompilationUnit cu) {
		
		CompilationUnit astRoot = parseUnit(cu);
		
		return astRoot;
		
	}

	private static CompilationUnit parseUnit(ICompilationUnit unit) {
		ASTParser parser = ASTParser.newParser(AST.JLS8); 
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit); // set source
		parser.setResolveBindings(true); // we need bindings later on
		return (CompilationUnit) parser.createAST(null /* IProgressMonitor */); // parse
	}
	
	/**
	 * Get a class (changed class) AST and extract:
	 * - its package, 
	 * - its name, 
	 * - a map with method X throws clauses and
	 * - a map with method X catch clauses. 
	 * @param astRoot
	 * @return
	 */
	public static ASTRepresentation parseClassASTToARep(CompilationUnit astRoot) {
		//Ref: http://www.programcreek.com/2012/06/insertadd-statements-to-java-source-code-by-using-eclipse-jdt-astrewrite/
		//Ref: http://www.programcreek.com/java-api-examples/index.php?api=org.eclipse.jdt.core.dom.MethodDeclaration

		final ASTRepresentation astRep = new ASTRepresentation();
		
		astRep.setAstRoot(astRoot);
		
		final CompilationUnit astRootFinal = astRoot;

		astRootFinal.accept(new ASTVisitor() {
			
			private MethodRepresentation lastMethod;
			
			public boolean visit(CompilationUnit node) {
				astRep.setPackageDeclaration(node.getPackage());
				return true;
			}

			public boolean visit(TypeDeclaration node) {
				astRep.setTypeDeclaration(node);
				return true;
			}
			
			public boolean visit(FieldDeclaration node) {
				FieldRepresentation fr = new FieldRepresentation();
				fr.setFieldDeclaration(node);
			
				astRep.getFields().add(fr);
				
				return true;
			}
			
			public boolean visit(MethodDeclaration node) {
				MethodRepresentation mr = new MethodRepresentation();
				mr.setMethodDeclaration(node);
				mr.setAstRep(astRep);
				
				lastMethod = mr;

				astRep.getMethods().add(mr);
				
				return true;
			}

			public boolean visit(ThrowStatement node) {
				lastMethod.getThrowStatements().add(node);
				return true;
			}
			
			public boolean visit(CatchClause node) {
				lastMethod.getCatchClauses().add(node);
				return true;
			}
			
			public boolean visit(VariableDeclarationStatement node) {
				lastMethod.getVariableDeclarationsStmt().add(node);
				return true;
			}
			
		});
		
		return astRep;
	}

}
