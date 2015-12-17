package com.zczczy.by.xfy.tools;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import com.zczczy.by.xfy.listener.PhotoCallBack;

import java.io.File;
import java.io.IOException;


public class GetPhotoFromAlbum {

    private static File mCurrentPhotoFile;
    private static File cropfile;

    /**
     * 通过何种方式获取图片 PHOTO_PICKED_WITH_DATA 相册 CAMERA_WITH_DATA 相机
     *
     * @param way 判断方式
     */
    public static void ChooseWay(Context context, int way) {
        if (CameraUtil.PHOTO_PICKED_WITH_DATA == way) {
            try {
                Intent intent = CameraUtil.getPhotoPickIntent();
                ((Activity) context).startActivityForResult(intent,
                        CameraUtil.PHOTO_PICKED_WITH_DATA);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (CameraUtil.CAMERA_WITH_DATA == way) {
            try {
                if (!CameraUtil.PHOTO_DIR.exists()) {
                    CameraUtil.PHOTO_DIR.mkdirs();
                }
                mCurrentPhotoFile = new File(CameraUtil.PHOTO_DIR,
                        CameraUtil.getPhotoFileName());
                Intent intent = CameraUtil.getTakePickIntent(mCurrentPhotoFile);
                ((Activity) context).startActivityForResult(intent,
                        CameraUtil.CAMERA_WITH_DATA);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param context   context
     * @param requestCode 如果是相册调用 cameraFile传null即可
     * @param cut         是否调用系统剪辑
     */
    public static void GetPhoto(Activity context, int requestCode, Intent data,
                                boolean cut, PhotoCallBack callBack) {
        // ***********图库*****************//
        if (requestCode == CameraUtil.PHOTO_PICKED_WITH_DATA) {
            try {
                String imagePath;
                Uri uri = data.getData();
                if (uri != null) {// 直接根据uri处理照片
                    getImagePathFromURI(context, uri, cut, callBack);
                } else {
                    Bundle extras = data.getExtras();
                    if (extras != null) {
                        // 这里是有些拍照后的图片是直接存放到Bundle中的所以我们可以从这里面获取Bitmap图片
                        Bitmap image = extras.getParcelable("data");
                        if (image != null) {
                            imagePath = CameraUtil.savePhotoBitmap(image);
                            if (callBack != null) {
                                if (cut) {
                                    doCropPhoto(context, uri, new File(
                                            imagePath));
                                } else {
                                    callBack.Success(imagePath);
                                }
                            }

                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // ***********照相*****************//
        if (requestCode == CameraUtil.CAMERA_WITH_DATA) {
            if (callBack != null) {
                if (cut) {
                    doCropPhoto(context, null, mCurrentPhotoFile);
                } else {
                    callBack.Success(mCurrentPhotoFile.getAbsolutePath());
                }
            }

        }

        // ***********裁剪*****************//
        if (requestCode == CameraUtil.CAMERA_COMPLETE) {
            callBack.Success(cropfile.getAbsolutePath());
        }

    }

    /**
     * 直接根据uri 获取图片
     *
     * @param context context
     * @param uri uri
     * @param cut 是否裁剪
     * @param callBack 回调
     */
    public static void getImagePathFromURI(Context context, Uri uri,
                                           boolean cut, PhotoCallBack callBack) {
        Cursor cursor = context.getContentResolver().query(uri, null, null,
                null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();
        cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();
        if (callBack != null) {
            callBack.Success(path);
        }
        if (cut) {
            doCropPhoto(context, uri, new File(path));
        }
    }

    /**
     * 裁剪调用
     *
     * @param context context
     * @param uri uri
     * @param filePath 文件路径
     */
    public static void doCropPhoto(Context context, Uri uri, File filePath) {
        try {
            Intent intent ;
            if (uri != null) {
                intent = getCropImageIntent(uri, 1, 1, CameraUtil.CROP_WIDTH,
                        CameraUtil.CROP_HEIGHT);
            } else {
                intent = getCropImageIntent(
                        Uri.parse("file://"
                                + Uri.decode(filePath.getAbsolutePath())), 1,
                        1, CameraUtil.CROP_WIDTH, CameraUtil.CROP_HEIGHT);
            }
            ((Activity) context).startActivityForResult(intent, CameraUtil.CAMERA_COMPLETE);
        } catch (Exception e) {
            Toast.makeText(context, "图片获取失败", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 调用图片剪辑程序
     */
    public static Intent getCropImageIntent(Uri photoUri, int aspectX,
                                            int aspectY, int outputX, int outputY) {
        Intent intent;
        intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", aspectX);// 宽高比
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);

        cropfile = new File(CameraUtil.CROP_FILE_DIR + File.separator
                + CameraUtil.getPhotoFileName());

        try {
            if (cropfile.exists()) {
                cropfile.delete();
            }
            cropfile.getParentFile().mkdirs();
            cropfile.createNewFile();
        } catch (IOException ex) {
            Log.e("io", ex.getMessage());
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cropfile));
        intent.putExtra("return-data", false);// 若为false则表示不返回数据
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", false);
        return intent;
    }

}

