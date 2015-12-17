package com.zczczy.by.xfy.items;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.FireInfo;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2015/11/12.
 * 火警 故障
 */
@EViewGroup(R.layout.fireinfo_item_layout)
public class FireInfoItemView extends  ItemView<FireInfo>{


    @ViewById
    TextView txt_bjsj,txt_ttwz,txt_cljg;
    @ViewById
    LinearLayout ll_ll;

    public FireInfoItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        int i=0;

        if(objects.length==3){
            i= (int) objects[1];
        }

        if(i%2==0){
            ll_ll.setBackgroundColor(Color.WHITE);
        }else {
            ll_ll.setBackgroundColor(Color.parseColor("#f5f5f5"));
        }

        if(_data.A_R_Time==null){
            txt_bjsj.setText(_data.A_Time.replace("/", "-"));
            txt_cljg.setTextColor(Color.parseColor("#f34541"));
        }else{
            txt_bjsj.setText(_data.A_R_Time.replace("/","-"));
            txt_cljg.setTextColor(Color.parseColor("#58c272"));
        }
        if (_data.f_desc==null){
            txt_ttwz.setText("监控室报警");
        }
        else {
            txt_ttwz.setText(_data.f_desc);
        }
        txt_cljg.setText(_data.A_desc);



    }



}
