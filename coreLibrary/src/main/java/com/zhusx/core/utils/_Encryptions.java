package com.zhusx.core.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加密解密工具包
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:31
 */
public class _Encryptions {
    /**
     * 对称加密
     * <p>
     * 1.电码本模式（Electronic Codebook Book (ECB)）;
     * 2.密码分组链接模式（Cipher Block Chaining (CBC)）;
     * 3.计算器模式（Counter (CTR)）;
     * 4.密码反馈模式（Cipher FeedBack (CFB)）;
     * 5.输出反馈模式（Output FeedBack (OFB)）;
     * <p>
     * 填充模式
     * 1. PKCS5Padding
     * 2. NoPadding         不填充
     * 3. ISO10126Padding
     * <p>
     * 算法/模式/填充                16字节加密后数据长度        不满16字节加密后长度
     * AES/CBC/NoPadding             16                          不支持
     * AES/CBC/PKCS5Padding          32                          16
     * AES/CBC/ISO10126Padding       32                          16
     * AES/CFB/NoPadding             16                          原始数据长度
     * AES/CFB/PKCS5Padding          32                          16
     * AES/CFB/ISO10126Padding       32                          16
     * AES/ECB/NoPadding             16                          不支持
     * AES/ECB/PKCS5Padding          32                          16
     * AES/ECB/ISO10126Padding       32                          16
     * AES/OFB/NoPadding             16                          原始数据长度
     * AES/OFB/PKCS5Padding          32                          16
     * AES/OFB/ISO10126Padding       32                          16
     * AES/PCBC/NoPadding            16                          不支持
     * AES/PCBC/PKCS5Padding         32                          16
     * AES/PCBC/ISO10126Padding      32                          16
     */
    public enum Symmetry {
        DES, // key 必须大于8位
        DES_CBC_PKCS5, // key 必须大于8位
        AES_128, // key 长度无限制
        AES_192, // 可能不支持
        AES_256, // 可能不支持
        AES_ECB_PKCS5 // key 必须大于16位
    }

    /**
     * 摘要
     */
    public enum Digest {
        MD5, SHA
    }

    public static String decodeHex(Symmetry type, String key, String message) {
        try {
            return new String(decode(type, key, hex2byte(message.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeHex(Digest type, String message) {
        try {
            return byte2hex(encode(type, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBase64(Symmetry type, String key, String message) {
        try {
            return _Base64.encode(encode(type, key, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decodeBase64(Symmetry type, String key, String message) {
        try {
            return new String(decode(type, key, _Base64.decode(message)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeHex(Symmetry type, String key, String message) {
        try {
            return byte2hex(encode(type, key, message));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encode(Digest type, String data)
            throws NoSuchAlgorithmException {
        MessageDigest digest;
        switch (type) {
            case MD5:
                digest = MessageDigest.getInstance("MD5");
                digest.update(data.getBytes());
                return digest.digest();
            case SHA:
                digest = MessageDigest.getInstance("SHA");
                digest.update(data.getBytes());
                return digest.digest();
            default:
                break;
        }
        return null;
    }

    private static byte[] encode(Symmetry type, String key, String data) throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher;
        switch (type) {
            case DES:
                cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes())), new SecureRandom());
                return cipher.doFinal(data.getBytes());
            case DES_CBC_PKCS5:
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes())), new IvParameterSpec(key.getBytes()/*Iv 随便16位的数*/));
                return cipher.doFinal(data.getBytes());
            case AES_128:
            case AES_192:
            case AES_256:
                KeyGenerator kGen = KeyGenerator.getInstance("AES");
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(key.getBytes());
                switch (type) {
                    case AES_128:
                        kGen.init(128, sr);
                        break;
                    case AES_192:
                        kGen.init(192, sr);
                        break;
                    case AES_256:
                        kGen.init(256, sr);
                        break;
                    default:
                }
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kGen.generateKey().getEncoded(), "AES"));
                return cipher.doFinal(data.getBytes());
            case AES_ECB_PKCS5:
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
                return cipher.doFinal(data.getBytes());
            default:
                break;
        }
        return null;
    }

    private static byte[] decode(Symmetry type, String key, byte[] data)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher;
        switch (type) {
            case DES:
                cipher = Cipher.getInstance("DES");
                cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes())), new SecureRandom());
                return cipher.doFinal(data);
            case DES_CBC_PKCS5:
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes())), new IvParameterSpec(key.getBytes()));
                return cipher.doFinal(data);
            case AES_128:
            case AES_192:
            case AES_256:
                KeyGenerator kGen = KeyGenerator.getInstance("AES");// //实例化一个用AES加密算法的密钥生成器
                SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                sr.setSeed(key.getBytes());
                switch (type) {
                    case AES_128:
                        kGen.init(128, sr);
                        break;
                    case AES_192:
                        kGen.init(192, sr);
                        break;
                    case AES_256:
                        kGen.init(256, sr);
                        break;
                    default:
                }
                cipher = Cipher.getInstance("AES");// 创建密码器
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(kGen.generateKey().getEncoded()/*生成一个密钥 返回基本编码格式的密钥*/, "AES"));
                return cipher.doFinal(data);
            case AES_ECB_PKCS5:
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(), "AES"));
                return cipher.doFinal(data);
            default:
                break;
        }
        return null;
    }

    /**
     * 二行制转字符串
     *
     * @param b
     * @return
     */
    public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        for (int n = 0; b != null && n < b.length; n++) {
            String temp = Integer.toHexString(b[n] & 0XFF);
            if (temp.length() == 1) {
                hs.append('0');
            }
            hs.append(temp);
        }
        return hs.toString();
    }

    public static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

}
