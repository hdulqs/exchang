package com.batsoft.core.web.tpl;


import com.batsoft.core.ApplicationConfigure;
import com.batsoft.core.common.Constants;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileTplManagerImpl implements TplManager {
	private static Logger log = LoggerFactory.getLogger(FileTplManagerImpl.class);


	@Override
        public int delete(String[] names) {
		File f;
		int count = 0;
		for (String name : names) {
			f = new File(ApplicationConfigure.tplPath+name);
			if (f.isDirectory()) {
				if (FileUtils.deleteQuietly(f)) {
					count++;
				}
			} else {
				if (f.delete()) {
					count++;
				}
			}
		}
		return count;
	}

	@Override
        public Tpl get(String name) {
		File f = new File(ApplicationConfigure.tplPath+name);
		if (f.exists()) {
			return new FileTpl(f);
		} else {
			return null;
		}
	}

	@Override
        public List<Tpl> getListByPrefix(String prefix) {

		File f = new File(ApplicationConfigure.tplPath+prefix);
		String name = prefix.substring(prefix.lastIndexOf("/") + 1);
		File parent;
		if (prefix.endsWith("/")) {
			name = "";
			parent = f;
		} else {
			parent = f.getParentFile();
		}
		if (parent.exists()) {
			File[] files = parent.listFiles(new PrefixFilter(name));
			if (files != null) {
				List<Tpl> list = new ArrayList<Tpl>();
				for (File file : files) {
					list.add(new FileTpl(file));
				}
				return list;
			} else {
				return new ArrayList<Tpl>(0);
			}
		} else {
			return new ArrayList<Tpl>(0);
		}
	}

	@Override
        public List<String> getNameListByPrefix(String prefix) {
		List<Tpl> list = getListByPrefix(prefix);
		List<String> result = new ArrayList<String>(list.size());
		for (Tpl tpl : list) {
			result.add(tpl.getName());
		}
		return result;
	}

	@Override
        public List<Tpl> getChild(String path) {

		File file = new File(ApplicationConfigure.tplPath+path);
		File[] child = file.listFiles();
		if (child != null) {
			List<Tpl> list = new ArrayList<Tpl>(child.length);
			for (File f : child) {
				list.add(new FileTpl(f));
			}
			return list;
		} else {
			return new ArrayList<Tpl>(0);
		}
	}

	@Override
        public void rename(String orig, String dist) {

		String os = ApplicationConfigure.tplPath+orig;
		File of = new File(os);
		String ds = ApplicationConfigure.tplPath+dist;
		File df = new File(ds);
		try {
			if (of.isDirectory()) {
				FileUtils.moveDirectory(of, df);
			} else {
				FileUtils.moveFile(of, df);
			}
		} catch (IOException e) {
			log.error("Move template error: " + orig + " to " + dist, e);
		}
	}

	@Override
        public void save(String name, String source, boolean isDirectory) {
		String real = ApplicationConfigure.tplPath+name;
		File f = new File(real);
		if (isDirectory) {
			f.mkdirs();
		} else {
			try {
				FileUtils.writeStringToFile(f, source, Constants.UTF8);
			} catch (IOException e) {
				log.error("Save template error: " + name, e);
				throw new RuntimeException(e);
			}
		}
	}

	@Override
        public void save(String path, MultipartFile file) {
		String real = ApplicationConfigure.tplPath+path;
		File f = new File(real, file
				.getOriginalFilename());
		try {
			file.transferTo(f);
		} catch (IllegalStateException e) {
			log.error("upload template error!", e);
		} catch (IOException e) {
			log.error("upload template error!", e);
		}
	}

	@Override
        public void update(String name, String source) {
		//String real = realPathResolver.get(name);
		String real = ApplicationConfigure.tplPath+name;
		File f = new File(real);
		try {
			FileUtils.writeStringToFile(f, source, Constants.UTF8);
		} catch (IOException e) {
			log.error("Save template error: " + name, e);
			throw new RuntimeException(e);
		}
	}



	private static class PrefixFilter implements FileFilter {
		private String prefix;

		public PrefixFilter(String prefix) {
			this.prefix = prefix;
		}

		@Override
                public boolean accept(File file) {
			String name = file.getName();
			return file.isFile() && name.startsWith(prefix);
		}
	}

}
