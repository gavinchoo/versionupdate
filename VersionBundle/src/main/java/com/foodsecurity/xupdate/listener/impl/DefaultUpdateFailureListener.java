

package com.foodsecurity.xupdate.listener.impl;

import android.widget.Toast;

import com.foodsecurity.xupdate.Xupdate;
import com.foodsecurity.xupdate.entity.UpdateException;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.logs.UpdateLog;

import static com.foodsecurity.xupdate.entity.UpdateException.Error.CHECK_NO_NEW_VERSION;

/**
 * 默认的更新出错的处理
 *
 * @author zhujianwei134
 * @since 2018/7/1 下午7:48
 */
public class DefaultUpdateFailureListener implements OnUpdateFailureListener {

    @Override
    public void onFailure(UpdateException error) {
        UpdateLog.e(error.getDetailMsg());
        // 不同错误进行处理
        if (error.getCode() != CHECK_NO_NEW_VERSION) {
            Toast.makeText(Xupdate.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
