package edu.zao.fire;

import java.util.Stack;

/**
 * RenamerHistory is the functionality for undo/redo nameChanges It consists of
 * two stacks of RenamerEvents and the methods to add, undo, and redo events.
 * 
 * @author savicj
 * 
 */
public class RenamerHistory {
	private final Stack<RenamerEvent> undos = new Stack<RenamerEvent>();
	private final Stack<RenamerEvent> redos = new Stack<RenamerEvent>();

	/**
	 * addRenamerEvent will push a new event onto the RenamerEvent stack, then
	 * it will then clear the redos Stack. Call this after every name change.
	 * 
	 * @param newEvent
	 *            The most recent renaming event
	 */
	public void addRenamerEvent(RenamerEvent newEvent) {
		if (!redos.empty())
			redos.clear();
		undos.push(newEvent);
	}

	/**
	 * Undo will undo the last renaming event and push it onto the redos stack
	 */
	public void undo() {
		System.err.println("Attempting to undo\n");
		RenamerEvent undoEvent = undos.pop();
		undoEvent.undo();
		redos.push(undoEvent);
	}

	/**
	 * Redo will redo the last renaming event undone, and push it onto the undos
	 * stack
	 */
	public void redo() {
		System.err.println("Attempting to redo\n");
		RenamerEvent redoEvent = redos.pop();
		redoEvent.redo();
		undos.push(redoEvent);
	}
}
