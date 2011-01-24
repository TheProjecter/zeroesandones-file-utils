package edu.zao.fire.views.filter;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import edu.zao.fire.Renamer;
import edu.zao.fire.filters.BrowserFileFilter.FilterListener;
import edu.zao.fire.filters.GeneralMatchingFilter;
import edu.zao.fire.filters.UserFilters;
import edu.zao.fire.filters.UserIgnoreFileFilter;
import edu.zao.fire.util.Filter;

public class FilterView extends ViewPart {

	@Override
	public void createPartControl(Composite parent) {
		// parent.setLayout(new FillLayout());
		// parent = new ScrolledComposite(parent, SWT.V_SCROLL);

		ExpandBar bar = new ExpandBar(parent, SWT.V_SCROLL);

		// area for individual files being ignored
		Composite composite = new Composite(bar, SWT.NONE);
		new IndividualFiltersSubView(composite);

		ExpandItem item1 = new ExpandItem(bar, SWT.NONE);
		item1.setControl(composite);
		item1.setText("Individually Ignored Items");
		item1.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);

		// area for files ignored by a pattern
		composite = new Composite(bar, SWT.NONE);
		new GeneralFiltersSubView().createPartControl(composite);

		ExpandItem item2 = new ExpandItem(bar, SWT.NONE);
		item2.setControl(composite);
		item2.setText("Items Ignored by Instruction");
		item2.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
	}

	private class GeneralFiltersSubView {

		private Button buttonEdit;
		private Button buttonRemove;
		private TableViewer viewer;
		private Table table;
		private UserFilters userFilters;

		// private List<GeneralMatchingFilter> filtersList;

		protected void createPartControl(final Composite parent) {
			parent.setLayout(new GridLayout(2, false));

			viewer = new TableViewer(parent);
			table = viewer.getTable();
			GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
			tableData.verticalSpan = 3;
			tableData.heightHint = 150;
			table.setLayoutData(tableData);

			Button buttonNew = new Button(parent, SWT.PUSH);
			buttonNew.setText("New...");
			buttonNew.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			buttonEdit = new Button(parent, SWT.PUSH);
			buttonEdit.setText("Edit...");
			buttonEdit.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			buttonRemove = new Button(parent, SWT.PUSH);
			buttonRemove.setText("Remove");
			buttonRemove.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			viewer.setContentProvider(new GeneralMatchingFilterContentProvider());
			viewer.setLabelProvider(new GeneralMatchingFilterLabelProvider(table.getFont()));

			userFilters = Renamer.getDefault().getUserFilters();
			viewer.setInput(userFilters);

			buttonNew.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					GeneralMatchingFilter newFilter = GeneralMatchingFilterDialog.createNewFilter(parent.getShell());
					if (newFilter != null) {
						userFilters.add(newFilter);
						viewer.refresh();
					}
				}
			});

			table.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateButtonStates();
				}
			});

			buttonEdit.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// should be safe to assume that selection is exactly 1 item
					TableItem selectedItem = table.getSelection()[0];
					Object selectedObject = selectedItem.getData();
					GeneralMatchingFilter filter = (GeneralMatchingFilter) selectedObject;
					GeneralMatchingFilterDialog.editFilter(parent.getShell(), filter);
					updateButtonStates();
					viewer.refresh();
				}
			});

			buttonRemove.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					for (TableItem item : table.getSelection()) {
						userFilters.remove((GeneralMatchingFilter) item.getData());
					}
					updateButtonStates();
					viewer.refresh();
				}
			});

			updateButtonStates();
		}

		private void updateButtonStates() {
			int userGeneralFilterCount = userFilters.getGeneralFilters().size();
			buttonEdit.setEnabled(table.getSelectionCount() == 1 && userGeneralFilterCount > 0);
			buttonRemove.setEnabled(table.getSelectionCount() > 0 && userGeneralFilterCount > 0);
		}
	}

	private class IndividualFiltersSubView {
		private Button addFileButton;
		private Button addFolderButton;
		private Button removeButton;
		private TableViewer viewer;

		public IndividualFiltersSubView(final Composite parent) {
			parent.setLayout(new GridLayout(2, false));

			Label label = new Label(parent, SWT.SINGLE);
			label.setText("Individual files that are currently \"ignored\"");
			label.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, false, 2, 1));

			viewer = new TableViewer(parent);
			viewer.setLabelProvider(new StyledCellLabelProvider() {
				@Override
				public void update(ViewerCell cell) {
					File file = (File) cell.getElement();

					try {
						cell.setText(file.getCanonicalPath());
					} catch (IOException e) {
						e.printStackTrace();
					}
					String fileIcon = null;
					if (file.isDirectory()) {
						fileIcon = ISharedImages.IMG_OBJ_FOLDER;
					} else if (file.isFile()) {
						fileIcon = ISharedImages.IMG_OBJ_FILE;
					}
					Image icon = PlatformUI.getWorkbench().getSharedImages().getImage(fileIcon);
					cell.setImage(icon);

					super.update(cell);
				}
			});

			viewer.setContentProvider(new IStructuredContentProvider() {

				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				}

				@Override
				public void dispose() {
				}

				@Override
				public Object[] getElements(Object inputElement) {
					UserIgnoreFileFilter filter = (UserIgnoreFileFilter) inputElement;
					List<File> ignoredFiles = new LinkedList<File>();
					for (String filename : filter.getIgnoredFileNames()) {
						File file = new File(filename);
						if (file.exists()) {
							ignoredFiles.add(file);
						}
					}
					return ignoredFiles.toArray();
				}
			});

			final UserIgnoreFileFilter individualFilter = Renamer.getDefault().getUserFilters().getIndividualFilter();
			viewer.setInput(individualFilter);

			individualFilter.addFilterListener(new FilterListener() {
				@Override
				public void filterChanged(Filter<File> filter) {
					viewer.refresh();
				}
			});

			GridData tableLayoutData = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 3);
			tableLayoutData.heightHint = 150;
			viewer.getTable().setLayoutData(tableLayoutData);

			addFileButton = new Button(parent, SWT.PUSH);
			addFileButton.setText("Add File...");
			addFileButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			addFileButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String filePath = new FileDialog(parent.getShell()).open();
					if (filePath != null) {
						individualFilter.addIgnoredFile(new File(filePath));
					}
				}
			});

			addFolderButton = new Button(parent, SWT.PUSH);
			addFolderButton.setText("Add Folder...");
			addFolderButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			addFolderButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					String folderPath = new DirectoryDialog(parent.getShell()).open();
					if (folderPath != null) {
						individualFilter.addIgnoredFile(new File(folderPath));
					}
				}
			});

			removeButton = new Button(parent, SWT.PUSH);
			removeButton.setText("Remove");
			removeButton.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

			removeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					TableItem[] selection = viewer.getTable().getSelection();
					File[] filesToRemove = new File[selection.length];
					int i = 0;
					for (TableItem selectedItem : selection) {
						filesToRemove[i++] = (File) selectedItem.getData();
					}
					for (File file : filesToRemove) {
						individualFilter.removeIgnoredFile(file);
					}
				}
			});
		}
	}

	@Override
	public void setFocus() {
	}

}
