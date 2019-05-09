

package com.foodsecurity.xupdate.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密工具类
 *
 * @author zhujianwei134
 * @since 2018/7/2 下午3:14
 */
public final class Md5Utils {

    private Md5Utils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static String getFileMD5(File file) {
        if (!file.exists()) {
            return "";
        }
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            FileChannel channel = in.getChannel();
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(buffer);
            return bytes2Hex(md.digest());
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException ignored) {
                }
            }
        }
        return "";
    }

    /**
     * 验证文件是否有效【通过MD5值比较】
     *
     * @param md5  如果md5值为空，则不进行校验，直接为true
     * @param file 需要校验的文件
     * @return 文件是否有效
     */
    public static boolean isFileValid(String md5, File file) {
        return TextUtils.isEmpty(md5) || md5.equalsIgnoreCase(Md5Utils.getFileMD5(file));
    }

    /**
     * 一个byte转为2个hex字符
     *
     * @param src byte数组
     * @return 16进制大写字符串
     */
    private static String bytes2Hex(byte[] src) {
        char[] res = new char[src.length << 1];
        final char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        for (int i = 0, j = 0; i < src.length; i++) {
            res[j++] = hexDigits[src[i] >>> 4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }
        return new String(res);
    }

}
