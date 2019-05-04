

package com.foodsecurity.xupdate;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.listener.OnInstallListener;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.listener.impl.DefaultApkInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultBundleInstallListener;
import com.foodsecurity.xupdate.listener.impl.DefaultUpdateFailureListener;
import com.foodsecurity.xupdate.logs.ILogger;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateChecker;
import com.foodsecurity.xupdate.proxy.IUpdateDownloader;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;
import com.foodsecurity.xupdate.proxy.IUpdateParser;
import com.foodsecurity.xupdate.proxy.impl.DefaultUpdateChecker;
import com.foodsecurity.xupdate.proxy.impl.DefaultUpdateDownloader;
import com.foodsecurity.xupdate.proxy.impl.DefaultUpdateParser;

import java.util.Map;
import java.util.TreeMap;

/**
 * 版本更新的入口
 *
 * @author zhujianwei134
 * @date 2019/4/28
 */
public class XUpdate {

    private Application mContext;
    private static XUpdate sInstance;

    //========全局属性==========//
    /**
     * 请求参数【比如apk-key或者versionCode等】
     */
    Map<String, String> mParams;
    /**
     * 是否使用的是Get请求
     */
    boolean mIsGet;
    /**
     * 是否只在wifi下进行版本更新检查
     */
    boolean mIsWifiOnly;
    /**
     * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
     */
    boolean mIsAutoMode;
    /**
     * 下载的apk文件缓存目录
     */
    String mApkCacheDir;
    //========全局更新实现接口==========//
    /**
     * 版本更新网络请求服务API
     */
    IUpdateHttpService mIUpdateHttpService;
    /**
     * 版本更新检查器【有默认】
     */
    IUpdateChecker mIUpdateChecker;
    /**
     * 版本更新解析器【有默认】
     */
    IUpdateParser mIUpdateParser;
    /**
     * 版本更新下载器【有默认】
     */
    IUpdateDownloader mIUpdateDownloader;
    /**
     * APK安装监听【有默认】
     */
    OnInstallListener mOnApkInstallListener;

    OnInstallListener mOnBundleInstallListener;

    /**
     * 更新出错监听【有默认】
     */
    OnUpdateFailureListener mOnUpdateFailureListener;

    //===========================初始化===================================//

    private XUpdate() {
        mIsGet = false;
        mIsWifiOnly = true;
        mIsAutoMode = false;

        mIUpdateChecker = new DefaultUpdateChecker();
        mIUpdateParser = new DefaultUpdateParser();
        mIUpdateDownloader = new DefaultUpdateDownloader();
        mOnApkInstallListener = new DefaultApkInstallListener();
        mOnBundleInstallListener = new DefaultBundleInstallListener();
        mOnUpdateFailureListener = new DefaultUpdateFailureListener();
    }


