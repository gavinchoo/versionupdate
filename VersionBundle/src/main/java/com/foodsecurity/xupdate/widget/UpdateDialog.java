

package com.foodsecurity.xupdate.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.foodsecurity.xupdate.R;
import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;
import com.foodsecurity.xupdate.utils.ColorUtils;
import com.foodsecurity.xupdate.utils.DrawableUtils;
import com.foodsecurity.xupdate.utils.UpdateUtils;

import java.io.File;

import static com.foodsecurity.xupdate.widget.UpdateDialogFragment.REQUEST_CODE_REQUEST_PERMISSIONS;

/**
 * 版本更新弹窗
 *
 * @author zhujianwei134
 * @since 2018/7/24 上午9:29
 */
public class UpdateDialog extends BaseDialog implements View.OnClickListener {

    //======顶部========//
    /**
     * 顶部图片
     */
    private ImageView mIvTop;
    /**
     * 标题
     */
    private TextView mTvTitle;
    //======更新内容========//
    /**
     * 版本更新内容
     */
    private TextView mTvUpdateInfo;
    /**
     * 版本更新
     */
    private Button mBtnUpdate;
    /**
     * 后台更新
     */
    private Button mBtnBackgroundUpdate;
    /**
     * 忽略版本
     */
    private TextView mTvIgnore;
    /**
     * 进度条
     */
    private NumberProgressBar mNumberProgressBar;
    //======底部========//
    /**
     * 底部关闭
     */
    private Button mIvClose;

    //======更新信息========//
    /**
     * 更新信息
     */
    private UpdateEntity mUpdateEntity;
    /**
     * 更新代理
     */
    private IUpdateProxy mIUpdateProxy;
    /**
     * 提示器参数信息
     */
    private PromptEntity mPromptEntity;

    /**
     * 获取更新提示
     *
     * @param updateEntity 更新信息
     * @param updateProxy  更新代理
     * @param promptEntity 提示器参数信息
     * @return
     */
    public static UpdateDialog newInstance(@NonNull UpdateEntity updateEntity, @NonNull IUpdateProxy updateProxy, PromptEntity promptEntity) {
        UpdateDialog dialog = new UpdateDialog(updateProxy.getContext());
        dialog.setIUpdateProxy(updateProxy)
                .setUpdateEntity(updateEntity)
                .setPromptEntity(promptEntity);
        dialog.initTheme(promptEntity.getThemeColor(), promptEntity.getTopResId());
        return dialog;
    }

    private UpdateDialog(Context context) {
        super(context, R.layout.xupdate_dialog_app);
    }

    public UpdateDialog setPromptEntity(PromptEntity promptEntity) {
        mPromptEntity = promptEntity;
        return this;
    }

    @Override
    protected void initViews() {
        //顶部图片
        mIvTop = findViewById(R.id.iv_top);
        //标题
        mTvTitle = findViewById(R.id.tv_title);
        //提示内容
        mTvUpdateInfo = findViewById(R.id.tv_update_info);
        //更新按钮
        mBtnUpdate = findViewById(R.id.btn_update);
        //后台更新按钮
        mBtnBackgroundUpdate = findViewById(R.id.btn_background_update);
        //忽略
        mTvIgnore = findViewById(R.id.tv_ignore);
        //进度条
        mNumberProgressBar = findViewById(R.id.npb_progress);

        //关闭按钮
        mIvClose = findViewById(R.id.iv_close);
    }

