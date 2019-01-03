package br.ufrn.lets.crashawaredev.view;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import br.ufrn.lets.crashawaredev.util.CrashInfoProvider;

public class InfoExceptionView extends ViewPart {
	
	public static final String ID = "br.ufrn.lets.crashawaredev.view.InfoExceptionViewId";
	
	private StyledText text;
	
	@Override
	public void createPartControl(Composite parent) {
		text = new StyledText(parent, SWT.BORDER | SWT.WRAP);
		text.setText("");
		text.setEditable(false);
		text.setCaret(null);
	}
	
	public void show(String rootCause) {
		if(rootCause != null && !rootCause.isEmpty()) {
			text.setText(rootCause + "\n\n" + CrashInfoProvider.getInstance().getInfo(rootCause));
			
			FontData data = text.getFont().getFontData()[0];
		    Font font1 = new Font(text.getDisplay(), data.getName(), data.getHeight() * 3, data.getStyle());
		    
		    StyleRange styleTitle = new StyleRange();
		    styleTitle.font = font1;
		    styleTitle.start = 0;
		    styleTitle.length = rootCause.length();
		    
			text.setStyleRange(styleTitle);
		}
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

}
