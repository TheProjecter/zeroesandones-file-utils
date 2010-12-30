package edu.zao.fire.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.zao.fire.util.Filter;

public abstract class BrowserFileFilter implements Filter<File> {
	public static interface FilterListener {
		public void filterChanged(Filter<File> filter);
	}

	protected final List<FilterListener> filterListeners = new ArrayList<FilterListener>();

	public void addFilterListener(FilterListener listener) {
		filterListeners.add(listener);
	}

	public void removeFilterListener(FilterListener listener) {
		filterListeners.remove(listener);
	}

	protected void fireFilterChanged() {
		for (FilterListener listener : filterListeners) {
			listener.filterChanged(this);
		}
	}
}
