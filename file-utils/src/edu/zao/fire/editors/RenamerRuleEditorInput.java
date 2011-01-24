package edu.zao.fire.editors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import edu.zao.fire.RenamerRule;

/**
 * Common ground for all inputs to RenamerRuleEditors. Inputs must have a save
 * file (which may be null if no rule has been saved yet). Basic saving
 * functionality for renamer rules is implemented here.
 * 
 * @author Dylan
 * 
 */
public abstract class RenamerRuleEditorInput implements IEditorInput {

	protected File saveFile = null;

	/**
	 * @return The text to be displayed in the rule editor's title tab before
	 *         the rule has been saved to a file.
	 */
	public abstract String getDefaultName();

	/**
	 * @return The text to be displayed in the rule editor's title tab. If the
	 *         rule has been saved to a file, the title will be that file's
	 *         name. Otherwise, whatever value returned by
	 *         {@link #getDefaultName()} will be chosen.
	 */
	@Override
	public String getName() {
		if (saveFile == null) {
			return getDefaultName();
		}
		return saveFile.getName();
	};

	/**
	 * Save the given rule to the given file, using Object serialization. Due to
	 * java-related overhead, the save files are generally in the neighborhood
	 * of a kilobyte, but the process is fast and painless.
	 * 
	 * @param rule
	 * @param file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void saveRuleToFile(RenamerRule rule, File file) throws FileNotFoundException, IOException {
		saveFile = file;
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(rule);
		oos.close();
	}

	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

}
