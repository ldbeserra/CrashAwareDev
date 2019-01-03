package br.ufrn.lets.crashawaredev.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class InfoExceptionView extends ViewPart {
	
	public static final String ID = "br.ufrn.lets.crashawaredev.view.InfoExceptionViewId";
	
	private StyledText text;
	
	@Override
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.BORDER);
		text.setText("");
		text.setEditable(false);
		text.setCaret(null);
		
	}
	
	public void show(String rootCause) {
		text.setText(rootCause);
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

}
