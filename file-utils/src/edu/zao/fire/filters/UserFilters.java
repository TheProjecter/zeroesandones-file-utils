package edu.zao.fire.filters;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.zao.fire.filters.BrowserFileFilter.FilterListener;
import edu.zao.fire.util.Filter;

public class UserFilters extends BrowserFileFilter implements FilterListener {

	private UserIgnoreFileFilter individualFilter;

	/**
	 * Defines an array list that fires a filter-changed event when an item is
	 * added or removed.
	 */
	private final List<GeneralMatchingFilter> generalFilters = new ArrayList<GeneralMatchingFilter>();

	@Override
	public boolean accept(File file) {
		boolean accept = true;
		if (individualFilter != null) {
			accept &= individualFilter.accept(file);
		}
		for (GeneralMatchingFilter filter : generalFilters) {
			accept &= filter.accept(file);
		}
		return accept;
	}

	public UserIgnoreFileFilter getIndividualFilter() {
		return individualFilter;
	}

	public void setIndividualFilter(UserIgnoreFileFilter individualFilter) {
		if (this.individualFilter != individualFilter) {
			if (this.individualFilter != null) {
				this.individualFilter.removeFilterListener(this);
			}
			if (individualFilter != null) {
				individualFilter.addFilterListener(this);
			}
		}
		this.individualFilter = individualFilter;
	}

	public List<GeneralMatchingFilter> getGeneralFilters() {
		return Collections.unmodifiableList(generalFilters);
	}

	public boolean add(GeneralMatchingFilter filter) {
		if (filter != null) {
			filter.addFilterListener(this);
		}
		boolean changed = generalFilters.add(filter);
		if (changed) {
			fireFilterChanged();
		}
		return changed;
	}

	public boolean remove(GeneralMatchingFilter filter) {
		if (filter != null) {
			filter.removeFilterListener(this);
		}
		boolean changed = generalFilters.remove(filter);
		if (changed) {
			fireFilterChanged();
		}
		return changed;
	}

	@Override
	public void filterChanged(Filter<File> filter) {
		fireFilterChanged();
	}

}
