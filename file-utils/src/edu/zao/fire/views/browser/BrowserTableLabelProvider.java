package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.zao.fire.Renamer;

public class BrowserTableLabelProvider extends StyledCellLabelProvider {

	private final Renamer renamer;

	public BrowserTableLabelProvider(Renamer renamer) {
		this.renamer = renamer;
	}

	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		int columnIndex = cell.getColumnIndex();
		String cellText = getCellText(element, columnIndex);
		Image cellImage = getCellImage(element, columnIndex);

		cell.setText(cellText);
		cell.setImage(cellImage);
	}

	private String getCellText(Object element, int columnIndex) {
		if (!(element instanceof File)) {
			throw new IllegalArgumentException("element must be a File");
		}
		File file = (File) element;
		switch (columnIndex) {
		case 0:
			return file.getName();
		case 1:
			return renamer.getNewName(file);
		default:
			return "-";
		}
	}

	private Image getCellImage(Object element, int columnIndex) {
		if (columnIndex != 0) {
			return null;
		}

		if (!(element instanceof File)) {
			throw new IllegalArgumentException("element must be a File");
		}
		File file = (File) element;

		if (file.isDirectory()) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
		} else if (file.isFile()) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		} else {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

}
