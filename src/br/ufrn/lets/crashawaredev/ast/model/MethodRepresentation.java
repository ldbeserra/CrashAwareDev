package br.ufrn.lets.crashawaredev.ast.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

public class MethodRepresentation {

	private ASTRepresentation astRep;
	
	private MethodDeclaration methodDeclaration;
	
	private List<CatchClause> catchClauses;

	private List<ThrowStatement> throwStatements;

	private List<VariableDeclarationStatement> variableDeclarationsStmt;
	
	public MethodRepresentation() {
		setCatchClauses(new ArrayList<CatchClause>());
		throwStatements = new ArrayList<ThrowStatement>();
		variableDeclarationsStmt = new ArrayList<VariableDeclarationStatement>();
	}

	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.methodDeclaration = methodDeclaration;
	}

	public List<ThrowStatement> getThrowStatements() {
		return throwStatements;
	}

	public void setThrowStatements(List<ThrowStatement> throwStatements) {
		this.throwStatements = throwStatements;
	}

	public List<CatchClause> getCatchClauses() {
		return catchClauses;
	}

	public void setCatchClauses(List<CatchClause> catchClauses) {
		this.catchClauses = catchClauses;
	}

	public ASTRepresentation getAstRep() {
		return astRep;
	}

	public void setAstRep(ASTRepresentation astRep) {
		this.astRep = astRep;
	}

	public List<VariableDeclarationStatement> getVariableDeclarationsStmt() {
		return variableDeclarationsStmt;
	}

	public void setVariableDeclarationsStmt(List<VariableDeclarationStatement> variableDeclarationsStmt) {
		this.variableDeclarationsStmt = variableDeclarationsStmt;
	}
	
}
