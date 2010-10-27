package edu.zao.fire;

import java.io.File;
import java.io.IOException;

public class MatchReplaceRule implements RenamerRule {

	/**
	 * A generated serial ID
	 */
	private static final long serialVersionUID = 6189927401626899561L;

	private String matchString = " ";
	private String replaceString = " ";

	private boolean isCaseSensitive = false;
	private boolean matchRegularExpressions = false;

	public static enum CapitalizationStyle {
		NONE, ALL_CAPS, NO_CAPS, SENTENCE, TITLE
	}

	private CapitalizationStyle capitalizationState = CapitalizationStyle.NONE;

	/**
	 * Finds each instance of the <code>matchString</code> within the
	 * <code>file</code>'s name, and replaces it with <code>replaceString</code>
	 */
	@Override
	public String getNewName(File file) throws IOException {
		String fileName = file.getName();
		String newName = fileName.replace(matchString, replaceString);
		return newName;
		// String fullName = file.getCanonicalPath();
		// int fileNameIndex = fullName.lastIndexOf(fileName);
		// return fullName.substring(0, fileNameIndex) + newName;
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

	@Override
	public void setup() {
		// no setup actions are required
	}

	@Override
	public void tearDown() {
		// no teardown actions are required
	}
}
