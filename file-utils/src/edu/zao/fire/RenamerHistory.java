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
	private boolean canUndo = false;
	private boolean canRedo = false;

	private void updateStatus() {
		canUndo = !undos.empty();
		canRedo = !redos.empty();
	}

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
		updateStatus();
	}

	/**
	 * Undo will undo the last renaming event and push it onto the redos stack
	 */
	public void undo() {
		if (canUndo) {
			RenamerEvent undoEvent = undos.pop();
			undoEvent.undo();
			redos.push(undoEvent);
		} else
			System.err.println("Cannot undo.\n");
		updateStatus();
	}

	/**
	 * Redo will redo the last renaming event undone, and push it onto the undos
	 * stack
	 */
	public void redo() {
		if (canRedo) {
			RenamerEvent redoEvent = redos.pop();
			redoEvent.redo();
			undos.push(redoEvent);
		} else
			System.err.println("Cannot redo.\n");
		updateStatus();
	}

	public boolean canUndo() {
		return canUndo;
	}

	public boolean canRedo() {
		return canRedo;
	}
}
