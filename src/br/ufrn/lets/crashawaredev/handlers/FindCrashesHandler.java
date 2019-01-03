package br.ufrn.lets.crashawaredev.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import br.ufrn.lets.crashawaredev.view.CrashView;

public class FindCrashesHandler implements IHandler{
	
	@Override
	public void addHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		
		try {
			CrashView crashView = (CrashView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(CrashView.ID);
			crashView.search(true, false);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub
		
	}

}
