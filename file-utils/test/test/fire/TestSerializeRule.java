package test.fire;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MatchReplaceRule.CapitalizationStyle;

public class TestSerializeRule {

	@Test
	public void serializeRule() throws FileNotFoundException, IOException {
		MatchReplaceRule rule = new MatchReplaceRule();
		rule.setMatchString("hello");
		rule.setCapitalizationState(CapitalizationStyle.SENTENCE);
		rule.setReplaceString("goodbye");

		File savedRuleFile = new File("./data/savedRule.rule");
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(savedRuleFile));
		oos.writeObject(rule);
		oos.close();
	}

	public void deserializeRule() throws FileNotFoundException, IOException, ClassNotFoundException {
		File savedRuleFile = new File("./data/savedRule.rule");
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(savedRuleFile));

		MatchReplaceRule rule = (MatchReplaceRule) ois.readObject();
		assertTrue(rule.getMatchString().equals("hello"));
		assertTrue(rule.getCapitalizationState() == CapitalizationStyle.SENTENCE);
		assertTrue(rule.getReplaceString().equals("goodbye"));
	}
}
