package test.fire;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MatchReplaceRule.CapitalizationStyle;
import edu.zao.fire.MatchReplaceRule.ReplacementLimit;

public class MatchReplaceRuleTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File tempFolder = new File("data/mrr_test");
		tempFolder.mkdirs();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File tempFolder = new File("data/mrr_test");
		for (File file : tempFolder.listFiles()) {
			file.delete();
		}
		tempFolder.delete();
	}

	private static File makeEmptyTempFile(String filename) {
		File file = new File("data/mrr_test/" + filename);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}

	@Test
	public void testGetNewName_NoModifications() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("aaa");
		rule.setReplaceString("bbb");

		File aaaFile = new File("aaaaaa.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("bbbaaa.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_MatchAll() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("aaa");
		rule.setReplaceString("bbb");
		rule.setReplacementLimit(ReplacementLimit.ALL);

		File aaaFile = new File("aaaaaa.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("bbbbbb.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Regex_First() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("[ab]");
		rule.setReplaceString("c");
		rule.setMatchRegularExpressions(true);

		File aaaFile = new File("ababab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("cbabab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Regex_All() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("[ab]");
		rule.setReplaceString("c");
		rule.setMatchRegularExpressions(true);
		rule.setReplacementLimit(ReplacementLimit.ALL);

		File aaaFile = new File("ababab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("cccccc.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Capitalization_All() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setCapitalizationState(CapitalizationStyle.ALL_CAPS);

		File aaaFile = new File("ababab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("ABABAB.TXT"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Capitalization_Title() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setCapitalizationState(CapitalizationStyle.TITLE);

		File aaaFile = new File("aba bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("Aba Bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Capitalization_Sentence() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setCapitalizationState(CapitalizationStyle.SENTENCE);

		File aaaFile = new File("aba bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("Aba bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_Capitalization_NoCaps() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setCapitalizationState(CapitalizationStyle.NO_CAPS);

		File aaaFile = new File("AbA BAb.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("aba bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_CaseSensitive() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("aba");
		rule.setReplaceString("cdc");
		rule.setCaseSensitive(true);

		File aaaFile = new File("aba bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("cdc bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_CaseSensitive2() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("aba");
		rule.setReplaceString("cdc");
		rule.setCaseSensitive(true);

		File aaaFile = new File("ABA bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("ABA bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_CaseInsensitive() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("aba");
		rule.setReplaceString("cdc");
		rule.setCaseSensitive(false);

		File aaaFile = new File("ABA bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("cdc bab.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNewName_RegexGroup() {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("(ab)");
		rule.setReplaceString("-$1-");
		rule.setMatchRegularExpressions(true);
		rule.setReplacementLimit(ReplacementLimit.ALL);

		File aaaFile = new File("ABA bab.txt");
		try {
			String newName = rule.getNewName(aaaFile);
			assertTrue(newName.equals("-AB-A b-ab-.txt"));
		} catch (IOException e) {
			fail("Should not throw an exception");
			e.printStackTrace();
		}
	}

}
