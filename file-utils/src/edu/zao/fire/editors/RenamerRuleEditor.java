package edu.zao.fire.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
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
