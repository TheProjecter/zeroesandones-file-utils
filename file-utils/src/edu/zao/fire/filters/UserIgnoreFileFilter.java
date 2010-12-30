package edu.zao.fire.filters;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

public class UserIgnoreFileFilter extends BrowserFileFilter {

	private static UserIgnoreFileFilter globalInstance;

	public static UserIgnoreFileFilter getGlobalInstance() {
		if (globalInstance == null) {
			globalInstance = new UserIgnoreFileFilter();
		}
		return globalInstance;
	}

	// files will be stored by name. any files in this set should never be
	// accepted by this filter
	private final Set<String> ignoredFileNames = new TreeSet<String>();

	@Override
	public boolean accept(File file) {
		return !isIgnored(file);
	}

	public boolean isIgnored(File file) {
		return ignoredFileNames.contains(getFilePath(file));
	}

	private String getFilePath(File file) {
		try {
			return file.getCanonicalPath();
		} catch (IOException e) {
			return file.getAbsolutePath();
		}
	}

	public void addIgnoredFile(File file) {
		ignoredFileNames.add(getFilePath(file));
		fireFilterChanged();
	}

	public void removeIgnoredFile(File file) {
		System.out.println("removed? " + ignoredFileNames.remove(getFilePath(file)));
		fireFilterChanged();
	}
}
