package com.click.cn.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.File;

public class FrescoUtil {

    // final String RES_PREFIX = "res://" + MyApplication.getMainAppContext() + "/";
    //                int defaultImgResourceId = rowData.isMan() ? R.drawable.head_man_offline : R.drawable.head_woman_offline;
    // asset:///
    //fixme: 性能优化，context 替换为对应Activity的Context
    private static final String RES_PREFIX = "res://" + BaseAppContextRef.getAppContext() + "/";
    public static final int MAX_WIDTH = DensityUtils.dppx(BaseAppContextRef.getAppContext(), 50F);


    private FrescoUtil() {
    }

    /**
     * 按原图加载本地图片
     *
     * @param draweeView
     * @param localImgUri
     * @param defaultDrawable
     */
    public static void loadLocalImg(@NonNull SimpleDraweeView draweeView, Uri localImgUri, @Nullable Drawable defaultDrawable) {
        draweeView.getHierarchy().setFailureImage(defaultDrawable);
        draweeView.getHierarchy().setPlaceholderImage(defaultDrawable);
        draweeView.getHierarchy().setFadeDuration(0);
        draweeView.setImageURI(localImgUri);
    }

    /**
     * 按路径加载本地图片
     *
     * @param draweeView
     * @param filePath
     */
    public static void loadLocalImg(@NonNull SimpleDraweeView draweeView, @NonNull String filePath) {
        draweeView.setImageURI(Uri.fromFile(new File(filePath)));
    }

    /**
     * 加载应用资源图片
     *
     * @param draweeView
     * @param drawableId
     */
    public static void loadDrawableId(@NonNull SimpleDraweeView draweeView, @DrawableRes int drawableId) {
        draweeView.setImageURI(Uri.parse(RES_PREFIX + drawableId));
    }

    /**
     * 清除指定图像的缓存并重新加载
     *
     * @param filePath
     */
    public static void reload(@NonNull SimpleDraweeView imageView, @NonNull String filePath) {

        Uri uri = Uri.fromFile(new File(filePath));
        reload(imageView, uri);
    }

    public static void reload(@NonNull SimpleDraweeView draweeView, @NonNull Uri uri) {
        clearCache(uri);
        Phoenix.evictFromCache(uri.getPath()); // 本地路径
        Phoenix.evictFromCache(uri); // 网络地址
//        draweeView.setImageURI(uri);
        Phoenix.with(draweeView).load(uri.getPath());
    }

    /**
     * 加载网络图像
     *
     * @param draweeView
     * @param url
     */
    @Deprecated
    public static void loadRemoteImg(@NonNull SimpleDraweeView draweeView, @NonNull String url) {
        // setResizeAndRotateEnabledForNetwork
        draweeView.setImageURI(Uri.parse(url));
    }


    /**
     * resize 方式加载图片
     *
     * @param draweeView
     * @param uri
     * @param width
     * @param height
     */
    public static void loadLocalImg(@NonNull SimpleDraweeView draweeView, @NonNull Uri uri, int width, int height) {
        // https://www.fresco-cn.org/docs/resizing-rotating.html
        // todo: 缩放存在诸多限制
//        String filePath = uri.getEncodedPath();
//        int[] imgFileSize = getImgFileSize(filePath);
//        if(imgFileSize[0] <= 0 || imgFileSize[1] <= 0){
//
//        }else{
//            int tempWidth = imgFileSize[0] / 8 + 1;
//            int tempHeight = imgFileSize[1] / 8 + 1;
//            if(width < tempWidth || height < tempHeight){
//                width = tempWidth;
//                height = tempHeight;
//            }else{
//                // 保持想要的尺寸继续操作
//            }
//        }
//        Log.wtf("fresco_resize", "filepath:" + filePath
//                + " org_width:" + imgFileSize[0] + " org_height:" + imgFileSize[1]
//                + " width:" + width + " height:" + height);
        ResizeOptions resizeOptions = new ResizeOptions(width, height);
        RotationOptions rotationOptions = /*RotationOptions.forceRotation(RotationOptions.NO_ROTATION)*/ RotationOptions.autoRotate();
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri).setResizeOptions(resizeOptions)
                .setLocalThumbnailPreviewsEnabled(true)
//                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
//                .setProgressiveRenderingEnabled(false)
                .setRotationOptions(rotationOptions)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(draweeView.getController())
                .setImageRequest(imageRequest)
                .setAutoPlayAnimations(true)
//                .setControllerListener(new ControllerListener<ImageInfo>() {
//                    @Override
//                    public void onSubmit(String id, Object callerContext) {
//
//                    }
//
//                    @Override
//                    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
//                        Log.wtf("fresco", "id:" + id + " width:" + imageInfo.getWidth() + " height:" + imageInfo.getHeight()
//                                + " quality:" + imageInfo.getQualityInfo());
//                    }
//
//                    @Override
//                    public void onIntermediateImageSet(String id, ImageInfo imageInfo) {
//                        Log.wtf("fresco", "id:" + id + " width:" + imageInfo.getWidth() + " height:" + imageInfo.getHeight()
//                                + " quality:" + imageInfo.getQualityInfo());
//                    }
//
//                    @Override
//                    public void onIntermediateImageFailed(String id, Throwable throwable) {
//                        Log.wtf("fresco", "id:" + id + " exception:" + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onFailure(String id, Throwable throwable) {
//                        Log.wtf("fresco","id:" + id + " exception:" + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onRelease(String id) {
//
//                    }
//                })
                .build();

        draweeView.setController(controller);
    }

    /**
     * 固定Resize方式加载本地图片
     *
     * @param draweeView
     * @param uri
     */
    public static void loadLocalThumnailImg(@NonNull SimpleDraweeView draweeView, @NonNull Uri uri) {
        loadLocalImg(draweeView, uri, MAX_WIDTH, MAX_WIDTH);
    }

    /**
     * 清楚制定缓存
     *
     * @param uri
     */
    public static void clearCache(@NonNull Uri uri) {
        Fresco.getImagePipeline().evictFromCache(uri);
        Fresco.getImagePipeline().evictFromMemoryCache(uri);
        Fresco.getImagePipeline().evictFromDiskCache(uri);
    }

    /**
     * 清除所有的Fresco缓存
     */
    public static void clearAll() {
        Fresco.getImagePipeline().clearCaches();
//        Fresco.getImagePipeline().clearMemoryCaches();
//        Fresco.getImagePipeline().clearDiskCaches();
        Phoenix.clearCaches();
    }

    /**
     * 应用内资源转为uri
     *
     * @param drawableId
     * @return
     */
    public static Uri getDrawableIdUri(@DrawableRes int drawableId) {
        Uri uri = Uri.parse(RES_PREFIX + drawableId);
        return uri;
    }

    /**
     * 本地图像资源转为uri表示
     *
     * @param filePath
     * @return
     */
    public static Uri getLocalImgUri(@NonNull String filePath) {
        Uri uri = Uri.fromFile(new File(filePath));
        return uri;
    }

    /**
     * 获取图片的大小(宽高)
     *
     * @param filePath
     * @return
     */
    private static int[] getImgFileSize(String filePath) {
        // fixme: 以后添加判断是否是图片
        int[] size = {0, 0};
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        size[0] = options.outWidth;
        size[1] = options.outHeight;
        if (null != bitmap) {
            bitmap.recycle();
        }
        bitmap = null;
        return size;
    }
}
