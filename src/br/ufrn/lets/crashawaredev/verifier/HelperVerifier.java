package br.ufrn.lets.crashawaredev.verifier;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

import br.ufrn.lets.exceptionexpert.models.ASTExceptionRepresentation;
import br.ufrn.lets.exceptionexpert.models.MethodRepresentation;
import br.ufrn.lets.exceptionexpert.models.ReturnMessage;

public class HelperVerifier extends PatternVerifier {

	public HelperVerifier(ASTExceptionRepresentation astRep, ILog log) {
		super(astRep, log);
	}

	@Override
	public List<ReturnMessage> verify() {
		
		List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
		
		String className = astRep.getTypeDeclaration().getName().toString();
		
		if(className.endsWith("Helper")) {
			
			List<MethodRepresentation> methods = astRep.getMethods();
			
			for(MethodRepresentation method : methods) {
				
				String methodBodyString = method.getMethodDeclaration().getBody().statements().toString();
				
				for(Object param : method.getMethodDeclaration().parameters()) {
					
					SingleVariableDeclaration var = (SingleVariableDeclaration) param;
					
					if(!validateArgument(var, methodBodyString)) {
						ReturnMessage rm = new ReturnMessage();
						rm.setMessage("Valide os argumentos deste método!");
						rm.setLineNumber(astRep.getAstRoot().getLineNumber(method.getMethodDeclaration().getStartPosition()));
						rm.setMarkerSeverity(IMarker.SEVERITY_WARNING);
						messages.add(rm);
					}
					
				}
				
			}
			
		}
		
		return messages;
		
	}
	
	private boolean validateArgument(SingleVariableDeclaration var, String methodBody) {
		
		// Não vamos validar tipos primitivos, pois não gera null pointer
		if(var.getType().isPrimitiveType())
			return true;
		
		String varName = var.getName().toString();
		
		// Valida se existe checkagem da variável igual ou diferente de null
		Pattern pattern = Pattern.compile(varName + "\\s*==|!=\\s*null");
		Matcher matcher = pattern.matcher(methodBody);
		if(matcher.find())
			return true;
		
		// Valida se foi usado o ValidatorUtil para checkar a variável
		pattern = Pattern.compile("ValidatorUtil\\.is(Not)Empty\\(\\s*" + varName);
		matcher = pattern.matcher(methodBody);
		if(matcher.find())
			return true;
		
		return false;
		
	}

}
