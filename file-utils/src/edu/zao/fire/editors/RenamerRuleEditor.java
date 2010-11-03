package edu.zao.fire.editors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.part.EditorPart;

import edu.zao.fire.RenamerRule;
import edu.zao.fire.rcp.Activator;

/**
 * Common ground for the various renamer rule editors.
 * 
 * @author dylan
 * 
 */
public abstract class RenamerRuleEditor extends EditorPart {
	private final List<RenamerRuleChangeListener> changeListeners = new LinkedList<RenamerRuleChangeListener>();
	private boolean isDirty = false;

	/**
	 * @return The RenamerRule that this editor makes changes to.
	 */
	public abstract RenamerRule getRule();

	protected void fireRuleChanged(RenamerRule changedRule) {
		setDirty(true);
		for (RenamerRuleChangeListener listener : changeListeners) {
			listener.ruleChanged(changedRule);
		}
	}

	public void addRuleChangeListener(RenamerRuleChangeListener listener) {
		changeListeners.add(listener);
	}

	public void removeRuleChangeListener(RenamerRuleChangeListener listener) {
		changeListeners.remove(listener);
	}

	private void setDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public boolean isSaveOnCloseNeeded() {
		return false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println("Saving " + this + " with input " + getRule());

		RenamerRuleEditorInput input = (RenamerRuleEditorInput) getEditorInput();
		File outFile;
		if (input.saveFile != null && input.saveFile.isFile()) {
			outFile = input.saveFile;
		} else {
			FileDialog saveDialog = new FileDialog(this.getSite().getShell());
			saveDialog.setFilterExtensions(new String[] {
				"*.frr"
			});
			saveDialog.setOverwrite(true);
			saveDialog.setText("Save Rule...");
			String filename = saveDialog.open();
			if (!filename.endsWith(".frr")) {
				filename = filename + ".frr";
			}
			System.out.println("save to " + filename);
			outFile = new File(filename);
		}
		try {
			input.saveRuleToFile(getRule(), outFile);
			String newTitle = outFile.getName();
			// int frrIndex = newTitle.indexOf(".frr");
			// if (frrIndex >= 0) {
			// newTitle = newTitle.substring(0, frrIndex);
			// }
			setPartName(newTitle);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		setDirty(false);
	}

	@Override
	public void doSaveAs() {
		// do nothing, as this is not yet supported
		// TODO: not sure if we should "Save as" or not...
	}

	@Override
	public void setFocus() {
		Activator.getDefault().getEditorManager().setActiveEditor(this);
	}

	@Override
	public void dispose() {
		Activator.getDefault().getEditorManager().editorDisposed(this);
		super.dispose();
	}
}