    @Override
    protected void initListeners() {
        mBtnUpdate.setOnClickListener(this);
        mBtnBackgroundUpdate.setOnClickListener(this);
        mIvClose.setOnClickListener(this);
        mTvIgnore.setOnClickListener(this);

        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    //====================生命周期============================//

    @Override
    public void show() {
        UpdateFacade.setIsShowUpdatePrompter(true);
        super.show();
    }

    @Override
    public void dismiss() {
        UpdateFacade.setIsShowUpdatePrompter(false);
        super.dismiss();
    }

    //====================UI构建============================//

    public UpdateDialog setUpdateEntity(UpdateEntity updateEntity) {
        mUpdateEntity = updateEntity;
        initUpdateInfo(mUpdateEntity);
        return this;
    }

    /**
     * 初始化更新信息
     *
     * @param updateEntity
     */
    private void initUpdateInfo(UpdateEntity updateEntity) {
        // 弹出对话框
        final String newVersion = updateEntity.getVersionName();
        // 更新内容
        mTvUpdateInfo.setText(updateEntity.getUpdateContent());
        mBtnUpdate.setVisibility(View.VISIBLE);
        mNumberProgressBar.setVisibility(View.GONE);
        mNumberProgressBar.setProgress(0);
        mIvClose.setVisibility(View.VISIBLE);

        //强制更新,不显示关闭按钮
        if (updateEntity.isForce()) {
            mTvTitle.setText(String.format(getString(R.string.xupdate_lab_ready_force_update), newVersion));
            mIvClose.setVisibility(View.GONE);
        } else {
            mTvTitle.setText(String.format(getString(R.string.xupdate_lab_ready_update), newVersion));
            //不是强制更新时，才生效
            if (updateEntity.isIgnorable()) {
                mIvClose.setVisibility(View.GONE);
                mTvIgnore.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 初始化主题色
     */
    private void initTheme(@ColorInt int themeColor, @DrawableRes int topResId) {
        if (themeColor == -1) {
            themeColor = ColorUtils.getColor(getContext(), R.color.xupdate_default_theme_color);
        }
        setDialogTheme(themeColor, topResId);
    }

    /**
     * 设置
     *
     * @param color    主色
     * @param topResId 图片
     */
    private void setDialogTheme(int color, int topResId) {
        mIvTop.setImageResource(topResId);
        mNumberProgressBar.setProgressTextColor(color);
        mNumberProgressBar.setReachedBarColor(color);
        //随背景颜色变化
        mBtnUpdate.setTextColor(color);
    }

    //====================更新功能============================//

    public UpdateDialog setIUpdateProxy(IUpdateProxy updateProxy) {
        mIUpdateProxy = updateProxy;
        return this;
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        //点击版本升级按钮【下载apk】
        if (i == R.id.btn_update) {
            //权限判断是否有访问外部存储空间权限
            int flag = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (flag != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) mIUpdateProxy.getContext(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_REQUEST_PERMISSIONS);
            } else {
                mTvIgnore.setVisibility(View.GONE);
                mIvClose.setVisibility(View.GONE);
                installApp();
            }
        } else if (i == R.id.btn_background_update) {
            //点击后台更新按钮
            mIUpdateProxy.backgroundDownload();
            dismiss();
        } else if (i == R.id.iv_close) {
            //点击关闭按钮
            mIUpdateProxy.cancelDownload();
            dismiss();
        } else if (i == R.id.tv_ignore) {
            //点击忽略按钮
            UpdateUtils.saveIgnoreVersion(getContext(), mUpdateEntity.getVersionName());
            dismiss();
        }
    }

    private void installApp() {
        if (UpdateUtils.isApkDownloaded(mUpdateEntity)) {
            onInstallApk();
            //安装完自杀
            //如果上次是强制更新，但是用户在下载完，强制杀掉后台，重新启动app后，则会走到这一步，所以要进行强制更新的判断。
            if (!mUpdateEntity.isForce()) {
                dismiss();
            } else {
                showInstallButton(UpdateUtils.getApkFileByUpdateEntity(mUpdateEntity));
            }
        } else {
            mIUpdateProxy.startDownload(mUpdateEntity, mOnFileDownloadListener);
        }
    }

    /**
     * 文件下载监听
     */
    private OnFileDownloadListener mOnFileDownloadListener = new OnFileDownloadListener() {
        @Override
        public void onStart() {
            if (isShowing()) {
                mTvTitle.setText(R.string.xupdate_downloading);
                mNumberProgressBar.setVisibility(View.VISIBLE);
                mBtnUpdate.setVisibility(View.GONE);
                if (mPromptEntity.isSupportBackgroundUpdate()
                        && !mUpdateEntity.isForce()) {
                    mBtnBackgroundUpdate.setVisibility(View.VISIBLE);
                } else {
                    mBtnBackgroundUpdate.setVisibility(View.GONE);
                }
            }
        }

        @Override
        public void onProgress(long total, float progress) {
            if (isShowing()) {
                mNumberProgressBar.setProgress(Math.round(progress));
                mNumberProgressBar.setMax(100);
            }
        }

        @Override
        public boolean onCompleted(File file) {
            if (isShowing()) {
                mBtnBackgroundUpdate.setVisibility(View.GONE);
                if (mUpdateEntity.isForce()) {
                    showInstallButton(file);
                } else {
                    dismiss();
                }
            }
            //返回true，自动进行apk安装
            return true;
        }

        @Override
        public void onError(Throwable throwable) {
            if (null != mUpdateEntity && mUpdateEntity.isForce()) {
                initUpdateInfo(mUpdateEntity);
            } else {
                if (isShowing()) {
                    dismiss();
                }
            }
        }
    };

    /**
     * 显示安装的按钮
     */
    private void showInstallButton(final File apkFile) {
        mNumberProgressBar.setVisibility(View.GONE);
        mBtnUpdate.setText(R.string.xupdate_lab_install);
        mBtnUpdate.setVisibility(View.VISIBLE);
        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInstallApk(apkFile);
            }
        });
    }

    private void onInstallApk() {
        UpdateFacade.startInstallApk(getContext(), UpdateUtils.getApkFileByUpdateEntity(mUpdateEntity), mUpdateEntity.getDownLoadEntity());
    }

    private void onInstallApk(File apkFile) {
        UpdateFacade.startInstallApk(getContext(), apkFile, mUpdateEntity.getDownLoadEntity());
    }

}
