

package com.foodsecurity.xupdate.listener;


import com.foodsecurity.xupdate.entity.UpdateException;

/**
 * 更新失败监听
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午7:43
 */
public interface OnUpdateFailureListener {
    /**
     * 更新失败
     *
     * @param error 错误
     */
    void onFailure(UpdateException error);
}