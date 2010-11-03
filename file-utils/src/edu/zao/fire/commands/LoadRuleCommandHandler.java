package edu.zao.fire.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.zao.fire.ListRule;
import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MetadataRule;
import edu.zao.fire.editors.list.ListRuleEditor;
import edu.zao.fire.editors.list.ListRuleEditorInput;
import edu.zao.fire.editors.matchreplace.MatchReplaceRuleEditor;
import edu.zao.fire.editors.matchreplace.MatchReplaceRuleEditorInput;
import edu.zao.fire.editors.metadata.MetadataRuleEditor;
import edu.zao.fire.editors.metadata.MetadataRuleEditorInput;

public class LoadRuleCommandHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPage page = window.getActivePage();
		FileDialog fd = new FileDialog(window.getShell());
		fd.setFilterExtensions(new String[] {
			"*.frr"
		});
		fd.setText("Load Renaming Rule...");
		String filename = fd.open();
		if (filename != null) {
			File ruleFile = new File(filename);
			if (ruleFile.isFile()) {
				Object rule;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruleFile));
					rule = ois.readObject();

				} catch (Exception e) {
					throw new ExecutionException("Failure to read file: " + ruleFile, e);
				}

				if (rule instanceof MatchReplaceRule) {
					MatchReplaceRule mrRule = (MatchReplaceRule) rule;
					MatchReplaceRuleEditorInput input = new MatchReplaceRuleEditorInput(mrRule);
					try {
						page.openEditor(input, MatchReplaceRuleEditor.ID);
					} catch (PartInitException e) {
						throw new ExecutionException("Could not open the Match/Replace Rule Editor", e);
					}
				}

				if (rule instanceof ListRule) {
					ListRule lRule = (ListRule) rule;
					ListRuleEditorInput input = new ListRuleEditorInput(lRule);
					try {
						page.openEditor(input, ListRuleEditor.ID);
					} catch (PartInitException e) {
						throw new ExecutionException("Could not open the List Rule Editor", e);
					}
				}

				if (rule instanceof MetadataRule) {
					MetadataRule mRule = (MetadataRule) rule;
					MetadataRuleEditorInput input = new MetadataRuleEditorInput(mRule);
					try {
						page.openEditor(input, MetadataRuleEditor.ID);
					} catch (PartInitException e) {
						throw new ExecutionException("Could not open the Metadata Rule Editor", e);
					}
				}
			}
		}

		return null;
	}

}
