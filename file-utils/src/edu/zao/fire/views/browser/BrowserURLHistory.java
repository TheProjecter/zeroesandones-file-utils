package edu.zao.fire.views.browser;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * A BrowserURLHistory keeps track of the locations that the user has visited,
 * in order. Locations are stored as a String, representing the file path (or
 * "url"). The history may be stepped back and forth, updating internal state
 * automatically.
 * 
 * @author Dylan, Chris
 */
public class BrowserURLHistory {
	private final ArrayList<String> currentHistorySequence = new ArrayList<String>();
	private final Set<String> visitedLocations = new TreeSet<String>();

	private int currentHistoryLocation = -1;

	/**
	 * Add the given <code>url</code> to the url history. If the current history
	 * position has been regressed to any point before the <code>head</code>,
	 * the new <code>url</code> becomes the head, and any existing history
	 * beyond the new <code>url</code> is erased.
	 * 
	 * @param url
	 *            The location to be added to the history
	 */
	public void visitLocation(String url) {
		visitedLocations.add(url);

		int index = currentHistoryLocation + 1;
		while (index < currentHistorySequence.size()) {
			currentHistorySequence.remove(index);
		}
		currentHistorySequence.add(url);
		currentHistoryLocation++;

		debugPrintHistory();
	}

	/**
	 * Causes the history to "Go Back." The current history position goes back
	 * by one location. If the history has gone back as far as it can, this
	 * method should do nothing.
	 */
	public void regressHistory() {
		if (currentHistoryLocation > 0) {
			// currentHistorySequence.remove(currentHistoryLocation);
			currentHistoryLocation--;
		}
		debugPrintHistory();
	}

	/**
	 * Determines whether or not the history may be regressed.
	 * 
	 * @return <code>true</code> if the history may "Go Back,"
	 *         <code>false</code> otherwise.
	 */
	public boolean canRegressHistory() {
		return currentHistoryLocation > 0;
	}

	/**
	 * Causes the history to "Go Forward." The current history position goes
	 * forward by one location. If the history has gone as far forward as it can
	 * (i.e. the current position is the head position), then this method should
	 * do nothing.
	 */
	public void progressHistory() {
		if (currentHistoryLocation != currentHistorySequence.size()) {
			currentHistoryLocation++;
		}
		debugPrintHistory();
	}

	/**
	 * Determines whether or not the history may be progressed.
	 * 
	 * @return <code>true</code> if the history may "Go Forward,"
	 *         <code>false</code> otherwise.
	 */
	public boolean canProgressHistory() {
		return currentHistoryLocation < currentHistorySequence.size() - 1;
	}

	/**
	 * @return An object that can iterate over all of the locations that this
	 *         history has ever visited (regardless of whether they have been
	 *         cleared by a backwards navigational path.
	 */
	public Iterable<String> getVisitedLocations() {
		return visitedLocations;
	}

	/**
	 * Returns the current location in the url history ArrayList
	 * 
	 */
	public String getCurrentLocation() {
		return currentHistorySequence.get(currentHistoryLocation);
	}

	/**
	 * Debug Print method. Prints out all urls that are currently held in the
	 * history, with angle brackets "<" and ">" around the current location.
	 */
	private void debugPrintHistory() {
		System.out.print("History:\t");
		for (int i = 0; i < currentHistorySequence.size(); i++) {
			if (i == currentHistoryLocation)
				System.out.print("<");

			System.out.print(currentHistorySequence.get(i));

			if (i == currentHistoryLocation)
				System.out.print(">");
			System.out.print("\t");
		}
		System.out.println();
	}
}
