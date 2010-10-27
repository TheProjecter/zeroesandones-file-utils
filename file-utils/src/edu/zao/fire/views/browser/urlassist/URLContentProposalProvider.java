package edu.zao.fire.views.browser.urlassist;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import edu.zao.fire.views.browser.BrowserURLHistory;

public class URLContentProposalProvider implements IContentProposalProvider {

	private final BrowserURLHistory history;

	public URLContentProposalProvider(BrowserURLHistory history) {
		this.history = history;
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		ArrayList<IContentProposal> proposals = new ArrayList<IContentProposal>();
		String userText = contents.substring(0, position);
		for (String url : history.getVisitedLocations()) {
			if (url.toUpperCase().startsWith(userText.toUpperCase())) {
				proposals.add(new URLContentProposal(url, userText));
			}
		}
		return proposals.toArray(new IContentProposal[proposals.size()]);
	}

}
