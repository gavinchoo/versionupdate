

package com.foodsecurity.xupdate.proxy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;

import java.util.List;

/**
 * 版本更新代理
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午9:45
 */
public interface IUpdateProxy {

    /**
     * 获取上下文
     *
     * @return
     */
    Context getContext();

    /**
     * 获取版本更新网络请求服务API
     *
     * @return
     */
    IUpdateHttpService getIUpdateHttpService();

    /**
     * 开始版本更新
     */
    void update();

    /**
     * 开始插件版本更新
     */
    void updateBundle();

    /**
     * 版本检查之前
     */
    void onBeforeCheck();

    /**
     * 执行网络请求，检查应用的版本信息
     */
    void checkVersion();

    /**
     * 版本检查之后
     */
    void onAfterCheck();

    /**
     * 设置更新实体
     * @param entity
     */
    void setUpdateEntity(UpdateEntity entity);

    /**
     * 获取更新实体
     * @return
     */
    UpdateEntity getUpdateEntity();

    /**
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @return
     * @throws Exception
     */
    UpdateEntity parseJson(@NonNull String json) throws Exception;

    /**
     * 将请求的json结果解析为版本更新信息实体
     *
     * @param json
     * @return
     * @throws Exception
     */
    List<UpdateEntity> parseBundleJson(@NonNull String json) throws Exception;

    /**
     * 发现新版本
     *
     * @param updateEntity 版本更新信息
     * @param updateProxy  版本更新代理
     */
    void findNewVersion(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy);

    /**
     * 发现新版本
     *
     * @param updateEntity 版本更新信息
     * @param updateProxy  版本更新代理
     */
    void findBundleNewVersion(@NonNull List<UpdateEntity> updateEntity, @NonNull IUpdateProxy updateProxy);

    /**
     * 未发现新版本
     *
     * @param throwable 未发现的原因
     */
    void noNewVersion(@NonNull Throwable throwable);

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 后台下载
     */
    void backgroundDownload();

    /**
     * 取消下载
     */
    void cancelDownload();

}
