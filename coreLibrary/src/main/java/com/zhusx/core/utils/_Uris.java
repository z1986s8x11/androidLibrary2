package com.zhusx.core.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.RawRes;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Author       zhusx
 * Email        327270607@qq.com
 * Created      2017/9/29 9:19
 */

public class _Uris {
    /**
     * 如果是Intent 跳转  需要 Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
     */
    public static Uri fromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".libFileProvider", file);
        } else {
            return Uri.fromFile(file);
        }
    }

    public static Uri fromResource(Context context, @ColorRes @RawRes @DrawableRes int id) {
        Resources res = context.getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + res.getResourcePackageName(id) + "/" + res.getResourceTypeName(id) + "/" + res.getResourceEntryName(id));
    }
}
