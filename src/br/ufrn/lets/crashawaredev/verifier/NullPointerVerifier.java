package br.ufrn.lets.crashawaredev.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.ufrn.lets.exceptionexpert.models.ASTExceptionRepresentation;
import br.ufrn.lets.exceptionexpert.models.MethodRepresentation;
import br.ufrn.lets.exceptionexpert.models.ReturnMessage;

public class NullPointerVerifier extends PatternVerifier{
	
	public NullPointerVerifier(ASTExceptionRepresentation astRep, ILog log) {
		super(astRep, log);
	}

	@Override
	public List<ReturnMessage> verify() {
		
		List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
		
		List<MethodRepresentation> methods = astRep.getMethods();
		
		for(MethodRepresentation method : methods) {
			
			List<Statement> stmts = method.getMethodDeclaration().getBody().statements();
			
			List<VariableDeclarationStatement> findByPKStatements = new ArrayList<>();
			
			for(Object stmt : stmts) {
				if(stmt instanceof VariableDeclarationStatement && isFindByPKDeclaration(stmt))
					findByPKStatements.add((VariableDeclarationStatement) stmt);
			}
			
			for(VariableDeclarationStatement varStmt : findByPKStatements) {
				String varName = ((VariableDeclarationFragment) varStmt.fragments().get(0)).getName().toString();
				// TODO
//				method.getMethodDeclaration().getpos
			}
			
		}
		
		return null;
	}
	
	private boolean isFindByPKDeclaration(Object stmt) {
		final String WHITESPACE = "[ \t\n\r\f]";
		
		Pattern pattern = Pattern.compile(".*" + WHITESPACE + "+.*=.*findByPrimaryKey.*");
		Matcher matcher = pattern.matcher(stmt.toString());
		if(matcher.find())
			return true;
		
		return false;
	}
	
}
