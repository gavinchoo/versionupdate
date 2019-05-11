

package com.foodsecurity.xupdate.http;

import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * XHttp2实现的请求更新
 *
 * @author zhujianwei134
 * @since 2018/8/12 上午11:46
 */
public class DefaultUpdateServiceImpl implements IUpdateHttpService {

    private OkhttpClient okhttpClient;

    public DefaultUpdateServiceImpl() {
        okhttpClient = new OkhttpClient();
    }

    @Override
    public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {

        okhttpClient.get(url, params, new OkhttpClient.SimpleCallBack() {
            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    @Override
    public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
        okhttpClient.post(url, params, new OkhttpClient.SimpleCallBack() {
            @Override
            public void onSuccess(String response) {
                callBack.onSuccess(response);
            }

            @Override
            public void onError(Exception e) {
                callBack.onError(e);
            }
        });
    }

    @Override
    public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, long fileSize, @NonNull final DownloadCallback callback) {
        okhttpClient.download(url, path, fileName, fileSize, new OkhttpClient.DownloadProgressCallBack() {
            @Override
            public void onStart() {
                callback.onStart();
            }

            @Override
            public void onError(Exception e) {
                callback.onError(e);
            }

            @Override
            public void update(long totalSize, int progress) {
                UpdateLog.i("开始下载更新文件,下载进度: " + progress);
                callback.onProgress(totalSize, progress);
            }

            @Override
            public void onComplete(File downloadFile) {
                UpdateLog.i("更新文件下载完成");
                callback.onSuccess(downloadFile);
            }
        });
    }

    @Override
    public void cancelDownload(@NonNull String url) {
        okhttpClient.cancelRequest();
    }
}
