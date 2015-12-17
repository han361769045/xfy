package com.zczczy.by.xfy.tools;

/**
 * ����������ǹ���ͨ����������ý���ȡͼƬ�ĺ���
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraUtil {

    public static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    public static final File CROP_FILE_DIR = new File(Environment.getExternalStorageDirectory() + "/DCIM/CROP");
    public static final int CROP_WIDTH = 400;
    public static final int CROP_HEIGHT = 400;

    public static final int CAMERA_WITH_DATA = 0x3023;
    public static final int CAMERA_COMPLETE = 0x3022;
    public static final int PHOTO_PICKED_WITH_DATA = 0x3021;

    private static CameraUtil instance = null;

    public static CameraUtil getInstance(Activity activity) {
        if (instance == null)
            instance = new CameraUtil();
        return instance;
    }

    /**
     * 拍照
     *
     * @param file
     * @return
     */
    public static Intent getTakePickIntent(File file) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        return intent;
    }

    /**
     * 获得一个文件名，文件名为当前的时间
     *
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * 图片浏览器 该intent兼容android4.4 的图库，该方法打开的只是图库，不包括下载内容和最新。
     *
     * @return
     */
    public static Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return intent;
    }

    /***
     * 保存一个图片到CameraUtil.PHOTO_DIR目录，并返回得到的路径
     *
     * @param mBitmap
     * @return
     */
    public static String savePhotoBitmap(Bitmap mBitmap) {
        File file;
        FileOutputStream fOut = null;
        String imagePath = "";
        try {
            if (!CROP_FILE_DIR.exists()) {
                CROP_FILE_DIR.mkdirs();
            }
            file = new File(PHOTO_DIR, getPhotoFileName());
            file.createNewFile();
            imagePath = file.getAbsolutePath();
            fOut = new FileOutputStream(file);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            return imagePath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fOut.flush();
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return imagePath;

    }

    /**
     * 根据路径获取图片
     *
     * @param outPut
     * @return
     */
    public static Bitmap decodeUriAsBitmap(String outPut) {
        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(outPut);
        return bitmap;
    }

    // 按图片大小(字节大小)缩放图片
    public static Bitmap fitSizeImg(String path) {
        if (path == null || path.length() < 1)
            return null;
        File file = new File(path);
        Bitmap resizeBmp = null;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        // 数字越大读出的图片占用的heap越小 不然总是溢出
        if (file.length() < 20480) { // 0-20k
            opts.inSampleSize = 1;
        } else if (file.length() < 51200) { // 20-50k
            opts.inSampleSize = 2;
        } else if (file.length() < 307200) { // 50-300k
            opts.inSampleSize = 4;
        } else if (file.length() < 819200) { // 300-800k
            opts.inSampleSize = 6;
        } else if (file.length() < 1048576) { // 800-1024k
            opts.inSampleSize = 8;
        } else {
            opts.inSampleSize = 10;
        }
        resizeBmp = rotaingImageView(readPictureDegree(path), BitmapFactory.decodeFile(file.getPath(), opts));
        return resizeBmp;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /*
     * 旋转图片
     *
     * @param angle
     *
     * @param bitmap
     *
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}
