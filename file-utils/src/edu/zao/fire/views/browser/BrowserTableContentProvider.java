package edu.zao.fire.views.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.zao.fire.Renamer;

public class BrowserTableContentProvider implements IStructuredContentProvider {

	Renamer input;

	@Override
	public Object[] getElements(Object inputElement) {
		List<File> elements = new ArrayList<File>();
		for (File file : input.getLocalFiles()) {
			elements.add(file);
		}
		return elements.toArray();
	}

	@Override
	public void dispose() {
		// nothing happens here. This function is only defined because it has to
		// be (as demanded by the interface)
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			return;
		}
		if (newInput instanceof Renamer) {
			input = (Renamer) newInput;
		} else {
			throw new IllegalArgumentException("newInput must be an instance of Renamer.");
		}
	}
}
