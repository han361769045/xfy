package com.zczczy.by.xfy.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zczczy.by.xfy.items.ItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * 适配
 * Created by zczczy on 2015/8/24.
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

    private List<T> list = new ArrayList<>();

    private int total = 0;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    protected String type ="0";

    public abstract void getMoreData(int pageIndex, int pageSize, Object... objects);

    /**
     * 获取lsit的大小
     *
     * @return
     */
    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * 根据i 获取对应的对象
     *
     * @param i
     * @return
     */
    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * 构建 视图
     *
     * @param i
     * @param view
     * @param viewGroup
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ItemView itemView;

        if (view == null) {

            itemView = getItemView(viewGroup.getContext());

        } else {

            itemView = (ItemView) view;

        }
        itemView.init(getItem(i),this,i,type);
        return itemView;
    }

    /**
     * 获取list
     *
     * @return
     */
    public List<T> getList() {
        return list;
    }

    /**
     * 设置list的值
     *
     * @param list
     */
    protected void setList(List<T> list) {
        this.list = list;
    }

    ;

    /**
     * 获取ItemView
     *
     * @param context
     * @return
     */
    protected abstract ItemView<T> getItemView(Context context);


}


