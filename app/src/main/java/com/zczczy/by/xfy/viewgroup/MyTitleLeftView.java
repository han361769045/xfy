package com.zczczy.by.xfy.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.listener.TitleClickListener;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2015/10/22.
 */
@EViewGroup(R.layout.titleleft_layout)
public class MyTitleLeftView extends LinearLayout {
    @ViewById
    TextView tv_title;

    @ViewById
    LinearLayout img_back, ll_other;

    @ViewById
    ImageView i_bg,in_bg;


    public MyTitleLeftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    TitleClickListener clickListener;

    public void setListener(TitleClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

//    public void setOther(String other) {
//        tv_other.setText(other);
//    }

    @Click
    public void img_back() {
        clickListener.backClick();
    }



    public void setImg_back() {
        i_bg.setImageBitmap(null);
    }

    public ImageView getI_bg() {
        return i_bg;
    }

    @Click
    public void ll_other() {
        clickListener.otherClick();
    }
    public void setImg_other() {
        in_bg.setImageBitmap(null);
    }
}
