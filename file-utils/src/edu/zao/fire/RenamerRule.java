package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

public interface RenamerRule extends Serializable {
	/**
	 * @param file
	 *            A file that this RenamerRule will be applied to.
	 * @return The new name of the file
	 * @throws IOException
	 */
	String getNewName(File file) throws IOException;

	/**
	 * Perform any actions necessary to the rule before renaming is applied.
	 * This method will be called by the Renamer before it iterates over all of
	 * its local files in order to rename them.
	 */
	void setup();

	/**
	 * Perform any actions necessary to un-setup the rule after renaming has
	 * been applied to a list of local files. This method will be called after
	 * the Renamer has iterated over all of its local files in order to rename
	 * them.
	 */
	void tearDown();
}
