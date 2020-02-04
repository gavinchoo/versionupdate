package com.foodsecurity.xupdate.plugin.impl;

import com.foodsecurity.xupdate.entity.PluginEntity;
import com.foodsecurity.xupdate.plugin.PluginBase;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhujianwei
 * @date 2020-02-04 11:24
 */
public class PluginNative implements PluginBase {
    @Override
    public PluginEntity install(String pluginName, String path) {
        PluginInfo repluginInfo = RePlugin.install(path);
        return repluginInfoToPlugin(repluginInfo);
    }

    @Override
    public boolean uninstall(String pluginName) {
        return RePlugin.uninstall(pluginName);
    }

    @Override
    public boolean isPluginInstalled(String pluginName) {
        return RePlugin.isPluginInstalled(pluginName);
    }

    @Override
    public PluginEntity getPluginInfo(String name) {
        PluginInfo repluginInfo = RePlugin.getPluginInfo(name);
        return repluginInfoToPlugin(repluginInfo);
    }

    @Override
    public List<PluginEntity> getPluginInfoList() {
        List<PluginInfo> repluginInfos = RePlugin.getPluginInfoList();
        List<PluginEntity> pluginEntities = new ArrayList<>();
        for (int i = 0; i < repluginInfos.size(); i++) {
            pluginEntities.add(repluginInfoToPlugin(repluginInfos.get(i)));
        }
        return pluginEntities;
    }

    private PluginEntity repluginInfoToPlugin(PluginInfo repluginInfo) {
        PluginEntity pluginEntity = new PluginEntity();
        pluginEntity.setAlias(repluginInfo.getAlias());
        pluginEntity.setName(repluginInfo.getName());
        pluginEntity.setVersionCode(repluginInfo.getVersion());
        return pluginEntity;
    }
}
