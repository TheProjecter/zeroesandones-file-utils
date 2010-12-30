package edu.zao.fire;

import java.io.File;
import java.util.Set;

import org.eclipse.core.expressions.PropertyTester;

import edu.zao.fire.rcp.Activator;

public class RecentFilesPropertyTester extends PropertyTester {

	public final String HAVE_RECENT_ITEMS = "haveRecentItems";

	public RecentFilesPropertyTester() {
	}

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		if (HAVE_RECENT_ITEMS.equals(property)) {
			Set<File> recentRules = Activator.getDefault().getRecentlyLoadedRules();
			return !recentRules.isEmpty();
		}
		return false;
	}

}
