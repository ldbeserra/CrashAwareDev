package br.ufrn.lets.crashawaredev.startup;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStartup;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.reflections.Reflections;

import br.ufrn.lets.crashawaredev.ast.ParseAST;
import br.ufrn.lets.crashawaredev.ast.model.ASTRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.MethodRepresentation;
import br.ufrn.lets.crashawaredev.ast.model.ReturnMessage;
import br.ufrn.lets.crashawaredev.provider.CrashProvider;
import br.ufrn.lets.crashawaredev.verifier.PatternVerifier;

public class StartupClass implements IStartup {
    
	private static final String PLUGIN_LOG_IDENTIFIER = "br.ufrn.lets.CrashAwareDev";

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
		    	
		    	log.log(new Status(Status.INFO, PLUGIN_LOG_IDENTIFIER, "INFO - Initializing CrashAwareDev Plug-in..."));
		    	
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
									verifyPatterns(changedClass);
								}
								
							} catch (CoreException e) {
						    	log.log(new Status(Status.ERROR, PLUGIN_LOG_IDENTIFIER, "ERROR - The workspace does not have the file /src-gen/contract.xml, with ECL rules. Plug-in aborted. " + e.getLocalizedMessage()));
								e.printStackTrace();
								
							} catch (IOException e) {
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
	 * @throws IOException 
	 */
	private void verifyPatterns(IResource changedClass) throws CoreException, IOException {

		deleteMarkers(changedClass);
		
		ICompilationUnit compilationUnit = (ICompilationUnit) JavaCore.create(changedClass);

		//AST Tree from changed class
		CompilationUnit astRoot = ParseAST.parse(compilationUnit);
		
		ASTRepresentation astRep = ParseAST.parseClassASTToARep(astRoot);

		messages = new ArrayList<ReturnMessage>();
		
		String className = astRoot.getPackage().getName() + "." + astRoot.getTypeRoot().getElementName();
		className = className.substring(0, className.length() - 5); 
		
		Long totalCrashes = CrashProvider.INSTANCE.getClassCrashCount(className);
		
		if(totalCrashes > 0 && !astRoot.types().isEmpty()) {
			ReturnMessage rm = new ReturnMessage();
			rm.setMessage("Esta classe esteve presente stack traces de falhas nos últimos 3 dias.");
			
			rm.setLineNumber(astRoot.getLineNumber( ((TypeDeclaration) astRoot.types().get(0)).getStartPosition()));
			rm.setMarkerSeverity(IMarker.SEVERITY_INFO);
			messages.add(rm);
		}
		
		// Recupera todas as classes do pacote verifier
		Reflections reflections = new Reflections("br.ufrn.lets.crashawaredev.verifier");
		Set<Class<? extends PatternVerifier>> allClasses = reflections.getSubTypesOf(PatternVerifier.class);
		
		// Executa a verificação de cada classe filha de PatternVerifier
		for(Class<? extends PatternVerifier> verifier : allClasses) {
			
			try {
				
				Constructor constructor = verifier.getConstructor(new Class[] {ASTRepresentation.class, ILog.class});
				PatternVerifier instance = (PatternVerifier) constructor.newInstance(new Object[] {astRep, log});
				
				List<ReturnMessage> msgs = instance.verify();
				messages.addAll(msgs);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			
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
	private void deleteMarkers(IResource res) throws CoreException {
		IMarker[] problems = null;
		int depth = IResource.DEPTH_INFINITE;
		problems = res.getProject().findMarkers("br.ufrn.lets.view.CrashAwareDevId", true, depth);

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
	public void createMarker(IResource res, ReturnMessage rm, ICompilationUnit compilationUnit)
			throws CoreException, BadLocationException {
		
		IMarker marker = null;
		marker = res.createMarker("br.ufrn.lets.view.CrashAwareDevId");
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
