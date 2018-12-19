package com.click.cn.view.contact;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by vito-xa49 on 2017/8/3.
 * <p>
 * 面向holder编程
 */

public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    private BaseRecyclerViewAdapter.OnItemClickListener itemClickListener;
    private BaseRecyclerViewAdapter.OnItemClickListener2 itemClickListener2;

    public BaseViewHolder(View itemView, BaseRecyclerViewAdapter.OnItemClickListener itemClickListener, BaseRecyclerViewAdapter.OnItemClickListener2 itemClickListener2) {
        super(itemView);
        this.itemClickListener = itemClickListener;
        this.itemClickListener2 = itemClickListener2;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (null != itemClickListener) {
            itemClickListener.itemClick(getAdapterPosition());
        }

        if (null != itemClickListener2) {
            itemClickListener2.itemClick(getAdapterPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {

        if (null == itemClickListener2) {
            return false;
        } else {
            itemClickListener2.itemLongClick(getAdapterPosition());
            return true;
        }
    }


    /**
     * 设置view中的展示信息
     *
     * @param data
     */
    public abstract void setData(T data);
}
