package edu.zao.fire;

import java.io.File;
import java.io.IOException;

public class ListRule implements RenamerRule {

	/**
	 * A generated serial ID
	 */
	private static final long serialVersionUID = 5732001028450514775L;
	private boolean isAscending = true;
	private boolean isAddToEnd = false;
	private String seperatorToken = "-";
	private int startFrom = 1;
	private int counter = 0;

	public static enum ListStyle {
		NONE, NUMERIC, ALPHABETICAL, ROMAN_NUMERALS
	}

	private ListStyle listStyleState = ListStyle.NUMERIC;
	private int digitsDisplayed = 1;

	@Override
	public String getNewName(File file) throws IOException {
		String numeral = "";
		String oldName = file.getName();
		String newName = "";
		switch (listStyleState) {
		case NONE:
			return "";
		case NUMERIC:
			numeral = getNumeric();
			break;
		case ALPHABETICAL:
			numeral = getAlphabetical();
			break;
		case ROMAN_NUMERALS:
			numeral = getRoman();
			break;
		}

		if (isAddToEnd)
			newName = oldName + seperatorToken + numeral;
		else
			newName = numeral + seperatorToken + oldName;

		if (isAscending)
			counter++;
		else
			counter--;

		return newName;
	}

	private String getNumeric() {
		String output = "";
		if (digitsDisplayed == 0) {
			output = Integer.toString(counter);
		} else {
			String format = "%0" + Integer.toString(digitsDisplayed) + "d";
			output = String.format(format, counter);
		}
		return output;
	}

	private String getAlphabetical() {
		int num = counter;
		String s = new String("");
		for (int i = 6; i >= 0; i--) {
			int divisor = (int) Math.pow(26, i);
			char alpha = 'a';
			if ((num / divisor) != 0) {
				while ((num - divisor) > 0) {
					num = num - divisor;
					alpha++;
				}
				s = s + alpha;
			}
		}
		while (s.length() < digitsDisplayed) {
			s = "a" + s.toString();
		}
		return s.toString();
	}

	private String getRoman() {
		return RomanConversion.binaryToRoman(counter);
	}

	/**
	 * Set the value to start the list from
	 * 
	 * @param startFrom
	 */
	public void setStartFrom(int startFrom) {
		this.startFrom = startFrom;
	}

	public int getStartFrom() {
		return startFrom;
	}

	/**
	 * Set the seperatorToken to a desired value
	 * 
	 * @param seperatorToken
	 *            The new value of seperatorToken
	 */
	public void setSeperatorToken(String seperatorToken) {
		this.seperatorToken = seperatorToken;
	}

	/**
	 * 
	 * @return The current value of seperatorToken
	 */
	public String getSeperatorToken() {
		return seperatorToken;
	}

	/**
	 * Set the ascending list style
	 * 
	 * @param isAscending
	 *            The new value of isAscending
	 */
	public void setAscending(boolean isAscending) {
		this.isAscending = isAscending;
	}

	/**
	 * 
	 * @return The current value of isAscending
	 */
	public boolean isAscending() {
		return isAscending;
	}

	/**
	 * Set the add to end list style
	 * 
	 * @param addToEnd
	 *            The new value of addToEnd
	 */
	public void setAddToEnd(boolean isAddToEnd) {
		this.isAddToEnd = isAddToEnd;
	}

	/**
	 * 
	 * @return The value of isAddToEnd
	 */
	public boolean isAddToEnd() {
		return isAddToEnd;
	}

	/**
	 * Set the value of the listStyleState, from ListStyle enumeration
	 * 
	 * @param listStyleState
	 *            The new value of listStyleState
	 */
	public void setListStyleState(ListStyle listStyleState) {
		this.listStyleState = listStyleState;
	}

	/**
	 * 
	 * @return The Value of listStyleState
	 */
	public ListStyle getListStyleState() {
		return listStyleState;
	}

	/**
	 * Set the number of digits displayed in the list
	 * 
	 * @param digitsDisplayed
	 *            The new value of digitsDisplayed
	 */
	public void setDigitsDisplayed(int digitsDisplayed) {
		this.digitsDisplayed = digitsDisplayed;
	}

	/**
	 * 
	 * @return The current value of digitsDisplayed
	 */
	public int getDigitsDisplayed() {
		return digitsDisplayed;
	}

	@Override
	public void setup() {
		counter = startFrom;
	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub

	}

}
