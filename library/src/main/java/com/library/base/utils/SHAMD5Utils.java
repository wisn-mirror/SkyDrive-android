package com.library.base.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.CRC32;


public class SHAMD5Utils {
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public static MessageDigest messagedigest = null;


    public static String getSHA1(String path) {
        return getSHAFile(path, "SHA-1");

    }


    /**
     * @param filepath  大文件
     * @param algorithm
     * @return
     */
    private static String getSHAFile(String filepath, String algorithm) {
        FileInputStream in = null;
        FileChannel ch = null;
        try {
            File file = new File(filepath);
            if (!file.exists()) {
                Log.e("SHAMD5Utils", " not exists！");
                return null;
            }
            messagedigest = MessageDigest.getInstance(algorithm);
            in = new FileInputStream(file);
            ch = in.getChannel();
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0,
                    file.length());

            messagedigest.update(byteBuffer);
            return bufferToHex(messagedigest.digest());
        } catch (NoSuchAlgorithmException e) {
            Log.e("SHAMD5Utils", algorithm + " 编码出错！");
        } catch (UnsupportedEncodingException e) {
            Log.e("SHAMD5Utils", "UTF-8 ,SHA编码操作，不支持字符集！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ch != null) {
                    ch.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }
}
