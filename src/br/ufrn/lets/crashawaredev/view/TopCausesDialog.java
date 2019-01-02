package br.ufrn.lets.crashawaredev.view;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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

import br.ufrn.lets.crashawaredev.model.Buckets;
import br.ufrn.lets.crashawaredev.model.ResultConsume;
import br.ufrn.lets.crashawaredev.model.RootCause;
import br.ufrn.lets.crashawaredev.provider.CrashProvider;

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
	    
	    ResultConsume result = null;
	    try {
			result = CrashProvider.INSTANCE.getTopCauses();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    List<RootCause> rootCauses = new ArrayList<RootCause>();
	    int index = 1;
	    if(result != null && result.getAggs() != null && !result.getAggs().getBuckets().isEmpty()) {
	    	for(Buckets bucket : result.getAggs().getBuckets())
	    		rootCauses.add(new RootCause(index++, bucket.getKey(), Integer.parseInt(bucket.getCount()), "")); 
	    }
	    
	    calculateRates(rootCauses);

	    viewer.setInput(rootCauses);
	    
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

    private void calculateRates(List<RootCause> rootCauses) {
		long sum = 0;
		for(RootCause c : rootCauses) {
			sum += c.getOcurrences();
		}
		
		NumberFormat formater = new DecimalFormat("00.00");
		for(RootCause c : rootCauses) {
			Double rate = (new Double(c.getOcurrences())/sum) * 100;
			c.setRate(formater.format(rate));
		}
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
        return new Point(450, 500);
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
