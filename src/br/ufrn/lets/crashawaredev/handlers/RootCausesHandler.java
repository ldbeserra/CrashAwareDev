package br.ufrn.lets.crashawaredev.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

import br.ufrn.lets.crashawaredev.view.TopCausesDialog;

public class RootCausesHandler implements IHandler{

	@Override
	public void addHandlerListener(IHandlerListener arg0) {
		
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		Shell activeShell = HandlerUtil.getActiveShell(arg0);
		TopCausesDialog dialog = new TopCausesDialog(activeShell);
		dialog.open();
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
		
	}
	
	
}
