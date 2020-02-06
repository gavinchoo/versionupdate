

package com.foodsecurity.xupdate.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.foodsecurity.xupdate.proxy.IUpdateHttpService;

/**
 * 版本更新信息实体
 *
 * @author zhujianwei134
 * @since 2018/6/29 下午9:33
 */
public class UpdateEntity implements Parcelable {
    //===========是否可以升级=============//
    /**
     * 是否有新版本
     */
    private boolean hasUpdate;

    /**
     * 是否强制安装：不安装无法使用app
     */
    private boolean isForce;

    /**
     * 是否可忽略该版本
     */
    private boolean isIgnorable;

    //===========升级的信息=============//

    /**
     * 文件名称
     */
    private String fileName;

    private String name;

    /**
     * 版本号
     */
    private int versionCode;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 别名
     */
    private String alias;

    /**
     * 插件类型, 1 原生， 2 本地H5, 3 H5链接
     */
    private int type;
    /**
     * 更新内容
     */
    private String updateContent;

    /**
     * 下载信息实体
     */
    private DownloadEntity downloadEntity;

    //============升级行为============//
    /**
     * 是否静默下载：有新版本时不提示直接下载
     */
    private boolean isSilent;
    /**
     * 是否下载完成后自动安装[默认是true]
     */
    private boolean isAutoInstall;

    private float downloadProgress;

    public UpdateEntity() {
        versionName = "unknown_version";
        downloadEntity = new DownloadEntity();
        isAutoInstall = true;
    }

    protected UpdateEntity(Parcel in) {
        hasUpdate = in.readByte() != 0;
        isForce = in.readByte() != 0;
        isIgnorable = in.readByte() != 0;
        versionCode = in.readInt();
        versionName = in.readString();
        alias = in.readString();
        fileName = in.readString();
        name = in.readString();
        updateContent = in.readString();
        downloadEntity = in.readParcelable(DownloadEntity.class.getClassLoader());
        isSilent = in.readByte() != 0;
        isAutoInstall = in.readByte() != 0;
    }

    public static final Creator<UpdateEntity> CREATOR = new Creator<UpdateEntity>() {
        @Override
        public UpdateEntity createFromParcel(Parcel in) {
            return new UpdateEntity(in);
        }

        @Override
        public UpdateEntity[] newArray(int size) {
            return new UpdateEntity[size];
        }
    };

    public boolean isHasUpdate() {
        return hasUpdate;
    }

    public UpdateEntity setHasUpdate(boolean hasUpdate) {
        this.hasUpdate = hasUpdate;
        return this;
    }

    public boolean isForce() {
        return isForce;
    }

    public UpdateEntity setForce(boolean force) {
        if (force) {
            //强制更新，不可以忽略
            isIgnorable = false;
        }
        isForce = force;
        return this;
    }

    public boolean isIgnorable() {
        return isIgnorable;
    }

    public UpdateEntity setIsIgnorable(boolean isIgnorable) {
        if (isIgnorable) {
            //可忽略的，不能是强制更新
            isForce = false;
        }
        this.isIgnorable = isIgnorable;
        return this;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public UpdateEntity setIsSilent(boolean isSilent) {
        this.isSilent = isSilent;
        return this;
    }

    public boolean isAutoInstall() {
        return isAutoInstall;
    }

    public UpdateEntity setIsAutoInstall(boolean isAutoInstall) {
        this.isAutoInstall = isAutoInstall;
        return this;
    }

    /**
     * 设置apk的缓存地址，只支持设置一次
     *
     * @param apkCacheDir
     * @return
     */
    public UpdateEntity setApkCacheDir(String apkCacheDir) {
        if (!TextUtils.isEmpty(apkCacheDir) && TextUtils.isEmpty(downloadEntity.getCacheDir())) {
            downloadEntity.setCacheDir(apkCacheDir);
        }
        return this;
    }

    /**
     * 设置是否是自动模式【自动静默下载，自动安装】
     *
     * @param isAutoMode
     */
    public void setIsAutoMode(boolean isAutoMode) {
        if (isAutoMode) {
            //自动下载
            isSilent = true;
            //自动安装
            isAutoInstall = true;
            //自动模式下，默认下载进度条在通知栏显示
            downloadEntity.setShowNotification(true);
        }
    }

    public String getUpdateContent() {
        return updateContent;
    }

    public UpdateEntity setUpdateContent(String updateContent) {
        this.updateContent = updateContent;
        return this;
    }

    public String getDownloadUrl() {
        return downloadEntity.getDownloadUrl();
    }

    public UpdateEntity setDownloadUrl(String downloadUrl) {
        downloadEntity.setDownloadUrl(downloadUrl);
        return this;
    }

    public String getMd5() {
        return downloadEntity.getMd5();
    }

    public UpdateEntity setMd5(String md5) {
        downloadEntity.setMd5(md5);
        return this;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public UpdateEntity setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    public String getVersionName() {
        return versionName;
    }

    public UpdateEntity setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public UpdateEntity setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public UpdateEntity setType(int type) {
        this.type = type;
        return this;
    }

    public int getType() {
        return type;
    }

    public UpdateEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public long getSize() {
        return downloadEntity.getSize();
    }

    public UpdateEntity setSize(long size) {
        downloadEntity.setSize(size);
        return this;
    }

    public float getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(float downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public UpdateEntity setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public String getApkCacheDir() {
        return downloadEntity.getCacheDir();
    }

    public UpdateEntity setDownLoadEntity(@NonNull DownloadEntity downloadEntity) {
        this.downloadEntity = downloadEntity;
        return this;
    }

    @NonNull
    public DownloadEntity getDownLoadEntity() {
        return downloadEntity;
    }

    /**
     * 内部变量，请勿设置
     */
    private IUpdateHttpService mIUpdateHttpService;

    public UpdateEntity setIUpdateHttpService(IUpdateHttpService updateHttpService) {
        mIUpdateHttpService = updateHttpService;
        return this;
    }

    public IUpdateHttpService getIUpdateHttpService() {
        return mIUpdateHttpService;
    }

    @Override
    public String toString() {
        return "UpdateEntity{" +
                "hasUpdate=" + hasUpdate +
                ", isForce=" + isForce +
                ", isIgnorable=" + isIgnorable +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", alias='" + alias + '\'' +
                ", fileName='" + fileName + '\'' +
                ", name='" + name + '\'' +
                ", updateContent='" + updateContent + '\'' +
                ", downloadEntity=" + downloadEntity +
                ", isSilent=" + isSilent +
                ", isAutoInstall=" + isAutoInstall +
                ", mIUpdateHttpService=" + mIUpdateHttpService +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (hasUpdate ? 1 : 0));
        dest.writeByte((byte) (isForce ? 1 : 0));
        dest.writeByte((byte) (isIgnorable ? 1 : 0));
        dest.writeInt(versionCode);
        dest.writeString(versionName);
        dest.writeString(versionName);
        dest.writeString(alias);
        dest.writeString(fileName);
        dest.writeString(name);
        dest.writeString(updateContent);
        dest.writeParcelable(downloadEntity, flags);
        dest.writeByte((byte) (isSilent ? 1 : 0));
        dest.writeByte((byte) (isAutoInstall ? 1 : 0));
    }
}
