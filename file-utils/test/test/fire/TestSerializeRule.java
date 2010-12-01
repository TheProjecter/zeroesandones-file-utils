package test.fire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.zao.fire.ListRule;
import edu.zao.fire.ListRule.ListStyle;
import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MatchReplaceRule.CapitalizationStyle;

public class TestSerializeRule {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		File tempFolder = new File("data/serialize_test");
		tempFolder.mkdirs();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		File tempFolder = new File("data/serialize_test");
		for (File file : tempFolder.listFiles()) {
			file.delete();
		}
		tempFolder.delete();
	}

	private static File makeTempFile(String filename) {
		File file = new File("data/serialize_test/" + filename);
		return file;
	}

	@Test
	public void serializeRule() throws FileNotFoundException, IOException {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("hello");
		rule.setCapitalizationState(CapitalizationStyle.SENTENCE);
		rule.setReplaceString("goodbye");

		File savedRuleFile = makeTempFile("savedRule.rule");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedRuleFile));
		oos.writeObject(rule);
		oos.close();
	}

	@Test
	public void deserializeRule() throws FileNotFoundException, IOException, ClassNotFoundException {
		File savedRuleFile = makeTempFile("savedRule.rule");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedRuleFile));

		MatchReplaceRule rule = (MatchReplaceRule) ois.readObject();
		assertTrue(rule.getMatchString().equals("hello"));
		assertTrue(rule.getCapitalizationState() == CapitalizationStyle.SENTENCE);
		assertTrue(rule.getReplaceString().equals("goodbye"));
	}

	@Test
	public void testSerializeListRule() throws FileNotFoundException, IOException {
		ListRule rule = new ListRule();
		rule.setAddToEnd(true);
		rule.setAscending(false);
		rule.setDigitsDisplayed(3);
		rule.setListStyleState(ListStyle.ROMAN_NUMERALS);
		rule.setSeperatorToken("<(^^<)");
		rule.setStartFrom(1);

		try {
			File saveFile = makeTempFile("listRule.frr");
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(saveFile));
			oos.writeObject(rule);
			oos.close();
		} catch (Exception e) {
			fail("Shouldn't get exceptions here");
		}
	}

	@Test
	public void testDeserializeListRule() throws IOException, ClassNotFoundException {
		File savedRule = makeTempFile("listRule.frr");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedRule));
		ListRule rule = (ListRule) ois.readObject();

		assertTrue(rule.isAddToEnd());
		assertFalse(rule.isAscending());
		assertTrue(rule.getDigitsDisplayed() == 3);
		assertTrue(rule.getListStyleState() == ListStyle.ROMAN_NUMERALS);
		assertTrue(rule.getSeperatorToken().equals("<(^^<)"));
		assertTrue(rule.getStartFrom() == 1);
	}
}
