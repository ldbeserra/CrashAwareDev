package br.ufrn.lets.crashawaredev.verifier;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.ILog;

import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.FieldRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.MethodRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.ReturnMessage;

public class GetSetPresentVerifier extends PatternVerifier {

	public GetSetPresentVerifier(ASTRepresentation astRep, ILog log) {
		super(astRep, log);
	}

	@Override
	public List<ReturnMessage> verify() {
		
		List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
		
		if(astRep.getTypeDeclaration() != null) {
			
			String className = astRep.getTypeDeclaration().getName().toString();
			
			if(className.endsWith("MBean") || className.endsWith("Form")) {
				
				for(FieldRepresentation field : astRep.getFields()) {
	
					if(field.getFieldDeclaration().fragments() != null &&  field.getFieldDeclaration().fragments().size() > 0) {
					
						if(field.getFieldDeclaration().getModifiers() == Modifier.PRIVATE) {
	
							String fieldName = field.getFieldDeclaration().fragments().get(0).toString();
							String getName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
							String setName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
							
							boolean getFound = false;
							boolean setFound = false;
							
							for(MethodRepresentation method : astRep.getMethods()) {
							
								if(method.getMethodDeclaration().getName().toString().equals(getName))
									getFound = true;
								
								if(method.getMethodDeclaration().getName().toString().equals(setName))
									setFound = true;
								
							}
							
							if(!getFound || !setFound) {
								ReturnMessage rm = new ReturnMessage();
								rm.setMessage("Variável private sem método get ou set associado.");
								rm.setLineNumber(astRep.getAstRoot().getLineNumber(field.getFieldDeclaration().getStartPosition()));
								rm.setMarkerSeverity(IMarker.SEVERITY_WARNING);
								messages.add(rm);
							}
						}
					}
					
				}
			}
			
		}
		
		return messages;
	}

}
