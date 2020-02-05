package com.foodsecurity.xupdate.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.Xupdate;
import com.foodsecurity.xupdate.entity.PluginEntity;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.plugin.PluginBase;
import com.foodsecurity.xupdate.plugin.impl.PluginH5;
import com.foodsecurity.xupdate.plugin.impl.PluginNative;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;

import java.io.File;
import java.util.List;

/**
 * 插件版本更新管理
 *
 * @author zhujianwei
 * @date 2019/4/29 20:28
 */
public class UpdateBundleMgr {

    /**
     * 插件已安装，没有新版本
     */
    public static final String BUNDLE_STATUS_DEFAULT = "default";
    /**
     * 插件没有安装
     */
    public static final String BUNDLE_STATUS_NOT_INSTALL = "not_install";
    /**
     * 插件已安装，有新版本
     */
    public static final String BUNDLE_STATUS_HAS_NEW_VERSION = "has_new_version";
    /**
     * 插件已安装，检测版本信息失败了
     */
    public static final String BUNDLE_STATUS_INSTALLED_NO_VERSION = "installed_no_version_info";

    /**
     * 插件未安装，检测版本信息失败了
     */
    public static final String BUNDLE_STATUS_NOT_INSTALL_NO_VERSION = "not_install_no_version_info";

    /**
     * 插件类型 原生
     */
    public static final int PLUGIN_TYPE_NATIVE = 1;
    /**
     * 插件类型 H5本地
     */
    public static final int PLUGIN_TYPE_NATIVE_H5 = 2;
    /**
     * 插件类型 H5链接
     */
    public static final int PLUGIN_TYPE_H5_LINK = 3;

    private static UpdateBundleMgr sInstance;

    /**
     * 更新信息
     */
    private List<UpdateEntity> mUpdateEntity;
    /**
     * 更新代理
     */
    private IUpdateProxy mIUpdateProxy;
    /**
     * 提示器参数信息
     */
    private PromptEntity mPromptEntity;

    private Context mContext;

    private PluginBase mPluginH5;
    private PluginBase mPluginNative;

    private IUpdateBundlePrompter mUpdatePrompter;

    public static UpdateBundleMgr get() {
        if (sInstance == null) {
            synchronized (UpdateBundleMgr.class) {
                if (sInstance == null) {
                    sInstance = new UpdateBundleMgr();
                }
            }
        }
        return sInstance;
    }

    public UpdateBundleMgr() {

    }

    public void init(Context context) {
        mContext = context;
        mPluginH5 = new PluginH5(context);
        mPluginNative = new PluginNative(context);
    }

    /**
     * 获取更新提示
     *
     * @param updateEntity 更新信息
     * @param promptEntity 提示器参数信息
     * @return
     */
    public void setPluginsUpdateInfo(@NonNull List<UpdateEntity> updateEntity, PromptEntity promptEntity) {
        setPromptEntity(promptEntity);
        setUpdateEntity(updateEntity);
    }

    public UpdateBundleMgr setIUpdateProxy(IUpdateProxy updateProxy) {
        this.mIUpdateProxy = updateProxy;
        return this;
    }

    public UpdateBundleMgr setUpdatePrompter(IUpdateBundlePrompter updatePrompter) {
        this.mUpdatePrompter = updatePrompter;
        return this;
    }

    public UpdateBundleMgr setUpdateEntity(List<UpdateEntity> updateEntity) {
        this.mUpdateEntity = updateEntity;
        return this;
    }

    public void setPromptEntity(PromptEntity promptEntity) {
        this.mPromptEntity = promptEntity;
    }

    public PluginBase getPluginH5() {
        return mPluginH5;
    }

    public PluginBase getPluginNative() {
        return mPluginNative;
    }

    public PluginEntity installH5(String alias, String path){
        return mPluginH5.install(alias, path);
    }

    public PluginEntity installNative(String alias, String path){
        return mPluginNative.install(alias, path);
    }

    public boolean isInstalledH5(String alias) {
        return mPluginH5.isPluginInstalled(alias);
    }

    public boolean isInstalledNative(String alias) {
        return mPluginNative.isPluginInstalled(alias);
    }

    public PluginEntity getPluginInfo(String name){
        PluginEntity pluginInfo = mPluginNative.getPluginInfo(name);
        if (null == pluginInfo) {
            pluginInfo = mPluginH5.getPluginInfo(name);
        }
        return pluginInfo;
    }

    public boolean canOpenBundle(String alias) {
        return canOpen(alias);
    }

    public boolean canOpen(String alias) {
        UpdateEntity newUpdate = getNewVersionInfoByAlias(alias);
        return canOpen(alias, newUpdate);
    }

