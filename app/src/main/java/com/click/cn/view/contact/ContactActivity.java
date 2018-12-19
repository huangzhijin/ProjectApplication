package com.click.cn.view.contact;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.click.cn.R;
import com.click.cn.util.ContactUtil;
import com.click.cn.util.DensityUtils;
import com.click.cn.widget.IndexBar;
import com.click.cn.widget.RecycleViewDivider;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.fresco.helper.Phoenix;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ContactActivity extends AppCompatActivity {
    private ViewGroup mRightIndexBarLayout;
    private RecyclerView mRecyclerView;
    private ContactListAdapter mContactListAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private IndexBar mIndexBar;
    private TextView toastView;
    private ViewGroup mSearchLayout;
    private LinearLayoutManager layoutManager;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_contact);
        mContext=this;
        mSearchLayout = findViewById(R.id.ll_search);
        mRecyclerView = findViewById(R.id.recycler_view);
        mIndexBar = findViewById(R.id.indexbar);
        mRightIndexBarLayout = findViewById(R.id.ll_right_index);
        toastView = findViewById(R.id.tv_toast);

        initContent();

    }


    public void initContent() {
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        final RecycleViewDivider recycleViewDivider = new RecycleViewDivider(mContext, LinearLayoutManager.VERTICAL,
                DensityUtils.dppx(mContext, 0.5F), ContextCompat.getColor(mContext, R.color.common_divider_color));
        mRecyclerView.addItemDecoration(recycleViewDivider);
        initContactInfo();

        mContactListAdapter = new ContactListAdapter(mContext, contactList);

        mRecyclerView.setAdapter(mContactListAdapter);


        mIndexBar.setSelectedIndexTextView(toastView);
        mIndexBar.setOnIndexChangedListener(new IndexBar.OnIndexChangedListener() {
            @Override
            public void onIndexChanged(String index) {
                if ("↑".equals(index)) {
                    layoutManager.scrollToPosition(0);
                    return;
                }

                for (int i = 0; i < contactList.size(); i++) {
                    String firstWord = contactList.get(i).getPinyin();
                    if (index.equals(firstWord)) {
                        // 滚动列表到指定的位置
                        layoutManager.scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
        final String[] letters = new String[]{"↑", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J",
                "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
        mIndexBar.setIndexs(letters);
    }

    private void initContactInfo() {

         Contact contact=new Contact();
            contact.setNickname("刘三");
            contact.setPinyin(ContactUtil.getPinyin(contact));
        contactList.add(contact);
        Contact contact1=new Contact();
        contact1.setNickname("王五");
        contact1.setPinyin(ContactUtil.getPinyin(contact1));
        contactList.add(contact1);
        Contact contact2=new Contact();
        contact2.setNickname("谢菲");
        contact2.setPinyin(ContactUtil.getPinyin(contact2));
        contactList.add(contact2);
        Contact contact3=new Contact();
        contact3.setNickname("白旗");
        contact3.setPinyin(ContactUtil.getPinyin(contact3));
        contactList.add(contact3);

       PinyinComparator comparator=new PinyinComparator();
       Collections.sort(contactList,comparator);

        }



    @Override
    public void onResume() {
        super.onResume();

    }


}
