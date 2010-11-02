package edu.zao.fire.editors.list;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;

import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.rcp.Activator;

public class ListRuleEditor extends RenamerRuleEditor {

	public final static String ID = "file-utils.editors.list";

	public ListRuleEditor() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public RenamerRule getRule() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		// create the ascending/descending radio buttons
		Composite topArea = new Composite(parent, SWT.NONE);
		topArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		topArea.setLayout(new FillLayout());

		Button ascendingRadio = new Button(topArea, SWT.RADIO);
		ascendingRadio.setText("Ascending");

		Button descendingRadio = new Button(topArea, SWT.RADIO);
		descendingRadio.setText("Descending");

		// create a separator below the topArea
		Label topSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		topSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// create an area for the middle bits
		Composite midArea = new Composite(parent, SWT.NONE);
		midArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		GridLayout midAreaLayout = new GridLayout(3, false);
		midAreaLayout.marginHeight = 0;
		midAreaLayout.marginWidth = 0;
		midArea.setLayout(midAreaLayout);

		// create the area for the type radio buttons
		Composite typeRadioArea = new Composite(midArea, SWT.NONE);
		GridLayout typeRadioAreaLayout = new GridLayout(1, false);
		typeRadioAreaLayout.marginWidth = 0;
		typeRadioAreaLayout.marginHeight = 0;
		typeRadioArea.setLayout(typeRadioAreaLayout);
		GridData typeRadioAreaData = new GridData(SWT.LEFT, SWT.TOP, false, false);
		typeRadioAreaData.verticalSpan = 2;
		typeRadioArea.setLayoutData(typeRadioAreaData);

		// create the type radio buttons
		Button typeABCRadio = new Button(typeRadioArea, SWT.RADIO);
		typeABCRadio.setText("a, b, c...");

		Button type123Radio = new Button(typeRadioArea, SWT.RADIO);
		type123Radio.setText("1, 2, 3...");

		Button typeiiiRadio = new Button(typeRadioArea, SWT.RADIO);
		typeiiiRadio.setText("i, ii, iii...");

		// create the "Start From" label and spinner(?)
		Label startFromLabel = new Label(midArea, SWT.SINGLE);
		startFromLabel.setText("Start from: ");
		startFromLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));

		Text startFromText = new Text(midArea, SWT.SINGLE | SWT.BORDER);
		GridData startFromTextData = new GridData(SWT.FILL, SWT.CENTER, true, true);
		startFromText.setLayoutData(startFromTextData);

		// create the number of digits label and spinner
		Label numDigitsLabel = new Label(midArea, SWT.SINGLE);
		numDigitsLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false));
		numDigitsLabel.setText("Digits Displayed: ");

		Spinner numDigitsSpinner = new Spinner(midArea, SWT.WRAP | SWT.BORDER);
		numDigitsSpinner.setValues(0, 0, 6, 0, 1, 1);
		numDigitsSpinner.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));

		// create another separator
		Label midSeparator = new Label(parent, SWT.HORIZONTAL | SWT.SEPARATOR);
		midSeparator.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// create the bottom area
		Composite bottomArea = new Composite(parent, SWT.NONE);
		FillLayout bottomAreaLayout = new FillLayout();
		bottomAreaLayout.marginHeight = 0;
		bottomAreaLayout.marginWidth = 0;
		bottomArea.setLayout(bottomAreaLayout);
		bottomArea.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// create the "Add to:" controls
		Label addToLabel = new Label(bottomArea, SWT.SINGLE);
		addToLabel.setText("Add to:");

		Button addToStartRadio = new Button(bottomArea, SWT.RADIO);
		addToStartRadio.setText("Start");
		Button addToEndRadio = new Button(bottomArea, SWT.RADIO);
		addToEndRadio.setText("End");

		// create another bottom area...
		Composite bottomArea2 = new Composite(parent, SWT.NONE);
		bottomArea2.setLayout(bottomAreaLayout);
		bottomArea2.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));

		// // create the "Separate with: " field
		Label sepWithLabel = new Label(bottomArea2, SWT.SINGLE);
		sepWithLabel.setText("Separate with: ");
		//
		Text sepWithText = new Text(bottomArea2, SWT.SINGLE | SWT.BORDER);
		// sepWithText.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true,
		// false));
	}

	@Override
	public void setFocus() {
		Activator.getDefault().getEditorManager().setActiveEditor(this);
	}

}
