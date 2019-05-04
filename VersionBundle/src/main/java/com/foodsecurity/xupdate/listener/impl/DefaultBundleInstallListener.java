

package com.foodsecurity.xupdate.listener.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.DownloadEntity;
import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.utils.BundleInstallUtils;

import java.io.File;

import static com.foodsecurity.xupdate.entity.UpdateError.ERROR.INSTALL_FAILED;

/**
 * 默认的插件Bundle安装监听
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午11:58
 */
public class DefaultBundleInstallListener implements OnInstallListener {

    @Override
    public boolean onInstall(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        try {
            if (downloadEntity.isApkFileValid(apkFile)) {
                boolean success = BundleInstallUtils.install(apkFile);
                if (success) {
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
