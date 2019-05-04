

package com.foodsecurity.xupdate.proxy.impl;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;

import java.util.List;

/**
 * 默认的插件更新提示器
 *
 * @author zhujianwei134
 * @since 2018/7/2 下午4:05
 */
public class DefaultUpdateBundlePrompter implements IUpdateBundlePrompter {

    private FragmentManager mFragmentManager;

    /**
     * 使用默认Dialog
     */
    public DefaultUpdateBundlePrompter() {
    }

    /**
     * 使用FragmentDialog
     *
     * @param manager
     */
    public DefaultUpdateBundlePrompter(@NonNull FragmentManager manager) {
        mFragmentManager = manager;
    }

    @Override
    public void showBundlePrompt(@NonNull List<UpdateEntity> updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
    }
}
