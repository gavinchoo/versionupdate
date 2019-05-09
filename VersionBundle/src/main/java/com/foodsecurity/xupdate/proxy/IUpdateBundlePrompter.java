

package com.foodsecurity.xupdate.proxy;

import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;

import java.io.File;
import java.util.List;

/**
 * 版本更新提示器
 *
 * @author zhujianwei134
 * @since 2018/6/29 下午8:35
 */
public interface IUpdateBundlePrompter {

    /**
     * 显示插件版本更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param promptEntity 提示界面参数
     */
    void showBundlePrompt(@NonNull List<UpdateEntity> updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity);


    /**
     * 插件下载之前
     * @param updateEntity
     */
    void onStart(UpdateEntity updateEntity);

    /**
     * 插件下载更新进度
     *
     * @param updateEntity
     * @param progress 进度0.00 - 0.50  - 1.00
     */
    void onProgress(UpdateEntity updateEntity, float progress);

    /**
     * 下载安装完毕
     *
     * @param updateEntity
     * @return 下载完毕后是否打开文件进行安装<br>{@code true} ：安装<br>{@code false} ：不安装
     */
    void onCompleted(UpdateEntity updateEntity);

    /**
     * 错误回调
     *
     * @param updateEntity
     * @param throwable 错误提示
     */
    void onError(UpdateEntity updateEntity, Throwable throwable);
}
