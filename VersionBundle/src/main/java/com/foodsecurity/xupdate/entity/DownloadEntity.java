

package com.foodsecurity.xupdate.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.foodsecurity.xupdate.utils.Md5Utils;

import java.io.File;

/**
 * 下载信息实体
 *
 * @author zhujianwei134
 * @since 2018/7/9 上午11:41
 */
public class DownloadEntity implements Parcelable {
    /**
     * 下载地址
     */
    private String mDownloadUrl;
    /**
     * 文件下载的目录
     */
    private String mCacheDir;
    /**
     * 下载文件的md5值，用于校验，防止下载的apk文件被替换
     */
    private String mMd5;

    private String mFileName;

    /**
     * 下载文件的大小【单位：KB】
     */
    private long mSize;
    //==========================//
    /**
     * 是否在通知栏上显示下载进度
     */
    private boolean mIsShowNotification;

    public DownloadEntity() {

    }

    protected DownloadEntity(Parcel in) {
        mDownloadUrl = in.readString();
        mCacheDir = in.readString();
        mMd5 = in.readString();
        mFileName = in.readString();
        mSize = in.readLong();
        mIsShowNotification = in.readByte() != 0;
    }

    public static final Creator<DownloadEntity> CREATOR = new Creator<DownloadEntity>() {
        @Override
        public DownloadEntity createFromParcel(Parcel in) {
            return new DownloadEntity(in);
        }

        @Override
        public DownloadEntity[] newArray(int size) {
            return new DownloadEntity[size];
        }
    };

    public String getDownloadUrl() {
        return mDownloadUrl;
    }

    public DownloadEntity setDownloadUrl(String downloadUrl) {
        mDownloadUrl = downloadUrl;
        return this;
    }

    public String getCacheDir() {
        return mCacheDir;
    }

    public DownloadEntity setCacheDir(String cacheDir) {
        mCacheDir = cacheDir;
        return this;
    }

    public String getMd5() {
        return mMd5;
    }

    public DownloadEntity setMd5(String md5) {
        mMd5 = md5;
        return this;
    }

    public long getSize() {
        return mSize;
    }

    public DownloadEntity setSize(long size) {
        mSize = size;
        return this;
    }

    public boolean isShowNotification() {
        return mIsShowNotification;
    }

    public DownloadEntity setShowNotification(boolean showNotification) {
        mIsShowNotification = showNotification;
        return this;
    }
    /**
     * apk文件是否有效
     *
     * @param apkFile
     * @return
     */
    public boolean isApkFileValid(File apkFile) {
        return Md5Utils.isFileValid(mMd5, apkFile);
    }

    @Override
    public String toString() {
        return "DownloadEntity{" +
                "mDownloadUrl='" + mDownloadUrl + '\'' +
                ", mCacheDir='" + mCacheDir + '\'' +
                ", mMd5='" + mMd5 + '\'' +
                ", mFileName='" + mFileName + '\'' +
                ", mSize=" + mSize +
                ", mIsShowNotification=" + mIsShowNotification +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mDownloadUrl);
        dest.writeString(mCacheDir);
        dest.writeString(mMd5);
        dest.writeString(mFileName);
        dest.writeLong(mSize);
        dest.writeByte((byte) (mIsShowNotification ? 1 : 0));
    }
}
