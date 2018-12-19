package com.click.cn.util;

import android.content.Context;
import android.support.annotation.ArrayRes;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import com.afollestad.materialdialogs.MaterialDialog;

import static android.R.attr.inputType;

/**
 * Created by vito-xa49 on 2017/8/30.
 */

public class DialogUtil {

    private DialogUtil() {
    }

    // 单选dialog
    public static MaterialDialog buildSingleChoiceDialog(Context context, String title, CharSequence[] items, int selectIndex, MaterialDialog.ListCallbackSingleChoice callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        builder.items(items);
        builder.itemsCallbackSingleChoice(selectIndex, callback);
        MaterialDialog dialog = builder.build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    //单选dialog
    public static MaterialDialog buildSingleChoiceDialog(Context context, String title, @ArrayRes int itemsId, int selectIndex, MaterialDialog.ListCallbackSingleChoice callback) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(context);
        if (!TextUtils.isEmpty(title)) {
            builder.title(title);
        }
        builder.items(itemsId);
        builder.itemsCallbackSingleChoice(selectIndex, callback);
        MaterialDialog dialog = builder.build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    /**
     * 简单进度框
     *
     * @param context
     * @return
     */
//    public static MaterialDialog buildProgressDialog(Context context) {
//        View mContentView = LayoutInflater.from(context).inflate(R.layout.layout_custom_process, null);
//        MaterialDialog materialDialog = new MaterialDialog.Builder(context)
//                .customView(mContentView, false).build();
//        setDialogWindowDimDisable(materialDialog);
//        Window window = materialDialog.getWindow();
//        if (null != window) {
//            window.setDimAmount(0F);
////            window.setBackgroundDrawableResource(R.drawable.pure_transparent_bg);
//            // or
////            window.setDimAmount(0F);
//            window.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, android.R.color.transparent)));
//        }
//        return materialDialog;
//    }

    /**
     * 环形进度条
     *
     * @param context
     * @param content
     * @return
     */
//    @Deprecated
//    public static MaterialDialog buildProgressDialog2(Context context, String content) {
//        return buildProgressDialog(context);
//    }

    /**
     * 水平进度条对话框
     *
     * @param context
     * @param title
     * @param startProgress
     * @return
     */
    public static MaterialDialog buildProgressDialog(Context context, String title, int startProgress) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
//                .content(R.string.please_wait)
                .progress(true, startProgress)
                .progressIndeterminateStyle(true)
                .build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    /**
     * 获取一个简单的dialog
     *
     * @param mContext
     * @param title
     * @param postiveTxt
     * @param negativeTxt
     * @param postiveCallback
     * @return
     */
    public static MaterialDialog buildSimpleDialog(Context mContext, String title, String postiveTxt, String negativeTxt,
                                                   boolean cancellable, MaterialDialog.SingleButtonCallback postiveCallback,
                                                   MaterialDialog.SingleButtonCallback negativeCallback) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(title)
                .positiveText(postiveTxt)
                .negativeText(negativeTxt)
                .onPositive(postiveCallback)
                .onNegative(negativeCallback)
                .cancelable(cancellable)
                .build();

        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    /**
     * @param mContext
     * @param title
     * @param postiveTxt
     * @param negativeTxt
     * @param cancellable
     * @param postiveCallback
     * @param negativeCallback
     * @return
     */
    public static MaterialDialog buildSimpleDialog(Context mContext, String title, String content, String postiveTxt, String negativeTxt,
                                                   boolean cancellable, MaterialDialog.SingleButtonCallback postiveCallback,
                                                   MaterialDialog.SingleButtonCallback negativeCallback) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(title)
                .content(content)
                .positiveText(postiveTxt)
                .negativeText(negativeTxt)
                .onPositive(postiveCallback)
                .onNegative(negativeCallback)
                .cancelable(cancellable)
                .build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    public static MaterialDialog buildSimpleDialog(Context mContext, String title, MaterialDialog.SingleButtonCallback postiveCallback) {
        MaterialDialog dialog = new MaterialDialog.Builder(mContext)
                .title(title)
                .positiveText("确定")
                .negativeText("取消")
                .onPositive(postiveCallback)
                .build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    /**
     * 提示对话框
     *
     * @param mContext
     * @param title
     * @param callback
     * @return
     */
    public static MaterialDialog buildTipsDialog(Context mContext, String title, MaterialDialog.SingleButtonCallback callback) {
        return buildSimpleDialog(mContext, title, "确定", "", false, callback, null);
    }


//    public static MaterialDialog buildPermissionTipsDialog(Context context){
//        DialogUtil.buildSimpleDialog(context, "请授予必要的权限以保证App正常运行", "确定",
//                "在设置中查看", false, new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                requestPermission();
//            }
//        }, new MaterialDialog.SingleButtonCallback() {
//            @Override
//            public void onClick(@NonNull MaterialDialog materialDialog, @NonNull DialogAction dialogAction) {
//                // 打开App设置界面
//                JumpPermissionManagement.GoToSetting((Activity)context);
//            }
//        }).show();
//    }

    /**
     * @param context
     * @param title
     * @param contentHint
     * @param isPwd
     * @param maxEms
     * @param inputCallback
     * @param callback
     * @return
     */
    public static MaterialDialog buildInputDilog(Context context, String title, String defaultStr, String contentHint, boolean isPwd, final int maxEms, final int minLength, MaterialDialog.InputCallback inputCallback, MaterialDialog.SingleButtonCallback callback) {
        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(title)
//                .content(contentHint)
                .inputType(inputType)
                .inputType(isPwd ? InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT)
                .inputRangeRes(minLength, maxEms, android.R.color.holo_red_light)
                .input(contentHint, defaultStr/*默认值*/, inputCallback).positiveText("确定")
                .alwaysCallInputCallback()
                .negativeText("取消")
                .onPositive(callback)
                .build();

        setDialogWindowDimDisable(dialog);
        dialog.show();

        return dialog;
    }

//    public static MaterialDialog buildCustomViewDialog(){
//        new MaterialDialog.Builder()
//    }


    /**
     * 自定义view的dialog
     *
     * @param context
     * @param contentView
     * @param wrapInScrollView
     * @return
     */
    public static MaterialDialog buildCustomeViewDialog(Context context, View contentView, boolean wrapInScrollView) {
        MaterialDialog dialog = new MaterialDialog.Builder(context).customView(contentView, wrapInScrollView)
                .build();
        setDialogWindowDimDisable(dialog);
        return dialog;
    }

    private static void setDialogWindowDimDisable(MaterialDialog dialog) {
        Window window = dialog.getWindow();
        if (null != window) {
            window.setDimAmount(0.6F);
        }
    }


}
