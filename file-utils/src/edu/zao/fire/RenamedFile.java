package edu.zao.fire;

public class RenamedFile {
	public String beforeFullPath;
	public String afterFullPath;

	/**
	 * A helper class for the Renamer history, stores before and after paths
	 * 
	 * @param beforeFullPath
	 *            Is the full canonical path to the file before its change
	 * @param afterName
	 *            Is the full canonical path to the file after its change
	 */
	RenamedFile(String beforeFullPath, String afterFullPath) {
		this.beforeFullPath = beforeFullPath;
		this.afterFullPath = afterFullPath;
	}

	public RenamedFile() {
		// TODO Auto-generated constructor stub
	}
};
