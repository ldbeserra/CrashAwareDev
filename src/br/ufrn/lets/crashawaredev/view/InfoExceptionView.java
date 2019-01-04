package br.ufrn.lets.crashawaredev.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

import br.ufrn.lets.crashawaredev.dao.ExceptionInfoDao;

public class InfoExceptionView extends ViewPart {
	
	public static final String ID = "br.ufrn.lets.crashawaredev.view.InfoExceptionViewId";
	
	private Label title;
	
	private StyledText text;
	
	private Button editButton;
	private Button cancelButton;
	
	@Override
	public void createPartControl(Composite parent) {
		
		GridLayout layout = new GridLayout(2, false);
	    parent.setLayout(layout);
	    
	    GridData gridData;
	    
	    title = new Label(parent, SWT.CENTER);
	    title.setText("");
	    title.setFont(new Font(Display.getCurrent(),"Arial", 18, SWT.BOLD ));
	    
	    gridData = new GridData();
	    gridData.horizontalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    title.setLayoutData(gridData);
	    
		text = new StyledText(parent, SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text.setText("");
		text.setEditable(false);
		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));

		gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    text.setLayoutData(gridData);
	    
		editButton = new Button(parent, SWT.PUSH);
		editButton.setText("     Edit     ");
		editButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	if(!text.getEditable()) {
	        		editButton.setText("Confirm");
	        		cancelButton.setVisible(true);
	        		text.setEditable(true);
	        		text.getCaret().setVisible(true);
	        		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
	        		text.setFocus();
	        	} else {
	        		
    				ExceptionInfoDao dao = new ExceptionInfoDao();
    				dao.presistExceptionInformation(title.getText(), text.getText());
	        		
	        		editButton.setText("Edit");
	        		text.setEditable(false);
	        		text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	        		cancelButton.setVisible(false);
	        	}
	        }
        });
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.GRAB_HORIZONTAL;
		editButton.setLayoutData(gridData);
		
		cancelButton = new Button(parent, SWT.PUSH);
		cancelButton.setText("Cancel");
		cancelButton.setVisible(editButton.getText().equals("Confirm"));
		cancelButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	show(title.getText());
	        	text.setEditable(false);
	        	editButton.setText("Edit");
	        	cancelButton.setVisible(false);
	        	text.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
	        }
        });
		
		gridData = new GridData();
		gridData.horizontalAlignment = GridData.GRAB_HORIZONTAL;
		cancelButton.setLayoutData(gridData);
		
	}
	
	public void show(String rootCause) {
		if(rootCause != null && !rootCause.isEmpty()) {
			
			title.setText(rootCause);
			
			ExceptionInfoDao dao = new ExceptionInfoDao();
			String information = dao.getExceptionInformation(rootCause);
			text.setText(information);
		}
	}

	@Override
	public void setFocus() {
		text.setFocus();
	}

}
