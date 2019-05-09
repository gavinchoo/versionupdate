

package com.foodsecurity.xupdate.entity;


import android.text.TextUtils;
import android.util.SparseArray;

/**
 * 更新错误
 *
 * @author zhujianwei134
 * @since 2018/6/29 下午9:01
 */
public class UpdateException extends Throwable {

    /**
     * 错误码
     */
    private final int mCode;

    public UpdateException(int code) {
        this(code, null);
    }

    public UpdateException(int code, String message) {
        super(make(code, message));
        mCode = code;
    }

    public UpdateException(Throwable e) {
        super(e);
        mCode = Error.UPDATE_UNKNOWN;
    }

    public int getCode() {
        return mCode;
    }

    @Override
    public String toString() {
        return getMessage();
    }

    private static String make(int code, String message) {
        String m = S_MESSAGES.get(code);
        if (TextUtils.isEmpty(m)) {
            return "";
        }
        if (TextUtils.isEmpty(message)) {
            return m;
        }
        return m + "(" + message + ")";
    }

    /**
     * 获取详细的错误信息
     *
     * @return
     */
    public String getDetailMsg() {
        return "Code:" + mCode + ", msg:" + getMessage();
    }

    /**
     * 版本更新错误码
     */
    public final static class Error {
        /**
         * 查询更新失败
         */
        public static final int CHECK_NET_REQUEST = 2000;
        public static final int CHECK_NO_WIFI = CHECK_NET_REQUEST + 1;
        public static final int CHECK_NO_NETWORK = CHECK_NO_WIFI + 1;
        public static final int CHECK_UPDATING = CHECK_NO_NETWORK + 1;
        public static final int CHECK_NO_NEW_VERSION = CHECK_UPDATING + 1;
        public static final int CHECK_JSON_EMPTY = CHECK_NO_NEW_VERSION + 1;
        public static final int CHECK_PARSE = CHECK_JSON_EMPTY + 1;
        public static final int CHECK_IGNORED_VERSION = CHECK_PARSE + 1;
        public static final int CHECK_APK_CACHE_DIR_EMPTY = CHECK_IGNORED_VERSION + 1;

        public static final int PROMPT_UNKNOWN = 3000;
        public static final int PROMPT_ACTIVITY_DESTROY = PROMPT_UNKNOWN + 1;

        public static final int DOWNLOAD_FAILED = 4000;
        public static final int DOWNLOAD_PERMISSION_DENIED = DOWNLOAD_FAILED + 1;

        /**
         * apk安装错误
         */
        public static final int INSTALL_FAILED = 5000;

        /**
         * 未知的错误
         */
        public static final int UPDATE_UNKNOWN = 5100;
    }

    private static final SparseArray<String> S_MESSAGES = new SparseArray<String>();

    static {
        S_MESSAGES.append(Error.CHECK_NET_REQUEST, "查询失败：网络请求错误");
        S_MESSAGES.append(Error.CHECK_NO_WIFI, "查询失败：没有WIFI");
        S_MESSAGES.append(Error.CHECK_NO_NETWORK, "查询失败：没有网络");
        S_MESSAGES.append(Error.CHECK_UPDATING, "程序正在进行版本更新！");
        S_MESSAGES.append(Error.CHECK_NO_NEW_VERSION, "查询更新：没有新版本");
        S_MESSAGES.append(Error.CHECK_JSON_EMPTY, "查询失败：Json 为空");
        S_MESSAGES.append(Error.CHECK_PARSE, "查询失败：解析Json错误");
        S_MESSAGES.append(Error.CHECK_IGNORED_VERSION, "更新失败：已经被忽略的版本");
        S_MESSAGES.append(Error.CHECK_APK_CACHE_DIR_EMPTY, "更新失败：apk的下载缓存目录为空");

        S_MESSAGES.append(Error.PROMPT_UNKNOWN, "提示失败：未知错误");
        S_MESSAGES.append(Error.PROMPT_ACTIVITY_DESTROY, "提示失败：activity已被销毁");

        S_MESSAGES.append(Error.DOWNLOAD_FAILED, "下载失败");
        S_MESSAGES.append(Error.DOWNLOAD_PERMISSION_DENIED, "无法下载：存储权限申请被拒绝！");


        S_MESSAGES.append(Error.INSTALL_FAILED, "安装APK失败！");
    }
}
