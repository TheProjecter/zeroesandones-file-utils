package edu.zao.fire.views.filter;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import edu.zao.fire.filters.UserFilters;

public class GeneralMatchingFilterContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	@Override
	public Object[] getElements(Object inputElement) {
		UserFilters filters = (UserFilters) inputElement;
		return filters.getGeneralFilters().toArray();
	}

}
