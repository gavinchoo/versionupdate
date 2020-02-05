

package com.foodsecurity.xupdate;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.http.DefaultUpdateServiceImpl;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateChecker;
import com.foodsecurity.xupdate.proxy.IUpdateDownloader;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;
import com.foodsecurity.xupdate.proxy.IUpdateParser;
import com.foodsecurity.xupdate.proxy.IUpdatePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.proxy.impl.DefaultUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.impl.DefaultUpdatePrompter;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;
import com.foodsecurity.xupdate.utils.UpdateUtils;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_NO_NETWORK;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_NO_NEW_VERSION;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_NO_WIFI;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.PROMPT_ACTIVITY_DESTROY;

/**
 * 版本更新管理者
 *
 * @author zhujianwei134
 * @date 2019/4/28
 */
public class UpdateManager implements IUpdateProxy {
    /**
     * 版本更新代理
     */
    private IUpdateProxy mIUpdateProxy;
    /**
     * 更新信息
     */
    private UpdateEntity mUpdateEntity;

    private List<UpdateEntity> mBundleUpdateEntity;

    private Context mContext;
    //============请求参数==============//
    /**
     * 版本更新的url地址
     */
    private String mUpdateUrl;
    /**
     * 请求参数
     */
    private Map<String, String> mParams;

    /**
     * apk缓存的目录
     */
    private String mApkCacheDir;

    //===========更新模式================//
    /**
     * 是否只在wifi下进行版本更新检查
     */
    private boolean mIsWifiOnly;
    /**
     * 是否是Get请求
     */
    private boolean mIsGet;
    /**
     * 是否是自动版本更新模式【无人干预,自动下载，自动更新】
     */
    private boolean mIsAutoMode;
    //===========更新组件===============//
    /**
     * 版本更新网络请求服务API
     */
    private IUpdateHttpService mIUpdateHttpService;
    /**
     * 版本更新检查器
     */
    private IUpdateChecker mIUpdateChecker;
    /**
     * 版本更新解析器
     */
    private IUpdateParser mIUpdateParser;
    /**
     * 版本更新下载器
     */
    private IUpdateDownloader mIUpdateDownloader;
    /**
     * 文件下载监听
     */
    private OnFileDownloadListener mOnFileDownloadListener;
    /**
     * 版本更新提示器
     */
    private IUpdatePrompter mIUpdatePrompter;

    /**
     * 插件版本更新提示器
     */
    private IUpdateBundlePrompter mIUpdateBundlePrompter;

    /**
     * 版本更新提示器参数信息
     */
    private PromptEntity mPromptEntity;

    /**
     * 构造函数
     *
     * @param builder
     */
    private UpdateManager(Builder builder) {
        mContext = builder.context;
        mUpdateUrl = builder.updateUrl;
        mParams = builder.params;
        mApkCacheDir = builder.apkCacheDir;

        mIsWifiOnly = builder.isWifiOnly;
        mIsGet = builder.isGet;
        mIsAutoMode = builder.isAutoMode;

        mIUpdateHttpService = builder.updateHttpService;

        mIUpdateChecker = builder.updateChecker;
        mIUpdateParser = builder.updateParser;
        mIUpdateDownloader = builder.updateDownLoader;
        mOnFileDownloadListener = builder.onFileDownloadListener;

        mIUpdatePrompter = builder.updatePrompter;
        mIUpdateBundlePrompter = builder.updateBundlePrompter;
        mPromptEntity = builder.promptEntity;

        UpdateFacade.initBundleMgr(mContext);
    }

    /**
     * 设置版本更新的代理，可自定义版本更新
     *
     * @param updateProxy
     * @return
     */
    public UpdateManager setIUpdateProxy(IUpdateProxy updateProxy) {
        mIUpdateProxy = updateProxy;
        return this;
    }

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public IUpdateHttpService getIUpdateHttpService() {
        if (null == mIUpdateHttpService) {
            mIUpdateHttpService = new DefaultUpdateServiceImpl();
        }
        return mIUpdateHttpService;
    }

    @Override
    public void post() {
        UpdateLog.d("Xupdate.update()启动:" + toString());
        if (mIUpdateProxy != null) {
            mIUpdateProxy.post();
        } else {
            doPost();
        }
    }

