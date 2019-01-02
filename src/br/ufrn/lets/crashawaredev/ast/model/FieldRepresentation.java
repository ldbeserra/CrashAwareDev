package br.ufrn.lets.crashawaredev.ast.model;

import org.eclipse.jdt.core.dom.FieldDeclaration;

public class FieldRepresentation {

	private ASTRepresentation astRep;
	
	private FieldDeclaration fieldDeclaration;

	public ASTRepresentation getAstRep() {
		return astRep;
	}

	public void setAstRep(ASTRepresentation astRep) {
		this.astRep = astRep;
	}

	public FieldDeclaration getFieldDeclaration() {
		return fieldDeclaration;
	}

	public void setFieldDeclaration(FieldDeclaration fieldDeclaration) {
		this.fieldDeclaration = fieldDeclaration;
	}
	
}
