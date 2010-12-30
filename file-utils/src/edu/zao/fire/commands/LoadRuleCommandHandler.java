package edu.zao.fire.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import edu.zao.fire.ListRule;
import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MetadataRule;
import edu.zao.fire.RenamerRule;
import edu.zao.fire.editors.RenamerRuleEditorInput;
import edu.zao.fire.editors.list.ListRuleEditor;
import edu.zao.fire.editors.list.ListRuleEditorInput;
import edu.zao.fire.editors.matchreplace.MatchReplaceRuleEditor;
import edu.zao.fire.editors.matchreplace.MatchReplaceRuleEditorInput;
import edu.zao.fire.editors.metadata.MetadataRuleEditor;
import edu.zao.fire.editors.metadata.MetadataRuleEditorInput;
import edu.zao.fire.rcp.Activator;

/**
 * The command that is executed when the
 * <code>file-utils.commands.openRule</code> command is called through the
 * Eclipse RCP framework. (see plugin.xml)
 * 
 * @author Dylan
 * 
 */
public class LoadRuleCommandHandler extends AbstractHandler {

	public final static String CommandID = "file-utils.commands.openRule";

	public final static String fileNameKey = "fileName";

	/**
	 * Opens a file dialog and asks the user to select a ".frr" (file renamer
	 * rule) file. If a file is selected, the UI will attempt to open the
	 * appropriate RenamerRuleEditor for that file's renamer rule.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Object fileObject = event.getParameter(fileNameKey);

		IWorkbenchPage page = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage();

		String filename = (String) fileObject;

		openEditor(filename, page);

		return null;
	}

	protected void openEditor(String filename, IWorkbenchPage page) throws ExecutionException {
		if (filename != null) {
			File ruleFile = new File(filename);
			if (ruleFile.isFile()) {
				Object object;
				try {
					ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruleFile));
					object = ois.readObject();

				} catch (Exception e) {
					throw new ExecutionException("Failure to read file: " + ruleFile, e);
				}

				if (!(object instanceof RenamerRule)) {
					throw new ExecutionException("The object that was read was not a RenamerRule");
				}

				RenamerRule rule = (RenamerRule) object;

				// add the file to the recently loaded file set
				Activator.getDefault().getRecentlyLoadedRules().add(ruleFile);

				RenamerRuleEditorInput input = getEditorInput(rule, ruleFile);
				String editorID = getEditorID(rule);

				try {
					page.openEditor(input, editorID);
				} catch (PartInitException e) {
					throw new ExecutionException("Failed to open editor.", e);
				}
			}
		}
	}

	protected RenamerRuleEditorInput getEditorInput(RenamerRule rule, File file) {
		if (rule instanceof MatchReplaceRule) {
			return new MatchReplaceRuleEditorInput((MatchReplaceRule) rule, file);
		}
		if (rule instanceof ListRule) {
			return new ListRuleEditorInput((ListRule) rule, file);
		}
		if (rule instanceof MetadataRule) {
			return new MetadataRuleEditorInput((MetadataRule) rule, file);
		}
		return null;
	}

	protected String getEditorID(RenamerRule rule) {
		if (rule instanceof MatchReplaceRule) {
			return MatchReplaceRuleEditor.ID;
		}
		if (rule instanceof ListRule) {
			return ListRuleEditor.ID;
		}
		if (rule instanceof MetadataRule) {
			return MetadataRuleEditor.ID;
		}
		return null;
	}
}
