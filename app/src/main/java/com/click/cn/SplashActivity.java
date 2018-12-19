package com.click.cn;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.click.cn.base.BaseActivity;
import com.click.cn.util.DialogUtil;
import com.click.cn.util.JumpPermissionManagement;
import com.click.cn.util.MkVUtils;
import com.click.cn.util.TransluteStatuBarUtil;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends BaseActivity {

    private static final int REQ_PERMISSION = 100;
    private static final int TYPE_MSG_ONLINE = 200;
    private static final int TYPE_MSG_STEP = 100;
    private int requestCount = 0;
    private Handler mHandler;
    private MaterialDialog materialDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void dispatchMessage(Message msg) {
                if (TYPE_MSG_STEP == msg.what) {
                    gotoLogin();
//                    redirectTo();
                } else if (TYPE_MSG_ONLINE == msg.what) {
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                    removeAllHandlerEvent();
                    finish();
                }
            }
        };
        super.onCreate(savedInstanceState);
        // 设置状态栏亮色模式
        TransluteStatuBarUtil.statusBarLightMode(getWindow(), true);

        if (null != materialDialog && materialDialog.isShowing()) {

        } else {
            checkPermission(getCheckPermissions());
        }
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
//        AccountManager.isConnectedToServer(new BaseBooleanCallback() {
//            @Override
//            public void success(final Boolean isOnline) {
//                Log.wtf(logTag, "已获取到当前连接状态---" + isOnline);
//                if (isOnline) {
//                    mHandler.sendEmptyMessageDelayed(TYPE_MSG_ONLINE, 1600);
//                } else {
//                    gotoLogin();
//                }
//            }
//
//            @Override
//            public void fail(int errorCode, @Nullable String msg) {
//                Log.wtf(logTag, "获取连接状态失败---如果可能，开始执行自动登录");
//                gotoLogin();
//            }
//        });
    }

    private void gotoLogin() {
        removeAllHandlerEvent();

        int previousVersionCode = MkVUtils.getInstance(MyApplication.getMainAppContext()).getUiProcessMMKV().decodeInt(MkVUtils.KEY_VERSION_CODE, 0);
        if (BuildConfig.VERSION_CODE != previousVersionCode) {
            Log.wtf(logTag, "需要显示引导界面");
            //引导界面
            Intent intent = new Intent(this, WelComeActivity.class);
            this.startActivity(intent);
        } else {
            Log.wtf(logTag, "开始执行自动登录");
            Intent intent = new Intent(this, LoginActivity.class);
            this.startActivity(intent);
            finish();
            // 执行到这里， 未登录
//            LoginHelper.autoLogin(new LoginHelper.ILoginCallback() {
//                @Override
//                public void startLogin() {
//                    Log.wtf(logTag, "开始执行中...");
//                }
//
//                @Override
//                public void loginSuccess() {
//                    Log.wtf(logTag, "开始回调成功...");
//                    PhotoActivity.open(SplashActivity.this);
//                    finish();
//                }
//
//                @Override
//                public void loginFail(int errorCode, String msg) {
//                    Log.wtf(logTag, "开始回调失败..." + msg);
//                    LoginActivity.start(SplashActivity.this);
//                    finish();
//                }
//            });
        }
    }

    private void removeAllHandlerEvent() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    private static String[] getCheckPermissions() {
        String[] permissionArray = new String[4];
        permissionArray[0] = (Manifest.permission.INTERNET);
        permissionArray[1] = (Manifest.permission.READ_EXTERNAL_STORAGE);
        permissionArray[2] = (Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissionArray[3] = (Manifest.permission.READ_PHONE_STATE);
        return permissionArray;
    }

    private void checkPermission(String[] permissionArray) {

        int denyCount = 0;
        for (String permission : permissionArray) {
            if (PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(this, permission)) {
                denyCount++;
            }
        }
        if (denyCount > 0) {
            if (requestCount > 2) {
                showTipsDialog();
            } else {
                requestCount++;
                ActivityCompat.requestPermissions(this, permissionArray, REQ_PERMISSION);
            }
        } else {
            // fixme: 此处预留进程连接时间,多次测试估计最大时间， 后期内化为回调接口，不暴露此行为
            mHandler.sendEmptyMessageDelayed(TYPE_MSG_STEP, 2 * 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        int deniedCount = 0;
        List<String> list = new ArrayList<>();
        if (requestCode == REQ_PERMISSION) {
            for (int i = 0; i < grantResults.length; i++) {
                int result = grantResults[i];
                if (result == PackageManager.PERMISSION_DENIED) {
                    deniedCount++;
                    list.add(permissions[i]);
                }
            }

            if (deniedCount > 0) {
                if (requestCount <= 2) {
                    String[] permissionArray = new String[list.size()];
                    list.toArray(permissionArray);
                    checkPermission(permissionArray);
                } else {
                    showTipsDialog();
                }
            } else {
                mHandler.sendEmptyMessageDelayed(TYPE_MSG_STEP, 1000);
            }
        }
    }

    private void showTipsDialog() {
        materialDialog = DialogUtil.buildSimpleDialog(SplashActivity.this, null,
                "在设置-应用 " + getString(R.string.app_name) + " 权限中开启\"存储空间、电话、网络\"权限,以正常使用本软件",
                "去设置", "取消", false,
                new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        JumpPermissionManagement.GoToSetting(SplashActivity.this);
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        finish();
                    }
                });
        materialDialog.show();
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_splash;
    }

    @Override
    public void findViews() {
    }

    @Override
    public void initHeader() {
        fullScreen();
    }

    @Override
    public void initContent() {

    }

    @Override
    public void setListener() {

    }

}