    /**
     * 开始版本更新
     */
    @Override
    public void update() {
        UpdateLog.d("Xupdate.update()启动:" + toString());
        if (mIUpdateProxy != null) {
            mIUpdateProxy.update();
        } else {
            doUpdate();
        }
    }

    @Override
    public void updateBundle() {
        UpdateLog.d("Xupdate.setBundleNewVersion()启动:" + toString());
        UpdateFacade.initUpdateBundle(this, mIUpdateBundlePrompter);
        update();
    }

    /**
     * 执行版本更新操作
     */
    private void doPost() {
        onBeforeCheck();
        if (mIsWifiOnly) {
            if (UpdateUtils.checkWifi(mContext)) {
                checkPost();
            } else {
                onAfterCheck();
                UpdateFacade.onUpdateError(CHECK_NO_WIFI);
            }
        } else {
            if (UpdateUtils.checkNetwork(mContext)) {
                checkPost();
            } else {
                onAfterCheck();
                UpdateFacade.onUpdateError(CHECK_NO_NETWORK);
            }
        }
    }

    private void checkPost() {
        UpdateLog.d("开始获取数据...");
        if (mIUpdateProxy != null) {
            mIUpdateProxy.checkVersion();
        } else {
            if (TextUtils.isEmpty(mUpdateUrl)) {
                throw new NullPointerException("[UpdateManager] : mUpdateUrl 不能为空");
            }
            mIUpdateChecker.check(mIsGet, mUpdateUrl, mParams, this);
        }
    }

    /**
     * 执行版本更新操作
     */
    private void doUpdate() {
        onBeforeCheck();

        if (mIsWifiOnly) {
            if (UpdateUtils.checkWifi(mContext)) {
                checkVersion();
            } else {
                onAfterCheck();
                UpdateFacade.onUpdateError(CHECK_NO_WIFI);
            }
        } else {
            if (UpdateUtils.checkNetwork(mContext)) {
                checkVersion();
            } else {
                onAfterCheck();
                UpdateFacade.onUpdateError(CHECK_NO_NETWORK);
            }
        }
    }

