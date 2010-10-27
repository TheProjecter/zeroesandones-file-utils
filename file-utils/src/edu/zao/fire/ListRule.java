package edu.zao.fire;

import java.io.File;
import java.io.IOException;

public class ListRule implements RenamerRule {
// TODO: Finish setup(), tearDown() and getNewName().
// TODO: Change startFrom to an integer, parse accordingly in getNewName
	/**
	 * A generated serial ID
	 */
	private static final long serialVersionUID = 5732001028450514775L;
	private boolean isAscending = false;
	private boolean isDescending = false;
	private boolean isAddToEnd = false;
	private boolean isAddToStart = false;
	private String seperatorToken = "";
	private String startFrom = "";
	private int counter = 0;

	
	public static enum ListStyle{
		NONE, NUMERIC, ALPHABETICAL, ROMAN_NUMERALS
	}
	private ListStyle listStyleState = ListStyle.NONE;
	private int digitsDisplayed = 0;
	
	
	@Override
	public String getNewName(File file) throws IOException {
		// TODO: Need a decimal to Roman numeral converter (http://www.roseindia.net/java/java-tips/45examples/misc/roman/roman.shtml)
		return null;
	}
	
	/**
	 * Set the value to start the list from
	 * 
	 * @param startFrom
	 */
	public void setStartFrom(String startFrom){
		this.startFrom = startFrom;
	}
	public String getStartFrom(){
		return startFrom;
	}
	/**
	 * Set the seperatorToken to a desired value
	 * 
	 * @param seperatorToken
	 * 				The new value of seperatorToken
	 */
	public void setSeperatorToken(String seperatorToken){
		this.seperatorToken = seperatorToken;
	}
	/**
	 * 
	 * @return The current value of seperatorToken
	 */
	public String getSeperatorToken(){
		return seperatorToken;
	}
	/**
	 * Set the ascending list style
	 * @param isAscending
	 * 				The new value of isAscending
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
	 * Set the descending list style
	 * 
	 * @param isDescending
	 * 				The new value of isDescending
	 */
	public void setDescending(boolean isDescending) {
		this.isDescending = isDescending;
	}
	/**
	 * 
	 * @return The current value of isDescending
	 */
	public boolean isDescending() {
		return isDescending;
	}
	/**
	 * Set the add to end list style
	 * 
	 * @param addToEnd 
	 * 				The new value of addToEnd
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
	 * Set the add to start list style
	 * 
	 * @param isAddToStart
	 * 				The new value of isAddToStart
	 */
	public void setAddToStart(boolean isAddToStart) {
		this.isAddToStart = isAddToStart;
	}
	/**
	 * 
	 * @return The value of isAddToStart
	 */
	public boolean isAddToStart() {
		return isAddToStart;
	}
	/**
	 * Set the value of the listStyleState, from ListStyle enumeration
	 * 
	 * @param listStyleState
	 * 					The new value of listStyleState
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
	 * 					The new value of digitsDisplayed
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
		// TODO Throw an error if the start from value does not match the listStyle
		
	}

	@Override
	public void tearDown() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	
	
	

}
