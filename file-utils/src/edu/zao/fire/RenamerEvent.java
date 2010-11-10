package edu.zao.fire;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * RenamerEvent consists of a list of all files involved in a single RenamerRule
 * rename. The file's before and after paths are held in a helper class
 * RenamedFile. Provides functionality for undoing and redoing all name changes
 * to the files in filesList
 * 
 * @author savicj
 * 
 */

public class RenamerEvent {
	private final List<RenamedFile> filesList;

	RenamerEvent() {
		filesList = new LinkedList<RenamedFile>();
	}

	/**
	 * Will add a new RenamedFile to the event, needs to be called after each
	 * individual name change
	 * 
	 * @param newRenamedFile
	 *            The before and after paths of a name change
	 */
	public void addRenamedFile(RenamedFile newRenamedFile) {
		filesList.add(newRenamedFile);
	}

	/**
	 * Will undo all the filename changes in this event
	 */
	public void undo() {
		// For each file in the list of Files
		for (Iterator<RenamedFile> p = filesList.iterator(); p.hasNext();) {
			RenamedFile temp = p.next();
			File toBeChanged = new File(temp.getAfterPath());
			if (!toBeChanged.exists()) {
				System.err.println("Could not undo changes to file: " + temp.getAfterPath() + ", File does not exist!\n");
			} else {
				File newFile = new File(temp.getBeforePath());
				boolean success = toBeChanged.renameTo(newFile);
				if (!success) {
					System.err.println("Could not rename " + toBeChanged);
				}
			}
		}
	}

	/**
	 * Will redo all the filename changes in this event
	 */
	public void redo() {
		for (Iterator<RenamedFile> p = filesList.iterator(); p.hasNext();) {
			RenamedFile temp = p.next();
			File toBeChanged = new File(temp.getBeforePath());
			if (!toBeChanged.exists()) {
				// TODO: Throw an exception because the files not there
			} else {
				File newFile = new File(temp.getAfterPath());
				boolean success = toBeChanged.renameTo(newFile);
				if (!success) {
					System.err.println("Could not rename " + toBeChanged);
				}
			}
		}
	}
}
