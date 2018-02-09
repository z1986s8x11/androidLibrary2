package com.zhusx.core.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.zhusx.core.debug.LogUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:32
 */
public class _Files {
    public static final String ASSET_PATH = "file:///android_asset/";
    /**
     * 读取txt文件乱码：
     BufferedReader read = new BufferedReader(new FileReader(new File(filename)));

     解决办法：
     InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
     BufferedReader read = new BufferedReader(isr);
     */

    /**
     * 是否存在SD卡
     */
    public static boolean isExistSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @return 拿到SD可用空间大小
     */
    public static long getSDAvaiableSize() {
        if (isExistSDCard()) {
            File SDPath = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(SDPath.getPath());
            /* 空闲的Block数量 */
            long availableBlocks = stat.getAvailableBlocks();
            /* 获取Block大小 */
            long blockSize = stat.getBlockSize();
            return availableBlocks * blockSize;
        }
        return -1;
    }

    /**
     * @return 拿到SD可用空间大小
     */
    public static long getSDSize() {
        if (isExistSDCard()) {
            File SDPath = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(SDPath.getPath());
            /* 空闲的Block数量 */
            long blocks = stat.getBlockCount();
            /* 获取Block大小 */
            long blockSize = stat.getBlockSize();
            return blocks * blockSize;
        }
        return -1;
    }

    /**
     * 保存到文件中
     */
    public static boolean saveToFile(InputStream in, String savePath) {
        boolean isSuccess = false;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int count;
            while ((count = in.read(buffer)) != -1) {
                stream.write(buffer, 0, count);
            }
            stream.close();
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 保存到文件中
     */
    public static boolean saveToFile(Bitmap bitmap, String savePath) {
        int quality = 100;// 压缩到文件的品质 0-100
        boolean isSuccess = true;
        File file = new File(savePath);
        if (!file.getParentFile().exists()) {
            file.mkdirs();
        }
        if (file.exists()) {
            file.delete();
        }
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(savePath);
            isSuccess = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        } catch (Exception e) {
            e.printStackTrace();
            if (file.exists()) {
                file.delete();
            }
            isSuccess = false;
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 保存到文件中
     */
    public static boolean saveToFile(String txt, String savePath) {
        if (TextUtils.isEmpty(txt)) {
            return false;
        }
        boolean isSuccess = false;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(savePath);
            stream.write(txt.getBytes());
            stream.close();
            isSuccess = true;
        } catch (Exception e) {
            isSuccess = false;
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return isSuccess;
    }

    /**
     * 计算SD卡的剩余空间
     *
     * @return 返回-1，说明没有安装sd卡
     */
    public static long getSDFreeDiskSpace() {
        String status = Environment.getExternalStorageState();
        long freeSpace;
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            freeSpace = availableBlocks * blockSize;
        } else {
            return -1;
        }
        return (freeSpace);
    }

    /**
     * 计算系统的剩余空间
     */
    public static long getSystemFreeDiskSpace() {
        File root = Environment.getRootDirectory();
        long freeSpace;
        StatFs stat = new StatFs(root.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        freeSpace = availableBlocks * blockSize;
        return (freeSpace);
    }

    /**
     * 删除文件夹中所有文件
     */
    public static void deleteFile(File file) {
        if (file == null) {
            return;
        }
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            for (int i = 0; i < childFiles.length; i++) {
                if (childFiles[i].isDirectory()) {
                    deleteFile(childFiles[i]);
                } else {
                    childFiles[i].delete();
                }
            }
        }
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileSuffix(File file) {
        if (file.isDirectory()) {
            return null;
        }
        return getFileSuffix(file.getName());
    }

    /**
     * 获取文件扩展名
     */
    public static String getFileSuffix(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return "";
        }
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return "";
        }
        return fileName.substring(index + 1).toLowerCase();
    }

    @SuppressWarnings("resource")
    public static Serializable readObject(String objectFilePath) {
        File objectFile = new File(objectFilePath);
        if (!objectFile.exists()) {
            return null;
        }
        Serializable object = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(objectFilePath);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = (Serializable) ois.readObject();
        } catch (Exception e) {
            LogUtil.w(e);
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (objectFile.exists()) {
                objectFile.delete();
            }
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return object;
    }

    public static boolean writeObject(Serializable object, String savePath) {
        if (object == null) {
            return false;
        }
        File objectFile = new File(savePath);
        if (!objectFile.getParentFile().exists()) {
            objectFile.getParentFile().mkdirs();
        }
        if (objectFile.exists()) {
            objectFile.delete();
        }
        File f = new File(savePath);
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = new FileOutputStream(f);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            return true;
        } catch (IOException e) {
            LogUtil.w(e);
            if (objectFile.exists()) {
                objectFile.delete();
            }
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 获取内置存储位置
     */
    public static String getBuildInPath(Context context) {
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePathsMethod = StorageManager.class.getMethod("getVolumePaths", paramClasses);
            getVolumePathsMethod.setAccessible(true);
            Object[] params = {};
            Object invoke = getVolumePathsMethod.invoke(storageManager, params);
            String[] invokes = (String[]) invoke;
            for (int i = 0; i < invokes.length; i++) {
                if (!Environment.getExternalStorageDirectory().getPath().equals(invokes[i])) {
                    return invokes[i];
                }
            }
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 文件或者文件夹的大小
     */
    public static long getFileSize(File f) {
        if (!f.exists()) {
            return -1;
        }
        if (f.isFile()) {
            return f.length();
        }
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    /**
     * 复制Assets文件到指定file下
     * 使用数据库用 SQLiteDatabase.openOrCreateDatabase(new File(context.getFilesDir(), dbName).getPath(), null);
     */
    public static void copyAssetToFile(Context context, String assetsFileName, File sdFile) {
        if (sdFile.exists()) {
            if (sdFile.length() != 0) {
                return;
            }
            sdFile.delete();
        }
        InputStream ins = null;
        FileOutputStream out = null;
        try {
            ins = context.getAssets().open(assetsFileName);
            out = new FileOutputStream(sdFile);
            byte[] buffer = new byte[1024];
            int count = -1;
            while ((count = ins.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
        } catch (IOException e) {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException ee) {
                    ee.printStackTrace();
                }
                out = null;
            }
            if (sdFile.exists()) {
                sdFile.delete();
            }
            e.printStackTrace();
        } finally {
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 通过Uri 拿到文件路径
     */
    public static String getUriPath(final Context context, final Uri uri) {
        // Whether the Uri authority is ExternalStorageProvider.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // Whether the Uri authority is DownloadsProvider.
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // Whether the Uri authority is MediaProvider.
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Whether the Uri authority is Google Photos.
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority())) {
                return uri.getLastPathSegment();
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
