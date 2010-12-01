package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchReplaceRule implements RenamerRule {

	/**
	 * A generated serial ID
	 */
	private static final long serialVersionUID = 6189927401626899561L;

	private String matchString = "";
	private String replaceString = "";

	private boolean isCaseSensitive = false;
	private boolean matchRegularExpressions = false;

	public static enum CapitalizationStyle {
		NONE, ALL_CAPS, NO_CAPS, SENTENCE, TITLE
	}

	private CapitalizationStyle capitalizationState = CapitalizationStyle.NONE;

	public static enum ReplacementLimit {
		FIRST, ALL
	}

	private ReplacementLimit replacementLimit = ReplacementLimit.FIRST;

	/**
	 * Finds each instance of the <code>matchString</code> within the
	 * <code>file</code>'s name, and replaces it with <code>replaceString</code>
	 */
	@Override
	public String getNewName(File file) throws IOException {
		String fileName = file.getName();
		String newName;
		if (matchString.isEmpty()) {
			newName = fileName;
		} else {
			newName = doReplacement(fileName);
		}
		return getNameWithCapitalizationStyle(newName, capitalizationState);
	}

	private String doReplacement(String name) throws IOException {
		String localMatch;
		String localReplace;
		if (matchRegularExpressions) {
			localMatch = matchString;
			localReplace = replaceString;
		} else {
			localMatch = Pattern.quote(matchString);
			localReplace = Matcher.quoteReplacement(replaceString);
		}
		try {
			Pattern matchRegex;
			if (isCaseSensitive) {
				matchRegex = Pattern.compile(localMatch);
			} else {
				matchRegex = Pattern.compile(localMatch, Pattern.CASE_INSENSITIVE);
			}
			Matcher matcher = matchRegex.matcher(name);
			switch (replacementLimit) {
			case ALL:
				return matcher.replaceAll(localReplace);
			case FIRST:
			default:
				return matcher.replaceFirst(localReplace);
			}
		} catch (Exception e) {
			throw new UserRegexException(e, name, matchString, replaceString);
		}
	}

	private String getNameWithCapitalizationStyle(String name, CapitalizationStyle capStyle) {
		switch (capStyle) {
		case ALL_CAPS:
			return name.toUpperCase();
		case NO_CAPS:
			return name.toLowerCase();
		case NONE:
			return name;
		case SENTENCE:
			return makeSentenceCaseString(name);
		case TITLE:
			return makeTitleCaseString(name);
		}
		return null;
	}

	private String makeTitleCaseString(String name) {
		// capitalize the first letter after any space found in the name
		StringBuilder sb = new StringBuilder();
		boolean gotSpace = true;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (gotSpace && Character.isLetter(c)) {
				sb.append(Character.toUpperCase(c));
				gotSpace = false;
			} else {
				sb.append(Character.toLowerCase(c));
			}
			if (Character.isWhitespace(c)) {
				gotSpace = true;
			}
		}
		return sb.toString();
	}

	private String makeSentenceCaseString(String name) {
		// capitalize the first letter found in the name
		StringBuilder sb = new StringBuilder();
		boolean gotFirstLetter = false;
		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (!gotFirstLetter && Character.isLetter(c)) {
				sb.append(Character.toUpperCase(c));
				gotFirstLetter = true;
			} else {
				sb.append(Character.toLowerCase(c));
				// System.out.print(sb.toString() + " -> ");
			}
		}
		return sb.toString();
	}

	/**
	 * Set the matchString to a desired value.
	 * 
	 * @param matchString
	 *            The new value of matchString
	 */
	public void setMatchString(String matchString) {
		this.matchString = matchString;
	}

	/**
	 * @return The current value of matchString
	 */
	public String getMatchString() {
		return matchString;
	}

	/**
	 * Set the replaceString to a desired value.
	 * 
	 * @param replaceString
	 */
	public void setReplaceString(String replaceString) {
		this.replaceString = replaceString;
	}

	/**
	 * @return The current value of replaceString
	 */
	public String getReplaceString() {
		return replaceString;
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		this.isCaseSensitive = isCaseSensitive;
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public void setMatchRegularExpressions(boolean matchRegularExpressions) {
		this.matchRegularExpressions = matchRegularExpressions;
	}

	public boolean isMatchRegularExpressions() {
		return matchRegularExpressions;
	}

	public void setCapitalizationState(CapitalizationStyle capitalizationState) {
		this.capitalizationState = capitalizationState;
	}

	public CapitalizationStyle getCapitalizationState() {
		return capitalizationState;
	}

	public void setReplacementLimit(ReplacementLimit replacementLimit) {
		this.replacementLimit = replacementLimit;
	}

	public ReplacementLimit getReplacementLimit() {
		return replacementLimit;
	}

	@Override
	public void setup() {
		// no setup actions are required
	}

	@Override
	public void tearDown() {
		// no teardown actions are required
	}

}
