package edu.zao.fire.editors.matchreplace;

import org.eclipse.jface.resource.ImageDescriptor;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.editors.RenamerRuleEditorInput;

public class MatchReplaceRuleEditorInput extends RenamerRuleEditorInput {

	private final MatchReplaceRule rule;

	public MatchReplaceRuleEditorInput() {
		rule = new MatchReplaceRule();
	}

	public MatchReplaceRuleEditorInput(MatchReplaceRule rule) {
		this.rule = rule;
	}

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
		// TODO Auto-generated method stub
		return "new Match/Replace rule";
	}

	@Override
	public String getToolTipText() {
		return "";
	}

}
