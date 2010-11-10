package edu.zao.fire.views.browser.urlassist;

import java.util.ArrayList;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import edu.zao.fire.views.browser.BrowserURLHistory;

/**
 * Helper class for the URL text helper popup. The ProposalProvider creates a
 * list of proposals that will be fed to the popop for the user to choose.
 * Proposals are chosen based on relevance to the user's currently-entered text,
 * and all of the locations that the user has visited through the browser in the
 * current session.
 * 
 * @author Dylan
 */
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
