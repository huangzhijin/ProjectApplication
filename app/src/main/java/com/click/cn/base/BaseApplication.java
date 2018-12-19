package com.click.cn.base;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.click.cn.MyApplication;
import com.click.cn.util.LogUtil;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class BaseApplication extends Application implements Thread.UncaughtExceptionHandler{


    @Override
    public void onCreate() {
        super.onCreate();
//        Thread.setDefaultUncaughtExceptionHandler(this);


    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        LogUtil.e("uncaught ex is ", throwable);
        BufferedWriter writer = null;
        try {
            String exceptionStr = Log.getStackTraceString(throwable);
            String version = MyApplication.getVersionName(this);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
            String time = sdf.format(new Date(System.currentTimeMillis()));
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),
                    "gldn_log/"+ version +"/log_" + time +".txt");
            LogUtil.d("filePath = " + file.toString());
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(exceptionStr, 0, exceptionStr.length());
            writer.flush();
            writer.close();

        }catch (Exception e){
            LogUtil.e("write file error", e);
            try {
                if(writer != null){
                    writer.close();
                }
            } catch (IOException exce) {
                exce.printStackTrace();
            }
        }
        /*Intent finishIntent = new Intent(HAS_CRASH_EXCEPTION);
        ActionTipsManager.getInstance().sendBroadCast(finishIntent);
        Intent intent = new Intent(this, StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);*/
    }
}
