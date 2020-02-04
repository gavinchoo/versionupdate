package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2019/4/27 22:19
 */
public class VersionEntity extends BaseVersionEntity {

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
