package edu.zao.fire.commands;

import java.io.File;
import java.util.Iterator;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;

public class UserSelectionUnIgnoreHandler extends UserSelectionAbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		// ignore all files that are selected
		Iterator<?> iterator = getSelection().iterator();
		while (iterator.hasNext()) {
			File file = (File) iterator.next();
			getFilter().removeIgnoredFile(file);
		}

		return null;
	}

	@Override
	public boolean isEnabled() {
		IStructuredSelection selection = getSelection();
		if (selection.size() > 1) {
			// always enabled when a group is selected
			return true;
		}
		if (selection.size() < 1) {
			// must have at least 1 thing selected
			return false;
		}

		// check to see if the selected file is ignored
		File selectedFile = (File) selection.getFirstElement();
		return getFilter().isIgnored(selectedFile);
	}

}
