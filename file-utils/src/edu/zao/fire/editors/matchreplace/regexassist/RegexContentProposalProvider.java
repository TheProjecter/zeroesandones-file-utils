package edu.zao.fire.editors.matchreplace.regexassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

public class RegexContentProposalProvider implements IContentProposalProvider {

	private final List<RegexContentProposal> proposals = new ArrayList<RegexContentProposal>();

	public RegexContentProposalProvider() {
		proposals.add(new RegexContentProposal("^", 1, "Start of line"));
		proposals.add(new RegexContentProposal("\\A", 2, "Start of string"));
		proposals.add(new RegexContentProposal("$", 1, "End of line"));
		proposals.add(new RegexContentProposal("\\Z", 2, "End of string"));
		proposals.add(new RegexContentProposal("\\b", 2, "Word boundary"));
		proposals.add(new RegexContentProposal("\\B", 2, "Not word boundary"));
		proposals.add(new RegexContentProposal(".", 1, "Any character"));
		proposals.add(new RegexContentProposal("()", 1, "Group"));
		proposals.add(new RegexContentProposal("(?:)", 2, "Passive Group"));
		proposals.add(new RegexContentProposal("[]", 1, "Range"));
		proposals.add(new RegexContentProposal("[^]", 2, "Excluding Range"));
		proposals.add(new RegexContentProposal("[a-zA-Z]", 8, "Match any letter"));
		proposals.add(new RegexContentProposal("*", 1, "0 or more"));
		proposals.add(new RegexContentProposal("+", 1, "1 or more"));
		proposals.add(new RegexContentProposal("?", 1, "0 or 1"));
		proposals.add(new RegexContentProposal("{3}", 2, "exactly 3"));
		proposals.add(new RegexContentProposal("{3,}", 2, "3 or more"));
		proposals.add(new RegexContentProposal("{3,5}", 4, "3, 4, or 5"));
	}

	@Override
	public IContentProposal[] getProposals(String contents, int position) {
		for (RegexContentProposal proposal : proposals) {
			proposal.setUserCaratPos(position);
		}
		RegexContentProposal[] proposalsArray = new RegexContentProposal[proposals.size()];
		return proposals.toArray(proposalsArray);
	}

}
