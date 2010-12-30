package edu.zao.fire;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.jface.action.IContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.CompoundContributionItem;
import org.eclipse.ui.menus.CommandContributionItem;
import org.eclipse.ui.menus.CommandContributionItemParameter;

import edu.zao.fire.commands.LoadRuleCommandHandler;
import edu.zao.fire.rcp.Activator;

public class RecentRulesContribution extends CompoundContributionItem {

	@Override
	protected IContributionItem[] getContributionItems() {
		Set<File> fileSet = Activator.getDefault().getRecentlyLoadedRules();
		int numFiles = fileSet.size();
		IContributionItem[] list = new IContributionItem[numFiles];
		int index = 0;

		for (File file : fileSet) {
			Map<String, String> params = new TreeMap<String, String>();
			params.put(LoadRuleCommandHandler.fileNameKey, file.getAbsolutePath());

			CommandContributionItemParameter itemParam = new CommandContributionItemParameter(//
					PlatformUI.getWorkbench(), // service_locator
					"edu.zao.fire.recentFiles" + index, // ID of the
														// contribution item
					"edu.zao.fire.commands.loadRuleDirectly", // ID of the
																// command
					SWT.PUSH // style
			);
			itemParam.label = file.getName();
			itemParam.parameters = params;

			list[index++] = new CommandContributionItem(itemParam);
		}
		return list;
	}
}
