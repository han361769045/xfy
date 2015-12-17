package com.zczczy.by.xfy.items;

/**
 * Created by zczczy on 2015/11/13.
 */

import android.content.Context;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.CityProvince;


import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

@EViewGroup(R.layout.city_item_layout)
public class CityItemView extends  ItemView<CityProvince> {

    @ViewById
    TextView title_name;

    public CityItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {

        title_name.setText(_data.province_name);

    }
}
