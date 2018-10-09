package br.ufrn.lets.exceptionexpert.startup;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;

import br.ufrn.lets.exceptionexpert.ast.ParseAST;
import br.ufrn.lets.exceptionexpert.models.ASTExceptionRepresentation;
import br.ufrn.lets.exceptionexpert.models.MethodRepresentation;
import br.ufrn.lets.exceptionexpert.models.ReturnMessage;

public class StartupClass implements IStartup {
    
	private static final String PLUGIN_LOG_IDENTIFIER = "br.ufrn.lets.exceptionExpert";

	protected final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	List<ReturnMessage> messages = new ArrayList<ReturnMessage>();
	
	// List with projects that do not have a properly configuration, i.e., the contract.xml file configured. 
	List<IProject> projectsWithoutConfig = new ArrayList<>();
	
	//http://stackoverflow.com/questions/28481943/proper-logging-for-eclipse-plug-in-development
	private ILog log = Activator.getDefault().getLog();
	
	@Override
	public void earlyStartup() {
		Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		    	
		    	log.log(new Status(Status.INFO, PLUGIN_LOG_IDENTIFIER, "INFO - Initializing ExceptionPolicyExpert Plug-in..."));
		    	
		    	IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
			    IEditorPart part = page.getActiveEditor();
			    if (!(part instanceof AbstractTextEditor))
			      return;

				//Configures the change listener
				IWorkspace workspace = ResourcesPlugin.getWorkspace();
				IResourceChangeListener listener = new IResourceChangeListener() {
					public void resourceChanged(IResourceChangeEvent event) {
						
						//we are only interested in POST_BUILD events
						if (event.getType() != IResourceChangeEvent.POST_BUILD)
				            return;
						
						IResourceDelta rootDelta = event.getDelta();
						
						//List with all .java changed files
						final ArrayList<IResource> changedClasses = new ArrayList<IResource>();

						//Visit the children to get all .java changed files
						IResourceDeltaVisitor visitor = new IResourceDeltaVisitor() {
							public boolean visit(IResourceDelta delta) {
								//only interested in changed resources (not added or removed)
								if (delta.getKind() != IResourceDelta.CHANGED)
									return true;
								//only interested in content changes
								if ((delta.getFlags() & IResourceDelta.CONTENT) == 0)
									return true;
								IResource resource = delta.getResource();
								//only interested in files with the "java" extension
								if (resource.getType() == IResource.FILE && 
										"java".equalsIgnoreCase(resource.getFileExtension())) {
									changedClasses.add(resource);
								}
								return true;
							}
						};
						
						try {
							rootDelta.accept(visitor);
						} catch (CoreException e) {
					    	log.log(new Status(Status.ERROR, PLUGIN_LOG_IDENTIFIER, "ERROR - Something wrong happened when processing modified files. " + e.getLocalizedMessage()));
							e.printStackTrace();
						}
				         
						if (!changedClasses.isEmpty()) {
							
							try{
																
								//If there are changed files
								for (IResource changedClass : changedClasses) {
									//Call the verifier for each changed class
									verifyHandlersAndSignalers(changedClass);
								}
								
							} catch (CoreException e) {
						    	log.log(new Status(Status.ERROR, PLUGIN_LOG_IDENTIFIER, "ERROR - The workspace does not have the file /src-gen/contract.xml, with ECL rules. Plug-in aborted. " + e.getLocalizedMessage()));
								e.printStackTrace();
								
							} 
							
						}
						
					}
					
				};
				
				//Plug the listener in the workspace
				workspace.addResourceChangeListener(listener, IResourceChangeEvent.POST_BUILD);
				
		    }
		});	
	}
	
	/**
	 * Method that calls the verifications
	 * @param changedClass
	 */
	private void verifyHandlersAndSignalers(IResource changedClass) throws CoreException {

		deleteMarkers(changedClass);
		
		ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(changedClass);

		//AST Tree from changed class
		CompilationUnit astRoot = ParseAST.parse(compilationUnit);
		
		ASTExceptionRepresentation astRep = ParseAST.parseClassASTToExceptionRep(astRoot);

		messages = new ArrayList<ReturnMessage>();
		
		int totalMessages = 0;
		
		String className = astRoot.getPackage().getName() + "." + astRoot.getTypeRoot().getElementName();
		className = className.substring(0, className.length() - 5);
		
		List<MethodRepresentation> methods = astRep.getMethods();
		for(MethodRepresentation m : methods) {
			String methodName = className + "." + m.getMethodDeclaration().getName();
			
			int occurrences = 11;
			// TODO: buscar ocorr�ncias do m�todo nos stacks do elasticsearch
			
			if(occurrences > 10) {
				ReturnMessage rm = new ReturnMessage();
				
				rm.setMessage("Este m�todo esteve presente " + occurrences + " vezes em stack traces de falhas nos �ltimos 30 dias.");
				
				rm.setLineNumber(astRoot.getLineNumber(m.getMethodDeclaration().getStartPosition()));
				rm.setMarkerSeverity(IMarker.SEVERITY_WARNING);
				messages.add(rm);
			}
		}
		
		//Rule 1
//		ImproperThrowingVerifier improperThrowingVerifier = new ImproperThrowingVerifier(astRep, log);
//		List<ReturnMessage> verify1 = improperThrowingVerifier.verify();
//		messages.addAll(verify1);
//		int totalImproperThrowingVerifier = verify1.size();
//		totalMessages += totalImproperThrowingVerifier;
//
//		//Rule 3
//		ImproperHandlingVerifier improperHandlingVerifier = new ImproperHandlingVerifier(astRep, log);
//		List<ReturnMessage> verify2 = improperHandlingVerifier.verify();
//		messages.addAll(verify2);
//		int totalImproperHandlingVerifier = verify2.size();
//		totalMessages += totalImproperHandlingVerifier;
//
		
//		//Rule 4
//		PossibleHandlersInformation possibleHandlersInformation = new PossibleHandlersInformation(astRep, log);
//		List<ReturnMessage> verify3 = possibleHandlersInformation.verify();
//		messages.addAll(verify3);
//		int totalPossibleHandlersInformation = verify3.size();
//		totalMessages += totalPossibleHandlersInformation;
//
//		//Debug log for statistics metrics
//	   	log.log(new Status(Status.INFO, PLUGIN_LOG_IDENTIFIER, 
//	   			"INFO - Changed class: " + compilationUnit.getParent().getElementName() + "." +  compilationUnit.getElementName() +
//    			" / Total of messages: " + totalMessages +
//    			" ("+ totalImproperThrowingVerifier + ", " + totalImproperHandlingVerifier + ", " + totalPossibleHandlersInformation + ")" + 
//	   			" / Project: " + changedClass.getProject().getName() + 
//    			" / Methods: " + astRep.getMethods().size() +
//    			" / Throws: " + astRep.getNumberOfThrowStatements() + 
//    			" / Catches: " + astRep.getNumberOfCatchStatements()
//	   			));
	   	
		try {
			for(ReturnMessage rm : messages) {
				createMarker(changedClass, rm, compilationUnit);
			}
		} catch (CoreException e) {
	    	log.log(new Status(Status.ERROR, PLUGIN_LOG_IDENTIFIER, "ERROR - Something wrong happened when creating/removing markers. " + e.getLocalizedMessage()));
			e.printStackTrace();
			throw e;
		} catch (BadLocationException e) {
	    	log.log(new Status(Status.ERROR, PLUGIN_LOG_IDENTIFIER, "ERROR - Something wrong happened when creating/removing markers. " + e.getLocalizedMessage()));
			e.printStackTrace();
		}
			
	}
	
	/**
	 * Delete all the markers of ExceptionPolicyExpert type
	 * @param res Resource (class) to delete the marker
	 * @throws CoreException 
	 */
	private static void deleteMarkers(IResource res) throws CoreException {
		IMarker[] problems = null;
		int depth = IResource.DEPTH_INFINITE;
		problems = res.getProject().findMarkers("br.ufrn.lets.view.ExceptionPolicyExpertId", true, depth);

		for (int i = 0; i < problems.length; i++) {
			problems[i].delete();
		}
	}
	
	/**
	 * Create a marker for each returned message (information or violation)
	 * @param res Resource (class) to attach the marker
	 * @param rm Object with the marker message and mark line
	 * @param compilationUnit 
	 * @throws CoreException
	 * @throws BadLocationException 
	 */
	public static void createMarker(IResource res, ReturnMessage rm, ICompilationUnit compilationUnit)
			throws CoreException, BadLocationException {
		
		IMarker marker = null;
		marker = res.createMarker("br.ufrn.lets.view.ExceptionPolicyExpertId");
		marker.setAttribute(IMarker.SEVERITY, rm.getMarkerSeverity());
		marker.setAttribute(IMarker.TEXT, rm.getMessage());
		marker.setAttribute(IMarker.MESSAGE, rm.getMessage());
		
		marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
		
		org.eclipse.jface.text.Document document = new org.eclipse.jface.text.Document(compilationUnit.getSource());

		int offset = document.getLineOffset(rm.getLineNumber()-1);
		int length = document.getLineLength(rm.getLineNumber()-1);

		marker.setAttribute(IMarker.CHAR_START, offset);
		marker.setAttribute(IMarker.CHAR_END, offset + length);

	}

	public ILog getLog() {
		return log;
	}

	public void setLog(ILog log) {
		this.log = log;
	}
	
	
}
