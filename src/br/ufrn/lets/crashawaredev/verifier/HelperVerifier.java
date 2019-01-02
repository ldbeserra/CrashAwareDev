package br.ufrn.lets.crashawaredev.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.MethodRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.ReturnMessage;

public class HelperVerifier extends PatternVerifier {

	public HelperVerifier(ASTRepresentation astRep, ILog log) {
		super(astRep, log);
	}

	@Override
	public List<ReturnMessage> verify() {
		
		List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
		
		String className = astRep.getTypeDeclaration().getName().toString();
		
		if(className.endsWith("Helper") || className.endsWith("Util") || className.endsWith("Utils")) {
			
			List<MethodRepresentation> methods = astRep.getMethods();
			
			for(MethodRepresentation method : methods) {
				
				String methodBodyString = method.getMethodDeclaration().getBody().statements().toString();
				
				List<SingleVariableDeclaration> varsToValidate = new ArrayList<>();
				
				for(Object param : method.getMethodDeclaration().parameters()) {
					
					SingleVariableDeclaration var = (SingleVariableDeclaration) param;
					
					if(!argumentValidated(var, methodBodyString)) {
						varsToValidate.add(var);
					}
				}
				
				if(!varsToValidate.isEmpty()) {
					
					String varStr = "(";
					for(SingleVariableDeclaration svar : varsToValidate) {
						varStr += svar.toString() + ", ";
					}
					varStr = varStr.substring(0, varStr.length()-2) + ")";	
										
					ReturnMessage rm = new ReturnMessage();
					rm.setMessage("Os argumentos deste método devem ser validados para evitar potenciais null pointers."
							+ "\nValide cada um dos seguintes argumentos antes de referenciá-los: " + varStr);
					rm.setLineNumber(astRep.getAstRoot().getLineNumber(method.getMethodDeclaration().getStartPosition()));
					rm.setMarkerSeverity(IMarker.SEVERITY_WARNING);
					messages.add(rm);
				}
			}
		}
		
		return messages;
		
	}
	
}
