

package com.foodsecurity.xupdate.proxy.impl;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdatePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.widget.UpdateDialog;
import com.foodsecurity.xupdate.widget.UpdateDialogFragment;

import java.util.List;

/**
 * 默认的更新提示器
 *
 * @author zhujianwei134
 * @since 2018/7/2 下午4:05
 */
public class DefaultUpdatePrompter implements IUpdatePrompter {

    private FragmentManager mFragmentManager;

    /**
     * 使用默认Dialog
     */
    public DefaultUpdatePrompter() {
    }

    /**
     * 使用FragmentDialog
     *
     * @param manager
     */
    public DefaultUpdatePrompter(@NonNull FragmentManager manager) {
        mFragmentManager = manager;
    }

    /**
     * 显示版本更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param promptEntity 提示界面参数
     */
    @Override
    public void showPrompt(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
        if (mFragmentManager != null) {
            UpdateDialogFragment.newInstance(updateEntity, updateProxy, promptEntity)
                    .show(mFragmentManager);
        } else {
            UpdateDialog.newInstance(updateEntity, updateProxy, promptEntity)
                    .show();
        }
    }
}