    public boolean canOpen(String alias, UpdateEntity newUpdate) {
        String bundleStatus = getBundleStatus(alias, newUpdate);
        if (BUNDLE_STATUS_DEFAULT.equals(bundleStatus)) {
            return true;
        }

        if (BUNDLE_STATUS_INSTALLED_NO_VERSION.equals(bundleStatus)) {
            return true;
        }

        if (BUNDLE_STATUS_NOT_INSTALL_NO_VERSION.equals(bundleStatus)) {
            return false;
        }

        if (TextUtils.isEmpty(newUpdate.getApkCacheDir())) {
            if (newUpdate.getType() == PLUGIN_TYPE_NATIVE_H5) {
                newUpdate.setApkCacheDir(mPluginH5.getRootPath());
            } else {
                newUpdate.setApkCacheDir(mPluginNative.getRootPath());
            }
        }
        mIUpdateProxy.setUpdateEntity(newUpdate);
        if (BUNDLE_STATUS_NOT_INSTALL.equals(bundleStatus)) {
            mIUpdateProxy.startDownload(newUpdate, mOnFileDownloadListener);
        } else if (BUNDLE_STATUS_HAS_NEW_VERSION.equals(bundleStatus)) {
            mIUpdateProxy.startDownload(newUpdate, mOnFileDownloadListener);
        }
        return false;
    }

    public void updateBundlesVersion(UpdateEntity entity, OnFileDownloadListener listener) {
        if (entity.getType() == PLUGIN_TYPE_NATIVE) {
            entity.setApkCacheDir(mPluginNative.getRootPath());
        } else if (entity.getType() == PLUGIN_TYPE_NATIVE_H5) {
            entity.setApkCacheDir(mPluginH5.getRootPath());
        }
        mIUpdateProxy.setUpdateEntity(entity);
        mIUpdateProxy.startDownload(entity, listener);
    }

    /**
     * 直接下载安装插件
     */
    public void updateBundlesVersion(final UpdateEntity entity) {
        updateBundlesVersion(entity, mOnFileDownloadListener);
    }

    public String getBundleStatus(String alias, UpdateEntity newUpdate) {
        PluginEntity localUpdate = null;
        if (newUpdate.getType() == PLUGIN_TYPE_NATIVE_H5) {
            localUpdate = mPluginH5.getPluginInfo(alias);
        } else {
            if (mPluginNative.isPluginInstalled(alias)) {
                localUpdate = mPluginNative.getPluginInfo(alias);
            }
        }

        if (null == localUpdate && null == newUpdate) {
            return BUNDLE_STATUS_NOT_INSTALL_NO_VERSION;
        }

        if (null == localUpdate) {
            return BUNDLE_STATUS_NOT_INSTALL;
        }

        if (null == newUpdate) {
            return BUNDLE_STATUS_INSTALLED_NO_VERSION;
        }

        int localVersionCode = localUpdate.getVersionCode();
        int newVersionCode = newUpdate.getVersionCode();
        if (localVersionCode < newVersionCode) {
            return BUNDLE_STATUS_HAS_NEW_VERSION;
        }
        return BUNDLE_STATUS_DEFAULT;
    }

    public UpdateEntity getNewVersionInfoByAlias(String alias) {
        if (null == mUpdateEntity) {
            return null;
        }
        for (int i = 0; i < mUpdateEntity.size(); i++) {
            if (mUpdateEntity.get(i).getAlias().equals(alias)) {
                return mUpdateEntity.get(i);
            }
        }
        return null;
    }

    /**
     * 文件下载监听
     */
    private OnFileDownloadListener mOnFileDownloadListener = new OnFileDownloadListener() {

        @Override
        public void onStart() {
            if (null != mUpdatePrompter) {
                mUpdatePrompter.beforDownloadStart(mIUpdateProxy.getUpdateEntity());
            }
        }

        @Override
        public void onProgress(long total, float progress) {
            if (null != mUpdatePrompter) {
                mUpdatePrompter.downloadProgress(mIUpdateProxy.getUpdateEntity(), progress);
            }
        }

        @Override
        public boolean onCompleted(File file) {
            //返回true，自动进行apk安装
            UpdateFacade.startInstallBundle(mIUpdateProxy.getContext(), mIUpdateProxy.getUpdateEntity(), file);
            if (null != mUpdatePrompter) {
                mUpdatePrompter.installCompleted(mIUpdateProxy.getUpdateEntity());
            }
            return false;
        }

        @Override
        public void onError(Throwable throwable) {
            if (null != mUpdatePrompter) {
                mUpdatePrompter.downloadError(mIUpdateProxy.getUpdateEntity(), throwable);
            }
        }
    };
}
