package edu.zao.fire.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.HandlerEvent;

import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.editors.RenamerRuleEditorManager.ActiveEditorListener;
import edu.zao.fire.rcp.Activator;

public class SaveCurrentCommandHandler extends AbstractHandler implements ActiveEditorListener {

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
		return Activator.getDefault().getEditorManager().getActiveEditor() != null;
	}

	@Override
	public void activeEditorChanged(RenamerRuleEditor newActiveEditor) {
		fireHandlerChanged(new HandlerEvent(this, true, false));
		System.out.println("enabled changed");
	}
}
