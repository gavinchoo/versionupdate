package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2019/4/27 22:19
 */
public class BaseVersionEntity {
    /**
     * 更新的状态
     */
    protected int updateStatus;
    /**
     * 最新版本号[根据版本号来判别是否需要升级]
     */
    protected int versionCode;
    /**
     * 最新APP版本的名称[用于展示的版本名]
     */
    protected String versionName;
    /**
     * APP更新时间
     */
    protected String uploadTime;
    /**
     * APP变更的内容
     */
    protected String updateContent;
    /**
     * 下载地址
     */
    protected String downloadUrl;

    protected FileEntity fileInfo;

    /**
     * 产品名称
     */
    protected String productName;

    /**
     * 是否自动下载安装
     */
    protected boolean isSilent;

    /**
     * 是否可以忽略升级
     */
    protected boolean isIgnorable;

    protected boolean forceUpdate;
    protected boolean wifiUpdate;
    protected boolean timeRemind;

    public int getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(int updateStatus) {
        this.updateStatus = updateStatus;
    }

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

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public void setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public FileEntity getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileEntity fileInfo) {
        this.fileInfo = fileInfo;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }

    public boolean isIgnorable() {
        return isIgnorable;
    }

    public void setIgnorable(boolean ignorable) {
        isIgnorable = ignorable;
    }

    public boolean isForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public boolean isWifiUpdate() {
        return wifiUpdate;
    }

    public void setWifiUpdate(boolean wifiUpdate) {
        this.wifiUpdate = wifiUpdate;
    }

    public boolean isTimeRemind() {
        return timeRemind;
    }

    public void setTimeRemind(boolean timeRemind) {
        this.timeRemind = timeRemind;
    }

    @Override
    public String toString() {
        return "VersionEntity{" +
                ", updateStatus=" + updateStatus +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", uploadTime='" + uploadTime + '\'' +
                ", modifyContent='" + updateContent + '\'' +
                ", downloadUrl='" + downloadUrl + '\'' +
                ", isIgnorable=" + isIgnorable +
                ", isSilent=" + isSilent +
                '}';
    }
}
