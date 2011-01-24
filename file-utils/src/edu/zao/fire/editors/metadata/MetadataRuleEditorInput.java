package edu.zao.fire.editors.metadata;

import java.io.File;

import org.eclipse.jface.resource.ImageDescriptor;

import edu.zao.fire.MetadataRule;
import edu.zao.fire.editors.RenamerRuleEditorInput;

public class MetadataRuleEditorInput extends RenamerRuleEditorInput {

	private final MetadataRule rule;

	public MetadataRuleEditorInput() {
		rule = new MetadataRule();
	}

	public MetadataRuleEditorInput(MetadataRule rule, File saveFile) {
		this.rule = rule;
		this.saveFile = saveFile;
	}

	public MetadataRule getRule() {
		return rule;
	}

	@Override
	public String getDefaultName() {
		return "new Metadata Rule";
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "";
	}

}