    /**
     * 版本检查之前
     */
    @Override
    public void onBeforeCheck() {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.onBeforeCheck();
        } else {
            mIUpdateChecker.onBeforeCheck();
        }
    }

    /**
     * 执行网络请求，检查应用的版本信息
     */
    @Override
    public void checkVersion() {
        UpdateLog.d("开始检查版本信息...");
        if (mIUpdateProxy != null) {
            mIUpdateProxy.checkVersion();
        } else {
            if (TextUtils.isEmpty(mUpdateUrl)) {
                throw new NullPointerException("[UpdateManager] : mUpdateUrl 不能为空");
            }
            mIUpdateChecker.check(mIsGet, mUpdateUrl, mParams, this);
        }
    }

    /**
     * 版本检查之后
     */
    @Override
    public void onAfterCheck() {
        if (mIUpdateProxy != null) {
            mIUpdateProxy.onAfterCheck();
        } else {
            mIUpdateChecker.onAfterCheck();
        }
    }

    /**
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @return
     */
    @Override
    public UpdateEntity parseJson(@NonNull String json) throws Exception {
        UpdateLog.i("服务端返回的最新版本信息:" + json);
        if (mIUpdateProxy != null) {
            mUpdateEntity = mIUpdateProxy.parseJson(json);
        } else {
            mUpdateEntity = mIUpdateParser.parseJson(json);
        }
        mUpdateEntity = refreshParams(mUpdateEntity);
        return mUpdateEntity;
    }

    @Override
    public List<UpdateEntity> parseBundleJson(@NonNull String json) throws Exception {
        UpdateLog.i("服务端返回的最新插件版本信息:" + json);
        if (mIUpdateProxy != null) {
            mBundleUpdateEntity = mIUpdateProxy.parseBundleJson(json);
        } else {
            mBundleUpdateEntity = mIUpdateParser.parseBundleJson(json);
        }
        return mBundleUpdateEntity;
    }

    @Override
    public void setUpdateEntity(UpdateEntity entity) {
        this.mUpdateEntity = entity;
        this.mUpdateEntity.setIUpdateHttpService(getIUpdateHttpService());
    }

    @Override
    public UpdateEntity getUpdateEntity() {
        return mUpdateEntity;
    }

    /**
     * 刷新本地参数
     *
     * @param updateEntity
     */
    private UpdateEntity refreshParams(UpdateEntity updateEntity) {
        //更新信息（本地信息）
        if (updateEntity != null) {
            updateEntity.setApkCacheDir(mApkCacheDir);
            updateEntity.setIsAutoMode(mIsAutoMode);
            updateEntity.setIUpdateHttpService(getIUpdateHttpService());
        }
        return updateEntity;
    }

    /**
     * 发现新版本
     *
     * @param updateEntity 版本更新信息
     * @param updateProxy  版本更新代理
     */
    @Override
    public void findNewVersion(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy) {
        UpdateLog.i("发现新版本:" + updateEntity);
        if (updateEntity.isSilent()) {
            //静默下载，发现新版本后，直接下载更新
            startDownload(updateEntity, mOnFileDownloadListener);
        } else {
            if (mIUpdateProxy != null) {
                //否则显示版本更新提示
                mIUpdateProxy.findNewVersion(updateEntity, updateProxy);
            } else {
                if (mIUpdatePrompter instanceof DefaultUpdatePrompter) {
                    if (mContext != null && !((Activity) mContext).isFinishing()) {
                        mIUpdatePrompter.showPrompt(updateEntity, updateProxy, mPromptEntity);
                    } else {
                        UpdateFacade.onUpdateError(PROMPT_ACTIVITY_DESTROY);
                    }
                } else {
                    mIUpdatePrompter.showPrompt(updateEntity, updateProxy, mPromptEntity);
                }
            }
        }
    }

    @Override
    public void findBundleNewVersion(@NonNull List<UpdateEntity> updateEntity, @NonNull IUpdateProxy updateProxy) {
        if (mIUpdateProxy != null) {
            //否则显示版本更新提示
            mIUpdateProxy.findBundleNewVersion(updateEntity, updateProxy);
        } else {
            if (mIUpdateBundlePrompter instanceof DefaultUpdateBundlePrompter) {
                if (mContext != null && !((Activity) mContext).isFinishing()) {
                    UpdateFacade.setBundleNewVersion(updateEntity, mPromptEntity);
                    mIUpdateBundlePrompter.showBundlePrompt(updateEntity, updateProxy, mPromptEntity);
                } else {
                    UpdateFacade.onUpdateError(PROMPT_ACTIVITY_DESTROY);
                }
            } else {
                UpdateFacade.setBundleNewVersion(updateEntity, mPromptEntity);
                mIUpdateBundlePrompter.showBundlePrompt(updateEntity, updateProxy, mPromptEntity);
            }
        }
    }

    /**
     * 未发现新版本
     *
     * @param throwable 未发现的原因
     */
    @Override
    public void noNewVersion(@NonNull Throwable throwable) {
        UpdateLog.i("未发现新版本:" + throwable.getMessage());
        if (mIUpdateProxy != null) {
            mIUpdateProxy.noNewVersion(throwable);
        } else {
            UpdateFacade.onUpdateError(CHECK_NO_NEW_VERSION, throwable.getMessage());
        }
    }

    @Override
    public void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener) {
        UpdateLog.i("开始下载更新文件:" + updateEntity);
        if (mIUpdateProxy != null) {
            mIUpdateProxy.startDownload(updateEntity, downloadListener);
        } else {
            mIUpdateDownloader.startDownload(updateEntity, downloadListener);
        }
    }

    /**
     * 后台下载
     */
    @Override
    public void backgroundDownload() {
        UpdateLog.i("点击了后台更新按钮, 在通知栏中显示下载进度...");
        if (mIUpdateProxy != null) {
            mIUpdateProxy.backgroundDownload();
        } else {
            mIUpdateDownloader.backgroundDownload();
        }
    }

    /**
     * 为外部提供简单的下载功能
     *
     * @param downloadUrl      下载地址
     * @param downloadListener 下载监听
     */
    public void download(String downloadUrl, String fileName, @Nullable OnFileDownloadListener downloadListener) {
        startDownload(refreshParams(new UpdateEntity()
                .setFileName(fileName)
                .setDownloadUrl(downloadUrl)), downloadListener);
    }

    @Override
    public void cancelDownload() {
        UpdateLog.d("正在取消更新文件的下载...");
        if (mIUpdateProxy != null) {
            mIUpdateProxy.cancelDownload();
        } else {
            mIUpdateDownloader.cancelDownload();
        }
    }

    //============================构建者===============================//

    /**
     * 版本更新管理构建者
     */
    public static class Builder {
        /**  */
        Context context;
        /**
         * 版本更新的url地址
         */
        String updateUrl;
        /**
         * 请求参数
         */
        Map<String, String> params;
        /**
         * 版本更新网络请求服务API
         */
        IUpdateHttpService updateHttpService;
        /**
         * 版本更新解析器
         */
        IUpdateParser updateParser;
        //===========更新模式================//
        /**
         * 是否使用的是Get请求
         */
        boolean isGet;
        /**
         * 是否只在wifi下进行版本更新检查
         */
        boolean isWifiOnly;
        /**
         * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装】
         */
        boolean isAutoMode;

        //===========更新行为================//
        /**
         * 版本更新检查器
         */
        IUpdateChecker updateChecker;
        /**
         * 版本更新提示器参数信息
         */
        PromptEntity promptEntity;
        /**
         * 版本更新提示器
         */
        IUpdatePrompter updatePrompter;

        /**
         * 插件版本更新提示器
         */
        IUpdateBundlePrompter updateBundlePrompter;

        /**
         * 下载器
         */
        IUpdateDownloader updateDownLoader;
        /**
         * 下载监听
         */
        OnFileDownloadListener onFileDownloadListener;
        /**
         * apk缓存的目录
         */
        String apkCacheDir;

        /**
         * 构建者
         *
         * @param context
         */
        Builder(@NonNull Context context) {
            this.context = context;

            params = new TreeMap<>();
            if (UpdateFacade.getParams() != null) {
                params.putAll(UpdateFacade.getParams());
            }

            promptEntity = new PromptEntity();

            updateHttpService = UpdateFacade.getIUpdateHttpService();

            updateChecker = UpdateFacade.getIUpdateChecker();
            updateParser = UpdateFacade.getIUpdateParser();
            updateDownLoader = UpdateFacade.getIUpdateDownLoader();

            isGet = UpdateFacade.isGet();
            isWifiOnly = UpdateFacade.isWifiOnly();
            isAutoMode = UpdateFacade.isAutoMode();
            apkCacheDir = UpdateFacade.getApkCacheDir();
        }

        /**
         * 设置版本更新检查的url
         *
         * @param updateUrl
         * @return
         */
        public Builder url(@NonNull String updateUrl) {
            this.updateUrl = updateUrl;
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param params
         * @return
         */
        public Builder params(@NonNull Map<String, String> params) {
            this.params.putAll(params);
            return this;
        }

        /**
         * 设置请求参数
         *
         * @param key
         * @param value
         * @return
         */
        public Builder param(@NonNull String key, @NonNull String value) {
            this.params.put(key, value);
            return this;
        }

        /**
         * 设置网络请求的请求服务API
         *
         * @param updateHttpService
         * @return
         */
        public Builder updateHttpService(@NonNull IUpdateHttpService updateHttpService) {
            this.updateHttpService = updateHttpService;
            return this;
        }

        /**
         * 设置apk下载的缓存目录
         *
         * @param apkCacheDir
         * @return
         */
        public Builder apkCacheDir(@NonNull String apkCacheDir) {
            this.apkCacheDir = apkCacheDir;
            return this;
        }

        /**
         * 是否使用Get请求
         *
         * @param isGet
         * @return
         */
        public Builder isGet(boolean isGet) {
            this.isGet = isGet;
            return this;
        }

        /**
         * 是否是自动版本更新模式【无人干预,有版本更新直接下载、安装，需要root权限】
         *
         * @param isAutoMode
         * @return
         */
        public Builder isAutoMode(boolean isAutoMode) {
            this.isAutoMode = isAutoMode;
            return this;
        }

        /**
         * 是否只在wifi下进行版本更新检查
         *
         * @param isWifiOnly
         * @return
         */
        public Builder isWifiOnly(boolean isWifiOnly) {
            this.isWifiOnly = isWifiOnly;
            return this;
        }

        /**
         * 设置版本更新检查器
         *
         * @param updateChecker
         * @return
         */
        public Builder updateChecker(@NonNull IUpdateChecker updateChecker) {
            this.updateChecker = updateChecker;
            return this;
        }

        /**
         * 设置版本更新的解析器
         *
         * @param updateParser
         * @return
         */
        public Builder updateParser(@NonNull IUpdateParser updateParser) {
            this.updateParser = updateParser;
            return this;
        }

        /**
         * 设置版本更新提示器
         *
         * @param updatePrompter
         * @return
         */
        public Builder updatePrompter(@NonNull IUpdatePrompter updatePrompter) {
            this.updatePrompter = updatePrompter;
            return this;
        }

        /**
         * 设置版本更新提示器
         *
         * @param updatePrompter
         * @return
         */
        public Builder updateBundlePrompter(@NonNull IUpdateBundlePrompter updatePrompter) {
            this.updateBundlePrompter = updatePrompter;
            return this;
        }


        /**
         * 设置文件的下载监听
         *
         * @param onFileDownloadListener
         * @return
         */
        public Builder setOnFileDownloadListener(OnFileDownloadListener onFileDownloadListener) {
            this.onFileDownloadListener = onFileDownloadListener;
            return this;
        }

        /**
         * 设置主题颜色
         *
         * @param themeColor
         * @return
         */
        public Builder themeColor(@ColorInt int themeColor) {
            promptEntity.setThemeColor(themeColor);
            return this;
        }

        /**
         * 设置顶部背景图片
         *
         * @param topResId
         * @return
         */
        public Builder topResId(@DrawableRes int topResId) {
            promptEntity.setTopResId(topResId);
            return this;
        }

        /**
         * 设置是否支持后台更新
         *
         * @param supportBackgroundUpdate
         * @return
         */
        public Builder supportBackgroundUpdate(boolean supportBackgroundUpdate) {
            promptEntity.setSupportBackgroundUpdate(supportBackgroundUpdate);
            return this;
        }

        /**
         * 设备版本更新下载器
         *
         * @param updateDownLoader
         * @return
         */
        public Builder updateDownLoader(@NonNull IUpdateDownloader updateDownLoader) {
            this.updateDownLoader = updateDownLoader;
            return this;
        }

        /**
         * 构建版本更新管理者
         *
         * @return 版本更新管理者
         */
        public UpdateManager build() {
            UpdateUtils.requireNonNull(this.context, "[UpdateManager.Builder] : context == null");

            if (this.updatePrompter == null) {
                if (context instanceof FragmentActivity) {
                    updatePrompter = new DefaultUpdatePrompter(((FragmentActivity) context).getSupportFragmentManager());
                } else if (context instanceof Activity) {
                    updatePrompter = new DefaultUpdatePrompter();
                } else {
                    throw new UnsupportedOperationException("[UpdateManager.Builder] : 使用默认的版本更新提示器，context必须传Activity！");
                }
            }

            if (this.updateBundlePrompter == null) {
                if (context instanceof FragmentActivity) {
                    updateBundlePrompter = new DefaultUpdateBundlePrompter(((FragmentActivity) context).getSupportFragmentManager());
                } else if (context instanceof Activity) {
                    updateBundlePrompter = new DefaultUpdateBundlePrompter();
                } else {
                    throw new UnsupportedOperationException("[UpdateManager.Builder] : 使用默认的版本更新提示器，context必须传Activity！");
                }
            }

            if (TextUtils.isEmpty(apkCacheDir)) {
                apkCacheDir = UpdateUtils.getDiskCacheDir(this.context, "xupdate");
            }
            return new UpdateManager(this);
        }

        /**
         * 进行版本更新
         */
        public void post() {
            build().post();
        }

        /**
         * 进行版本更新
         */
        public void update() {
            build().update();
        }

        /**
         * 进行版本更新
         *
         * @param updateProxy 版本更新代理
         */
        public void update(IUpdateProxy updateProxy) {
            build().setIUpdateProxy(updateProxy)
                    .update();
        }

        /**
         * 进行插件版本更新
         */
        public void updateBundle() {
            build().updateBundle();
        }
    }

    @Override
    public String toString() {
        return "Xupdate{" +
                "mUpdateUrl='" + mUpdateUrl + '\'' +
                ", mParams=" + mParams +
                ", mApkCacheDir='" + mApkCacheDir + '\'' +
                ", mIsWifiOnly=" + mIsWifiOnly +
                ", mIsGet=" + mIsGet +
                ", mIsAutoMode=" + mIsAutoMode +
                '}';
    }
}
