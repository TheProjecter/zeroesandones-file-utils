package edu.zao.fire.views.filter;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.TextStyle;

import edu.zao.fire.filters.GeneralMatchingFilter;

public class GeneralMatchingFilterLabelProvider extends StyledCellLabelProvider {

	private final Font defaultFont;
	private final Styler defaultStyler;
	private final Styler matchTypeStyler;
	private final Styler matchTextStyler;
	private final Styler matchRegexStyler;
	private final Styler afterthoughtStyler;

	public GeneralMatchingFilterLabelProvider(Font font) {
		this.defaultFont = font;
		Color defaultForeground = font.getDevice().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
		Color matchTypeColor = font.getDevice().getSystemColor(SWT.COLOR_DARK_CYAN);
		Color matchTextColor = font.getDevice().getSystemColor(SWT.COLOR_DARK_GREEN);
		Color matchRegexColor = font.getDevice().getSystemColor(SWT.COLOR_DARK_MAGENTA);
		Color afterthoughtColor = font.getDevice().getSystemColor(SWT.COLOR_GRAY);

		defaultStyler = new FontForegroundStyler(defaultForeground);
		matchTypeStyler = new FontForegroundStyler(matchTypeColor);
		matchTextStyler = new FontForegroundStyler(matchTextColor);
		matchRegexStyler = new FontForegroundStyler(matchRegexColor);
		afterthoughtStyler = new FontForegroundStyler(afterthoughtColor);
	}

	private class FontForegroundStyler extends Styler {
		private final Color foreground;

		public FontForegroundStyler(Color foreground) {
			this.foreground = foreground;
		}

		@Override
		public void applyStyles(TextStyle textStyle) {
			textStyle.background = null;
			textStyle.foreground = foreground;
			textStyle.font = defaultFont;
		}
	}

	@Override
	public void update(ViewerCell cell) {
		StyledString cellStyledString = getCellText(cell.getElement());
		cell.setText(cellStyledString.getString());
		cell.setStyleRanges(cellStyledString.getStyleRanges());
		super.update(cell);
	}

	protected StyledString getCellText(Object element) {
		if (element instanceof GeneralMatchingFilter) {
			return getCellText((GeneralMatchingFilter) element);
		}
		return new StyledString("???");
	}

	protected StyledString getCellText(GeneralMatchingFilter filter) {
		StyledString ss = new StyledString();
		ss.append("Filters ", defaultStyler);

		if (!filter.isUsesMatchText()) {
			ss.append("all ", defaultStyler);
		}

		switch (filter.getMatchType()) {
		case ALL_ITEMS:
			ss.append("Files and Folders ", matchTypeStyler);
			break;
		case FILE:
			ss.append("Files ", matchTypeStyler);
			break;
		case FOLDER:
			ss.append("Folders ", matchTypeStyler);
			break;
		}

		if (filter.isUsesMatchText()) {
			ss.append("whose names ", defaultStyler);

			if (filter.isUsesRegex()) {
				ss.append("match the pattern ", defaultStyler);
			} else {
				ss.append("contain ", defaultStyler);
			}

			ss.append(filter.getMatchText(), filter.isUsesRegex() ? matchRegexStyler : matchTextStyler);
		}

		if (filter.isCaseSensitive()) {
			ss.append(" (case sensitive)", afterthoughtStyler);
		}

		return ss;
	}
}
