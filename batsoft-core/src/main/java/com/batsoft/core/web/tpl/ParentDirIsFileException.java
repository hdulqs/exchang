package com.batsoft.core.web.tpl;

/**
 * 父目录是文件异常
 * 
 * 当移动文件或创建文件时，父目录也是文件，可以导致该异常。
 */
@SuppressWarnings("serial")
public class ParentDirIsFileException extends RuntimeException {
	private String parentDir;

	/**
	 * @param parentDir
	 *            The parent dir, which is a file.
	 */
	public ParentDirIsFileException(String parentDir) {
		this.parentDir = parentDir;
	}

	@Override
	public String getMessage() {
		return "parent directory is a file: " + parentDir;
	}

	/**
	 * Get the parent dir, which is a file.
	 * 
	 * @return
	 */
	public String getParentDir() {
		return parentDir;
	}
}
