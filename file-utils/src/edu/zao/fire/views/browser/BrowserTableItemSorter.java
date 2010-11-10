package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Comparator type object that determines what order the Browser table will
 * display items.
 * 
 * @author Dylan
 */
public class BrowserTableItemSorter extends ViewerSorter {
	private int columnIndex = 0;
	private boolean descending;

	/**
	 * Sets sorting to be based on a particular column. If the column is
	 * different from the current, it will be set to "ascending" mode;
	 * otherwise, the mode will simply be swapped. Currently, the sorter will
	 * actually ignore the column index; only the "ascending/descending" quality
	 * is taken into account.
	 * 
	 * @param column
	 */
	public void setColumn(int column) {
		if (columnIndex != column) {
			descending = false;
		} else {
			descending = !descending;
		}
		columnIndex = column;
	}

	/**
	 * The normal way to compare two objects in the browser viewer. e1 and e2
	 * are assumed to be Files of some type (this includes directories). Files
	 * are sorted first by whether or not they are a directory, then
	 * alphabetically by name.
	 * 
	 * @param viewer
	 * @param e1
	 * @param e2
	 * @return
	 */
	private int baseCompare(Viewer viewer, Object e1, Object e2) {
		File item1 = (File) e1;
		File item2 = (File) e2;
		int category1 = item1.isDirectory() ? 0 : 1;
		int category2 = item2.isDirectory() ? 0 : 1;

		if (category1 != category2) {
			return category1 - category2;
		}

		return item1.getName().compareToIgnoreCase(item2.getName());
	}

	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		int baseCompare = baseCompare(viewer, e1, e2);
		if (descending) {
			baseCompare = -baseCompare;
		}
		return baseCompare;
	}

}
