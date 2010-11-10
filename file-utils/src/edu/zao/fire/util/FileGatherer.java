package edu.zao.fire.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Helper class used for gathering a list of files that match an optional
 * FileFilter, within a given starting directory.
 * 
 * @author Dylan
 * 
 */
public class FileGatherer implements Iterable<File> {
	private final File m_root;
	private final FileFilter m_filter;
	private final List<File> m_files = new LinkedList<File>();

	/**
	 * Constructor. Initializes the FileGatherer at the given rootPath, with no
	 * file filter.
	 * 
	 * @param rootPath
	 *            The path to the directory where the file gatherer will get its
	 *            files.
	 */
	public FileGatherer(String rootPath) {
		this(new File(rootPath), null);
	}

	/**
	 * Constructor. Initializes the FileGatherer at the given rootPath and with
	 * the given filter.
	 * 
	 * @param rootPath
	 *            The path to the directory where the file gatherer will get its
	 *            files.
	 * @param filter
	 *            The filter that chooses whether to accept (i.e. "gather") a
	 *            given file.
	 */
	public FileGatherer(String rootPath, FileFilter filter) {
		this(new File(rootPath), filter);
	}

	/**
	 * Constructor. Initializes the FileGatherer at the given root directory,
	 * with no file filter.
	 * 
	 * @param root
	 *            The directory from which the gatherer "gathers" its files.
	 */
	public FileGatherer(File root) {
		this(root, null);
	}

	/**
	 * Constructor. Initializes the FileGatherer at the given rootPath and with
	 * the given filter.
	 * 
	 * @param root
	 *            The directory from which the gatherer "gathers" its files.
	 * @param filter
	 *            The filter that chooses whether to accept (i.e. "gather") a
	 *            given file.
	 */
	public FileGatherer(File root, FileFilter filter) {
		m_root = root;
		m_filter = filter;
		initializeFileList(root);
	}

	/**
	 * Gather up all of the files under the root directory that match the
	 * filter. If the filter is null, all of the files will be gathered.
	 * 
	 * @param root
	 *            The directory from which the gatherer "gathers" its files.
	 */
	private void initializeFileList(File root) {
		File[] files = root.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (m_filter != null) {
				if (m_filter.accept(file)) {
					m_files.add(file);
				}
			} else {
				m_files.add(file);
			}
		}
	}

	/**
	 * @return An iterator over the files that were "gathered" by this
	 *         FileGatherer.
	 */
	@Override
	public Iterator<File> iterator() {
		return m_files.iterator();
	}

}
