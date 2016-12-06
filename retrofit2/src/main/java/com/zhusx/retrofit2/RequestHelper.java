package com.zhusx.retrofit2;

/**
 * @author zhangbb
 * @date 2016/7/25.
 */
//public class RequestHelper {
//
//    private static String deviceId;
//    private static final String DEVICE_ID = "deviceid";
//
//    /**
//     * 日志与header初始化
//     *
//     * @param context
//     * @return
//     */
//    public static OkHttpClient genericClient(final Context context) {
//        OkHttpClient.Builder client = new OkHttpClient().newBuilder();
//        initCache(client, context);
//        if (BuildConfig.DEBUG) {
//            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//            return client
//                    .addInterceptor(new HeaderInterceptor(context))
//                    .addInterceptor(logging)
//                    .build();
//        } else {
//            return client
//                    .addInterceptor(new HeaderInterceptor(context)).build();
//        }
//    }
//
//    /**
//     * 配置缓存
//     *
//     * @param client
//     * @param context
//     */
//    private static void initCache(OkHttpClient.Builder client, Context context) {
//        File httpCacheDirectory = new File(context.getExternalCacheDir(), "responses");
//        client.cache(new Cache(httpCacheDirectory, 30 * 1024 * 1024));
//    }
//
//
//    /**
//     * 初始化header
//     *
//     * @param chain
//     * @param context
//     * @return
//     */
//    public static Request getCommonHeader(Interceptor.Chain chain, Context context) {
//        return addCommonHeader(chain.request(), context);
//    }
//
//    public static Request addCommonHeader(Request request, Context context) {
//        long time = System.currentTimeMillis();
//        if (deviceId == null) {
//            deviceId = PreferenceManager.getString(context, DEVICE_ID, "");
//        }
//        return request
//                .newBuilder()
//                .addHeader("App-Agent", generateHeader(context, time).toString())
//                .addHeader("sign", _Encryptions.encodeMD5(getToken() + deviceId + time))
//                .addHeader("Authorization", getToken())
//                .build();
//    }
//
//    /**
//     * @param context
//     * @param time
//     * @return
//     */
//    public static StringBuilder generateHeader(Context context, long time) {
//        StringBuilder headerStr = new StringBuilder();
//        headerStr
//                .append("version=").append(_Systems.getAppVersionName(context))
//                .append(",platform=").append("android")
//                .append(",app_store_name=").append(PalaApplication.CHANNEL_NAME)
//                .append(",software_version=").append(android.os.Build.VERSION.RELEASE)
//                .append(",models=").append(android.os.Build.MODEL)
//                .append(",package_name=").append(context.getPackageName())
//                .append(",time=").append(time);
//        return headerStr;
//    }
//
//    /**
//     * 设备交换token的header
//     *
//     * @param isGranted
//     * @param context
//     * @return
//     */
//    public static String generateTokenHeader(Context context, boolean isGranted) {
//        long time = System.currentTimeMillis();
//        if (isGranted) {
//            deviceId = _Systems.getIMEI(context);
//        } else {
//            deviceId = android.os.Build.SERIAL;
//            if (deviceId.equals("unknown")) {
//                deviceId = UUID.randomUUID().toString();
//            }
//        }
//        PreferenceManager.putString(context, DEVICE_ID, deviceId);
//        String crypt = encrypt(deviceId, Constant.ENCRYPT_KEY);
//        return generateHeader(context, time).append(",uuid=").append(crypt).toString();
//    }
//
//
//    public static String encrypt(String input, String key) {
//        try {
//            SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, sKey);
//            byte[] crypted = cipher.doFinal(input.getBytes());
//            return getHashString(crypted);
//        } catch (Exception e) {
//            LogUtil.e(e);
//        }
//        return null;
//    }
//
//    private static String getHashString(byte[] var2) {
//        StringBuilder builder = new StringBuilder();
//        int var3 = var2.length;
//        for (int var4 = 0; var4 < var3; ++var4) {
//            byte b = var2[var4];
//            builder.append(Integer.toHexString(b >> 4 & 15));
//            builder.append(Integer.toHexString(b & 15));
//        }
//        return builder.toString();
//    }
//
//    /**
//     * 获取token
//     *
//     * @return
//     */
//    private static String getToken() {
//        String token = UserManager.getInstance().getToken();
//        if (token != null) {
//            return token;
//        }
//        return "";
//    }
//}
