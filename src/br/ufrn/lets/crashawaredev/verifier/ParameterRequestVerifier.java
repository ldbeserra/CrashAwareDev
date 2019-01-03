package br.ufrn.lets.crashawaredev.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.MethodRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.ReturnMessage;

public class ParameterRequestVerifier extends PatternVerifier {

	public ParameterRequestVerifier(ASTRepresentation astRep, ILog log) {
		super(astRep, log);
	}

	@Override
	public List<ReturnMessage> verify() {
		List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
		
		List<MethodRepresentation> methods = astRep.getMethods();
		
		for(MethodRepresentation method : methods) {
			
			String methodBodyString = method.getMethodDeclaration().getBody().statements().toString();
			
			for(VariableDeclarationStatement varStmt : method.getVariableDeclarationsStmt()) {
				
				if(isGetParameterDeclaration(varStmt)) {
					List<VariableDeclarationFragment> fragments = varStmt.fragments();

					if(!argumentValidated(fragments.get(0), methodBodyString)) {
						
						ReturnMessage rm = new ReturnMessage();
						rm.setMessage("Nenhuma validação foi feita se a variável \"" 
								+ fragments.get(0).getName().toString() + "\" é não nula.\n"	
								+ "É aconselhável sempre validar se o objeto foi passado corretamente na request.");
						rm.setLineNumber(astRep.getAstRoot().getLineNumber(varStmt.getStartPosition()));
						rm.setMarkerSeverity(IMarker.SEVERITY_INFO);
						messages.add(rm);
						
					}
				}
				
			}
			
		}
		
		return messages;
	}
	
	private boolean isGetParameterDeclaration(Object stmt) {
		final String WHITESPACE = "[ \t\n\r\f]";
		
		Pattern pattern = Pattern.compile(".*" + WHITESPACE + ".*=.*getParameter.*;");
		Matcher matcher = pattern.matcher(stmt.toString());
		if(matcher.find())
			return true;
		
		return false;
	}

}
