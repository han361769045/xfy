package com.zczczy.by.xfy.adapters;

import android.content.Context;

import com.zczczy.by.xfy.activities.CityChooseActivity;
import com.zczczy.by.xfy.items.CityItemView_;
import com.zczczy.by.xfy.items.ItemView;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.CityProvince;
import com.zczczy.by.xfy.rest.MyRestClient;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.rest.RestService;

import java.util.Collections;
import java.util.List;

/**
 * Created by zczczy on 2015/11/16.
 */
@EBean
public class CityAdapter extends MyBaseAdapter<CityProvince> {

    @RestService
    MyRestClient myRestClient;

    @RootContext
    CityChooseActivity cityChooseActivity;



    @Override
    @Background
    public void getMoreData(int pageIndex, int pageSize, Object... objects) {
        BaseModelJson<List<CityProvince>> bmj= myRestClient.GetProvinceAndCity();
        setBind(bmj);

    }
    @UiThread
    void setBind(BaseModelJson<List<CityProvince>> bmj)
    {
        if(bmj!=null&&bmj.Successful)
        {
            setList(bmj.Data);
            cityChooseActivity.Notify();
        }
    }

    @Override
    protected ItemView<CityProvince> getItemView(Context context) {

        return CityItemView_.build(context);
    }
}
