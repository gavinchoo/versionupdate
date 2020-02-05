package com.foodsecurity.xupdate.entity;

/**
 * @author zhujianwei
 * @date 2020-02-04 11:15
 */
public class PluginEntity {
    /**
     * 版本号
     */
    protected int versionCode;

    /**
     * 版本名称
     */
    protected String versionName;

    /**
     * 别名
     */
    protected String alias;

    /**
     * 插件类型, 1 原生， 2 本地H5, 3 H5链接
     */
    protected int type;

    public int getVersionCode() {
        return versionCode;
    }

    public PluginEntity setVersionCode(int versionCode) {
        this.versionCode = versionCode;
        return this;
    }

    /**
     * 产品名称
     */
    protected String name;

    public String getVersionName() {
        return versionName;
    }

    public PluginEntity setVersionName(String versionName) {
        this.versionName = versionName;
        return this;
    }

    public PluginEntity setAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getAlias() {
        return alias;
    }

    public PluginEntity setType(int type) {
        this.type = type;
        return this;
    }

    public int getType() {
        return type;
    }

    public PluginEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }
}
