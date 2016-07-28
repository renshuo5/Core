package com.rensframework.core.spring;

import java.io.File;
import java.io.FilenameFilter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class SysPropertiesConfig implements FactoryBean<Resource[]> {

	private Resource[] res;

	private String sysPropName = "rens.home";

	private String subPath;
	private String extName = "properties";
	private boolean mustExist = true;
	private Resource[] mergeResources;

	public void setMergeResources(Resource[] mergeResources) {
		this.mergeResources = mergeResources;
	}

	public void setSysPropName(String sysPropName) {
		this.sysPropName = sysPropName;
	}

	public void setSubPath(String subPath) {
		this.subPath = subPath;
	}

	public void setExtName(String extName) {
		this.extName = extName;
	}

	public void setMustExist(boolean mustExist) {
		this.mustExist = mustExist;
	}

	public Class<?> getObjectType() {
		return Resource[].class;
	}

	public boolean isSingleton() {
		return Boolean.TRUE.booleanValue();
	}

	public Resource[] getObject() throws Exception {
		if (StringUtils.isBlank(this.subPath)) {
			throw new IllegalArgumentException("必须设置subPath!");
		}
		if (this.res == null) {
			File folder = null;
			if ((this.sysPropName != null)
					&& (!"".equals(this.sysPropName.trim()))) {
				String sysPropPath = System
						.getProperty(this.sysPropName.trim());
				if ((sysPropPath != null) && (!"".equals(sysPropPath))) {
					if (this.subPath.startsWith("/")) {
						this.subPath = this.subPath.substring(1);
					}
					String sep = System.getProperty("file.separator");
					if (!sysPropPath.endsWith(sep)) {
						sysPropPath = sysPropPath + sep;
					}
					String path = sysPropPath
							+ StringUtils.replace(this.subPath, "/", sep);
					folder = new File(path);
				}
			}
			if (folder == null) {
				ClassPathResource cpr = new ClassPathResource(this.subPath);
				if (cpr.exists()) {
					folder = cpr.getFile();
				} else {
					if (this.mustExist) {
						notFound(this.subPath);
					}
					if ((this.mergeResources == null)
							|| (this.mergeResources.length == 0)) {
						this.res = new Resource[0];
					} else {
						this.res = this.mergeResources;
					}
					return this.res;
				}
			}
			Resource[] subRes = null;
			if ((folder.exists()) && (folder.isDirectory())) {
				subRes = getSubResources(folder, this.extName);
				if ((this.mergeResources == null)
						|| (this.mergeResources.length == 0)) {
					this.res = subRes;
				} else {
					this.res = new Resource[subRes.length
							+ this.mergeResources.length];
					System.arraycopy(subRes, 0, this.res, 0, subRes.length);
					System.arraycopy(this.mergeResources, 0, this.res,
							subRes.length, this.mergeResources.length);
				}
				return this.res;
			}
			if (this.mustExist) {
				notFound(this.subPath);
			}
		}
		return this.res;
	}

	private void notFound(String subPath) {
		throw new IllegalArgumentException("在类路径下和系统指定路径下均未发现目录" + subPath);
	}

	private Resource[] getSubResources(File parent, final String extName) {
		File[] files = parent.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return (name.endsWith("." + extName))
						&& (!name.startsWith("."));
			}
		});
		Resource[] res = new Resource[files.length];
		for (int i = 0; i < files.length; i++) {
			res[i] = new FileSystemResource(files[i]);
		}
		return res;
	}
}
