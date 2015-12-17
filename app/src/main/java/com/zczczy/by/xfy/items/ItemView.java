package com.zczczy.by.xfy.items;

import android.content.Context;
import android.widget.LinearLayout;

/**
 * Created by zczczy on 2015/10/20.
 */
public abstract class ItemView<T> extends LinearLayout {
    protected T _data;

    Context context;

    public ItemView(Context context) {
        super(context);
        this.context=context;
    }

    public void init(T t, Object... objects) {
        this._data = t;
        init(objects);
    }

    protected abstract void init(Object... objects);
}
