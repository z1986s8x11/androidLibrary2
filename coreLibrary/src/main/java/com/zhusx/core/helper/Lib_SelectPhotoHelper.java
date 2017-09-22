package com.zhusx.core.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import com.zhusx.core.debug.LogUtil;
import com.zhusx.core.utils._Files;

import java.io.File;
import java.io.IOException;

/**
 * Author        zhusx
 * Email         327270607@qq.com
 * Created       2016/10/13 9:22
 */
public class Lib_SelectPhotoHelper {
    public static final int ActivityCameraRequestCode = 0x8A1;
    public static final int ActivityPhotoRequestCode = 0x8B2;
    private final int ActivityClopPhotoRequestCode = 0x8C3;
    private Activity activity;
    private Fragment fragment;
    private Context context;
    private boolean isClop = false;
    private int photoWidth, photoHeight;
    private File dirFile;
    private File saveFile;

    public Lib_SelectPhotoHelper(Activity activity, String dir, int width, int height) {
        this(activity, dir);
        this.photoWidth = width;
        this.photoHeight = height;
        this.isClop = true;
    }

    public Lib_SelectPhotoHelper(Fragment fragment, String dir, int width, int height) {
        this(fragment, dir);
        this.photoWidth = width;
        this.photoHeight = height;
        this.isClop = true;
    }

    public Lib_SelectPhotoHelper(Activity activity, String dir) {
        this.activity = activity;
        this.context = activity;
        this.dirFile = new File(dir);
    }

    public Lib_SelectPhotoHelper(Fragment fragment, String dir) {
        this.fragment = fragment;
        this.context = fragment.getActivity();
        this.dirFile = new File(dir);
    }

    private void startActivityForResult(Intent intent, int requestId) {
        if (fragment == null) {
            activity.startActivityForResult(intent, requestId);
        } else {
            fragment.startActivityForResult(intent, requestId);
        }
    }

    /**
     * 从相册选取
     */
    public void _startSelectPhoto() {
        saveFile = new File(dirFile, System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        startActivityForResult(intent, ActivityPhotoRequestCode);
    }

    /**
     * 照像获取图片
     */
    public void _startTakePhoto() {
        saveFile = new File(dirFile, System.currentTimeMillis() + ".jpg");
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (saveFile.exists()) {
                saveFile.delete();
            }
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, _Files.LIB_FILE_PROVIDER, saveFile));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(saveFile));
            }

            startActivityForResult(intent, ActivityCameraRequestCode);
        } else {
            Toast.makeText(context, "请插入SD卡", Toast.LENGTH_SHORT).show();
        }
    }

    public interface onDataListener {
        void onPhoto(File saveFile);
    }

    public void _onActivityResult(int requestCode, int resultCode, Intent data,
                                  onDataListener listener) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case ActivityCameraRequestCode:
                    if (isClop) {
                        gotoClop(saveFile, photoWidth, photoHeight);
                    } else {
                        listener.onPhoto(saveFile);
                    }
                    break;
                case ActivityPhotoRequestCode:
                    Uri uri = data.getData();
                    String filePath = _Files.getUriPath(context, uri);
                    if (!TextUtils.isEmpty(filePath)) {
                        if (isClop) {
                            gotoClop(new File(filePath), photoWidth, photoHeight);
                        } else {
                            File file = new File(filePath);
                            listener.onPhoto(file);
                        }
                    } else {
                        if (LogUtil.DEBUG) {
                            LogUtil.e("_Files.getUriPath(context, uri) at" + String.valueOf(uri) + " is null");
                        }
                    }
                    break;
                case ActivityClopPhotoRequestCode:
                    Bitmap bm = data.getParcelableExtra("data");
                    if (bm != null) {
                        if (saveFile.exists()) {
                            saveFile.delete();
                        }
                        boolean isSuccess = _Files.saveToFile(bm, saveFile.getPath());
                        if (!bm.isRecycled()) {
                            bm.recycle();
                        }
                        if (isSuccess) {
                            listener.onPhoto(saveFile);
                        }
                    }
                    break;
            }
        }
    }

    private void gotoClop(File file, int width, int height) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setData(FileProvider.getUriForFile(context, _Files.LIB_FILE_PROVIDER, file));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "image/*");
        }
        intent.putExtra("crop", "true");
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", width);
        intent.putExtra("outputY", height);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ActivityClopPhotoRequestCode);
    }

//
//    获取 虚拟文件 流
//    url : https://developer.android.google.cn/about/versions/nougat/android-7.0.html#gles_32
//          https://developer.android.com/about/versions/nougat/android-7.0.html
//
//    final static private int REQUEST_CODE = 64;
//    // We listen to the OnActivityResult event to respond to the user's selection.
//    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//        try {
//            if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//                Uri uri = null;
//                if (resultData != null) {
//                    uri = resultData.getData();
//                    ContentResolver resolver = getContentResolver();
//                    // Before attempting to coerce a file into a MIME type,
//                    // check to see what alternative MIME types are available to
//                    // coerce this file into.
//                    String[] streamTypes = resolver.getStreamTypes(uri, "*/*");
//                    AssetFileDescriptor descriptor =
//                            resolver.openTypedAssetFileDescriptor(
//                                    uri,
//                                    streamTypes[0],
//                                    null);
//                    // Retrieve a stream to the virtual file.
//                    InputStream inputStream = descriptor.createInputStream();
//                }
//            }
//        } catch (Exception ex) {
//            Log.e("EXCEPTION", "ERROR: ", ex);
//        }
//    }
}
