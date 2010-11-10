package edu.zao.fire;

public class RenamedFile {
	private String beforeFullPath = "";
	private String afterFullPath = "";

	/**
	 * A helper class for the RenamerHistory, stores before and after paths
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

	public void setBeforePath(String beforeFullPath) {
		this.beforeFullPath = beforeFullPath;
	}

	public void setAfterPath(String afterFullPath) {
		this.afterFullPath = afterFullPath;
	}

	public String getBeforePath() {
		return beforeFullPath;
	}

	public String getAfterPath() {
		return afterFullPath;
	}

	public RenamedFile() {

	}
};
