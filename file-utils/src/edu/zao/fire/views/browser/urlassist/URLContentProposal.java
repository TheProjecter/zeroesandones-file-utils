package edu.zao.fire.views.browser.urlassist;

import org.eclipse.jface.fieldassist.IContentProposal;

public class URLContentProposal implements IContentProposal {

	private final String url;
	private final String userText;

	public URLContentProposal(String url, String userText) {
		this.url = url;
		this.userText = userText;
	}

	@Override
	public String getContent() {
		return url.substring(userText.length());
	}

	@Override
	public int getCursorPosition() {
		return url.length();
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLabel() {
		return url;
	}

}
