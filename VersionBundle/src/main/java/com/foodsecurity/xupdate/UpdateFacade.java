

package com.foodsecurity.xupdate;

import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.entity.DownloadEntity;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.exception.UpdateException;
import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.listener.impl.DefaultBundleInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultApkInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateChecker;
import com.foodsecurity.xupdate.proxy.IUpdateDownloader;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;
import com.foodsecurity.xupdate.proxy.IUpdateParser;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;
import com.foodsecurity.xupdate.utils.ApkInstallUtils;
import com.foodsecurity.xupdate.widget.UpdateBundleMgr;
import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.foodsecurity.xupdate.exception.UpdateException.Error.INSTALL_FAILED;

/**
 * 内部版本更新参数的获取
 *
 * @author zhujianwei134
 * @since 2018/7/10 下午4:27
 */
public final class UpdateFacade {

    /**
     * 标志当前更新提示是否已显示
     */
    private static boolean sIsShowUpdatePrompter = false;

    public static void setIsShowUpdatePrompter(boolean isShowUpdatePrompter) {
        UpdateFacade.sIsShowUpdatePrompter = isShowUpdatePrompter;
    }

    public static boolean isShowUpdatePrompter() {
        return UpdateFacade.sIsShowUpdatePrompter;
    }

    //===========================属性设置===================================//

    public static Map<String, String> getParams() {
        return Xupdate.get().mParams;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return Xupdate.get().mIUpdateHttpService;
    }

    public static IUpdateChecker getIUpdateChecker() {
        return Xupdate.get().mIUpdateChecker;
    }

    public static IUpdateParser getIUpdateParser() {
        return Xupdate.get().mIUpdateParser;
    }

    public static IUpdateDownloader getIUpdateDownLoader() {
        return Xupdate.get().mIUpdateDownloader;
    }

    public static boolean isGet() {
        return Xupdate.get().mIsGet;
    }

    public static boolean isWifiOnly() {
        return Xupdate.get().mIsWifiOnly;
    }

    public static boolean isAutoMode() {
        return Xupdate.get().mIsAutoMode;
    }

    public static String getApkCacheDir() {
        return Xupdate.get().mApkCacheDir;
    }

    //===========================apk安装监听===================================//

    public static OnInstallListener getOnInstallListener() {
        return Xupdate.get().mOnApkInstallListener;
    }

    /**
     * 开始安装apk文件
     *
     * @param context 传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile apk文件
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile) {
        startInstallApk(context, apkFile, new DownloadEntity());
    }

    /**
     * 开始安装apk文件
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    public static void startInstallApk(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        UpdateLog.d("开始安装apk文件, 文件路径:" + apkFile.getAbsolutePath() + ", 下载信息:" + downloadEntity);
        if (onInstallApk(context, apkFile, downloadEntity)) {
            onApkInstallSuccess(); //静默安装的话，不会回调到这里
        } else {
            onUpdateError(INSTALL_FAILED);
        }
    }

    /**
     * 开始安装apk文件
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    public static void startInstallBundle(@NonNull Context context, @NonNull File apkFile, UpdateEntity updateEntity, @NonNull DownloadEntity downloadEntity) {
        UpdateLog.d("开始安装Bundle文件, 文件路径:" + apkFile.getAbsolutePath() + ", 下载信息:" + downloadEntity);
        if (onInstallBundle(context, apkFile, updateEntity, downloadEntity)) {
            updateEntity.setHasUpdate(true);
            onBundleInstallSuccess(); //静默安装的话，不会回调到这里
        } else {
            onUpdateError(INSTALL_FAILED);
        }
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    private static boolean onInstallApk(Context context, File apkFile, DownloadEntity downloadEntity) {
        if (Xupdate.get().mOnApkInstallListener == null) {
            Xupdate.get().mOnApkInstallListener = new DefaultApkInstallListener();
        }
        return Xupdate.get().mOnApkInstallListener.onInstall(context, apkFile, null, downloadEntity);
    }

    /**
     * apk安装完毕
     */
    private static void onApkInstallSuccess() {
        if (Xupdate.get().mOnApkInstallListener == null) {
            Xupdate.get().mOnApkInstallListener = new DefaultApkInstallListener();
        }
        Xupdate.get().mOnApkInstallListener.onInstallSuccess();
    }

    /**
     * 开始安装apk文件
     *
     * @param context 传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile apk文件
     */
    public static void startInstallBundle(@NonNull Context context, UpdateEntity updateEntity, @NonNull File apkFile) {
        startInstallBundle(context, apkFile, updateEntity, new DownloadEntity());
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    private static boolean onInstallBundle(Context context, File apkFile, UpdateEntity updateEntity, DownloadEntity downloadEntity) {
        if (Xupdate.get().mOnBundleInstallListener == null) {
            Xupdate.get().mOnBundleInstallListener = new DefaultBundleInstallListener();
        }
        return Xupdate.get().mOnBundleInstallListener.onInstall(context, apkFile, updateEntity, downloadEntity);
    }

    /**
     * apk安装完毕
     */
    private static void onBundleInstallSuccess() {
        if (Xupdate.get().mOnBundleInstallListener == null) {
            Xupdate.get().mOnBundleInstallListener = new DefaultBundleInstallListener();
        }
        Xupdate.get().mOnBundleInstallListener.onInstallSuccess();
    }

    //===========================更新出错===================================//

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return Xupdate.get().mOnUpdateFailureListener;
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     */
    public static void onUpdateError(int errorCode) {
        onUpdateError(new UpdateException(errorCode));
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     * @param message
     */
    public static void onUpdateError(int errorCode, String message) {
        onUpdateError(new UpdateException(errorCode, message));
    }

    /**
     * 更新出现错误
     *
     * @param updateError
     */
    public static void onUpdateError(@NonNull UpdateException updateError) {
        if (Xupdate.get().mOnUpdateFailureListener == null) {
            Xupdate.get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        Xupdate.get().mOnUpdateFailureListener.onFailure(updateError);
    }

    public static void initBundleMgr(Context context) {
        UpdateBundleMgr.get().init(context);
    }

    public static void setBundleNewVersion(List<UpdateEntity> updateEntity, PromptEntity promptEntity) {
        UpdateBundleMgr.get().setPluginsUpdateInfo(updateEntity, promptEntity);
        for (int i = 0; i < updateEntity.size(); i++) {
            if (updateEntity.get(i).isSilent()) {
                updateBundlesVersion(updateEntity.get(i));
            }
        }
    }

    public static void initUpdateBundle(IUpdateProxy updateProxy, IUpdateBundlePrompter updatePrompter) {
        UpdateBundleMgr.get()
                .setIUpdateProxy(updateProxy)
                .setUpdatePrompter(updatePrompter);
    }



    public static boolean isInstalledH5(String alias) {
        return UpdateBundleMgr.get().isInstalledH5(alias);
    }

    public static boolean isInstalledNative(String alias) {
        return UpdateBundleMgr.get().isInstalledNative(alias);
    }

    public static boolean canOpenBundle(String alias) {
        return UpdateBundleMgr.get().canOpen(alias);
    }

    public static void updateBundlesVersion(UpdateEntity entity, OnFileDownloadListener listener) {
        UpdateBundleMgr.get().updateBundlesVersion(entity, listener);
    }

    /**
     * 直接下载安装插件
     */
    public static void updateBundlesVersion(final UpdateEntity entity) {
        UpdateBundleMgr.get().updateBundlesVersion(entity);
    }
}
