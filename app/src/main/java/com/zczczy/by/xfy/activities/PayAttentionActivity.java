package com.zczczy.by.xfy.activities;

import android.view.Window;

import com.zczczy.by.xfy.R;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.WindowFeature;

/**
 * Created by zczczy on 2015/10/21.
 * 关注86云消防
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.payattention_layout)
public class PayAttentionActivity  extends BaseActivity{
}
