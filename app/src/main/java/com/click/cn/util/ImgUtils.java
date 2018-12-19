//package com.click.cn.util;
//
//import android.app.Activity;
//import android.content.ContentValues;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.PixelFormat;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.graphics.drawable.NinePatchDrawable;
//import android.graphics.drawable.ShapeDrawable;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.v4.app.Fragment;
//import android.support.v4.graphics.drawable.DrawableCompat;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import java.io.File;
//
///**
// * Created by vito-xa49 on 2017/9/25.
// */
//
//public class ImgUtils {
//    private static final String TAG = ImgUtils.class.getSimpleName();
//    public static final String FILE_PROVIDER = FileUtils.FILE_PROVIDER;
//    private static final String PHOTO_DIR = Environment.getExternalStorageDirectory() + "/vito_oa/thumnail"; // 缩略图目录
//    public static final String TAKE_PHOTO_PREFIX = Environment.getExternalStorageDirectory() + "/vito_oa/DCIM/DD_"; //拍摄的照片的路径及其前缀
//
//    public static String getThumnailDir() {
//        File storageFile = new File(PHOTO_DIR);
//        if (storageFile.exists()) {
//            if (storageFile.isFile()) {
//                storageFile.delete();
//                storageFile.mkdirs();
//            }
//        } else {
//            storageFile.mkdirs();
//        }
//        return PHOTO_DIR;
//    }
//
//    /**
//     * 拍照
//     *
//     * @param activity
//     * @param requestCode
//     * @return
//     */
//    @Deprecated
//    public static String takePic(Activity activity, int requestCode) {
//        if (!isSDExist(activity)) {
//            return null;
//        }
//        String photoPath = TAKE_PHOTO_PREFIX + System.currentTimeMillis() + ".jpg";
//        try {
//            File photoFile = createParent(photoPath);
//            if (photoFile == null)
//                return null;
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 照相
//            if (currentapiVersion < 24) {
//                // 将File对象转换为Uri并启动照相程序
//                Uri imageUri = Uri.fromFile(photoFile);
//                // "android.media.action.IMAGE_CAPTURE"
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); // 指定图片输出地址
//            } else {
//                ContentValues contentValues = new ContentValues(1);
//                contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
//                Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            }
//            activity.startActivityForResult(intent, requestCode); // 启动照相
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return photoPath;
//    }
//
//    public static String takePicForActivity(Activity activity, int requestCode) {
//        if (!isSDExist(activity)) {
//            return null;
//        }
//        String photoPath = TAKE_PHOTO_PREFIX + System.currentTimeMillis() + ".jpg";
//        Log.i(TAG, photoPath);
//        try {
//            File photoFile = createParent(photoPath);
//            if (photoFile == null)
//                return null;
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 照相
//            Uri uri = FileUtils.getFileUri(BaseAppContextRef.getAppContext(), photoFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            activity.startActivityForResult(intent, requestCode); // 启动照相
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return photoPath;
//    }
//
//    public static String takePic(Fragment fragment, int requestCode) {
//        if (!isSDExist(fragment.getContext())) {
//            return null;
//        }
//        String photoPath = TAKE_PHOTO_PREFIX + System.currentTimeMillis() + ".jpg";
//        Log.i(TAG, photoPath);
//        try {
//            File photoFile = createParent(photoPath);
//            if (photoFile == null)
//                return null;
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 照相
//            Uri uri = FileUtils.getFileUri(BaseAppContextRef.getAppContext(), photoFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            fragment.startActivityForResult(intent, requestCode); // 启动照相
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return photoPath;
//    }
//
//    public static String takeFrontPic(Fragment fragment, int requestCode) {
//        if (!isSDExist(fragment.getContext())) {
//            return null;
//        }
//        String photoPath = TAKE_PHOTO_PREFIX + System.currentTimeMillis() + ".jpg";
//        Log.i(TAG, photoPath);
//        try {
//            File photoFile = createParent(photoPath);
//            if (photoFile == null)
//                return null;
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 照相
//            Uri uri = FileUtils.getFileUri(BaseAppContextRef.getAppContext(), photoFile);
//            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//            intent.putExtra("autofocus", true); // 自动对焦
//            intent.putExtra("fullScreen", false); // 全屏
//            intent.putExtra("showActionIcons", false);
//            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
//            fragment.startActivityForResult(intent, requestCode); // 启动照相
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return photoPath;
//    }
//
//    private static boolean isSDExist(Context context) {
//        String SDState = Environment.getExternalStorageState();
//        if (!SDState.equals(Environment.MEDIA_MOUNTED)) {
//            Toast.makeText(BaseAppContextRef.getAppContext(), "扩展存储不存在", Toast.LENGTH_SHORT).show();
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 创建路径的父文件夹
//     *
//     * @param filePath
//     * @return
//     */
//    private static File createParent(String filePath) {
//        if (TextUtils.isEmpty(filePath)) {
//            return null;
//        } else {
//            File file = new File(filePath);
//            if (file.exists()) {
//                return file;
//            } else {
//                File parentFile = file.getParentFile();
//                if (parentFile != null && parentFile.exists()) {
//                    return file;
//                } else {
//                    if (parentFile.mkdirs()) {
//                        return file;
//                    } else {
//                        return null;
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 兼容各种版本的着色处理
//     *
//     * @param target
//     * @param tintColor
//     * @return
//     */
//    public static Drawable tintDrawableCompact(Drawable target, int tintColor) {
//        Drawable drawable = DrawableCompat.wrap(target).mutate();// tint之前必须进行一次包装，兼容低版本
//        DrawableCompat.setTint(drawable, tintColor);
//        return drawable;
//    }
//
//
//    // https://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
//    // 如果Drawable进行了着色处理，转换后tint的颜色会丢失
//    public static Bitmap drawable2Bitmap(Drawable drawable) {
//        Bitmap resultBitmap = null;
//        if (drawable instanceof BitmapDrawable) {
//            resultBitmap = ((BitmapDrawable) drawable).getBitmap();
//        } else if (drawable instanceof NinePatchDrawable) {
//            Bitmap bitmap = Bitmap
//                    .createBitmap(
//                            drawable.getIntrinsicWidth(), // >0
//                            drawable.getIntrinsicHeight(), // >0
//                            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
//                                    : Bitmap.Config.RGB_565);
//            Canvas canvas = new Canvas(bitmap);
//            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
//                    drawable.getIntrinsicHeight());
//            drawable.draw(canvas);
//            resultBitmap = bitmap;
//        } else if (drawable instanceof ShapeDrawable) {
//            Bitmap bitmap = Bitmap.createBitmap(drawable.getBounds().width(), drawable.getBounds().height(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
////            drawable.setBounds(0, 0, drawable.getBounds().width(), drawable.getBounds().height());
//            drawable.draw(canvas);
//            resultBitmap = bitmap;
//        } else {
//            resultBitmap = null;
//        }
//        return resultBitmap;
//    }
//
//    public static Drawable bitmap2Drawable(Bitmap bitmap) {
//        return new BitmapDrawable(bitmap);
//    }
//}
///**
// * 读取照片旋转角度
// *
// * @param path 照片路径
// * @return 角度
// */
//public int readPictureDegree(String path) {
//        int degree = 0;
//        try {
//        ExifInterface exifInterface = new ExifInterface(path);
//        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        Log.i("tag", "读取角度-" + orientation);
//        switch (orientation) {
//        case ExifInterface.ORIENTATION_ROTATE_90:
//        degree = 90;
//        break;
//        case ExifInterface.ORIENTATION_ROTATE_180:
//        degree = 180;
//        break;
//        case ExifInterface.ORIENTATION_ROTATE_270:
//        degree = 270;
//        break;
//        }
//        } catch (IOException e) {
//        e.printStackTrace();
//        }
//        return degree;
//        }

///**
// * 旋转图片
// *
// * @param angle  被旋转角度
// * @param bitmap 图片对象
// * @return 旋转后的图片
// */
//public Bitmap rotaingImageView(int angle, Bitmap bitmap) {
//        Bitmap returnBm = null;
//        // 根据旋转角度，生成旋转矩阵
//        Matrix matrix = new Matrix();
//        matrix.postRotate(-angle);
//        try {
//        // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
//        returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        } catch (OutOfMemoryError e) {
//        }
//        if (returnBm == null) {
//        returnBm = bitmap;
//        }
//        if (bitmap != returnBm) {
//        bitmap.recycle();
//        }
//        return returnBm;
//        }