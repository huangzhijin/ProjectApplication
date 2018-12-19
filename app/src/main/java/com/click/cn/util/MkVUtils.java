package com.click.cn.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tencent.mmkv.MMKV;

public class MkVUtils {

    public static final String KV_ID_MULTI_PROCESS = "kv-id-multi-process";
    public static final String KV_ID_UI_PROCESS = "kv-id-ui-process";
    private static final String KV_ID_ASHMEM = "kv-id_ashmem";

    public static final String KEY_REMIND = "remind";
    public static final String KEY_BURN_TIME = "burn_time";
    // 标识存储 版本号
    public static final String KEY_VERSION_CODE = "boot_page";

    // 用户登录后标识（跨进程保存）--------------------------------------------------------------------
    public static final String KEY_LOGIN_UID = "uid";
    // 身份认证标识，如果ui process 中自动失败，则导致token直接失效
    public static final String KEY_LOGIN_TOKEN = "token";
    private static final String KEY_LOGIN_TYPE = "login_type";
    // 最近一次登录的账户（账号或或者手机号）, 只有登录成功的情况下，才会覆盖上一次的信息
    private static final String KEY_LAST_LOGIN_PHONE = "last_login_phone";
    private static final String KEY_LOGIN_USERNAME = "username";
    private static final String KEY_LOGIN_SEX = "sex";
    //----------------------------------------------------------------------------------------------

    private static MkVUtils mkVUtils;

    private MMKV multiProcessMMKV;
    private MMKV uiProcessMMKV;
    private MMKV ashmemMMKV;

    private MkVUtils(@NonNull Context context) {
      String dir=  MMKV.initialize(context);
        Log.i("MkVUtils","-----------MkVUtils----dir--------"+dir);
        final String cryKey = "jzy";
        // fixme: 后期加密
        multiProcessMMKV = MMKV.mmkvWithID(KV_ID_MULTI_PROCESS, MMKV.MULTI_PROCESS_MODE, cryKey);
        uiProcessMMKV = MMKV.mmkvWithID(KV_ID_UI_PROCESS, MMKV.SINGLE_PROCESS_MODE, cryKey);
//        ashmemMMKV = MMKV.mmkvWithAshmemFD(KV_ID_ASHMEM);
    }

    public static MkVUtils getInstance(@NonNull Context context) {
        if (null == mkVUtils) {
            synchronized (MkVUtils.class) {
                if (null == mkVUtils) {
                    mkVUtils = new MkVUtils(context);
                }
            }
        }

        return mkVUtils;
    }

    public MMKV getMultiProcessMMKV() {
        return multiProcessMMKV;
    }

    public MMKV getUiProcessMMKV() {
        return uiProcessMMKV;
    }

//    public MMKV getAshmemMMKV(){
//        return ashmemMMKV;
//    }

//--------------------------------------------------------------------------------------------------

    /**
     * 登录成功保存信息 (AccountManager 登录成功后调用)
     *
     * @param phone
     * @param userId
     * @param loginType
     * @param token
     */
    public static void saveLoginInfoWithLoginSuccess(String phone, @NonNull String userId, int loginType, @NonNull String token,
                                                     @NonNull String username, @NonNull int sex) {
        MMKV multiProcessMMKV = getInstance(BaseAppContextRef.getAppContext()).getMultiProcessMMKV();
        multiProcessMMKV.encode(KEY_LAST_LOGIN_PHONE, phone);
        multiProcessMMKV.encode(KEY_LOGIN_UID, userId);
        multiProcessMMKV.encode(KEY_LOGIN_TYPE, loginType);
        multiProcessMMKV.encode(KEY_LOGIN_TOKEN, token);
        multiProcessMMKV.encode(KEY_LOGIN_USERNAME, username);
        multiProcessMMKV.encode(KEY_LOGIN_SEX, sex);
    }

    /**
     * 清除用户信息（AccountManager中登出成功后调用）
     */
    public static void clearLastLoginInfo() {
        MMKV multiProcessMMKV = getInstance(BaseAppContextRef.getAppContext()).getMultiProcessMMKV();
//        multiProcessMMKV.removeValueForKey(KEY_LAST_LOGIN_PHONE); // 留存此项，用于登出后重新登录
        multiProcessMMKV.removeValueForKey(MkVUtils.KEY_LOGIN_UID);
        multiProcessMMKV.removeValueForKey(MkVUtils.KEY_LOGIN_TYPE);
        multiProcessMMKV.removeValueForKey(MkVUtils.KEY_LOGIN_TOKEN); // 此项需要严格控制
        multiProcessMMKV.removeValueForKey(KEY_LOGIN_USERNAME);
        multiProcessMMKV.removeValueForKey(KEY_LOGIN_SEX);
    }

    /**
     * 多进程中获取当前uid
     *
     * @param isPushProcess
     * @return
     */
//    public static String getLastCurrentUid(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        return MkVUtils.getInstance(context).getMultiProcessMMKV().decodeString(KEY_LOGIN_UID);
//    }
//
//    public static int getLastLoginType(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        return MkVUtils.getInstance(context).getMultiProcessMMKV().decodeInt(KEY_LOGIN_TYPE, 0);
//    }
//
//    public static String getLastLoginToken(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        return MkVUtils.getInstance(context).getMultiProcessMMKV().decodeString(KEY_LOGIN_TOKEN);
//    }
//
//    public static String getLastLoginPhone(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        return MkVUtils.getInstance(context).getMultiProcessMMKV().decodeString(KEY_LAST_LOGIN_PHONE);
//    }
//
//    public static String getLastUsername(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        return MkVUtils.getInstance(context).getMultiProcessMMKV().decodeString(KEY_LOGIN_USERNAME);
//    }
//
//    /**
//     * 是否男， 默认为女
//     *
//     * @param isPushProcess
//     * @return
//     */
//    public static boolean getLastSex(boolean isPushProcess) {
//        Context context = isPushProcess ? PushContextRef.getSubAppContext() : BaseAppContextRef.getAppContext();
//        int sexInt = MkVUtils.getInstance(context).getMultiProcessMMKV().decodeInt(KEY_LOGIN_SEX, 0); // 默认为0，女
//        return RosterElementEntity.SEX_MAN.equals(String.valueOf(sexInt));
//    }
    //--------------------------------------------------------------------------------------------------

}
