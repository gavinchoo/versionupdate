

package com.foodsecurity.xupdate;

import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.entity.DownloadEntity;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.entity.UpdateError;
import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.listener.impl.DefaultBundleInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultApkInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateChecker;
import com.foodsecurity.xupdate.proxy.IUpdateDownloader;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;
import com.foodsecurity.xupdate.proxy.IUpdateParser;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.utils.ApkInstallUtils;
import com.foodsecurity.xupdate.widget.UpdateBundleMgr;

import java.io.File;
import java.util.List;
import java.util.Map;

import static com.foodsecurity.xupdate.entity.UpdateError.ERROR.INSTALL_FAILED;

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
        return XUpdate.get().mParams;
    }

    public static IUpdateHttpService getIUpdateHttpService() {
        return XUpdate.get().mIUpdateHttpService;
    }

    public static IUpdateChecker getIUpdateChecker() {
        return XUpdate.get().mIUpdateChecker;
    }

    public static IUpdateParser getIUpdateParser() {
        return XUpdate.get().mIUpdateParser;
    }

    public static IUpdateDownloader getIUpdateDownLoader() {
        return XUpdate.get().mIUpdateDownloader;
    }

    public static boolean isGet() {
        return XUpdate.get().mIsGet;
    }

    public static boolean isWifiOnly() {
        return XUpdate.get().mIsWifiOnly;
    }

    public static boolean isAutoMode() {
        return XUpdate.get().mIsAutoMode;
    }

    public static String getApkCacheDir() {
        return XUpdate.get().mApkCacheDir;
    }

    //===========================apk安装监听===================================//

    public static OnInstallListener getOnInstallListener() {
        return XUpdate.get().mOnApkInstallListener;
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
     * @param context 传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile apk文件
     */
    public static void startInstallBundle(@NonNull Context context, @NonNull File apkFile) {
        startInstallBundle(context, apkFile, new DownloadEntity());
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
    public static void startInstallBundle(@NonNull Context context, @NonNull File apkFile, @NonNull DownloadEntity downloadEntity) {
        UpdateLog.d("开始安装Bundle文件, 文件路径:" + apkFile.getAbsolutePath() + ", 下载信息:" + downloadEntity);
        if (onInstallBundle(context, apkFile, downloadEntity)) {
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
        if (XUpdate.get().mOnApkInstallListener == null) {
            XUpdate.get().mOnApkInstallListener = new DefaultApkInstallListener();
        }
        return XUpdate.get().mOnApkInstallListener.onInstall(context, apkFile, downloadEntity);
    }

    /**
     * 安装apk
     *
     * @param context        传activity可以获取安装的返回值，详见{@link ApkInstallUtils#REQUEST_CODE_INSTALL_APP}
     * @param apkFile        apk文件
     * @param downloadEntity 文件下载信息
     */
    private static boolean onInstallBundle(Context context, File apkFile, DownloadEntity downloadEntity) {
        if (XUpdate.get().mOnBundleInstallListener == null) {
            XUpdate.get().mOnBundleInstallListener = new DefaultBundleInstallListener();
        }
        return XUpdate.get().mOnBundleInstallListener.onInstall(context, apkFile, downloadEntity);
    }

    /**
     * apk安装完毕
     */
    private static void onApkInstallSuccess() {
        if (XUpdate.get().mOnApkInstallListener == null) {
            XUpdate.get().mOnApkInstallListener = new DefaultApkInstallListener();
        }
        XUpdate.get().mOnApkInstallListener.onInstallSuccess();
    }

    /**
     * apk安装完毕
     */
    private static void onBundleInstallSuccess() {
        if (XUpdate.get().mOnBundleInstallListener == null) {
            XUpdate.get().mOnBundleInstallListener = new DefaultBundleInstallListener();
        }
        XUpdate.get().mOnBundleInstallListener.onInstallSuccess();
    }

    //===========================更新出错===================================//

    public static OnUpdateFailureListener getOnUpdateFailureListener() {
        return XUpdate.get().mOnUpdateFailureListener;
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     */
    public static void onUpdateError(int errorCode) {
        onUpdateError(new UpdateError(errorCode));
    }

    /**
     * 更新出现错误
     *
     * @param errorCode
     * @param message
     */
    public static void onUpdateError(int errorCode, String message) {
        onUpdateError(new UpdateError(errorCode, message));
    }

    /**
     * 更新出现错误
     *
     * @param updateError
     */
    public static void onUpdateError(@NonNull UpdateError updateError) {
        if (XUpdate.get().mOnUpdateFailureListener == null) {
            XUpdate.get().mOnUpdateFailureListener = new DefaultUpdateFailureListener();
        }
        XUpdate.get().mOnUpdateFailureListener.onFailure(updateError);
    }

    public static String getBundlesRootPath() {
        return XUpdate.getContext().getFilesDir().getAbsolutePath() + File.separator + "jsbundles";
    }

    public static boolean isInstalled(String alias) {
        return UpdateBundleMgr.get().isInstalled(alias);
    }

    public static boolean canOpenBundle(String alias) {
        return UpdateBundleMgr.get().canOpen(alias);
    }

    public static void setBundleNewVersion(List<UpdateEntity> updateEntity, PromptEntity promptEntity) {
        UpdateBundleMgr.get().init(updateEntity, promptEntity);
    }

    public static void initUpdateBundle(IUpdateProxy updateProxy) {
        UpdateBundleMgr.get().setIUpdateProxy(updateProxy);
    }

}
