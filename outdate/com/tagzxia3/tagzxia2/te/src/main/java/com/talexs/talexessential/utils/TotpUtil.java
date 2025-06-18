package com.tagzxia3.tagzxia2.te.src.main.java.com.talexs.talexessential.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * 实现 RFC6238 中描述的 TOTP <br>
 * Time-Based One-Time Password 基于时间的一次性密码 <br>
 * 参考 <br>
 * 1. https://datatracker.ietf.org/doc/html/rfc6238 <br>
 * 2. https://www.aqniu.com/tools-tech/4671.html
 */
@SuppressWarnings("unused")
public class TotpUtil {
    public static String totp(byte[] secret, String algorithm, int distance, long offset) {
        // (当前时间 + 时间偏移量) / 时间片间隔
        long time = ((System.currentTimeMillis()) / 1000) / distance;
        byte[] data = sha1(longToByte(time), secret, algorithm);
        int index = Objects.requireNonNull(data)[19] & 0xf;
        int wip = byteToInt(data, index) & 0x7fffffff;
        return padding(wip);
    }

    /**
     * SHA1 计算
     *
     * @param source 数据源
     * @param key    密钥
     * @return String 计算结果
     */
    private static byte[] sha1(byte[] source, byte[] key, String algorithm) {
        try {
            Mac mac = Mac.getInstance(algorithm);
            SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
            mac.init(keySpec);
            return mac.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * long 转换为 byte
     *
     * @param time long 数据
     * @return byte 数组
     */
    private static byte[] longToByte(long time) {
        byte[] bytes = new byte[8];
        for (int count = 0; count < 8; count++) {
            int offset = 64 - (count + 1) * 8;
            bytes[count] = (byte) ((time >> offset) & 0xff);
        }
        return bytes;
    }

    /**
     * byte 转换 int
     *
     * @param bytes byte 数组
     * @param start 开始位移
     * @return int
     */
    private static int byteToInt(byte[] bytes, int start) {
        DataInput input = new DataInputStream(new ByteArrayInputStream(bytes, start, bytes.length - start));
        int temp;
        try {
            temp = input.readInt();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return temp;
    }

    /**
     * 高位补0
     *
     * @param wip 已经处理好的验证码
     * @return 最终结果
     */
    private static String padding(int wip) {
        StringBuilder code = new StringBuilder(String.valueOf(wip % 1000000));
        for (; ; ) {
            if (code.length() < 6) {
                code.insert(0, "0");
            } else {
                return code.toString();
            }
        }
    }
}