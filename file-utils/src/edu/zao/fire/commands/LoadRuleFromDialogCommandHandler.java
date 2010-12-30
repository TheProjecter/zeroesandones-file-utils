package edu.zao.fire.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class LoadRuleFromDialogCommandHandler extends LoadRuleCommandHandler {

	public final static String CommandID = "file-utils.commands.openRuleFromDialog";

	protected String selectFileFromDialog(ExecutionEvent event) {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		FileDialog fd = new FileDialog(window.getShell());
		fd.setFilterExtensions(new String[] { "*.frr" });
		fd.setText("Load Renaming Rule...");
		String filename = fd.open();

		return filename;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String filename = selectFileFromDialog(event);
		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();
		openEditor(filename, page);
		return null;
	}

}
