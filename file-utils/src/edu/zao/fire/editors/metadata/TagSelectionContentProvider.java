package edu.zao.fire.editors.metadata;

import java.util.Set;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class TagSelectionContentProvider implements IStructuredContentProvider {

	private Set<?> input;

	@Override
	public Object[] getElements(Object inputElement) {
		return input.toArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			return;
		}
		if (newInput instanceof Set<?>) {
			input = (Set<?>) newInput;
		} else {
			throw new IllegalArgumentException("newInput must be a Set of something (i.e. a Set<?>)");
		}
	}

}
