package edu.zao.fire;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.zao.fire.editors.RenamerRuleChangeListener;
import edu.zao.fire.editors.RenamerRuleEditor;
import edu.zao.fire.editors.RenamerRuleEditorManager.ActiveEditorListener;
import edu.zao.fire.util.FileGatherer;
import edu.zao.fire.util.Filter;

/**
 * Back end class that handles the actual changing of Files' names. It can be
 * pointed to a particular directory, then told to apply changes. At that point,
 * it will apply its <code>currentRule</code> to each file within that
 * directory.
 * 
 * @author dylan
 */
public class Renamer implements RenamerRuleChangeListener, ActiveEditorListener {
	private RenamerRule currentRule;
	private File currentDirectory;
	private final RenamerHistory renamingHistory = new RenamerHistory();
	private final List<File> localFiles = new ArrayList<File>();

	public RenamerHistory getRenamerHistory() {
		return renamingHistory;
	}

	public void undoRenamerEvent() {
		renamingHistory.undo();
		System.out.println("Undoing RenamerEvent\n");
		setCurrentDirectory(currentDirectory);
		rebuildNewNamesCache();
	}

	public void redoRenamerEvent() {
		renamingHistory.redo();
		System.out.println("Redoing RenamerEvent\n");
		setCurrentDirectory(currentDirectory);
		rebuildNewNamesCache();
	}

	/**
	 * 
	 * Maps original filenames to their new names; original value is the key
	 */
	private final Map<String, String> newNamesMap = new HashMap<String, String>();

	public final Filter<File> changingFileFilter = new Filter<File>() {
		@Override
		public boolean accept(File file) {
			String oldName = file.getName();
			String newName = newNamesMap.get(oldName);
			return oldName.compareTo(newName) != 0;
		}
	};

	// ----------------------------------------------------------------
	// Section for event handling and event listeners
	// ----------------------------------------------------------------

	/**
	 * Enumeration that describes the types of various events that could happen
	 * during the renaming process.
	 */
	public static enum EventType {
		UpdatedNames, IOException, BadRegex, NameConflict, RenamedWithNoProblems, CouldNotRename
		// TODO: add more when we find more event cases
	}

	/**
	 * Interface that defines the behavior of an object that listens to this
	 * Renamer for status update events.
	 * 
	 * @author dylan
	 */
	public static interface EventListener {
		/**
		 * This function will be called if this EventListener has been added to
		 * a {@link Renamer} via {@link Renamer#addEventListener(EventListener)}
		 * .
		 * 
		 * @param eventType
		 *            The type of the error caused by the Renamer.
		 * @param file
		 *            The file in question that was unable to be renamed.
		 * @param rule
		 *            The {@link RenamerRule} that was being used when the error
		 *            was caused.
		 */
		void seeEvent(EventType eventType, File file, RenamerRule rule);
	}

	private final List<EventListener> eventListeners = new ArrayList<EventListener>();

	/**
	 * Add an {@link EventListener} to be notified whenever this Renamer goes
	 * through some observable event.
	 * 
	 * @param listener
	 *            The {@link EventListener} to be added.
	 */
	public void addEventListener(EventListener listener) {
		eventListeners.add(listener);
	}

	/**
	 * Remove an {@link EventListener} so that it will no longer be notified
	 * whenever this Renamer goes through some observable event.
	 * 
	 * @param listener
	 *            The {@link EventListener} to be removed.
	 */
	public void removeEventListener(EventListener listener) {
		eventListeners.remove(listener);
	}

	/**
	 * Notify all {@link EventListener}s that an error has occurred.
	 * 
	 * @param eventType
	 *            The type of the error caused by this Renamer.
	 * @param file
	 *            The file in question that was unable to be renamed.
	 * @param rule
	 *            The {@link RenamerRule} that was being used when the error was
	 *            caused.
	 */
	private void fireEvent(EventType eventType, File file, RenamerRule rule) {
		for (EventListener listener : eventListeners) {
			listener.seeEvent(eventType, file, rule);
		}
	}

