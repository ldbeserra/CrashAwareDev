package br.ufrn.lets.crashawaredev.verifier;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.ILog;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.ReturnMessage;

public abstract class PatternVerifier {
	
	protected final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * Abstract Syntax Tree representation of target class
	 */
	ASTRepresentation astRep;
	
	private ILog log;
	
	public abstract List<ReturnMessage> verify();
	
	public PatternVerifier(ASTRepresentation astRep, ILog log) {
		this.astRep = astRep;
		this.log = log;
	}
	
	/**
	 * Valida se a variável passada foi validada como não nula
	 * @param var
	 * @param methodBody
	 * @return
	 */
	public boolean argumentValidated(VariableDeclaration var, String methodBody) {
		
		// Não vamos validar tipos primitivos, pois não gera null pointer
		if(var.resolveBinding().getType().isPrimitive())
			return true;
		
		String varName = var.getName().toString();
				
		// Valida se existe checkagem da variável igual ou diferente de null
		Pattern pattern = Pattern.compile(varName + "\\s*(==|!=)\\s*null");
		Matcher matcher = pattern.matcher(methodBody);
		if(matcher.find())
			return true;
		
		// Valida se foi usado o ValidatorUtil para checkar a variável
		pattern = Pattern.compile("ValidatorUtil\\.is(Not)*Empty\\(\\s*" + varName);
		matcher = pattern.matcher(methodBody);
		if(matcher.find())
			return true;
		
		return false;
			
	}
	
}
