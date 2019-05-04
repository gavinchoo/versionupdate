

package com.foodsecurity.xupdate.proxy;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;

/**
 * 版本更新下载器
 *
 * @author zhujianwei134
 * @since 2018/6/29 下午8:31
 */
public interface IUpdateDownloader {

    /**
     * 开始下载更新
     *
     * @param updateEntity     更新信息
     * @param downloadListener 文件下载监听
     */
    void startDownload(@NonNull UpdateEntity updateEntity, @Nullable OnFileDownloadListener downloadListener);

    /**
     * 取消下载
     */
    void cancelDownload();

    /**
     * 后台下载更新
     */
    void backgroundDownload();
}
