package com.foodsecurity.xupdate.plugin.impl;

import android.content.Context;

import com.foodsecurity.xupdate.entity.PluginEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.plugin.PluginBase;
import com.foodsecurity.xupdate.utils.ZipUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhujianwei
 * @date 2020-02-04 11:24
 */
public class PluginH5 implements PluginBase {

    private Context mContext;

    public PluginH5 (Context context) {
        mContext = context;
    }

    @Override
    public String getRootPath() {
        return mContext.getFilesDir().getAbsolutePath() + File.separator + "jsbundles";
    }

    @Override
    public PluginEntity install(String pluginName, String path) {
        try {
            File bundleFile = new File(path);
            ZipUtils.unZipFolder(bundleFile.getPath(), bundleFile.getParent());
            return getPluginInfo(pluginName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean uninstall(String pluginName) {
        List<PluginEntity> localVersionInfo = getPluginInfoList();
        for (int i = 0; i < localVersionInfo.size(); i++) {
            PluginEntity localUpdate = localVersionInfo.get(i);
            if (localUpdate.getAlias().equals(pluginName)) {
                File file = new File(getRootPath() +
                        File.separator + localUpdate.getAlias() + "_" + localUpdate.getVersionName());
                if (null != file && file.exists()) {
                    return deleteDirWihtFile(file);
                }
            }
        }
        return false;
    }


    @Override
    public boolean isPluginInstalled(String pluginName) {
        PluginEntity localUpdate = getPluginInfo(pluginName);
        return null != localUpdate;
    }

    @Override
    public PluginEntity getPluginInfo(String name) {
        List<PluginEntity> localVersionInfo = getPluginInfoList();
        for (int i = 0; i < localVersionInfo.size(); i++) {
            if (localVersionInfo.get(i).getAlias().equals(name)) {
                return localVersionInfo.get(i);
            }
        }
        return null;
    }

    @Override
    public List<PluginEntity> getPluginInfoList() {
        List<PluginEntity> localVersionInfo = new ArrayList<>();
        File rootPath = new File(getRootPath());
        File[] bundles = rootPath.listFiles();
        if (null == bundles) {
            return localVersionInfo;
        }
        for (int i = 0; i < bundles.length; i++) {
            if (!bundles[i].isDirectory()) {
                continue;
            }
            PluginEntity updateEntity = new PluginEntity();
            String bundle = bundles[i].getName();
            if (bundle.length() > 0 && bundle.indexOf("_") != -1) {
                String bundleAlias = bundle.substring(0, bundle.lastIndexOf("_"));
                updateEntity.setAlias(bundleAlias);

                String bundleVersion = bundle.substring(bundle.lastIndexOf("_") + 1);
                updateEntity.setVersionName(bundleVersion);
            } else {
                updateEntity.setAlias(bundle);
                // 文件夹上没有带版本号，检查*.json文件版本号
                File bundlePath = new File(getRootPath() + File.separator + bundle);
                File[] bundleFiles = bundlePath.listFiles();
                if (null != bundleFiles) {
                    for (int j = 0; j < bundleFiles.length; j++) {
                        String fileName = bundleFiles[j].getName();
                        if (fileName.contains(".json")) {
                            updateEntity.setVersionName(fileName.replace(".json", ""));
                            break;
                        }
                    }
                }
            }
//            updateEntity.setFileName(bundle);
            localVersionInfo.add(updateEntity);
        }
        UpdateLog.i("获取本地插件版本信息：" + localVersionInfo.toString());
        return localVersionInfo;
    }

    private boolean deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                // 删除所有文件
                file.delete();
            } else if (file.isDirectory()) {
                // 递规的方式删除文件夹
                deleteDirWihtFile(file);
            }
        }
        // 删除目录本身
        return dir.delete();
    }
}
