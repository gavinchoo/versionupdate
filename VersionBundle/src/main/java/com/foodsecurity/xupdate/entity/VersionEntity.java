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
    private int versionCode;
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
    private String updateContent;
    /**
     * 下载地址
     */
    private String downloadUrl;

    private FileEntity fileInfo;

    /**
     * 别名
     */
    private String alias;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 是否自动下载安装
     */
    private boolean isSilent;

    /**
     * 是否可以忽略升级
     */
    private boolean isIgnorable;

    private boolean forceUpdate;
    private boolean wifiUpdate;
    private boolean timeRemind;

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

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
                ", alias='" + alias + '\'' +
                ", isIgnorable=" + isIgnorable +
                ", isSilent=" + isSilent +
                '}';
    }
}
