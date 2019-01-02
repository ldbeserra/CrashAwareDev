package br.ufrn.lets.crashawaredev.view;

import java.io.IOException;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import br.ufrn.lets.crashawaredev.model.ResultConsume;
import br.ufrn.lets.crashawaredev.model.ResultConsumeItem;
import br.ufrn.lets.crashawaredev.provider.CrashProvider;

public class CrashView extends ViewPart {
	
	public static final String ID = "br.ufrn.lets.crashawaredev.view.CrashViewId";
	
	private Text searchText;
	private Button searchButton;
	private TableViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(3, false);
	    parent.setLayout(layout);
	    
	    final Label methodNameLabel = new Label(parent, SWT.NONE);
	    methodNameLabel.setText("Class Full Name: ");
	    
	    searchText = new Text(parent, SWT.BORDER);
	    searchText.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));
	    
	    searchButton = new Button(parent, SWT.PUSH);
	    searchButton.setText("Search");
	    searchButton.addSelectionListener(new SelectionAdapter() {
	        public void widgetSelected(SelectionEvent event) {
	        	search(true, true);
	        }
        });
	    
	    viewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	    
	    createColumns();
	    
	    final Table table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);
	    
	    // Table's line double click
	    table.addListener(SWT.MouseDoubleClick, event -> {
	    	if(table.getSelection() != null && table.getSelection().length > 0) {
		    	TableItem item = table.getSelection()[0];
		    	if(item != null && item.getData() != null)
		    		Program.launch(((ResultConsumeItem) item.getData()).getLink());
	    	}
	    });
	    
	    // Create context menu of table
	    Menu menuTable = new Menu(table);
	    table.setMenu(menuTable);

	    // Create menu item
	    MenuItem miTest = new MenuItem(menuTable, SWT.NONE);
	    
	    // Menu open data in external browser
	    miTest.setText("Open in external browser");
	    miTest.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {/* nothing to do */ }

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				if(table.getSelection() != null && table.getSelection().length > 0) {
			    	TableItem item = table.getSelection()[0];
			    	if(item != null && item.getData() != null)
			    		Program.launch(((ResultConsumeItem) item.getData()).getLink());
		    	}
			}
	    });

	    // Do not show menu, when no item is selected
	    table.addListener(SWT.MenuDetect, new Listener() {
	    	@Override
	    	public void handleEvent(Event event) {
	    		if (table.getSelectionCount() <= 0) {
	    			event.doit = false;
	    		}
	    	}
	    });

	    viewer.setContentProvider(ArrayContentProvider.getInstance());
	    getSite().setSelectionProvider(viewer);
	    
	    GridData gridData = new GridData();
	    gridData.verticalAlignment = GridData.FILL;
	    gridData.horizontalSpan = 2;
	    gridData.grabExcessHorizontalSpace = true;
	    gridData.grabExcessVerticalSpace = true;
	    gridData.horizontalAlignment = GridData.FILL;
	    viewer.getControl().setLayoutData(gridData);
	}
	
	private void createColumns() {
		String[] columns = {"Trace", "Exception", "Root Cause", "Date"};
	    int[] bounds = {400, 120, 200, 200};
	    
	    // Trace
	    TableViewerColumn col = createTableViewerColumn(columns[0], bounds[0], 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		ResultConsumeItem item = (ResultConsumeItem) element;
	    		return item.getMensagemExcecao();
	    	}
	    });
	    
	 // Exception
	    col = createTableViewerColumn(columns[1], bounds[1], 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		ResultConsumeItem item = (ResultConsumeItem) element;
	    		return item.getClasseExcecao();
	    	}
	    });
	    
	    // Root cause
	    col = createTableViewerColumn(columns[2], bounds[2], 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		ResultConsumeItem item = (ResultConsumeItem) element;
	    		return item.getRootCause();
	    	}
	    });
	    
	    // Date
	    col = createTableViewerColumn(columns[3], bounds[3], 3);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		ResultConsumeItem item = (ResultConsumeItem) element;
	    		return item.getDataHoraOperacao();
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
	
	public void search(boolean allClasses, boolean doLog){
		ResultConsume result = null;
		try {
			if(allClasses)
				result = CrashProvider.INSTANCE.getAllCrashes();
			else
				result = CrashProvider.INSTANCE.getCrashesByClassName(searchText.getText());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(result != null)
			viewer.setInput(result.getHits());
	}

	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public Text getSearchText() {
		return searchText;
	}

	public void setSearchText(Text searchText) {
		this.searchText = searchText;
	}

	public Button getSearchButton() {
		return searchButton;
	}

	public void setSearchButton(Button searchButton) {
		this.searchButton = searchButton;
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public void setViewer(TableViewer viewer) {
		this.viewer = viewer;
	}

	public static String getId() {
		return ID;
	}
	
}
