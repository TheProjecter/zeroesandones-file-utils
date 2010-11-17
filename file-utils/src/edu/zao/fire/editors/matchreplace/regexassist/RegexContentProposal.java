package edu.zao.fire.editors.matchreplace.regexassist;

import org.eclipse.jface.fieldassist.IContentProposal;

public class RegexContentProposal implements IContentProposal {

	private int userCaratPos;
	private final String regexText;
	private final int regexCaratPos;
	private final String conciseDescription;

	public RegexContentProposal(String regexText, int regexCaratPos, String conciseDescription) {
		this.regexText = regexText;
		this.regexCaratPos = regexCaratPos;
		this.conciseDescription = conciseDescription;
	}

	@Override
	public String getContent() {
		return regexText;
	}

	public void setUserCaratPos(int newCaratPos) {
		userCaratPos = newCaratPos;
	}

	@Override
	public int getCursorPosition() {
		return userCaratPos + regexCaratPos;
	}

	@Override
	public String getLabel() {
		return regexText + " - " + conciseDescription;
	}

	@Override
	public String getDescription() {
		return null;
	}

}
