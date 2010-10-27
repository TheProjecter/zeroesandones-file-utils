package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.zao.fire.util.FileGatherer;

/**
 * Back end class that handles the actual changing of Files' names. It can be
 * pointed to a particular directory, then told to apply changes. At that point,
 * it will apply its <code>currentRule</code> to each file within that
 * directory.
 * 
 * @author dylan
 */
public class Renamer {
	private RenamerRule currentRule;
	private File currentDirectory;
	private final List<File> localFiles = new ArrayList<File>();

	// ----------------------------------------------------------------
	// Section for error handling and error listeners
	// ----------------------------------------------------------------

	/**
	 * Enumeration that describes the types of various errors that could happen
	 * during the renaming process.
	 */
	public static enum ErrorType {
		IOException, NameConflict
		// TODO: add more when we find more error cases
	}

	/**
	 * Interface that defines the behavior of an object that listens to this
	 * Renamer for status updates concerning errors.
	 * 
	 * @author dylan
	 */
	public static interface ErrorListener {
		/**
		 * This function will be called if this ErrorListener has been added to
		 * a {@link Renamer} via {@link Renamer#addErrorListener(ErrorListener)}
		 * .
		 * 
		 * @param errorType
		 *            The type of the error caused by the Renamer.
		 * @param file
		 *            The file in question that was unable to be renamed.
		 * @param rule
		 *            The {@link RenamerRule} that was being used when the error
		 *            was caused.
		 */
		void gotError(ErrorType errorType, File file, RenamerRule rule);
	}

	private final List<ErrorListener> errorListeners = new ArrayList<ErrorListener>();

	/**
	 * Add an {@link ErrorListener} to be notified whenever this Renamer
	 * encounters an error.
	 * 
	 * @param listener
	 *            The {@link ErrorListener} to be added.
	 */
	public void addErrorListener(ErrorListener listener) {
		errorListeners.add(listener);
	}

	/**
	 * Remove an {@link ErrorListener} so that it will no longer be notified
	 * whenever this Renamer encounters an error.
	 * 
	 * @param listener
	 *            The {@link ErrorListener} to be removed.
	 */
	public void removeErrorListener(ErrorListener listener) {
		errorListeners.remove(listener);
	}

	/**
	 * Notify all {@link ErrorListener}s that an error has occurred.
	 * 
	 * @param errorType
	 *            The type of the error caused by this Renamer.
	 * @param file
	 *            The file in question that was unable to be renamed.
	 * @param rule
	 *            The {@link RenamerRule} that was being used when the error was
	 *            caused.
	 */
	private void throwError(ErrorType errorType, File file, RenamerRule rule) {
		for (ErrorListener listener : errorListeners) {
			listener.gotError(errorType, file, rule);
		}
	}

	// ----------------------------------------------------------------
	// End of error handling section
	// ----------------------------------------------------------------

	/**
	 * Apply the current renaming rule to all of the files in the current
	 * directory. If any errors are encountered during this process, the
	 * {@link ErrorListener}s should be notified.
	 */
	public void applyChanges() {
		// TODO: add a naming conflict check
		currentRule.setup();
		for (File file : localFiles) {
			try {
				// TODO: cause actual changes to happen
				String newName = currentRule.getNewName(file);

				String fullName = file.getCanonicalPath();
				int fileNameIndex = fullName.lastIndexOf(file.getName());
				fullName = fullName.substring(0, fileNameIndex) + newName;

				File newFile = new File(fullName);
				boolean success = file.renameTo(newFile);
				if (!success) {
					System.err.println("Could not rename " + file);
				}
			} catch (IOException e) {
				System.out.println("error :(");
				// tell the error listeners that something went wrong
				throwError(ErrorType.IOException, file, currentRule);
			}
		}
		currentRule.tearDown();
	}

	/**
	 * Move to the specified <code>directory</code> and update the list of local
	 * files.
	 * 
	 * @param directory
	 *            The directory to set as the current directory
	 */
	public void setCurrentDirectory(File directory) {
		if (directory == null) {
			return;
		}
		if (!directory.isDirectory()) {
			throw new IllegalArgumentException("The given 'directory' MUST be a directory.");
		}
		currentDirectory = directory;
		localFiles.clear();
		for (File file : new FileGatherer(directory)) {
			localFiles.add(file);
		}
	}

	/**
	 * @return The current root directory (i.e. the folder that contains all of
	 *         the <code>localFile</code>s)
	 */
	public File getCurrentDirectory() {
		return currentDirectory;
	}

	/**
	 * Sets the current {@link RenamerRule} to the given <code>rule</code>.
	 * 
	 * @param rule
	 *            The {@link RenamerRule} to be set as the new current rule.
	 */
	public void setCurrentRule(RenamerRule rule) {
		currentRule = rule;
	}

	/**
	 * @return The current {@link RenamerRule} being used by this Renamer
	 */
	public RenamerRule getCurrentRule() {
		return currentRule;
	}

	/**
	 * @return An object that can iterate over all of the <code>File</code>s
	 *         that are currently considered "Local."
	 */
	public Iterable<File> getLocalFiles() {
		return localFiles;
	}
}
