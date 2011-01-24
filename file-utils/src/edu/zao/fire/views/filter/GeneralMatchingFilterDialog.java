package edu.zao.fire.views.filter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import edu.zao.fire.filters.GeneralMatchingFilter;
import edu.zao.fire.filters.GeneralMatchingFilter.MatchType;

public class GeneralMatchingFilterDialog extends Dialog {

	public static final String TITLE_TEXT = "Edit filter settings";

	protected static final String appliesToFilesLink = "Applies to <a>Files</a>";
	protected static final String appliesToFoldersLink = "Applies to <a>Folders</a>";
	protected static final String appliesToBothLink = "Applies to <a>Files and Folders</a>";

	private Button checkFiles;

	private Button checkFolders;

	private Button checkMatching;

	private Text textField;

	private Button checkCaseSensitive;

	private Button checkRegex;

	private GeneralMatchingFilter editedFilter;

	public static GeneralMatchingFilter createNewFilter(Shell parentShell) {
		GeneralMatchingFilterDialog dlg = new GeneralMatchingFilterDialog(parentShell, null);
		GeneralMatchingFilter filter = dlg.open() == OK ? dlg.editedFilter : null;
		return filter;
	}

	public static void editFilter(Shell parentShell, GeneralMatchingFilter filter) {
		GeneralMatchingFilterDialog dlg = new GeneralMatchingFilterDialog(parentShell, filter);
		dlg.open();
	}

	protected GeneralMatchingFilterDialog(Shell parentShell, GeneralMatchingFilter filter) {
		super(parentShell);
		if (filter == null) {
			editedFilter = new GeneralMatchingFilter();
		} else {
			editedFilter = filter;
		}
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

		newShell.setText(TITLE_TEXT);
	}

	@Override
	protected void okPressed() {
		updateFilterToWidgetStates(editedFilter);
		super.okPressed();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite wrapper = (Composite) super.createDialogArea(parent);

		Composite mainArea = new Composite(wrapper, SWT.NONE);
		mainArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(1, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		mainArea.setLayout(layout);

		new Label(mainArea, SWT.SINGLE).setText("Affects...");

		checkFiles = new Button(mainArea, SWT.CHECK);
		checkFolders = new Button(mainArea, SWT.CHECK);
		checkMatching = new Button(mainArea, SWT.CHECK);

		checkFiles.setText("Files");
		checkFolders.setText("Folders");
		checkMatching.setText("Whose names contain...");

		GridData checkFilesData = new GridData();
		GridData checkFoldersData = new GridData();
		GridData checkMatchingData = new GridData();

		checkFilesData.horizontalIndent = 10;
		checkFoldersData.horizontalIndent = 10;
		checkMatchingData.horizontalIndent = 10;
		checkMatchingData.verticalIndent = 10;

		checkFiles.setLayoutData(checkFilesData);
		checkFolders.setLayoutData(checkFoldersData);
		checkMatching.setLayoutData(checkMatchingData);

		checkFiles.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isAppliedToSomething()) {
					checkFolders.setSelection(true);
				}
			}
		});

		checkFolders.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (!isAppliedToSomething()) {
					checkFiles.setSelection(true);
				}
			}
		});

		checkFiles.setSelection(true);
		checkFolders.setSelection(true);

		Composite matchArea = new Composite(mainArea, SWT.NONE);
		GridLayout matchLayout = new GridLayout(1, false);
		matchLayout.marginHeight = 0;
		matchLayout.marginWidth = 0;
		matchLayout.marginLeft = 20;
		matchArea.setLayout(matchLayout);
		matchArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		textField = new Text(matchArea, SWT.SINGLE | SWT.BORDER);
		textField.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		checkCaseSensitive = new Button(matchArea, SWT.CHECK);
		checkRegex = new Button(matchArea, SWT.CHECK);

		checkCaseSensitive.setText("Case Sensitive");
		checkRegex.setText("Use Regular Expressions");

		checkMatching.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateMatchWidgetsEnabledState();
			}
		});

		updateWidgetStates(editedFilter);

		return wrapper;
	}

	private void updateMatchWidgetsEnabledState() {
		boolean enabled = checkMatching.getSelection();
		textField.setEnabled(enabled);
		checkCaseSensitive.setEnabled(enabled);
		checkRegex.setEnabled(enabled);
	}

	private void updateWidgetStates(GeneralMatchingFilter filter) {
		MatchType matchType = filter.getMatchType();
		boolean affectsFiles = matchType == MatchType.FILE || matchType == MatchType.ALL_ITEMS;
		boolean affectsFolders = matchType == MatchType.FOLDER || matchType == MatchType.ALL_ITEMS;
		checkFiles.setSelection(affectsFiles);
		checkFolders.setSelection(affectsFolders);

		checkMatching.setSelection(filter.isUsesMatchText());
		textField.setText(filter.getMatchText());
		checkCaseSensitive.setSelection(filter.isCaseSensitive());
		checkRegex.setSelection(filter.isUsesRegex());

		updateMatchWidgetsEnabledState();
	}

	private void updateFilterToWidgetStates(GeneralMatchingFilter filter) {
		boolean affectsFiles = checkFiles.getSelection();
		boolean affectsFolders = checkFolders.getSelection();
		MatchType matchType;
		if (affectsFiles && affectsFolders) {
			matchType = MatchType.ALL_ITEMS;
		} else if (affectsFiles) {
			matchType = MatchType.FILE;
		} else {
			matchType = MatchType.FOLDER;
		}

		filter.setMatchType(matchType);

		filter.setUsesMatchText(checkMatching.getSelection());
		filter.setMatchText(textField.getText());
		filter.setCaseSensitive(checkCaseSensitive.getSelection());
		filter.setUsesRegex(checkRegex.getSelection());
	}

	private boolean isAppliedToSomething() {
		return checkFiles.getSelection() || checkFolders.getSelection();
	}

}
