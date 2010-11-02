package edu.zao.fire.editors.matchreplace;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MatchReplaceRule.CapitalizationStyle;
import edu.zao.fire.MatchReplaceRule.ReplacementLimit;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.rcp.Activator;

public class MatchReplaceRuleEditor extends RenamerRuleEditor {
	public final static String ID = "file-utils.editors.matchreplace";

	private MatchReplaceRule inputRule;

	private Text matchText;

	private Text replaceText;

	private Button radioReplaceFirst;

	private Button radioReplaceAll;

	private Button caseSensitiveCheckBox;

	private Button regexCheckBox;

	private Button radioAllCaps;

	private Button radioAllLower;

	private Button radioSentenceCaps;

	private Button radioTitleCaps;

	private Button radioDontCareCaps;

	public MatchReplaceRuleEditor() {
	}

	@Override
	public RenamerRule getRule() {
		return inputRule;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println("Doing save");
	}

	@Override
	public void doSaveAs() {
		System.out.println("Doing save as");
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
		inputRule = ((MatchReplaceRuleEditorInput) input).getRule();
	}

	private void selectRuleConfigurationInUI() {
		matchText.setText(inputRule.getMatchString());
		replaceText.setText(inputRule.getReplaceString());
		radioReplaceAll.setSelection(inputRule.getReplacementLimit() == ReplacementLimit.ALL);
		radioReplaceFirst.setSelection(inputRule.getReplacementLimit() == ReplacementLimit.FIRST);

		caseSensitiveCheckBox.setSelection(inputRule.isCaseSensitive());
		regexCheckBox.setSelection(inputRule.isMatchRegularExpressions());

		CapitalizationStyle capState = inputRule.getCapitalizationState();
		radioAllCaps.setSelection(capState == CapitalizationStyle.ALL_CAPS);
		radioAllLower.setSelection(capState == CapitalizationStyle.NO_CAPS);
		radioSentenceCaps.setSelection(capState == CapitalizationStyle.SENTENCE);
		radioTitleCaps.setSelection(capState == CapitalizationStyle.TITLE);
		radioDontCareCaps.setSelection(capState == CapitalizationStyle.NONE);
	}

	@Override
	public boolean isDirty() {
		return true;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public void createPartControl(Composite parent) {

		parent.setLayout(new GridLayout(1, false));

		// set up the area for the match/replace components
		Composite matchReplaceArea = new Composite(parent, SWT.BORDER);
		matchReplaceArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		matchReplaceArea.setLayout(new GridLayout(2, false));

		// set up the label for the "Match" field
		Label matchLabel = new Label(matchReplaceArea, SWT.SINGLE);
		matchLabel.setText("Match");
		matchLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		// set up the text box for the "Match" field
		matchText = new Text(matchReplaceArea, SWT.SINGLE | SWT.BORDER);
		matchText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// set up the label for the "Replace" field
		Label replaceLabel = new Label(matchReplaceArea, SWT.SINGLE);
		replaceLabel.setText("Replace");
		replaceLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		// set up the text box for the "Replace" field
		replaceText = new Text(matchReplaceArea, SWT.SINGLE | SWT.BORDER);
		replaceText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		Composite matchCountComposite = new Composite(parent, SWT.NONE);
		matchCountComposite.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		matchCountComposite.setLayout(new GridLayout(1, false));

		radioReplaceFirst = new Button(matchCountComposite, SWT.RADIO);
		radioReplaceFirst.setText("Replace first occurance only");

		radioReplaceAll = new Button(matchCountComposite, SWT.RADIO);
		radioReplaceAll.setText("Replace all occurances");

		caseSensitiveCheckBox = new Button(parent, SWT.CHECK);
		caseSensitiveCheckBox.setText("Case sensitive");
		caseSensitiveCheckBox.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		// set up the Regular Expressions check box
		regexCheckBox = new Button(parent, SWT.CHECK);
		regexCheckBox.setText("Use Regular Expressions");
		regexCheckBox.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, false, false));

		// set up the Capitalization button area
		Group capitalizationGroup = new Group(parent, SWT.NONE);
		capitalizationGroup.setText("Capitalization");
		capitalizationGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		capitalizationGroup.setLayout(new GridLayout(1, false));

