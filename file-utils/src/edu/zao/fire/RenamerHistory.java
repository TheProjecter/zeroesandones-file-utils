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
	private Stack<RenamerEvent> undos;
	private Stack<RenamerEvent> redos;

	/**
	 * addRenamerEvent will push a new event onto the RenamerEvent stack, then
	 * it will then clear the redos Stack. Call this after every name change.
	 * 
	 * @param newEvent
	 *            The most recent renaming event
	 */
	public void addRenamerEvent(RenamerEvent newEvent) {
		redos.clear();
		undos.push(newEvent);
	}

	/**
	 * Undo will undo the last renaming event and push it onto the redos stack
	 */
	public void undo() {
		RenamerEvent undoEvent = undos.pop();
		undoEvent.undo();
		redos.push(undoEvent);
	}

	/**
	 * Redo will redo the last renaming event undone, and push it onto the undos
	 * stack
	 */
	public void redo() {
		RenamerEvent redoEvent = redos.pop();
		redoEvent.redo();
		undos.push(redoEvent);
	}
}
