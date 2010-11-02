package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

public class BrowserTableItemSorter extends ViewerSorter {
	private int columnIndex = 0;
	private boolean descending;

	public void setColumn(int column) {
		if (columnIndex != column) {
			descending = false;
		} else {
			descending = !descending;
		}
		columnIndex = column;
	}

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
