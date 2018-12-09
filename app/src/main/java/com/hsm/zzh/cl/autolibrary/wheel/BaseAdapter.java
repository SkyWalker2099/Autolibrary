package com.hsm.zzh.cl.autolibrary.wheel;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

//这是一个带有header和footer的recyclerview.adapter的类
/*
在holder中需要添加判断
super(view);
if (view == view_headerView || view == view_footerView)
    return;
 */
public abstract class BaseAdapter<B, T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private List<B> dataList;
    private Context context;
    private View view_headerView;
    private View view_footerView;

    public BaseAdapter(List<B> dataList, Context context,
                       View view_headerView, View view_footerView) {
        this.context = context;
        this.dataList = dataList;
        this.view_headerView = view_headerView;
        this.view_footerView = view_footerView;
    }

    private final int TYPE_NORMAL = 0;
    private final int TYPE_HEADER = 1;
    private final int TYPE_FOOTER = 2;

    @Override
    public int getItemViewType(int position) {
        if (view_headerView == null && view_footerView == null) {
            return TYPE_NORMAL;
        }
        if (position == 0 && view_headerView != null) {
            return TYPE_HEADER;
        }
        if (position == getItemCount() - 1 && view_footerView != null) {
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER)
            view = view_headerView;
        else if (viewType == TYPE_FOOTER)
            view = view_footerView;
        else
            view = LayoutInflater.from(parent.getContext())
                    .inflate(layoutId(), parent, false);
        return myCreateViewHolder(view);
    }


    @Override
    public void onBindViewHolder(T holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER || getItemViewType(position) == TYPE_FOOTER)
            return;
        if (view_headerView != null)
            position--;

        myBindViewHolder(holder, position);
    }

    protected abstract int layoutId();

    protected abstract T myCreateViewHolder(View view);

    protected abstract void myBindViewHolder(T holder, int position);

    @Override
    public int getItemCount() {
        int size = dataList.size();
        if (view_headerView != null)
            size++;
        if (view_footerView != null)
            size++;
        return size;
    }

}
