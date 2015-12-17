package com.zczczy.by.xfy.activities;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.viewgroup.MyTitleView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import org.androidannotations.annotations.res.StringRes;

/**
 * Created by zczczy on 2015/10/21.
 * 反馈意见
 */
@WindowFeature(value = {Window.FEATURE_NO_TITLE, Window.FEATURE_INDETERMINATE_PROGRESS})
@EActivity(R.layout.feedback_layout)
public class FeedBackActivity extends BaseActivity {
    @ViewById
    MyTitleView title;
    @ViewById
    EditText edit_feedback;
    @ViewById
    Button btn_tijiao;
    @ViewById
    LinearLayout ll_feedback_info;

    @StringRes
    String txt_feedback;
    @AfterViews
    void afterView() {
        title.setListener(this);
        title.setTitle(txt_feedback);

    }

    private View.OnFocusChangeListener mOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus)
        {
            EditText textView = (EditText)v;
            String hint;
            if (hasFocus) {
                hint = textView.getHint().toString();
                textView.setTag(hint);
                textView.setHint("");
            } else {
                hint = textView.getTag().toString();
                textView.setHint(hint);
            }
        }
    };
}
