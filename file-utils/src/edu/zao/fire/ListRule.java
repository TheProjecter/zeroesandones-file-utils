package edu.zao.fire;

import java.io.File;
import java.io.IOException;

public class ListRule implements RenamerRule {
	// TODO: getAlphabetical

	/**
	 * A generated serial ID
	 */
	private static final long serialVersionUID = 5732001028450514775L;
	private boolean isAscending = false;
	private boolean isAddToEnd = false;
	private String seperatorToken = "";
	private int startFrom = 0;
	private int counter = 0;

	public static enum ListStyle {
		NONE, NUMERIC, ALPHABETICAL, ROMAN_NUMERALS
	}

	private ListStyle listStyleState = ListStyle.NONE;
	private int digitsDisplayed = 0;

	@Override
	// TODO: Need a decimal to Roman numeral converter
	// (http://www.roseindia.net/java/java-tips/45examples/misc/roman/roman.shtml)
	public String getNewName(File file) throws IOException {
		String numeral = "";
		String oldName = file.getName();
		String newName = "";
		switch (listStyleState) {
		case NUMERIC:
			numeral = getNumeric();
		case ALPHABETICAL:
			numeral = getAlphabetical();
		case ROMAN_NUMERALS:
			numeral = getRoman();
		}

		if (isAddToEnd)
			newName = numeral + numeral;
		else
			newName = oldName + numeral;

		if (isAscending)
			counter++;
		else
			counter--;

		return newName;
	}

	private String getNumeric() {
		return String.format("%0" + Integer.toString(digitsDisplayed) + "d", counter);
	}

	// http://stackoverflow.com/questions/342052/how-to-increment-a-java-string-through-all-the-possibilities
	private String getAlphabetical() {
		return "";
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
