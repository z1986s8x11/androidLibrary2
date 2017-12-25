package com.zhusx.core.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/12/25 10:19
 */

public class _Phones {
    /**
     * 返回手机服务商名字
     */
    public static String getProvidersName(Context context) {
        String ProvidersName = null;
        if (context == null) {
            return ProvidersName;
        }
        // 返回唯一的用户ID;就是这张卡的编号神马的
        String IMSI = getIMSI(context);
        // IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
        if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
            ProvidersName = "中国移动";
        } else if (IMSI.startsWith("46001")) {
            ProvidersName = "中国联通";
        } else if (IMSI.startsWith("46003")) {
            ProvidersName = "中国电信";
        } else {
            ProvidersName = "其他服务商";
        }
        return ProvidersName;
    }

    /**
     * @return 手机IMSI号码(国际移动用户识别码)
     */
    public static String getIMSI(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager telephonyManager;
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        // 返回唯一的用户ID;就是这张卡的IMSI编号
        return telephonyManager.getSubscriberId();
    }

    /**
     * 需要 android.permission.READ_PHONE_STATE
     *
     * @return 返回手机ICCID号码(国际移动装备辨识码)
     */
    public static String getICCID(Context context) {
        if (context == null) {
            return null;
        }
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            // 返回唯一的用户ID;就是这张卡的IMSI编号
            return telephonyManager.getSimSerialNumber();
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * @return 手机串号
     */
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return telephonyManager.getDeviceId();
        } catch (SecurityException e) {
            return null;
        }
    }

    /**
     * 返回本地手机号码，这个号码不一定能获取到
     */
    public static String getNativePhoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
}
