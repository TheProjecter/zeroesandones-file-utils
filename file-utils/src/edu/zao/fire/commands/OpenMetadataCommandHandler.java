package edu.zao.fire.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.zao.fire.editors.metadata.MetadataRuleEditor;
import edu.zao.fire.editors.metadata.MetadataRuleEditorInput;

/**
 * Command invoked by the Eclipse RCP framework (as set up in plugin.xml)
 * whenever the user selects the "New Metadata Rule" option from the menu or the
 * toolbar.
 * 
 * @author Dylan
 */
public class OpenMetadataCommandHandler extends AbstractHandler {

	/**
	 * Opens a new MetadataRuleEditor in the UI, with a brand new
	 * MetadataEditorInput.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();

		try {
			page.openEditor(new MetadataRuleEditorInput(), MetadataRuleEditor.ID);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
		return null;
	}
}
