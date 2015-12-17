package com.zczczy.by.xfy.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.BaseModelJson;
import com.zczczy.by.xfy.model.Equip;
import com.zczczy.by.xfy.model.UserLogin;
import com.zczczy.by.xfy.prefs.MyPrefs_;
import com.zczczy.by.xfy.rest.MyErrorHandler;
import com.zczczy.by.xfy.rest.MyRestClient;
import com.zczczy.by.xfy.tools.AndroidTool;
import com.zczczy.by.xfy.viewgroup.MyAlertDialog;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

/**
 * Created by zczczy on 2015/10/21.
 * 我的设备
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.myequipment_layout)
public class MyEquipmentActivity extends BaseActivity {

    @ViewById
    MyTitleView title;

    @ViewById
    LinearLayout ll_first,ll_second,ll_third;
    @ViewById
    TextView txt_equipone,txt_equiptwo,txt_equipthree;

    FragmentManager fragmentManager;
    EquipFirstFragment equipFirstFragment;
    EquipSecondFragment equipSecondFragment;
    EquipThirdFragment equipThirdFragment;

    //控制  选中状态时再被点中
    int flag = 0;
    @Extra
    int index=0;
    @Pref
    MyPrefs_ pre;
    @RestService
    MyRestClient myRestClient;
    @Bean
    MyErrorHandler myErrorHandler;
    @AfterInject
    void afterInject() {
        myRestClient.setRestErrorHandler(myErrorHandler);
    }

    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle("我的设备");
        fragmentManager = getFragmentManager();
        setTabSelection(index);

    }


    @Click
    void ll_first() {
        if (flag == 0) {
            return;
        }
        setTabSelection(0);
    }


    @Click
    void ll_second() {

        if (flag == 1) {
            return;
        }
        setTabSelection(1);
    }


    @Click
    void ll_third() {
        if (flag == 2) {
            return;
        }
        setTabSelection(2);
    }


    /**
     * 根据传入的index参数来设置选中的tab页。
     *
     * @param index
     */
    void setTabSelection(int index) {

        // 每次选中之前先清楚掉上次的选中状态
        clearSelection();
        // 开启一个Fragment事务
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        transaction.setCustomAnimations(R.anim.left_in, R.anim.left_out, R.anim.right_in, R.anim.right_out);

        switch (index) {
            case 0:
                // 当点击了消息tab时，改变控件的图片和文字颜色

                txt_equipone.setSelected(true);

                if (equipFirstFragment == null) {
                    // equipFirstFragment，则创建一个并添加到界面上
                    equipFirstFragment = EquipFirstFragment_.builder().build();
                    transaction.add(R.id.fl_content, equipFirstFragment);
                } else {
                    // 如果equipFirstFragment不为空，则直接将它显示出来
                    transaction.show(equipFirstFragment);
                }
                flag = 0;
                break;
            case 1:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                txt_equiptwo.setSelected(true);
                if (equipSecondFragment == null) {
                    // equipSecondFragment，则创建一个并添加到界面上
                    equipSecondFragment = EquipSecondFragment_.builder().build();
                    transaction.add(R.id.fl_content, equipSecondFragment);
                } else {
                    // equipSecondFragment，则直接将它显示出来
                    transaction.show(equipSecondFragment);
                }



                flag = 1;
                break;
            case 2:
                // 当点击了消息tab时，改变控件的图片和文字颜色
                txt_equipthree.setSelected(true);
                if (equipThirdFragment == null) {
                    // equipThirdFragment，则创建一个并添加到界面上
                    equipThirdFragment = EquipThirdFragment_.builder().build();
                    transaction.add(R.id.fl_content, equipThirdFragment);
                } else {
                    // equipThirdFragment，则直接将它显示出来
                    transaction.show(equipThirdFragment);
                }


                flag = 2;
                break;

            default:
                break;
        }
        transaction.commit();

    }

    /**
     * 清除掉所有的选中状态。
     */
    void clearSelection() {


        txt_equipone.setSelected(false);
        txt_equiptwo.setSelected(false);
        txt_equipthree.setSelected(false);

    }
    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    void hideFragments(FragmentTransaction transaction) {
        if (equipFirstFragment != null) {
            transaction.hide(equipFirstFragment);
        }
        if (equipSecondFragment != null) {
            transaction.hide(equipSecondFragment);
        }
        if (equipThirdFragment != null) {
            transaction.hide(equipThirdFragment);
        }

    }

}
