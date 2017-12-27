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

//	private String versionCode;
//	private String versionName;
//	private String downloadUrl;
//	private String updateLog;
//	private String isNeedUpdate;

	private int id;
	private int types;
	private int versions;
	private int mustUpdate;
	private String message;
	private String down_url;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTypes() {
		return types;
	}

	public void setTypes(int types) {
		this.types = types;
	}

	public int getVersions() {
		return versions;
	}

	public void setVersions(int versions) {
		this.versions = versions;
	}

	public int getMustUpdate() {
		return mustUpdate;
	}

	public void setMustUpdate(int mustUpdate) {
		this.mustUpdate = mustUpdate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getDown_url() {
		return down_url;
	}

	public void setDown_url(String down_url) {
		this.down_url = down_url;
	}

	//	public String getVersionCode() {
//		return versionCode;
//	}
//
//	public void setVersionCode(String versionCode) {
//		this.versionCode = versionCode;
//	}
//
//	public String getVersionName() {
//		return versionName;
//	}
//
//	public void setVersionName(String versionName) {
//		this.versionName = versionName;
//	}
//
//	public String getDownloadUrl() {
//		return downloadUrl;
//	}
//
//	public void setDownloadUrl(String downloadUrl) {
//		this.downloadUrl = downloadUrl;
//	}
//
//	public String getUpdateLog() {
//		return updateLog;
//	}
//
//	public void setUpdateLog(String updateLog) {
//		this.updateLog = updateLog;
//	}
//
//	public String getIsNeedUpdate() {
//		return isNeedUpdate;
//	}
//
//	public void setIsNeedUpdate(String isNeedUpdate) {
//		this.isNeedUpdate = isNeedUpdate;
//	}

}
