

package com.foodsecurity.xupdate.listener.impl;

import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.DownloadEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.utils.ApkInstallUtils;

import java.io.File;
import java.io.IOException;

import static com.foodsecurity.xupdate.exception.UpdateException.Error.INSTALL_FAILED;

/**
 * 默认的apk安装监听
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午11:58
 */
public class DefaultApkInstallListener implements OnInstallListener {

    @Override
    public boolean onInstall(@NonNull Context context, @NonNull File apkFile, UpdateEntity updateEntity, @NonNull DownloadEntity downloadEntity) {
        try {
            return downloadEntity.isApkFileValid(apkFile) && ApkInstallUtils.install(context, apkFile);
        } catch (IOException e) {
            UpdateFacade.onUpdateError(INSTALL_FAILED, "获取apk的路径出错！");
        }
        return false;
    }

    @Override
    public void onInstallSuccess() {

    }
}
