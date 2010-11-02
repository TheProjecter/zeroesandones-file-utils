package edu.zao.fire;

import java.io.IOException;

public class UserRegexException extends IOException {

	private final String originalName;
	private final String matchRegex;
	private final String replaceRegex;

	public UserRegexException(Throwable cause, String originalName, String matchRegex, String replaceRegex) {
		super(cause);
		this.originalName = originalName;
		this.matchRegex = matchRegex;
		this.replaceRegex = replaceRegex;
	}

	public String getOriginalName() {
		return originalName;
	}

	public String getMatchRegex() {
		return matchRegex;
	}

	public String getReplaceRegex() {
		return replaceRegex;
	}

	@Override
	public String getMessage() {
		return "Malformed regular expression: " + matchRegex;
	}
}
