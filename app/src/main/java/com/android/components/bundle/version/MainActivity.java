package com.android.components.bundle.version;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.XUpdate;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.entity.UpdateError;
import com.foodsecurity.xupdate.listener.OnUpdateFailureListener;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.proxy.impl.BundleUpdateChecker;
import com.foodsecurity.xupdate.service.OnFileDownloadListener;
import com.foodsecurity.xupdate.utils.BundleInstallUtils;
import com.foodsecurity.xupdate.utils.UpdateUtils;
import com.pingan.foodsecurity.bundle.version.R;

import java.io.File;
import java.util.List;

import static com.foodsecurity.xupdate.entity.UpdateError.ERROR.CHECK_NO_NEW_VERSION;

public class MainActivity extends AppCompatActivity {

    public static final String BUNDLE_ALIAS_COMMON = "common";
    public static final String BUNDLE_ALIAS_STATISTICS = "statistics_bundle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUpdate();

        checkBundlesVersion();
//        checkMainAppVersion();

        if (!XUpdate.get().isInstalled(BUNDLE_ALIAS_COMMON)) {
            updateBundlesVersion();
        }

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XUpdate.get()
                        .canOpenBundle(BUNDLE_ALIAS_STATISTICS);
            }
        });

        findViewById(R.id.open_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (XUpdate.get()
                        .canOpenBundle(BUNDLE_ALIAS_STATISTICS)) {
                    Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                    intent.putExtra("bundle", BUNDLE_ALIAS_STATISTICS);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * 检测主程序版本信息
     */
    private void checkMainAppVersion() {
        XUpdate.newBuild(this)
                .param("versionCode", "" + UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .supportBackgroundUpdate(true)
                .updateUrl("http://192.168.1.101:8000/version/queryversion")
                .update();
    }

    /**
     * 检测插件版本信息
     */
    private void checkBundlesVersion() {
        XUpdate.newBuild(this)
                .param("versionCode", "" + UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .param("bundles", "all")
                .updateBundlePrompter(new BundleUpdatePrompter())
                .updateChecker(new BundleUpdateChecker())
                .updateUrl("http://192.168.1.101:8000/version/querybundleversion")
                .updateBundle();
    }

    /**
     * 直接下载安装插件
     */
    private void updateBundlesVersion() {
        String url = "http://192.168.1.101:8000/version/download?filename=common.zip";
        XUpdate.newBuild(this)
                .apkCacheDir(UpdateFacade.getBundlesRootPath())
                .build()
                .download(url, "common.zip", new OnFileDownloadListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onProgress(long total, float progress) {

                    }

                    @Override
                    public boolean onCompleted(File file) {
                        UpdateFacade.startInstallBundle(XUpdate.getContext(), file);
                        return false;
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
    }

    /**
     * 插件检测版本信息，下载更新回调
     */
    public class BundleUpdatePrompter implements IUpdateBundlePrompter {

        public BundleUpdatePrompter() {

        }

        @Override
        public void showBundlePrompt(@NonNull List<UpdateEntity> updateEntity, @NonNull IUpdateProxy updateProxy, @NonNull PromptEntity promptEntity) {
            UpdateLog.i("showBundlePrompt");
        }

        @Override
        public void onStart(UpdateEntity updateEntity) {

        }

        @Override
        public void onProgress(UpdateEntity updateEntity, float progress) {

        }

        @Override
        public void onCompleted(UpdateEntity updateEntity) {
            Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
            intent.putExtra("bundle", updateEntity.getAlias());
            startActivity(intent);
        }

        @Override
        public void onError(UpdateEntity updateEntity, Throwable throwable) {

        }
    }

    /**
     * 初始化版本更新组件
     */
    private void initUpdate() {
        XUpdate.get()
                .debug(true)
                .isWifiOnly(true)     //默认设置只在wifi下检查版本更新
                .isGet(false)          //默认设置使用get请求检查版本
                .isAutoMode(false)    //默认设置非自动模式，可根据具体使用配置
                .param("versionCode", "" + UpdateUtils.getVersionCode(this)) //设置默认公共请求参数
                .param("appKey", getPackageName())
                .setOnUpdateFailureListener(new OnUpdateFailureListener() { //设置版本更新出错的监听
                    @Override
                    public void onFailure(UpdateError error) {
                        if (error.getCode() != CHECK_NO_NEW_VERSION) { //对不同错误进行处理
                            Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .init(this.getApplication());   //这个必须初始化

    }
}
