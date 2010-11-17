package edu.zao.fire.editors.metadata;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

public class SelectedTagsLabelProvider extends StyledCellLabelProvider {

	public final Color plaintextTagColor = Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
	private final Styler plaintextStyler = new Styler() {
		@Override
		public void applyStyles(TextStyle textStyle) {
			textStyle.foreground = plaintextTagColor;
		}
	};

	@Override
	public void update(ViewerCell cell) {
		Object cellElement = cell.getElement();
		MetadataTag tag = (MetadataTag) cellElement;

		StyledString styledString = new StyledString(tag.tagName, null);

		if (tag.getFieldKey() == null) {
			styledString.append(": (");
			styledString.append(tag.getDefaultText(), plaintextStyler);
			styledString.append(")");
		}
		cell.setText(styledString.getString());
		cell.setStyleRanges(styledString.getStyleRanges());
	}
}