package com.sunday.member.entity;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ApkInfo {

	private int versionCode;
	private String versionName;
	private String appName;
	private String apkPackage;
	private String minSdkVersion;
	private String apkName;
	private String fileMd5;
	private String fizeSize;
	private String fileLogs;
	private String apkUrl;
	@SerializedName(value = "downLoadUrl")
	private String downloadUrl;


	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getApkPackage() {
		return apkPackage;
	}
	public void setApkPackage(String apkPackage) {
		this.apkPackage = apkPackage;
	}
	public String getMinSdkVersion() {
		return minSdkVersion;
	}
	public void setMinSdkVersion(String minSdkVersion) {
		this.minSdkVersion = minSdkVersion;
	}
	public String getApkName() {
		return apkName;
	}
	public void setApkName(String apkName) {
		this.apkName = apkName;
	}
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	public String getFizeSize() {
		return fizeSize;
	}
	public void setFizeSize(String fizeSize) {
		this.fizeSize = fizeSize;
	}

	public String getFileLogs() {
		return fileLogs;
	}

	public void setFileLogs(String fileLogs) {
		this.fileLogs = fileLogs;
	}

	public String getApkUrl() {
		return apkUrl;
	}

	public void setApkUrl(String apkUrl) {
		this.apkUrl = apkUrl;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
}
