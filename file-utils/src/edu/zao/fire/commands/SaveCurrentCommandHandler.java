package edu.zao.fire.commands;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;

import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleChangeListener;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.editors.RenamerRuleEditorManager.ActiveEditorListener;
import edu.zao.fire.rcp.Activator;

/**
 * Command handler for the "Save" command. The handler handles the execution of
 * the command as well as deciding whether the command is enabled at any given
 * time.
 * 
 * @author Dylan
 */
public class SaveCurrentCommandHandler extends AbstractHandler implements ActiveEditorListener, RenamerRuleChangeListener {

	private final Set<RenamerRuleEditor> editorsObserved = new HashSet<RenamerRuleEditor>();
	private RenamerRuleEditor currentEditor;

	public SaveCurrentCommandHandler() {
		Activator.getDefault().getEditorManager().addListener(this);
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Going to save...");
		Activator.getDefault().getEditorManager().getActiveEditor().doSave(null);
		return null;
	}

	/**
	 * The save command is enabled whenever there is a dirty active editor.
	 */
	@Override
	public boolean isEnabled() {
		RenamerRuleEditor editor = Activator.getDefault().getEditorManager().getActiveEditor();
		if (editor == null) {
			return false;
		}
		return editor.isDirty();
	}

	/**
	 * Checks if the newActiveEditor is an editor that this handler has not
	 * subscribed to, and if so it registers itself as a listener, so that it
	 * will be notified whenever that editor becomes dirty (thereby enabling the
	 * save command).
	 */
	@Override
	public void activeEditorChanged(RenamerRuleEditor newActiveEditor) {
		if (newActiveEditor != null) {
			if (!editorsObserved.contains(newActiveEditor)) {
				editorsObserved.add(newActiveEditor);
				newActiveEditor.addRuleChangeListener(this);
			}
		}
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}

	/**
	 * Fire a handler event that says that the "enabled" state has changed
	 */
	@Override
	public void ruleChanged(RenamerRule rule) {
		// fire a handler event that says that the "enabled" state has changed
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}
}
