package com.zczczy.by.xfy.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.adapters.CityAdapter;
import com.zczczy.by.xfy.adapters.MyBaseAdapter;
import com.zczczy.by.xfy.model.CityProvince;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/11/13.
 * 城市选择
 */
@EActivity(R.layout.city_layout)
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
public class CityChooseActivity  extends  BaseActivity {

    @ViewById
    ListView country_lvcountry;

    @ViewById
    MyTitleView title;

    @Pref
    MyPrefs_ pre;

    @Extra
    int flag=0;

    @Bean(CityAdapter.class)
    MyBaseAdapter<CityProvince> myBaseAdapter;

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("地区选择");
        country_lvcountry.setAdapter(myBaseAdapter);
        myBaseAdapter.getMoreData(1, 10);

    }


    @ItemClick
    void country_lvcountry(CityProvince cp){

        if(cp.cities!=null){
            myBaseAdapter.getList().clear();
            for(int i=0;i<cp.cities.size();i++){
                CityProvince cpc= new CityProvince();
                cpc.province_name=cp.cities.get(i).city_name;
                cpc.id=cp.cities.get(i).id;
                myBaseAdapter.getList().add(cpc);
            }
            myBaseAdapter.notifyDataSetChanged();
        }else{

            if(flag==0){ Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("cityname",cp.province_name);
                bundle.putString("citycode",cp.id);
                resultIntent.putExtras(bundle);
                CityChooseActivity.this.setResult(1001, resultIntent);
            }else{
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("agentname",cp.province_name);
                bundle.putString("agentcode",cp.id);
                resultIntent.putExtras(bundle);
                CityChooseActivity.this.setResult(1002, resultIntent);
            }
            CityChooseActivity.this.finish();
        }
    }

    public void Notify(){
        myBaseAdapter.notifyDataSetChanged();
    }


    //监听键盘回车键
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if(event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() != KeyEvent.ACTION_UP){
			 /*隐藏软键盘*/
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(inputMethodManager.isActive()){
                inputMethodManager.hideSoftInputFromWindow(CityChooseActivity.this.getCurrentFocus().getWindowToken(), 0);
            }

            return false;
        }
        return super.dispatchKeyEvent(event);
    }

}
