package edu.zao.fire.views.browser;

import java.util.HashSet;

import edu.zao.fire.Renamer;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.editors.RenamerRuleEditorManager;
import edu.zao.fire.editors.RenamerRuleEditorManager.ActiveEditorListener;
import edu.zao.fire.rcp.Activator;

public class RenamerUIAdapter implements ActiveEditorListener {

	/**
	 * A Renamer instance that will be subscribed as a listener to various
	 * RenamerRuleEditors and the global EditorManager
	 */
	private final Renamer renamer;

	/**
	 * A set of the rule editors that the renamer has been subscribed to as a
	 * listener. This will help prevent multiple registration by the renamer to
	 * a single editor.
	 */
	private final HashSet<RenamerRuleEditor> editorsSubscribedTo = new HashSet<RenamerRuleEditor>();

	/**
	 * Constructor.
	 * 
	 * @param renamer
	 *            A reference to this will be stored internally within the
	 *            Adapter.
	 */
	public RenamerUIAdapter(Renamer renamer) {
		this.renamer = renamer;
	}

	/**
	 * Adds the <code>renamer</code> as a listener to the
	 * {@link RenamerRuleEditorManager}, as well as adding itself. The aim here
	 * is to ensure that the <code>renamer</code> is always notified when an
	 * editor is selected, and that the renamer will be registered as a listener
	 * to that editor so that rule changes will be forwarded to it.
	 */
	public void installListeners() {
		RenamerRuleEditorManager editorManager = Activator.getDefault().getEditorManager();

		editorManager.addListener(this);
		editorManager.addListener(renamer);
	}

	/**
	 * When this method is invoked by the {@link RenamerRuleEditorManager}, this
	 * adapter will automatically register the <code>renamer</code> as a
	 * listener to the new active editor, if it hasn't already done so.
	 */
	@Override
	public void activeEditorChanged(RenamerRuleEditor newActiveEditor) {
		if (newActiveEditor == null) {
			return;
		}
		if (!editorsSubscribedTo.contains(newActiveEditor)) {
			editorsSubscribedTo.add(newActiveEditor);
			newActiveEditor.addRuleChangeListener(renamer);
		}
	}

}
