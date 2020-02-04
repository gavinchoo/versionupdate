package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2019/4/27 22:19
 */
public class PluginVersionEntity extends BaseVersionEntity {
    /**
     * 别名
     */
    private String pluginAlias;

    private String pluginName;

    private String pluginId;

    private String pluginKey;

    public String getPluginAlias() {
        return pluginAlias;
    }

    public void setPluginAlias(String pluginAlias) {
        this.pluginAlias = pluginAlias;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }

    public String getPluginId() {
        return pluginId;
    }

    public void setPluginId(String pluginId) {
        this.pluginId = pluginId;
    }

    public String getPluginKey() {
        return pluginKey;
    }

    public void setPluginKey(String pluginKey) {
        this.pluginKey = pluginKey;
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
                ", alias='" + pluginAlias + '\'' +
                ", isIgnorable=" + isIgnorable +
                ", isSilent=" + isSilent +
                '}';
    }
}
