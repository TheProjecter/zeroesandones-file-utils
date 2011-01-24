package edu.zao.fire.editors.metadata;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SelectedTagsContentProvider implements IStructuredContentProvider {

	MetadataTagList input;

	@Override
	public Object[] getElements(Object inputElement) {
		return input.getTagsArray();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput == null) {
			return;
		}
		if (newInput instanceof MetadataTagList) {
			input = (MetadataTagList) newInput;
		} else {
			throw new IllegalArgumentException("newInput must be a MetadataTagList");
		}
	}
}