		// set up the Capitalization radio buttons
		radioDontCareCaps = new Button(capitalizationGroup, SWT.RADIO);
		radioDontCareCaps.setText("(no modifier)");

		radioAllCaps = new Button(capitalizationGroup, SWT.RADIO);
		radioAllCaps.setText("ALL CAPS");

		radioAllLower = new Button(capitalizationGroup, SWT.RADIO);
		radioAllLower.setText("all lowercase");

		radioSentenceCaps = new Button(capitalizationGroup, SWT.RADIO);
		radioSentenceCaps.setText("Sentence capitalization");

		radioTitleCaps = new Button(capitalizationGroup, SWT.RADIO);
		radioTitleCaps.setText("Title Capitalization");

		selectRuleConfigurationInUI();

		addRuleModificationListeners();
	}

	private void addRuleModificationListeners() {
		// listener to update the status of the rule's MATCH text
		ModifyListener matchModifiedListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				inputRule.setMatchString(matchText.getText());
				fireRuleChanged(inputRule);
			}
		};
		matchText.addModifyListener(matchModifiedListener);

		// listener to update the status of the rule's REPLACE text
		ModifyListener replaceModifiedListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				inputRule.setReplaceString(replaceText.getText());
				fireRuleChanged(inputRule);
			}
		};
		replaceText.addModifyListener(replaceModifiedListener);

		SelectionAdapter replaceAllListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputRule.setReplacementLimit(ReplacementLimit.ALL);
				fireRuleChanged(inputRule);
			}
		};
		radioReplaceAll.addSelectionListener(replaceAllListener);

		SelectionAdapter replaceFirstListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputRule.setReplacementLimit(ReplacementLimit.FIRST);
				fireRuleChanged(inputRule);
			}
		};
		radioReplaceFirst.addSelectionListener(replaceFirstListener);

		// listener to update the rule's CASE SENSITIVITY
		SelectionAdapter caseSensitiveCheckListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputRule.setCaseSensitive(caseSensitiveCheckBox.getSelection());
				fireRuleChanged(inputRule);
			}
		};
		caseSensitiveCheckBox.addSelectionListener(caseSensitiveCheckListener);

		// listener to update the rule's REGULAR EXPRESSION usage state
		SelectionAdapter regexCheckListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inputRule.setMatchRegularExpressions(regexCheckBox.getSelection());
				fireRuleChanged(inputRule);
			}
		};
		regexCheckBox.addSelectionListener(regexCheckListener);

		SelectionAdapter capitalizationRadioAdapter = new CapitalizationRadioButtonSelectionAdapter();

		radioAllCaps.addSelectionListener(capitalizationRadioAdapter);
		radioAllLower.addSelectionListener(capitalizationRadioAdapter);
		radioSentenceCaps.addSelectionListener(capitalizationRadioAdapter);
		radioTitleCaps.addSelectionListener(capitalizationRadioAdapter);
		radioDontCareCaps.addSelectionListener(capitalizationRadioAdapter);
	}

	private class CapitalizationRadioButtonSelectionAdapter extends SelectionAdapter {
		private final Map<Button, CapitalizationStyle> styleForButton = new HashMap<Button, CapitalizationStyle>();

		public CapitalizationRadioButtonSelectionAdapter() {
			styleForButton.put(radioAllCaps, CapitalizationStyle.ALL_CAPS);
			styleForButton.put(radioAllLower, CapitalizationStyle.NO_CAPS);
			styleForButton.put(radioSentenceCaps, CapitalizationStyle.SENTENCE);
			styleForButton.put(radioTitleCaps, CapitalizationStyle.TITLE);
			styleForButton.put(radioDontCareCaps, CapitalizationStyle.NONE);
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			Button buttonPressed = (Button) e.widget;
			CapitalizationStyle capStyle = styleForButton.get(buttonPressed);
			inputRule.setCapitalizationState(capStyle);

			System.out.println("Changed capitalization style to " + capStyle + " and now firing ruleChanged");
			fireRuleChanged(inputRule);
		}
	}

	@Override
	public void setFocus() {
		Activator.getDefault().getEditorManager().setActiveEditor(this);
	}

}