    /**
     * 获取版本更新的入口
     *
     * @return 版本更新的入口
     */
    public static XUpdate get() {
        if (sInstance == null) {
            synchronized (XUpdate.class) {
                if (sInstance == null) {
                    sInstance = new XUpdate();
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化
     *
     * @param application
     */
    public void init(Application application) {
        mContext = application;
    }

    private Application getApplication() {
        testInitialize();
        return mContext;
    }

    private void testInitialize() {
        if (mContext == null) {
            throw new ExceptionInInitializerError("请先在全局Application中调用 XUpdate.get().init() 初始化！");
        }
    }

    public static Context getContext() {
        return get().getApplication();
    }

    //===========================对外版本更新api===================================//

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context) {
        return new UpdateManager.Builder(context);
    }

    /**
     * 获取版本更新构建者
     *
     * @param context
     * @param updateUrl 版本更新检查的地址
     * @return
     */
    public static UpdateManager.Builder newBuild(@NonNull Context context, String updateUrl) {
        return new UpdateManager.Builder(context)
                .updateUrl(updateUrl);
    }

    //===========================属性设置===================================//

    /**
     * 设置全局的apk更新请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public XUpdate param(@NonNull String key, @NonNull String value) {
        if (mParams == null) {
            mParams = new TreeMap<>();
        }
        UpdateLog.d("设置全局参数, key:" + key + ", value:" + value.toString());
        mParams.put(key, value);
        return this;
    }

    /**
     * 设置全局的apk更新请求参数
     *
     * @param params
     * @return
     */
    public XUpdate params(@NonNull Map<String, String> params) {
        logForParams(params);
        mParams = params;
        return this;
    }

    private void logForParams(@NonNull Map<String, String> params) {
        StringBuilder sb = new StringBuilder("设置全局参数:{\n");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append("key = ")
                    .append(entry.getKey())
                    .append(", value = ")
                    .append(entry.getValue().toString())
                    .append("\n");
        }
        sb.append("}");
        UpdateLog.d(sb.toString());
    }


    /**
     * 设置版本更新网络请求服务API
     *
     * @param updateHttpService
     * @return
     */
    public XUpdate setIUpdateHttpService(@NonNull IUpdateHttpService updateHttpService) {
        UpdateLog.d("设置全局更新网络请求服务:" + updateHttpService.getClass().getCanonicalName());
        mIUpdateHttpService = updateHttpService;
        return this;
    }

    /**
     * 设置版本更新检查
     *
     * @param updateChecker
     * @return
     */
    public XUpdate setIUpdateChecker(@NonNull IUpdateChecker updateChecker) {
        mIUpdateChecker = updateChecker;
        return this;
    }

    /**
     * 设置版本更新的解析器
     *
     * @param updateParser
     * @return
     */
    public XUpdate setIUpdateParser(@NonNull IUpdateParser updateParser) {
        mIUpdateParser = updateParser;
        return this;
    }

    /**
     * 设置版本更新下载器
     *
     * @param updateDownLoader
     * @return
     */
    public XUpdate setIUpdateDownLoader(@NonNull IUpdateDownloader updateDownLoader) {
        mIUpdateDownloader = updateDownLoader;
        return this;
    }

    /**
     * 是否使用的是Get请求
     *
     * @param isGet
     * @return
     */
    public XUpdate isGet(boolean isGet) {
        UpdateLog.d("设置全局是否使用的是Get请求:" + isGet);
        mIsGet = isGet;
        return this;
    }

    /**
     * 设置是否只在wifi下进行版本更新检查
     *
     * @param isWifiOnly
     * @return
     */
    public XUpdate isWifiOnly(boolean isWifiOnly) {
        UpdateLog.d("设置全局是否只在wifi下进行版本更新检查:" + isWifiOnly);
        mIsWifiOnly = isWifiOnly;
        return this;
    }

    /**
     * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
     *
     * @param isAutoMode
     * @return
     */
    public XUpdate isAutoMode(boolean isAutoMode) {
        UpdateLog.d("设置全局是否是自动版本更新模式:" + isAutoMode);
        mIsAutoMode = isAutoMode;
        return this;
    }

    /**
     * 设置apk的缓存路径
     *
     * @param apkCacheDir
     * @return
     */
    public XUpdate setApkCacheDir(String apkCacheDir) {
        UpdateLog.d("设置全局apk的缓存路径:" + apkCacheDir);
        mApkCacheDir = apkCacheDir;
        return this;
    }

    /**
     * 设置是否是debug模式
     *
     * @param isDebug
     * @return
     */
    public XUpdate debug(boolean isDebug) {
        UpdateLog.debug(isDebug);
        return this;
    }

    /**
     * 设置日志打印接口
     *
     * @param logger
     * @return
     */
    public XUpdate setILogger(@NonNull ILogger logger) {
        UpdateLog.setLogger(logger);
        return this;
    }

    //===========================apk安装监听===================================//

    /**
     * 设置安装监听
     *
     * @param onInstallListener
     * @return
     */
    public XUpdate setOnInstallListener(OnInstallListener onInstallListener) {
        mOnApkInstallListener = onInstallListener;
        return this;
    }

    /**
     * 设置安装监听
     *
     * @param onInstallListener
     * @return
     */
    public XUpdate setOnBundleInstallListener(OnInstallListener onInstallListener) {
        mOnBundleInstallListener = onInstallListener;
        return this;
    }

    //===========================更新出错===================================//

    /**
     * 设置更新出错的监听
     *
     * @param onUpdateFailureListener
     * @return
     */
    public XUpdate setOnUpdateFailureListener(@NonNull OnUpdateFailureListener onUpdateFailureListener) {
        mOnUpdateFailureListener = onUpdateFailureListener;
        return this;
    }

    public boolean canOpenBundle(String alias) {
        return UpdateFacade.canOpenBundle(alias);
    }

    public boolean isInstalled(String alias) {
        return UpdateFacade.isInstalled(alias);
    }
}
