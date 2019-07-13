package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2019/4/27 22:19
 */
public class VersionEntity {
    /**
     * 更新的状态
     */
    private int updateStatus;
    /**
     * 最新版本号[根据版本号来判别是否需要升级]
     */
    private String versionCode;
    /**
     * 最新APP版本的名称[用于展示的版本名]
     */
    private String versionName;
    /**
     * APP更新时间
     */
    private String uploadTime;
    /**
     * APP变更的内容
     */
    private String modifyContent;
    /**
     * 下载地址
     */
    private String downloadUrl;
    /**
     * Apk MD5值
     */
    private String md5;
    /**
     * Apk大小【单位：KB】
     */
    private long fileSize;

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 别名
     */
    private String alias;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 是否自动下载安装
     */
    private boolean isSilent;

    /**
     * 是否可以忽略升级
     */
    private boolean isIgnorable;

    public boolean isIgnorable() {
        return isIgnorable;
    }

    public VersionEntity setIgnorable(boolean ignorable) {
        isIgnorable = ignorable;
        return this;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public VersionEntity setSilent(boolean silent) {
        isSilent = silent;
        return this;
    }

    public int getUpdateStatus() {
        return updateStatus;
    }

    public VersionEntity setRequireUpgrade(int updateStatus) {
        this.updateStatus = updateStatus;
        return this;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public VersionEntity setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
        return this;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public VersionEntity setVersionCode(String versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public VersionEntity setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public String getModifyContent() {
        return modifyContent;
    }

    public VersionEntity setModifyContent(String modifyContent) {
        this.modifyContent = modifyContent;
        return this;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public VersionEntity setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public VersionEntity setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public long getFileSize() {
        return fileSize;
    }

    public VersionEntity setFileSize(long fileSize) {
        this.fileSize = fileSize;
        return this;
    }

    public VersionEntity setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public VersionEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public VersionEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "VersionEntity{" +
                ", updateStatus=" + updateStatus +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", modifyContent='" + modifyContent + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", md5='" + md5 + '\'' +
                ", fileName='" + fileName + '\'' +
                ", alias='" + alias + '\'' +
                ", fileSize=" + fileSize +
                ", isIgnorable=" + isIgnorable +
                ", isSilent=" + isSilent +
                '}';
    }
}
