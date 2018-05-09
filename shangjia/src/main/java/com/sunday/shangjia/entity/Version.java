package com.sunday.shangjia.entity;

/**
 * Created by 刘涛 on 2017/1/7.
 */

public class Version {
    /**
     * versionCode : 41
     * versionName : 2.2.2
     * apkPackage : com.sunday.drink
     * minSdkVersion : 14
     * apkName : @7F060057
     * fileMd5 : 2c908d08a1bbe51f4a9b9ce90567a9b9
     * fizeSize : 9610758
     * fileLogs : 1,友盟替换
     * downLoadUrl : http://image.e-jiuquan.com/image/app-sunday-release.apk
     */

    private int versionCode;
    private String versionName;
    private String apkPackage;
    private String minSdkVersion;
    private String apkName;
    private String fileMd5;
    private String fizeSize;
    private String fileLogs;
    private String downLoadUrl;

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

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
