

package com.foodsecurity.xupdate.proxy.impl;

import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;

import static com.foodsecurity.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

/**
 * 简单的版本更新代理
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午9:47
 */
public abstract class AbstractUpdateProxy implements IUpdateProxy {

    @Override
    public void onBeforeCheck() {

    }

    @Override
    public void onAfterCheck() {

    }

    @Override
    public void noNewVersion(@NonNull Throwable throwable) {
        UpdateFacade.onUpdateError(CHECK_NO_NEW_VERSION, throwable.getMessage());
    }

}
