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

	// @Override
	// protected void fireHandlerChanged(HandlerEvent handlerEvent) {
	// // TODO Auto-generated method stub
	// super.fireHandlerChanged(handlerEvent);
	// }

	@Override
	public boolean isEnabled() {
		RenamerRuleEditor editor = Activator.getDefault().getEditorManager().getActiveEditor();
		if (editor == null) {
			return false;
		}
		return editor.isDirty();
	}

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

	@Override
	public void ruleChanged(RenamerRule rule) {
		fireHandlerChanged(new HandlerEvent(this, true, false));
	}
}
