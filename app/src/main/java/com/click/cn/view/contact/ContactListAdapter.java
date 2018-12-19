package com.click.cn.view.contact;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.click.cn.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends BaseRecyclerViewAdapter<Contact ,BaseViewHolder> {
    private static final int ITEM_NORMAL = 1;
    private static final int ITEM_TOP = 2;

    public ContactListAdapter(Context context, @Nullable List<Contact> list) {
        super(context, list);
    }

    @Override
    public BaseViewHolder getViewHolder(ViewGroup parent, int viewType, OnItemClickListener listener, OnItemClickListener2 clickListener2) {
        View itemView;
        if (viewType == ITEM_TOP) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.x_item_contact_top,
                    parent, false);
            return new ContactTopViewHolder(itemView, listener, clickListener2);
        } else {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.x_item_list,
                    parent, false);
            return new ContactListViewHolder(itemView, listener, clickListener2);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
//            super.onBindViewHolder(holder, position);
        if (0 == position) {
            ContactTopViewHolder topViewHolder = (ContactTopViewHolder) holder;
            topViewHolder.setData(true);
        } else if (1 == position) {
            Contact contact = dataList.get(position - 1);
            ContactListViewHolder tempHolder = (ContactListViewHolder) holder;
            tempHolder.indexView.setVisibility(View.VISIBLE);
            tempHolder.indexView.setText(contact.getPinyin());
            tempHolder.setData(contact);
        } else {
            Contact contact = dataList.get(position - 1);
            Contact preContact = dataList.get(position - 2);
            ContactListViewHolder tempHolder = (ContactListViewHolder) holder;
            // fixme: NUllPointerException
            if (contact.getPinyin().equals(preContact.getPinyin())) {
                tempHolder.indexView.setVisibility(View.GONE);
            } else {
                tempHolder.indexView.setVisibility(View.VISIBLE);
                tempHolder.indexView.setText(contact.getPinyin());
            }
            tempHolder.setData(dataList.get(position - 1));
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TOP;
        } else {
            return ITEM_NORMAL;
        }
    }



    public void notifyNewFriend(boolean needShow) {
        this.notifyItemChanged(0);
    }

    @Override
    public long getItemId(int position) {
//            return super.getItemId(position);
        if (0 == position) {
            return RecyclerView.NO_ID;
        } else {
            return Integer.parseInt(dataList.get(position - 1).getUser_uid());
        }

    }

    private class ContactTopViewHolder extends BaseViewHolder<Boolean> {
        private ViewGroup mPhoneContactLayout;
        private ViewGroup mNewFriendLayout;
        private LinearLayout groupItemLayout;



        public ContactTopViewHolder(View itemView, OnItemClickListener listener, OnItemClickListener2 clickListener2) {
            super(itemView,listener,clickListener2);
            mNewFriendLayout = itemView.findViewById(R.id.ll_new_friend);
            groupItemLayout = itemView.findViewById(R.id.ll_group_item);
            mPhoneContactLayout = itemView.findViewById(R.id.ll_phone_contact);
        }

        @Override
        public void setData(Boolean data) {
            mPhoneContactLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            groupItemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mNewFriendLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    private class ContactListViewHolder extends BaseViewHolder<Contact> {

        private TextView usernameTextView;
        private TextView indexView;

        public ContactListViewHolder(View itemView, OnItemClickListener listener, OnItemClickListener2 clickListener2) {
            super(itemView,listener,clickListener2);
            usernameTextView = itemView.findViewById(R.id.peer_name);
            indexView = itemView.findViewById(R.id.tv_index);
        }

        @Override
        public void setData(Contact data) {
            usernameTextView.setText(data.getNickname());
        }
    }
}
