package com.business;

/**
 * 应用程序更新实体类
 * 
 * @author morton
 * 
 */
public class Update {

	public final static String UTF8 = "UTF-8";
	public final static String NODE_ROOT = "heiche";

	private String versionCode;
	private String versionName;
	private String downloadUrl;
	private String updateLog;
	private String isNeedUpdate;

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getUpdateLog() {
		return updateLog;
	}

	public void setUpdateLog(String updateLog) {
		this.updateLog = updateLog;
	}

	public String getIsNeedUpdate() {
		return isNeedUpdate;
	}

	public void setIsNeedUpdate(String isNeedUpdate) {
		this.isNeedUpdate = isNeedUpdate;
	}

}
