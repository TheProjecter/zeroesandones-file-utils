package edu.zao.fire.editors.matchreplace;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.editors.RenamerRuleEditorInput;

/**
 * Editor Input class for the MatchReplaceRuleEditor.
 * 
 * @author Dylan
 */
public class MatchReplaceRuleEditorInput extends RenamerRuleEditorInput {

	private final MatchReplaceRule rule;

	public MatchReplaceRuleEditorInput() {
		rule = new MatchReplaceRule();
	}

	public MatchReplaceRuleEditorInput(MatchReplaceRule rule, File saveFile) {
		this.rule = rule;
		this.saveFile = saveFile;
	}

	/**
	 * @return A reference to the RenamerRule that this editor input
	 *         encapsulates
	 */
	public MatchReplaceRule getRule() {
		return rule;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultName() {
		return "new Match/Replace rule";
	}

	@Override
	public String getToolTipText() {
		// no tooltip is provided
		return "";
	}

}
