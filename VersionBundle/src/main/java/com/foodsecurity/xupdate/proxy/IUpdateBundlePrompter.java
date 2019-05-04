

package com.foodsecurity.xupdate.proxy;

import android.support.annotation.NonNull;

import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;

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
}
