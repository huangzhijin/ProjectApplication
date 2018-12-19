package com.click.cn;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.text.emoji.EmojiCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.click.cn.api.ApiCallBack;
import com.click.cn.api.ApiRequest;
import com.click.cn.base.Result;
import com.click.cn.bean.LoginBean;
import com.click.cn.view.glance.GlancePhotoActivity;
import com.click.cn.util.LogUtil;
import com.click.cn.util.NotificationUtils;
import com.kyleduo.switchbutton.SwitchButton;

import org.green.greenlibrary.DbMange;
import org.green.greenlibrary.User;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity implements View.OnClickListener{

    private Button add,update,delete,search;

    private SwitchButton   switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_user);

        add=(Button)findViewById(R.id.button_add);
        update=(Button)findViewById(R.id.button_update);
        delete=(Button)findViewById(R.id.button_delete);
        search=(Button)findViewById(R.id.button_search);
        add.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        search.setOnClickListener(this);
        switchButton = (SwitchButton) findViewById(R.id.switch_btn_top2);
        switchButton.setCheckedImmediatelyNoEvent(false);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                switchButton.setCheckedImmediatelyNoEvent(b);
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_add:
//                add();

                CharSequence processed = EmojiCompat.get().process("neutral face \uD83D\uDE10");

                add.setText(processed);


                break;
            case R.id.button_update:
                update();
                break;
            case R.id.button_delete:
                delete();
                break;
            case R.id.button_search:
                GlancePhotoActivity.startActivity(this);
//                function();
//                notifition();
//                search();
                break;
        }
    }

    private void search() {
      List<User> users= DbMange.getInstance().selectUserDao();
      for(int i=0;i<users.size();i++){
          LogUtil.i(users.get(i).getUserId()+"--------------"+users.get(i).getName());
      }

    }

    private void update() {
       User user=new User();
        user.setName("zhang");
        user.setUserId(2l);
        DbMange.getInstance().updateUserDaoItem(2l,user);
    }

    private void delete() {
        DbMange.getInstance().deleteUserDaoItem(1l);
    }

    private void add() {
//        for(int i=0;i<5;i++){
//            DbMange.getInstance().addUserDaoItem(new User( null,"san","zhangsan"+i,"2"+i, LoveType.TEXT));
//
//        }

        NotificationUtils notificationUtils = new NotificationUtils(this);
        notificationUtils.sendNotification("测试标题", "测试内容");

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void notifition(){


        NotificationManager manager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("channel_1", "ceshi", NotificationManager.IMPORTANCE_HIGH);
// 开启指示灯，如果设备有的话
        channel.enableLights(true);
// 设置指示灯颜色
        channel.setLightColor(ContextCompat.getColor(this, R.color.colorPrimary));
// 是否在久按桌面图标时显示此渠道的通知
        channel.setShowBadge(true);
// 设置是否应在锁定屏幕上显示此频道的通知
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PRIVATE);
// 设置绕过免打扰模式
        channel.setBypassDnd(true);
        manager.createNotificationChannel(channel);


        Notification notification=new Notification.Builder(this,"channel_1")
         .setContentText("ceshibiaoti").setContentTitle("biaoti").setSmallIcon(android.R.drawable.sym_def_app_icon)
                .setAutoCancel(true).build();

        manager.notify(1,notification);

//        ArrayList<NotificationChannelGroup> groups = new ArrayList<>();
//        NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
//        groups.add(group);
//        NotificationChannelGroup group_download = new NotificationChannelGroup(groupDownloadId, groupDownloadName);
//        groups.add(group_download);
//        manager.createNotificationChannelGroups(groups);

//        channel.setGroup(groupId);
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, channelId);

    }


    private void function(){
//        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
//        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
//        Uri imageUri = Uri.fromFile(file);
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
//        startActivityForResult(intent,1006);



          if(PackageManager.PERMISSION_GRANTED== ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)){
              takePhoto();
          }else{
              ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},10);
          }

    }



    private void takePhoto(){
        File file=new File(Environment.getExternalStorageDirectory(), "/temp/"+System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists())file.getParentFile().mkdirs();
        Uri imageUri = FileProvider.getUriForFile(this, "com.click.cn.fileProvider", file);//通过FileProvider创建一个content类型的Uri
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent,1006);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==10){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                takePhoto();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1006){
            Log.i("","------------------"+data);
        }


    }


    private void sendRequest(){
       Call<Result<LoginBean>> loginBeanCall= ApiRequest.getServiceApi().login("","");

        loginBeanCall.enqueue(new ApiCallBack<Result<LoginBean>>() {
            @Override
            public void onSuccessful(Call<Result<LoginBean>> call, Response<Result<LoginBean>> response) {
            }
        });

    }




}
