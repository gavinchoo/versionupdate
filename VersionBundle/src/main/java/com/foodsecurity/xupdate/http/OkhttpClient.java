package com.foodsecurity.xupdate.http;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author zhujianwei
 * @date 2019/4/27 19:59
 */
public class OkhttpClient {

    private final static int DOWNLOAD_STATUS_START = 100;
    private final static int DOWNLOAD_STATUS_COMPLETE = 101;
    private final static int DOWNLOAD_STATUS_PROGRESS = 102;
    private final static int DOWNLOAD_STATUS_FAIL = 103;


    private OkHttpClient okHttpClient;
    private DownloadProgressCallBack progressCallBack;
    private File downloadFile;
    private Call call;

    public OkhttpClient() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager}, null);
            okHttpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(25, TimeUnit.SECONDS)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void delFile(String fileName) {
        File file = new File(fileName);
        if (file.isFile()) {
            file.delete();
        }
    }


    public void get(String url, @NonNull Map<String, String> params, final SimpleCallBack callBack) {
        Request.Builder reqBuild = new Request.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url)
                .newBuilder();

        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
            }
        }
        reqBuild.url(urlBuilder.build());
        Request request = reqBuild.build();

        call = okHttpClient.newCall(request);
        //异步请求
        call.enqueue(new OkHttpCallack(callBack));
    }

    public void post(@NonNull String url, @NonNull Map<String, String> params, final SimpleCallBack callBack) {
        FormBody.Builder builder = new FormBody.Builder();
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = builder.build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        call = okHttpClient.newCall(request);

        //异步请求
        call.enqueue(new OkHttpCallack(callBack));
    }

    private class OkHttpCallack implements Callback {

        SimpleCallBack callBack;

        public OkHttpCallack(SimpleCallBack callBack) {
            this.callBack = callBack;
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            mHander.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onError(e);
                }
            });
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            final String body = response.body().string();
            mHander.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onSuccess(body);
                }
            });
        }
    }


    public void cancelRequest() {
        call.cancel();
    }

    public void download(final String url, final String destFileDir,
                         final String destFileName, final long fileSize, DownloadProgressCallBack callBack) {

        delFile(destFileDir + File.separator + destFileName);

        this.progressCallBack = callBack;

        Request request = new Request.Builder()
                .url(url)
                .build();
        call = okHttpClient.newCall(request);
        //异步请求
        call.enqueue(new DownloadCallback(destFileDir, destFileName, fileSize));
    }

    private class DownloadCallback implements Callback {

        private String destFileDir;
        private String destFileName;
        private long fileSize;

        public DownloadCallback(String destFileDir, String destFileName, long fileSize) {
            this.destFileDir = destFileDir;
            this.destFileName = destFileName;
            this.fileSize = fileSize;

            mHander.sendEmptyMessage(DOWNLOAD_STATUS_START);
        }

        @Override
        public void onFailure(Call call, final IOException e) {
            // 下载失败监听回调
            if (e != null) {
                e.printStackTrace();
            }
            handException(e);
        }

        @Override
        public void onResponse(Call call, Response response) {

            InputStream is = null;
            byte[] buf = new byte[2048];
            int len = 0;
            FileOutputStream fos = null;

            //储存下载文件的目录
            File dir = createDownloadDirs(destFileDir);

            downloadFile = new File(dir, destFileName);
            try {
                is = response.body().byteStream();
                long totalSize = fileSize;
                long total = response.body().contentLength();
                if (total != 0) {
                    totalSize = total;
                }
                fos = new FileOutputStream(downloadFile);
                long sum = 0;
                int curProgress = 0;
                while ((len = is.read(buf)) != -1) {
                    fos.write(buf, 0, len);
                    sum += len;
                    int progress = (int) (sum * 1.0f / totalSize * 100);

                    if (progress > curProgress) {
                        curProgress = progress;
                        //下载中更新进度条
                        Message msg = new Message();
                        msg.what = DOWNLOAD_STATUS_PROGRESS;
                        msg.arg1 = curProgress;
                        msg.obj = totalSize;
                        mHander.sendMessage(msg);
                    }
                }
                fos.flush();
                //下载完成
                mHander.sendEmptyMessage(DOWNLOAD_STATUS_COMPLETE);
            } catch (Exception e) {
                e.printStackTrace();
                handException(e);
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                } catch (IOException e) {
                }
            }
        }

        private File createDownloadDirs(String dirs) {
            File dir = new File(dirs);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        }

        private void handException(final Exception e) {
            mHander.post(new Runnable() {
                @Override
                public void run() {
                    progressCallBack.onError(e);
                }
            });
        }
    }


    private Handler mHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DOWNLOAD_STATUS_START: {
                    progressCallBack.onStart();
                    break;
                }
                case DOWNLOAD_STATUS_COMPLETE: {
                    progressCallBack.onComplete(downloadFile);
                    break;
                }
                case DOWNLOAD_STATUS_PROGRESS: {
                    progressCallBack.update((long) msg.obj, msg.arg1);
                    break;
                }
                default:
                    break;
            }
        }
    };

    public interface DownloadProgressCallBack {
        /**
         * 开始下载
         */
        void onStart();

        /**
         * 下载异常失败
         *
         * @param e
         */
        void onError(Exception e);

        /**
         * 下载中进度更新
         *
         * @param totalSize
         * @param progress
         */
        void update(long totalSize, int progress);

        /**
         * 文件下载完成
         *
         * @param downloadFile 下载的本地文件
         */
        void onComplete(File downloadFile);
    }

    public interface SimpleCallBack {
        /**
         * 请求异常
         *
         * @param e
         */
        void onError(IOException e);

        /**
         * 请求成功
         *
         * @param response
         */
        void onSuccess(String response);
    }

    private X509TrustManager x509TrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    };
}
