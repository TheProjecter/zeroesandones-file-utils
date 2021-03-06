package edu.zao.fire;

//Program: Convert from binary (decimal) to roman numerals.
//This is the model without any user interface.
//Author : Fred Swartz
//Date Authored   : 23 Jan 2006
//Grabbed From: http://www.roseindia.net/java/java-tips/45examples/misc/roman/roman.shtml
//On date: 10/3/2010

public class RomanConversion {

	// Parallel arrays used in the conversion process.
	private static final String[] RCODE = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" };
	private static final int[] BVAL = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 };

	// =========================================================== binaryToRoman
	public static String binaryToRoman(int binary) {
		if (binary <= 0 || binary >= 4000) {
			throw new NumberFormatException("Value outside roman numeral range.");
		}
		String roman = ""; // Roman notation will be accumualated here.

		// Loop from biggest value to smallest, successively subtracting,
		// from the binary value while adding to the roman representation.
		for (int i = 0; i < RCODE.length; i++) {
			while (binary >= BVAL[i]) {
				binary -= BVAL[i];
				roman += RCODE[i];
			}
		}
		return roman;
	}
}
