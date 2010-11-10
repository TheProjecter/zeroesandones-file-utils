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

	/**
	 * Mark this editor as "dirty" (it gets a star next to its name, and becomes
	 * saveable) and notifies any listeners that this editor's renamer rule has
	 * been modified.
	 * 
	 * @param changedRule
	 */
	protected void fireRuleChanged(RenamerRule changedRule) {
		setDirty(true);
		for (RenamerRuleChangeListener listener : changeListeners) {
			listener.ruleChanged(changedRule);
		}
	}

	/**
	 * Attach the given listener to this editor so that it will be notified
	 * whenever this editor's RenamerRule is modified.
	 * 
	 * @param listener
	 */
	public void addRuleChangeListener(RenamerRuleChangeListener listener) {
		changeListeners.add(listener);
	}

	/**
	 * Remove the given listener from this editor so that it will no longer be
	 * notified whenever this editor's RenamerRule is modified.
	 * 
	 * @param listener
	 */
	public void removeRuleChangeListener(RenamerRuleChangeListener listener) {
		changeListeners.remove(listener);
	}

	/**
	 * Marks this editor as dirty so that the {@link #isDirty()} method will
	 * return the correct value, then notifies Eclipse RCP so that any
	 * dirtiness-related events get triggered (such as adding the star next to
	 * the editor's name in the title tab).
	 * 
	 * @param isDirty
	 */
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

	/**
	 * Queries the user for a save location, then saves the current RenamerRule
	 * to that location. Un-marks this editor as dirty.
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		System.out.println("Saving " + this + " with input " + getRule());

		RenamerRuleEditorInput input = (RenamerRuleEditorInput) getEditorInput();
		File outFile;
		if (input.saveFile != null && input.saveFile.isFile()) {
			outFile = input.saveFile;
		} else {
			FileDialog saveDialog = new FileDialog(this.getSite().getShell());
			saveDialog.setFilterExtensions(new String[] { "*.frr" });
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

	/**
	 * Notify the editor manager that this editor is the new "active" editor.
	 */
	@Override
	public void setFocus() {
		Activator.getDefault().getEditorManager().setActiveEditor(this);
	}

	/**
	 * Notify the editor manager that this editor is now disposed.
	 */
	@Override
	public void dispose() {
		Activator.getDefault().getEditorManager().editorDisposed(this);
		super.dispose();
	}
}
