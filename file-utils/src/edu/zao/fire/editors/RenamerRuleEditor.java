package edu.zao.fire.editors;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.ui.part.EditorPart;

import edu.zao.fire.RenamerRule;

/**
 * Common ground for the various renamer rule editors.
 * 
 * @author dylan
 * 
 */
public abstract class RenamerRuleEditor extends EditorPart {
	private final List<RenamerRuleChangeListener> changeListeners = new LinkedList<RenamerRuleChangeListener>();

	/**
	 * @return The RenamerRule that this editor makes changes to.
	 */
	public abstract RenamerRule getRule();

	protected void fireRuleChanged(RenamerRule changedRule) {
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

}
