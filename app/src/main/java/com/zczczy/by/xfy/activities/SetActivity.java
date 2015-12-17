package com.zczczy.by.xfy.activities;

import android.view.Window;

import com.zczczy.by.xfy.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by zczczy on 2015/10/21.
 * 设置
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.set_layout)
public class SetActivity extends BaseActivity {

}
