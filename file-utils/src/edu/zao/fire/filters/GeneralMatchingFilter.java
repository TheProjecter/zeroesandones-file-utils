package edu.zao.fire.filters;

import java.io.File;
import java.util.regex.Pattern;

public class GeneralMatchingFilter extends BrowserFileFilter {

	public enum MatchType {
		FILE, FOLDER, ALL_ITEMS
	}

	/**
	 * Whether this filter applies to all objects described by
	 * <code>matchType</code>
	 */
	private boolean appliesGlobally;

	/**
	 * Whether this filter applies to Files, Folders, or Both
	 */
	private MatchType matchType = MatchType.ALL_ITEMS;

	private boolean usesMatchText;

	private String matchText = "";

	private boolean isCaseSensitive;

	private boolean usesRegex;

	@Override
	public boolean accept(File item) {
		String filename = item.getName();
		switch (getMatchType()) {
		case FILE:
			if (item.isFile()) {
				if (!isUsesMatchText()) {
					return false; // filter out all files
				}
				return doMatching(filename);
			}
			break;
		case FOLDER:
			if (item.isDirectory()) {
				if (!isUsesMatchText()) {
					return false; // filter out all folders
				}
				return doMatching(filename);
			}
			break;
		case ALL_ITEMS:
		default:
			if (!isUsesMatchText()) {
				return false; // filter out all items
			}
			return doMatching(filename);
		}
		return true;
	}

	private boolean doMatching(String filename) {
		if (!isUsesMatchText()) {
			throw new IllegalArgumentException("Pattern does not use matching. Don't try to use matching");
		}
		int patternFlags = isCaseSensitive() ? 0 : Pattern.CASE_INSENSITIVE;
		patternFlags |= isUsesRegex() ? 0 : Pattern.LITERAL;
		Pattern pattern = Pattern.compile(getMatchText(), patternFlags);
		return !pattern.matcher(filename).find();
	}

	public String getMatchText() {
		return matchText;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public boolean isCaseSensitive() {
		return isCaseSensitive;
	}

	public boolean isUsesMatchText() {
		return usesMatchText;
	}

	public boolean isUsesRegex() {
		return usesRegex;
	}

	public void setCaseSensitive(boolean isCaseSensitive) {
		if (this.isCaseSensitive != isCaseSensitive) {
			this.isCaseSensitive = isCaseSensitive;
			fireFilterChanged();
		}
	}

	public void setMatchText(String matchText) {
		this.matchText = matchText;
		fireFilterChanged();
	}

	public void setMatchType(MatchType matchType) {
		if (this.matchType != matchType) {
			this.matchType = matchType;
			fireFilterChanged();
		}
	}

	public void setUsesMatchText(boolean usesMatchText) {
		if (this.usesMatchText != usesMatchText) {
			this.usesMatchText = usesMatchText;
			fireFilterChanged();
		}
	}

	public void setUsesRegex(boolean usesRegex) {
		if (this.usesRegex != usesRegex) {
			this.usesRegex = usesRegex;
			fireFilterChanged();
		}
	}

}
