package com.click.cn.view.contact;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vito-xa49 on 2017/8/3.
 * RecyclerView 基类adapter, 可设置点击、长按事件
 */

public abstract class BaseRecyclerViewAdapter<T, VH extends BaseViewHolder> extends RecyclerView.Adapter<VH> {

    private static final String TAG = BaseRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener listener;
    private OnItemClickListener2 listener2;
    protected List<T> dataList;
    protected Context mContext;

    public void setItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setItemClickListener(OnItemClickListener2 listener) {
        this.listener2 = listener;
    }

    public BaseRecyclerViewAdapter(Context context, @Nullable List<T> list) {
        this.mContext = context;
        this.dataList = list;
    }

    public abstract VH getViewHolder(ViewGroup parent, int viewType, OnItemClickListener listener, OnItemClickListener2 clickListener2);

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return getViewHolder(parent, viewType, listener, listener2);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) { // 子类需要调用父类
        holder.setData(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        int count = dataList != null ? dataList.size() : 0;
        return count;
    }

    /**
     * @param list
     */
    public void setList(@Nullable List<T> list) {
        if (dataList == null) {
            dataList = new ArrayList<>();
        } else {
            dataList.clear();
        }
        if (list != null)
            dataList.addAll(list);
        notifyDataSetChanged();
    }

    @Nullable
    public List<T> getData() { // todo: 访问权限问题
        return dataList;
    }

    public void setData(List<T> list, Object... objs) {
    }

    public interface OnItemClickListener {
        void itemClick(int position);
    }

    public interface OnItemClickListener2 {
        void itemClick(int position);

        void itemLongClick(int position);
    }

}
