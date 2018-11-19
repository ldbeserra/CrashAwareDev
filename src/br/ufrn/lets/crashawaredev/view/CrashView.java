package br.ufrn.lets.crashawaredev.view;

import java.io.IOException;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

import br.ufrn.lets.crashawaredev.model.Crash;
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
	        	search(true);
	        }
        });
	    
	    viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
		        | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
	    
	    createColumns();
	    
	    final Table table = viewer.getTable();
	    table.setHeaderVisible(true);
	    table.setLinesVisible(true);

	    viewer.setContentProvider(ArrayContentProvider.getInstance());
	    viewer.setInput(CrashProvider.INSTANCE.getCrashes());
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
		String[] columns = {"Class", "Trace", "Root cause","Date", "Link"};
	    int[] bounds = {200, 300, 200, 50, 100};
	    
	    TableViewerColumn col = createTableViewerColumn(columns[0], bounds[0], 0);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		Crash c = (Crash) element;
	    		return c.getMensagem_excecao();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[1], bounds[1], 1);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		Crash c = (Crash) element;
	    		return c.getMensagem_excecao();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[2], bounds[2], 2);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		Crash c = (Crash) element;
	    		return c.getMensagem_excecao();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[3], bounds[3], 3);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		Crash c = (Crash) element;
	    		return c.getMensagem_excecao();
	    	}
	    });
	    
	    col = createTableViewerColumn(columns[4], bounds[4], 4);
	    col.setLabelProvider(new ColumnLabelProvider() {
	    	@Override
	    	public String getText(Object element) {
	    		Crash c = (Crash) element;
	    		return c.getMensagem_excecao();
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
	
	private void search(boolean doLog){
		List<Crash> crashes = null;
		try {
			crashes = CrashProvider.INSTANCE.getCrashesByClassName("AWcss7do5B0ps0XvbB1q");
		} catch (IOException e) {
			e.printStackTrace();
		}
		viewer.setInput(crashes);
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
