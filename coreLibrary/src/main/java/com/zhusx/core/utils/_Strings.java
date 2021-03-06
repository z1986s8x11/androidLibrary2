package com.zhusx.core.utils;

import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/12/22 14:13
 */
public class _Strings {
    public static String toString(File file) {
        if (!file.exists()) {
            return null;
        }
        try {
            return toString(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Stream转换为String
     */
    public static String toString(InputStream in) {
        if (in == null) {
            return null;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            LogUtil.w(e);
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 转换当前byteSize大小 保留两位
     */
    public static String toFileSize(long byteSize) {
        //return android.text.format.Formatter.formatFileSize(ZsxApplicationManager.getApplication(),byteSize);
        DecimalFormat fmt = new DecimalFormat("0.#");
        double f1 = byteSize;
        if (f1 < 1024) {
            return fmt.format(byteSize) + " byte";
        }
        f1 = f1 / 1024.0f;
        if (f1 <= 1024) {
            return fmt.format(f1) + " K";
        }
        f1 = f1 / 1024.0f;
        if (f1 <= 1024) {
            return fmt.format(f1) + " MB";
        }
        f1 = f1 / 1024.0f;
        if (f1 <= 1024) {
            return fmt.format(f1) + " G";
        }
        return fmt.format(f1) + " G";
    }

    /**
     * 验证是否有汉字
     */
    public static boolean isChineseChar(CharSequence str) {
        boolean temp = false;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            temp = true;
        }
        return temp;
    }

    /**
     * 验证是否全数字
     */
    public static boolean isNumber(String str) {
        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 判断是不是一个合法的电子邮件地址
     */
    public static boolean isEmail(String email) {
        if (email == null || email.trim().length() == 0)
            return false;
        Pattern emailer = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        return emailer.matcher(email).matches();
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobilePhone(String mobiles) {
        /*
         * 移动：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）、150、151、152、157(TD)、158、159、187、188（TD专用）、198
		 * 联通：130、131、132、152、155、156（世界风专用）、185（未启用）、186（3g）
		 * 电信：133、153、180（未启用）、189、（1349卫通）、199
		 * 虚拟运营商：170
		 * 中国联合网络通信：166
		 */
        String telRegex = "[1][3456789]\\d{9}";
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 隐藏超过2倍长度的字符串,中间显示***
     * 用于隐藏电话号码
     */
    public static String hideMiddleText(String phone, int startEndShowLength) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        int length = phone.length();
        if (length >= 2 * startEndShowLength + 1) {
            phone = phone.subSequence(0, startEndShowLength) + "***"
                    + phone.subSequence(length - startEndShowLength, length);
        }
        return phone;
    }

    /**
     * 中文转 unicode
     *
     * @param noUnicodeStr 需要转换的字符或者汉字
     * @return
     */
    public static String encodeUnicode(String noUnicodeStr) {
        char[] utfBytes = noUnicodeStr.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            buffer.append("\\u" + hexB);
        }
        return buffer.substring(0);
    }

    /**
     * unicode 转换成 中文
     *
     * @param theString
     * @return
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed      encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    public static String valueOf(String s) {
        if (s == null) {
            return "";
        }
        return String.valueOf(s);
    }
}
