package edu.zao.fire.editors;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class that gets informed by a RenamerRuleEditor when that editor gets
 * focus. Objects can be registered with this Manager to be notified of this as
 * well (like the Renamer, which needs to update its currentRule).
 * 
 * @author dylan
 * 
 */
public class RenamerRuleEditorManager {

	private RenamerRuleEditor activeEditor;

	/**
	 * Defines the behavior of an object that listens to a
	 * RenamerRuleEditorManager. If an object wants to be notified of changes to
	 * the activeEditor, it must implement this interface.
	 * 
	 * @author dylan
	 * 
	 */
	public static interface ActiveEditorListener {
		public void activeEditorChanged(RenamerRuleEditor newActiveEditor);
	}

	private final List<ActiveEditorListener> listeners = new ArrayList<ActiveEditorListener>();

	/**
	 * @return The editor that last received focus.
	 */
	public RenamerRuleEditor getActiveEditor() {
		return activeEditor;
	}

	/**
	 * Add an object to be notified whenever the active editor changes.
	 * 
	 * @param listener
	 *            The object being added.
	 */
	public void addListener(ActiveEditorListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove an object so that it is no longer notified whenever the active
	 * editor changes.
	 * 
	 * @param listener
	 *            The object being removed.
	 */
	public void removeListener(ActiveEditorListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all of the registered listeners that the active editor has changed
	 * 
	 * @param editor
	 *            The new active editor
	 */
	private void fireEditorChanged(RenamerRuleEditor editor) {
		System.out.println("new editor is " + editor);
		for (ActiveEditorListener listener : listeners) {
			listener.activeEditorChanged(editor);
		}
	}

	/**
	 * Change the active editor. Calling this function only notifies this
	 * manager that the active editor has changed; it does <i>not</i> actually
	 * change the active editor. This method should only be called by an editory
	 * when it receives focus.
	 * 
	 * @param editor
	 *            The editor that just received focus.
	 */
	public void setActiveEditor(RenamerRuleEditor editor) {
		if (editor != activeEditor) {
			activeEditor = editor;
			fireEditorChanged(editor);
		}
	}

	public void editorDisposed(RenamerRuleEditor editor) {
		if (editor == activeEditor) {
			activeEditor = null;
			fireEditorChanged(null);
		}
	}
}
