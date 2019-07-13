package com.android.components.bundle.version;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.foodsecurity.xupdate.Xupdate;
import com.foodsecurity.xupdate.entity.PromptEntity;
import com.foodsecurity.xupdate.entity.UpdateEntity;
import com.foodsecurity.xupdate.logs.UpdateLog;
import com.foodsecurity.xupdate.proxy.IUpdateBundlePrompter;
import com.foodsecurity.xupdate.proxy.IUpdateProxy;
import com.foodsecurity.xupdate.proxy.impl.BundleUpdateChecker;
import com.foodsecurity.xupdate.utils.UpdateUtils;
import com.pingan.foodsecurity.bundle.version.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String BUNDLE_ALIAS_COMMON = "common";
    public static final String BUNDLE_ALIAS_STATISTICS = "statistics_bundle";

    private String baseUrl = "http://192.168.23.173:8000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUpdate();

        checkBundlesVersion();
        checkMainAppVersion();

        if (!Xupdate.get().isInstalled(BUNDLE_ALIAS_COMMON)) {
            UpdateEntity entity = new UpdateEntity();
            entity.setDownloadUrl(baseUrl + "/version/download?filename=common.zip");
            entity.setFileName("common.zip");
            Xupdate.get().updateBundlesVersion(this, entity);
        }

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Xupdate.get()
//                        .canOpenBundle(BUNDLE_ALIAS_STATISTICS);
                startActivity(new Intent(MainActivity.this, com.example.flutterapp.MainActivity.class));
            }
        });

        findViewById(R.id.open_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Xupdate.get()
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
        Xupdate.newBuild(this)
                .param("versionCode", "" + UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .supportBackgroundUpdate(true)
                .updateUrl(baseUrl + "/version/queryversion")
                .update();
    }

    /**
     * 检测插件版本信息
     */
    private void checkBundlesVersion() {
        Xupdate.newBuild(this)
                .param("versionCode", "" + UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .param("bundles", "all")
                .updateBundlePrompter(new BundleUpdatePrompter())
                .updateChecker(new BundleUpdateChecker())
                .updateUrl(baseUrl + "/version/querybundleversion")
                .updateBundle();
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
        Xupdate.get()
                .debug(true)
                .isWifiOnly(true)
                .isGet(false)
                .isAutoMode(false)
                .param("versionCode", "" + UpdateUtils.getVersionCode(this))
                .param("appKey", getPackageName())
                .init(this.getApplication());
    }
}
