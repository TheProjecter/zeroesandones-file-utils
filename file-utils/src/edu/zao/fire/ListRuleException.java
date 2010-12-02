package edu.zao.fire;

import java.io.IOException;

import edu.zao.fire.ListRule.ListStyle;

/**
 * ListRuleException encompasses all exceptions that come out of a list rule.
 * Mostly negative list issues
 * 
 * @author savicj
 * 
 */
public class ListRuleException extends IOException {
	private static final long serialVersionUID = -8722907832615644135L;
	private final ListStyle styleOfList;
	private final int counter;

	public ListRuleException(Throwable cause, ListStyle styleOfList, int counter) {
		this.styleOfList = styleOfList;
		this.counter = counter;
	}

	public ListStyle getListStyle() {
		return this.styleOfList;
	}

	@Override
	public String getMessage() {
		switch (styleOfList) {
		case NUMERIC:
			return "Less than zero numeric lists are not allowed";
		case ALPHABETICAL:
			return "Less than zero alphabetical lists are not allowed";
		case ROMAN_NUMERALS:
			if (counter >= 4000)
				return "Roman numeral lists may only go up to 4000";
			else
				return "Less than zero roman number lists are not allowd";
		}
		return "This is an error";
	}

};
