package com.zczczy.by.xfy.finder;


import com.zczczy.by.xfy.listener.DataReceiveListener;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.PagerResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2015/3/9.
 */
public abstract class BaseFinder<T> {

    private List<T> list = new ArrayList<T>();

    private int total = 0;

    DataReceiveListener listener;

    //设置监听器
    public void setDataReceiveListener(DataReceiveListener listener) {
        this.listener = listener;
    }

    public abstract void pagingQuery(int pageCount, int pageSize, Object... objects );

    public abstract void afterPagingQuery(BaseModelJson<PagerResult<T>> bmj);

    //刷新
    public void cleanListAndCount() {
        list.clear();
        total = 0;
    }

    //获取 list
    public List<T> getList() {
        return list;
    }

    //设置list
    public void setList(List<T> list) {
        this.list = list;
    }

    //获取总数
    public int getTotal() {
        return total;
    }

    //设置总数
    public void setTotal(int total) {
        this.total = total;
    }
}
