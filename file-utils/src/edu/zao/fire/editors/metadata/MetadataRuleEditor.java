package edu.zao.fire.editors.metadata;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import edu.zao.fire.MetadataRule;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.rcp.Activator;

public class MetadataRuleEditor extends RenamerRuleEditor {

	public final static String ID = "file-utils.editors.metadata";

	private MetadataRule rule;

	private TableViewer addedTagListViewer;

	private MetadataTagList tagList;

	private Text plainTextBox;

	private Button upButton;

	private Button downButton;

	private Button removeButton;

	private TableViewer tagSelectionListViewer;

	private Button addTagButton;

	@Override
	public RenamerRule getRule() {
		return rule;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		rule = ((MetadataRuleEditorInput) input).getRule();
		setPartName(input.getName());
		tagList = rule.getTagList();
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		// create a label for the tag selection area
		Label tagsLabel = new Label(parent, SWT.SINGLE);
		tagsLabel.setText("Metadata Tags:");
		GridData tagsLabelGridData = new GridData(SWT.LEFT, SWT.TOP, true, false);
		tagsLabelGridData.horizontalSpan = 2;
		tagsLabel.setLayoutData(tagsLabelGridData);

		// create a composite where the tag selection list would go
		tagSelectionListViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tagSelectionListViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		Set<String> someTags = new TreeSet<String>();

		for (Field field : MetadataTagNames.class.getFields()) {
			try {
				String tagName = field.get(null).toString();
				someTags.add(tagName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tagSelectionListViewer.setContentProvider(new TagSelectionContentProvider());
		tagSelectionListViewer.setLabelProvider(new TagSelectionLabelProvider());
		tagSelectionListViewer.setInput(someTags);

		// create a grid data that will be common to all of the buttons
		GridData buttonGridData = new GridData(SWT.FILL, SWT.TOP, false, false);
		buttonGridData.widthHint = 64;

		// create an "Add" button next to the tag selection list
		addTagButton = new Button(parent, SWT.PUSH);
		addTagButton.setText("Add");
		addTagButton.setLayoutData(buttonGridData);

		// create a text box for editing Plain-Text tags
		plainTextBox = new Text(parent, SWT.SINGLE | SWT.BORDER);
		plainTextBox.setText("Edit plain-text tags here");
		plainTextBox.setEnabled(false);
		plainTextBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));

		// add a composite to take up the space next to the text box
		new Label(parent, SWT.NONE);

		// create a composite where the added tags would go
		addedTagListViewer = new TableViewer(parent, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		GridData addedTagListViewerGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		addedTagListViewerGridData.verticalSpan = 3;
		addedTagListViewer.getTable().setLayoutData(addedTagListViewerGridData);

		// tagList.addTag(MetadataTag.ARTIST);
		// tagList.addTag(MetadataTag.makePlainTextTag("-"));
		// tagList.addTag(MetadataTag.TRACK);
		// tagList.addTag(MetadataTag.makePlainTextTag("-"));
		// tagList.addTag(MetadataTag.TITLE);

		addedTagListViewer.setContentProvider(new SelectedTagsContentProvider());
		addedTagListViewer.setLabelProvider(new SelectedTagsLabelProvider());
		addedTagListViewer.setInput(tagList);

		// create up, down, and remove buttons

		upButton = new Button(parent, SWT.PUSH);
		upButton.setLayoutData(buttonGridData);
		upButton.setText("Up");

		downButton = new Button(parent, SWT.PUSH);
		downButton.setLayoutData(buttonGridData);
		downButton.setText("Down");

		removeButton = new Button(parent, SWT.PUSH);
		removeButton.setLayoutData(buttonGridData);
		removeButton.setText("Remove");

		addListeners();
	}

	private void addListeners() {
		addTagButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Table table = tagSelectionListViewer.getTable();
				for (TableItem item : table.getSelection()) {
					String tagName = item.getText();
					tagList.addTag(MetadataTagFactory.getTagInstance(tagName));
				}
				addedTagListViewer.refresh(true);
				fireRuleChanged(rule);
			}
		});

		addedTagListViewer.getTable().addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TableItem[] selections =
				// addedTagListViewer.getTable().getSelection();
				// if (selections.length == 1) {
				// int selectionIndex =
				// addedTagListViewer.getTable().getSelectionIndex();
				// MetadataTag tag = tagList.getTag(selectionIndex);
				// if (tag.tagName == MetadataTagNames.PLAINTEXT) {
				// plainTextBox.setEnabled(true);
				// plainTextBox.setText(tag.getDefaultText());
				// plainTextBox.setSelection(tag.getDefaultText().length());
				// } else {
				// plainTextBox.setEnabled(false);
				// }
				// }
				updatePlainTextStatus();
			}
		});

		plainTextBox.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!plainTextBox.isEnabled()) {
					return;
				}
				TableItem[] selections = addedTagListViewer.getTable().getSelection();
				if (selections.length == 1) {
					int selectionIndex = addedTagListViewer.getTable().getSelectionIndex();
					MetadataTag tag = tagList.getTag(selectionIndex);
					tag.setDefaultText(plainTextBox.getText());
					addedTagListViewer.refresh(true);
					fireRuleChanged(rule);
				}
			}
		});

		upButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selections = addedTagListViewer.getTable().getSelection();
				if (selections.length == 1) {
					int selectionIndex = addedTagListViewer.getTable().getSelectionIndex();
					MetadataTag tag = tagList.getTag(selectionIndex);
					tagList.sendItemUp(selectionIndex);
					addedTagListViewer.refresh(true);
					addedTagListViewer.reveal(tag);
					fireRuleChanged(rule);
				}
			}
		});

		downButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selections = addedTagListViewer.getTable().getSelection();
				if (selections.length == 1) {
					int selectionIndex = addedTagListViewer.getTable().getSelectionIndex();
					MetadataTag tag = tagList.getTag(selectionIndex);
					tagList.sendItemDown(selectionIndex);
					addedTagListViewer.refresh(true);
					addedTagListViewer.reveal(tag);
					fireRuleChanged(rule);
				}
			}
		});

		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] selections = addedTagListViewer.getTable().getSelection();
				if (selections.length == 1) {
					int selectionIndex = addedTagListViewer.getTable().getSelectionIndex();
					MetadataTag tag = tagList.getTag(selectionIndex);
					tagList.removeItem(selectionIndex);
					addedTagListViewer.refresh(true);
					addedTagListViewer.reveal(tag);
				}
				updatePlainTextStatus();
				fireRuleChanged(rule);
			}
		});
	}

	private void updatePlainTextStatus() {
		TableItem[] selections = addedTagListViewer.getTable().getSelection();
		if (selections.length == 1) {
			int selectionIndex = addedTagListViewer.getTable().getSelectionIndex();
			MetadataTag tag = tagList.getTag(selectionIndex);
			if (tag.tagName == MetadataTagNames.PLAINTEXT) {
				plainTextBox.setText(tag.getDefaultText());
				plainTextBox.setSelection(tag.getDefaultText().length());
				plainTextBox.setEnabled(true);
				return;
			}
		}
		plainTextBox.setEnabled(false);
		plainTextBox.setText("Edit plain-text tags here");
	}

	@Override
	public void setFocus() {
		Activator.getDefault().getEditorManager().setActiveEditor(this);
	}

}
