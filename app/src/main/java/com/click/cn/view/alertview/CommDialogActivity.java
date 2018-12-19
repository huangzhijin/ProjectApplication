package com.click.cn.view.alertview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnDismissListener;
import com.bigkoo.alertview.OnItemClickListener;
import com.click.cn.R;

public class CommDialogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_dialog);

    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button3:
                showDialog();
                break;

        }

    }


    private void showDialog() {
        new MaterialDialog.Builder(CommDialogActivity.this)
                .title("basic dialog")
                .content("一个简单的dialog,高度会随着内容改变，同时还可以嵌套RecyleView")
//                .iconRes(R.drawable.ic_error)
                .positiveText("同意")
                .negativeText("不同意")
                .neutralText("更多信息")
                .widgetColor(Color.BLUE)//不再提醒的checkbox 颜色
                //CheckBox
                  .checkBoxPrompt("不再提醒", false, new CompoundButton.OnCheckedChangeListener(){
                      @Override
                      public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                          if (b) {
                              Toast.makeText(CommDialogActivity.this, "不再提醒", Toast.LENGTH_LONG).show();
                          } else {
                              Toast.makeText(CommDialogActivity.this, "会再次提醒", Toast.LENGTH_LONG).show();
                          }
                      }
                  })
                  //嵌套recycleview，这个的点击事件可以先获取此Recycleview对象然后自己处理
//                  .adapter(new RecycleviewAdapter(getData(), CommDialogActivity.this), new LinearLayoutManager(CommDialogActivity.this))
//
//
//                  .itemsCallback(new MaterialDialog.ListCallback() {
//                      @Override
//                      public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
//                          dataChoose = "下标：" + position + " and 数据：" + mData.get(position);
//                      }
//                  })

                  //点击事件添加 方式1
                  .onAny(new MaterialDialog.SingleButtonCallback() {
                      @Override
                      public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                          if (which == DialogAction.NEUTRAL) {
                              Toast.makeText(CommDialogActivity.this, "更多信息", Toast.LENGTH_LONG).show();
                          } else if (which == DialogAction.POSITIVE) {
                              Toast.makeText(CommDialogActivity.this, "同意" , Toast.LENGTH_LONG).show();
                          } else if (which == DialogAction.NEGATIVE) {
                              Toast.makeText(CommDialogActivity.this, "不同意", Toast.LENGTH_LONG).show();
                          }

                      }
                  })
                .show();

    }

}
