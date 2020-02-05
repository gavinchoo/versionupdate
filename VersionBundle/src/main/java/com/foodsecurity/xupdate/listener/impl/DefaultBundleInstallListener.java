

package com.foodsecurity.xupdate.listener.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.DownloadEntity;
import com.foodsecurity.xupdate.entity.PluginEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.widget.UpdateBundleMgr;

import java.io.File;

import static com.foodsecurity.xupdate.exception.UpdateException.Error.INSTALL_FAILED;

/**
 * 默认的插件Bundle安装监听
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午11:58
 */
public class DefaultBundleInstallListener implements OnInstallListener {

    @Override
    public boolean onInstall(@NonNull Context context, @NonNull File apkFile, UpdateEntity updateEntity, @NonNull DownloadEntity downloadEntity) {
        try {
            if (downloadEntity.isApkFileValid(apkFile)) {
                PluginEntity pluginEntity;
                if (updateEntity.getType() == UpdateBundleMgr.PLUGIN_TYPE_NATIVE_H5) {
                    pluginEntity = UpdateBundleMgr.get().installH5(updateEntity.getAlias(), apkFile.getPath());
                } else {
                    pluginEntity = UpdateBundleMgr.get().installNative(updateEntity.getAlias(), apkFile.getPath());
                }
                if (null != pluginEntity) {
                    UpdateLog.d(apkFile.getName() + " Bundle文件安装成功");
                    return true;
                } else {
                    UpdateLog.d(apkFile.getName() + " Bundle文件安装失败");
                    return false;
                }
            } else {
                UpdateLog.d(apkFile.getName() + " Bundle文件安装失败，MD5验证错误");
                return false;
            }
        } catch (Exception e) {
            UpdateFacade.onUpdateError(INSTALL_FAILED, "获取插件的路径出错！");
        } finally {
            if (null != apkFile) {
                apkFile.delete();
            }
        }
        return false;
    }

    @Override
    public void onInstallSuccess() {

    }
}
