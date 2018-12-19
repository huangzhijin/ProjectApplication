package com.click.cn;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.click.cn.base.BaseActivity;
import com.click.cn.takephoto.PhotoActivity;
import com.click.cn.view.HeaderLayout;

public class LoginActivity extends BaseActivity {
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPlaceStatusBarViewColor(R.color.bg_status_bar_default);
        setWindowBackground(R.color.bg_window_default);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_activity_login;
    }

    @Override
    public void findViews() {
        loginButton=(Button)findViewById(R.id.button2);
    }

    @Override
    public void initHeader() {
//        mToolBar.showBackView();
        mToolBar.setTitle("登录");
    }

    @Override
    public void initContent() {

    }

    @Override
    public void setListener() {
        mToolBar.setEventListener(new HeaderLayout.ClickEvent() {
            @Override
            public void backClick(View v) {
                finish();
            }

            @Override
            public void titleClick(View v) {

            }

            @Override
            public void moreClick(View v) {

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });




        //ceshi------------------------------------

    }
}
