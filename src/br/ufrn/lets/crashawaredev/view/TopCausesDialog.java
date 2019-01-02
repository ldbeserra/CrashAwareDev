package br.ufrn.lets.crashawaredev.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import br.ufrn.lets.crashawaredev.model.RootCause;

public class TopCausesDialog extends Dialog{
	
	private TableViewer viewer;

	public TopCausesDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
    public Control createDialogArea(Composite parent) {
		
		GridLayout layout = new GridLayout(1, false);
	    parent.setLayout(layout);
	    
	    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	    
	    createColumns();
	    
	    final Table table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);

	    viewer.setContentProvider(ArrayContentProvider.getInstance());
	    List<RootCause> rootCauses = new ArrayList<RootCause>();
	    rootCauses.add(new RootCause(1, "NullPointerException", 700, 47.0));
	    rootCauses.add(new RootCause(2, "LazyInitializationException", 171, 10.60 ));
	    rootCauses.add(new RootCause(3, "JspException", 166, 10.29));
	    rootCauses.add(new RootCause(4, "IllegalArgumentException", 102, 6.32));
	    rootCauses.add(new RootCause(5, "ConcurrentModificationException", 54, 3.35));
	    viewer.setInput(rootCauses);
//	    getSite().setSelectionProvider(viewer);
	    
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.horizontalAlignment = GridData.FILL;
	    viewer.getControl().setLayoutData(gridData);
		
        Composite container = (Composite) super.createDialogArea(parent);

        return container;
    }

    // overriding this methods allows you to set the
    // title of the custom dialog
    @Override
    public void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Top Root Causes");
    }

    @Override
    public Point getInitialSize() {
        return new Point(450, 300);
    }
    
    private void createColumns() {
		String[] columns = {"#", "Root Cause", "Ocurrences", "Rate"};
	    int[] bounds = {20, 260, 100, 50};
	    
	    TableViewerColumn col = createTableViewerColumn(columns[0], bounds[0], 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		RootCause c = (RootCause) element;
	    		return c.getOrder().toString();
	    	}
	    });

	    col = createTableViewerColumn(columns[1], bounds[1], 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		RootCause c = (RootCause) element;
	    		return c.getCause();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[2], bounds[2], 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		RootCause c = (RootCause) element;
	    		return c.getOcurrences().toString();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[3], bounds[3], 3);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		RootCause c = (RootCause) element;
	    		return c.getRate().toString();
	    	}
	    });
	    
	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
	    final TableViewerColumn viewerColumn = new TableViewerColumn(viewer,
	        SWT.NONE);
	    final TableColumn column = viewerColumn.getColumn();
	    column.setText(title);
	    column.setWidth(bound);
	    column.setResizable(true);
	    column.setMoveable(true);
	    return viewerColumn;
	}
}
