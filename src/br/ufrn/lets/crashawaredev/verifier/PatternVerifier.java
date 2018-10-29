package br.ufrn.lets.crashawaredev.verifier;

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.runtime.ILog;

import br.ufrn.lets.exceptionexpert.models.ASTExceptionRepresentation;
import br.ufrn.lets.exceptionexpert.models.ReturnMessage;

public abstract class PatternVerifier {
	
	protected final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	
	/**
	 * Abstract Syntax Tree representation of target class
	 */
	ASTExceptionRepresentation astRep;
	
	private ILog log;
	
	protected abstract List<ReturnMessage> verify();
	
	public PatternVerifier(ASTExceptionRepresentation astRep, ILog log) {
		this.astRep = astRep;
		this.log = log;
	}
	

}
