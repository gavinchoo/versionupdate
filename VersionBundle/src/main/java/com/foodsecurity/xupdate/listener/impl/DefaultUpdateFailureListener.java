

package com.foodsecurity.xupdate.listener.impl;

import com.foodsecurity.xupdate.entity.UpdateException;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.logs.UpdateLog;

/**
 * 默认的更新出错的处理(简单地打印日志）
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午7:48
 */
public class DefaultUpdateFailureListener implements OnUpdateFailureListener {

    @Override
    public void onFailure(UpdateException error) {
        UpdateLog.e(error);
    }
}
