package com.zczczy.by.xfy.items;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zczczy.by.xfy.R;
import com.zczczy.by.xfy.model.Insp;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

/**
 * Created by zczczy on 2015/11/13.
 * 巡检查岗item
 */
@EViewGroup(R.layout.fireinfo_item_layout)
public class InspItemView extends  ItemView<Insp> {
    @ViewById
    TextView txt_bjsj,txt_ttwz,txt_cljg;
    @ViewById
    LinearLayout ll_ll;

    public InspItemView(Context context) {
        super(context);
    }

    @Override
    protected void init(Object... objects) {
        int i=0;
        String type="0";
        if(objects.length==3){
            i= (int) objects[1];
            type= (String) objects[2];
        }
        if (type.equals("1")){
            txt_cljg.setText(_data.c_result);
            txt_cljg.setTextColor(Color.parseColor("#58c272"));

        }  else {
            txt_cljg.setText(_data.c_result);
            txt_cljg.setTextColor(Color.parseColor("#f34541"));
        }
        txt_ttwz.setText(_data.c_time.replace("/","-"));
        txt_bjsj.setText(_data.c_send_time.replace("/","-"));
        if(i%2==0){
            ll_ll.setBackgroundColor(Color.WHITE);
        }else {
            ll_ll.setBackgroundColor(Color.parseColor("#f5f5f5"));
        }
    }


}
