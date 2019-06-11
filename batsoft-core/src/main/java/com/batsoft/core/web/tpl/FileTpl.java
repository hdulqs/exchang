package com.batsoft.core.web.tpl;

import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.common.Constants;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;



public class FileTpl implements Tpl {
	private File file;

	public FileTpl(File file) {
		this.file = file;
	}

	@Override
	public String getName() {
		int len= ApplicationConfigure.tplPath.length();
		String ap = file.getAbsolutePath().substring(len);
		ap = ap.replace(File.separatorChar, '/');
		if (!ap.startsWith("/")) {
			ap = "/" + ap;
		}
		return ap;
	}

	@Override
	public String getPath() {
		String name = getName();
		return name.substring(0, name.lastIndexOf('/'));
	}

	@Override
	public String getFilename() {
		return file.getName();
	}

	@Override
	public String getSource() {
		if (file.isDirectory()) {
			return null;
		}
		try {
			return FileUtils.readFileToString(this.file, Constants.UTF8);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public long getLastModified() {
		return file.lastModified();
	}

	@Override
	public Date getLastModifiedDate() {
		return new Timestamp(getLastModified());
	}

	@Override
	public long getLength() {
		return file.length();
	}

	@Override
	public int getSize() {
		return (int) (getLength() / 1024) + 1;
	}

	@Override
	public boolean isDirectory() {
		return file.isDirectory();
	}
}