	// ----------------------------------------------------------------
	// End of event handling section
	// ----------------------------------------------------------------

	/**
	 * Apply the current renaming rule to all of the files in the current
	 * directory. If any errors are encountered during this process, the
	 * {@link EventListener}s should be notified.
	 */
	public void applyChanges() {
		RenamerEvent newEvent = new RenamerEvent();
		for (File file : localFiles) {
			try {
				String currentName = file.getName();
				String newName = newNamesMap.get(currentName);
				if (!currentName.equals(newName)) {
					RenamedFile newRenamedFile = new RenamedFile();
					String fullName = file.getCanonicalPath();
					newRenamedFile.setBeforePath(fullName);
					int fileNameIndex = fullName.lastIndexOf(file.getName());
					fullName = fullName.substring(0, fileNameIndex) + newName;
					File newFile = new File(fullName);
					newRenamedFile.setAfterPath(fullName);
					boolean success = file.renameTo(newFile);
					if (!success) {
						fireEvent(EventType.CouldNotRename, file, currentRule);
					}
					newEvent.addRenamedFile(newRenamedFile);
				}
			} catch (IOException e) {
				// tell the error listeners that something went wrong
				fireEvent(EventType.IOException, file, currentRule);
			}
		}

		renamingHistory.addRenamerEvent(newEvent);
		setCurrentDirectory(currentDirectory);
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
		System.out.println("Directory changed");
		rebuildNewNamesCache();
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
		System.out.println("set current rule to " + rule);
		currentRule = rule;
		rebuildNewNamesCache();
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

	/**
	 * Listener Method: This method is invoked by the RenamerRuleEditorManager
	 * when a different RenamerRuleEditor gets user focus. When this happens,
	 * this Renamer's <code>currentRule</code> is set to the new active editor's
	 * rule.
	 */
	@Override
	public void activeEditorChanged(RenamerRuleEditor newActiveEditor) {
		System.out.println("active editor was changed");
		if (newActiveEditor == null) {
			setCurrentRule(null);
		} else {
			setCurrentRule(newActiveEditor.getRule());
		}
	}

	/**
	 * Listener Method: This method is invoked by a RenamerRuleEditor whenever
	 * that editor's rule has been modified by the UI. When this happens, the
	 * modified names of all of the local files will be recalculated.
	 */
	@Override
	public void ruleChanged(RenamerRule rule) {
		System.out.println("renamer rule was modified");
		rebuildNewNamesCache();
	}

	/**
	 * Recalculate all of the modified names for each local file within the
	 * <code>currentDirectory</code>. These names will be stored in a local
	 * cache.
	 */
	private void rebuildNewNamesCache() {
		System.out.println("rebuilding names cache");
		newNamesMap.clear();
		boolean noProblem = true;

		if (currentRule != null) {
			currentRule.setup();
		}
		for (File file : localFiles) {
			String oldName = file.getName();
			try {
				String newName = (currentRule == null) ? oldName : currentRule.getNewName(file);
				newNamesMap.put(oldName, newName);
			} catch (UserRegexException e) {
				noProblem = false;
				fireEvent(EventType.BadRegex, file, currentRule);
			} catch (IOException e) {
				noProblem = false;
				fireEvent(EventType.IOException, file, currentRule);
				newNamesMap.put(oldName, oldName);
			}
		}
		if (currentRule != null) {
			currentRule.tearDown();
		}

		boolean gotNameConflict = false;
		Set<String> newNames = new TreeSet<String>();
		for (File file : localFiles) {
			String currentName = file.getName();
			String newName = newNamesMap.get(currentName);
			if (newNames.contains(newName)) {
				gotNameConflict = true;
			}
			newNames.add(newName);
		}
		if (gotNameConflict) {
			noProblem = false;
			fireEvent(EventType.NameConflict, null, currentRule);
		}
		fireEvent(EventType.UpdatedNames, null, currentRule);
		if (noProblem) {
			fireEvent(EventType.RenamedWithNoProblems, null, currentRule);
		}
	}

	public String getNewName(File file) {
		return newNamesMap.get(file.getName());
	}
}
