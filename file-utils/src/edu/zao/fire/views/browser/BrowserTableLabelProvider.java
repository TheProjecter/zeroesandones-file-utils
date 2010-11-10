package edu.zao.fire.views.browser;

import java.io.File;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import edu.zao.fire.Renamer;

/**
 * Label provider that decides the text, styling, and image for cells in the
 * Browser table viewer.
 * 
 * @author Dylan
 */
public class BrowserTableLabelProvider extends StyledCellLabelProvider {

	private final Renamer renamer;

	public BrowserTableLabelProvider(Renamer renamer) {
		this.renamer = renamer;
	}

	/**
	 * Updates the label for the given <code>cell</code>. Sets the text and
	 * image to whatever it deems necessary.
	 */
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		int columnIndex = cell.getColumnIndex();
		String cellText = getCellText(element, columnIndex);
		Image cellImage = getCellImage(element, columnIndex);

		cell.setText(cellText);
		cell.setImage(cellImage);
	}

	/**
	 * Get the text for the given element at the given column. For the first
	 * column, the file's name is the text. For the second column, the file's
	 * new name according to the renamer is the text.
	 * 
	 * @param element
	 *            An File that is being represented by a row in the browser
	 *            table.
	 * @param columnIndex
	 *            The column of the File's row.
	 * @return The appropriate text for the given File in the given column.
	 */
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

	/**
	 * Gets the image to display for the given File (<code>element</code>) and
	 * the given column. Directories get a "folder" icon, other files get a
	 * "text file" icon, and strange files get a generic "object" icon. Icons
	 * are only assigned in the first column.
	 * 
	 * @param element
	 * @param columnIndex
	 * @return The icon to display for the given File.
	 */
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
