package test.fire;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.zao.fire.MatchReplaceRule;
import edu.zao.fire.MatchReplaceRule.ReplacementLimit;
import edu.zao.fire.Renamer;

public class TestBackend {

	private final static Renamer renamer = new Renamer();
	private final static MatchReplaceRule mrRule = new MatchReplaceRule();
	private final static File root = new File("./data");

	@BeforeClass
	public static void setupTestFiles() {
		for (File file : root.listFiles()) {
			if (!file.getName().equals(".svn")) {
				file.delete();
			}
		}

		File file1 = new File("./data/filename1.txt");
		File file2 = new File("./data/file2.txt");
		File file3 = new File("./data/anotherfile3");
		File file4 = new File("./data/bigfile4.cpp");
		File file5 = new File("./data/filefile.file");
		try {
			file1.createNewFile();
			file2.createNewFile();
			file3.createNewFile();
			file4.createNewFile();
			file5.createNewFile();
		} catch (IOException e) {
			assertTrue(false);
		}
	}

	@Test
	public void setupSucceeded() {
		Set<String> wantedFilenames = new TreeSet<String>();
		wantedFilenames.add("filename1.txt");
		wantedFilenames.add("file2.txt");
		wantedFilenames.add("anotherfile3");
		wantedFilenames.add("bigfile4.cpp");
		wantedFilenames.add("filefile.file");

		for (File file : root.listFiles()) {
			if (!file.getName().equals(".svn")) {
				assertTrue(wantedFilenames.contains(file.getName()));
			}
		}
	}

	@Test
	public void renameFiles() {
		renamer.setCurrentDirectory(root);

		mrRule.setMatchString("file");
		mrRule.setReplaceString("error");
		mrRule.setReplacementLimit(ReplacementLimit.ALL);
		renamer.setCurrentRule(mrRule);

		Set<String> wantedFilenames = new TreeSet<String>();
		wantedFilenames.add("errorname1.txt");
		wantedFilenames.add("error2.txt");
		wantedFilenames.add("anothererror3");
		wantedFilenames.add("bigerror4.cpp");
		wantedFilenames.add("errorerror.error");

		renamer.applyChanges();

		for (File file : root.listFiles()) {
			if (!file.getName().equals(".svn")) {
				assertTrue(wantedFilenames.contains(file.getName()));
			}
		}
	}
}
