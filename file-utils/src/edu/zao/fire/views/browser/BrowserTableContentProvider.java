package edu.zao.fire.views.browser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.zao.fire.Renamer;

/**
 * Content Provider class that interprets the input to the Browser Table viewer
 * and returns a list of object that can be displayed within the browser. These
 * objects are in turn passed to the {@link BrowserTableItemSorter} and
 * {@link BrowserTableLabelProvider} to determine display qualities and order.
 * 
 * @author Dylan
 */
public class BrowserTableContentProvider implements IStructuredContentProvider {

	Renamer input;

	/**
	 * Returns an array of the files that are considered local to the renamer's
	 * current directory.
	 */
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
