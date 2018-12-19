package com.click.cn.view.photo;

import android.util.Log;
import android.view.View;

import com.click.cn.R;
import com.facebook.fresco.helper.photoview.PictureBrowseActivity;
import com.facebook.fresco.helper.photoview.entity.PhotoInfo;

/**
 * 查看大图
 * Created by android_ls on 2017/1/20.
 */

public class PhotoBrowseActivity extends PictureBrowseActivity {

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_activity_photo_browse;
    }

    @Override
    protected void setupViews() {
        super.setupViews();
        findViewById(R.id.rl_top_deleted).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("TAG","用户点击了删除按钮");
                Log.i("TAG","mPhotoIndex = " + mPhotoIndex);

                PhotoInfo photoInfo = mItems.get(mPhotoIndex);
                Log.i("TAG","originalUrl = " + photoInfo.originalUrl);

            }
        });
    }

    @Override
    public boolean onLongClick(View view) {
        Log.i("TAG","currentPosition = " + getCurrentPosition());

        PhotoInfo photoInfo = getCurrentPhotoInfo();
        Log.i("TAG","current originalUrl = " + photoInfo.originalUrl);

        return super.onLongClick(view);
    }

}
