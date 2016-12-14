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
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加密解密工具包
 * <p>
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:31
 */
public class _Encryptions {
    /**
     * 对称加密
     */
    public enum Symmetry {
        DES, // key 必须大于8位
        DES_CBB_PKCS5, // key 必须大于8位
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

    public static byte[] encode(Symmetry type, String key, String data)
            throws InvalidKeyException, NoSuchAlgorithmException,
            InvalidKeySpecException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher;
        switch (type) {
            case DES:
                cipher = Cipher.getInstance("DES");
                cipher.init(
                        Cipher.ENCRYPT_MODE,
                        SecretKeyFactory.getInstance("DES").generateSecret(
                                new DESKeySpec(key.getBytes())), new SecureRandom());
                return cipher.doFinal(data.getBytes());
            case DES_CBB_PKCS5:
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(
                        Cipher.ENCRYPT_MODE,
                        SecretKeyFactory.getInstance("DES").generateSecret(
                                new DESKeySpec(key.getBytes())),
                        new IvParameterSpec(key.getBytes()));
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
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(kGen
                        .generateKey().getEncoded(), "AES"));
                return cipher.doFinal(data.getBytes());
            case AES_ECB_PKCS5:
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key.getBytes(),
                        "AES"));
                return cipher.doFinal(data.getBytes());
            default:
                break;
        }
        return null;
    }

    public static byte[] decode(Symmetry type, String key, byte[] data)
            throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidAlgorithmParameterException {
        Cipher cipher;
        switch (type) {
            case DES:
                cipher = Cipher.getInstance("DES");
                cipher.init(
                        Cipher.DECRYPT_MODE,
                        SecretKeyFactory.getInstance("DES").generateSecret(
                                new DESKeySpec(key.getBytes())), new SecureRandom());
                return cipher.doFinal(data);
            case DES_CBB_PKCS5:
                cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                cipher.init(
                        Cipher.DECRYPT_MODE,
                        SecretKeyFactory.getInstance("DES").generateSecret(
                                new DESKeySpec(key.getBytes())),
                        new IvParameterSpec(key.getBytes()));
                return cipher.doFinal(data);
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
                SecretKey sKey = kGen.generateKey();
                byte[] rawKey = sKey.getEncoded();
                SecretKeySpec sKeySpec = new SecretKeySpec(rawKey, "AES");
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, sKeySpec);
                return cipher.doFinal(data);
            case AES_ECB_PKCS5:
                cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key.getBytes(),
                        "AES"));
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
