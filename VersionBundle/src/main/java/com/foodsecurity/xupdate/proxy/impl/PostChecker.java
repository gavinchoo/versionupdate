

package com.foodsecurity.xupdate.proxy.impl;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.ApkVersionResult;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.exception.UpdateException;
import com.foodsecurity.xupdate.proxy.IUpdateChecker;
import com.foodsecurity.xupdate.proxy.IUpdateHttpService;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.service.DownloadService;
import com.foodsecurity.xupdate.utils.UpdateUtils;

import java.util.List;
import java.util.Map;

import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_JSON_EMPTY;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_NET_REQUEST;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_NO_NEW_VERSION;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_PARSE;
import static com.foodsecurity.xupdate.exception.UpdateException.Error.CHECK_UPDATING;

/**
 * 默认版本更新检查者
 *
 * @author zhujianwei134
 * @since 2018/7/2 下午10:21
 */
public class PostChecker implements IUpdateChecker {

    @Override
    public void onBeforeCheck() {

    }

    @Override
    public void check(boolean isGet, @NonNull String url, @NonNull Map<String, String> params, final @NonNull IUpdateProxy updateProxy) {
        if (DownloadService.isRunning() || UpdateFacade.isShowUpdatePrompter()) {
            updateProxy.onAfterCheck();
            UpdateFacade.onUpdateError(CHECK_UPDATING);
            return;
        }

        if (isGet) {
            updateProxy.getIUpdateHttpService().asyncGet(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(updateProxy, error);
                }
            });
        } else {
            updateProxy.getIUpdateHttpService().asyncPost(url, params, new IUpdateHttpService.Callback() {
                @Override
                public void onSuccess(String result) {
                    onCheckSuccess(result, updateProxy);
                }

                @Override
                public void onError(Throwable error) {
                    onCheckError(updateProxy, error);
                }
            });
        }
    }

    @Override
    public void onAfterCheck() {

    }

    /**
     * 查询成功
     *
     * @param result
     * @param updateProxy
     */
    private void onCheckSuccess(String result, @NonNull IUpdateProxy updateProxy) {
        updateProxy.onAfterCheck();
        if (!TextUtils.isEmpty(result)) {
            processCheckResult(result, updateProxy);
        } else {
            UpdateFacade.onUpdateError(CHECK_JSON_EMPTY);
        }
    }

    /**
     * 查询失败
     *
     * @param updateProxy
     * @param error
     */
    private void onCheckError(@NonNull IUpdateProxy updateProxy, Throwable error) {
        updateProxy.onAfterCheck();

        if (error instanceof UpdateException) {
            UpdateFacade.onUpdateError((UpdateException) error);
        } else {
            UpdateFacade.onUpdateError(CHECK_NET_REQUEST, error.getMessage());
        }
    }

    @Override
    public void processCheckResult(@NonNull String result, @NonNull IUpdateProxy updateProxy) {

    }
}
