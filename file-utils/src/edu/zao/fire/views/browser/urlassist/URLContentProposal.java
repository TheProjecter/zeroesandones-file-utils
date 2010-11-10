package edu.zao.fire.views.browser.urlassist;

import org.eclipse.jface.fieldassist.IContentProposal;

/**
 * Helper class for the text helper popup on the URL bar. A URLContentProposal
 * consists of a string to be chosen by the user, and another string that
 * represents the actual text content to be added when the user picks this
 * proposal.
 * 
 * @author Dylan
 * 
 */
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
		// by returning something non-null here, a tooltip text box will pop up
		return null;
	}

	@Override
	public String getLabel() {
		return url;
	}

}
