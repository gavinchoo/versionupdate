package com.android.components.bundle.version;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.foodsecurity.xupdate.UpdateFacade;
import com.foodsecurity.xupdate.entity.PluginEntity;
import com.foodsecurity.xupdate.widget.UpdateBundleMgr;
import com.pingan.foodsecurity.bundle.version.R;

import java.io.File;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String bundleName = getIntent().getStringExtra("bundle");
        int type = getIntent().getIntExtra("type", 0);
        PluginEntity updateEntity = UpdateBundleMgr.get().getPluginInfo(bundleName);
        WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        if (type == UpdateFacade.PLUGIN_TYPE_NATIVE_H5) {
            webView.loadUrl("file:" + getFilesDir() + File.separator + "jsbundles/"
                    + updateEntity.getAlias() + "_" + updateEntity.getVersionName() + "/index.html#/");
        } else if (type == UpdateFacade.PLUGIN_TYPE_H5_LINK) {
            webView.loadUrl("file:///android_asset/index.html#/");
        }
    }
}
