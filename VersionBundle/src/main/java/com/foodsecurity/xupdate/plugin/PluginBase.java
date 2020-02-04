package com.foodsecurity.xupdate.plugin;

import com.foodsecurity.xupdate.entity.PluginEntity;

import java.util.List;

/**
 * @author zhujianwei
 * @date 2020-02-04 11:08
 */
public interface PluginBase {
    PluginEntity install(String pluginName, String path);
    boolean uninstall(String pluginName);
    boolean isPluginInstalled(String pluginName);
    PluginEntity getPluginInfo(String name);
    List<PluginEntity> getPluginInfoList();
}
