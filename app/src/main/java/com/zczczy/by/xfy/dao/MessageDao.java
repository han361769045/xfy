package com.zczczy.by.xfy.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zczczy.by.xfy.model.PushMsg;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Created by zczczy on 2015/11/17.
 */
@EBean
public class MessageDao  {

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<PushMessage, Integer> iPushMessage;



    public int insertMessage(PushMessage pushMessage){
        int i=-1;
        try {
            i=iPushMessage.create(pushMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  i;
    }

    public void insertOrUpdate(List<PushMsg> pushMsgList){
        if(pushMsgList!=null){
            for (PushMsg p :pushMsgList ) {

                PushMessage pushMessage1=findMessage(p.key_id);
                if(pushMessage1==null||"".equals(pushMessage1.getKey_id())||pushMessage1.getKey_id()==null){
                    PushMessage pushMessage= new PushMessage();
                    pushMessage.setKey_id(p.key_id);
                    pushMessage.setSend_time(p.send_time);
                    pushMessage.setUsername(p.username);
                    pushMessage.setContent(p.content);
                    insertMessage(pushMessage);
                }
            }
        }
    }


    public  int updateMessage(int id,String readStatus){
        int i=-1;
        PushMessage pushMessage= findMessage(id);
        pushMessage.setReadStatus(readStatus);
        UpdateBuilder<PushMessage,Integer> ub= iPushMessage.updateBuilder();

        try {
            i=iPushMessage.update(pushMessage);
//            ub.updateColumnValue("readStatus",readStatus).where().idEq(id);
//            i=ub.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  i;
    }

    public  int updateMessage(String key_id,String readStatus){
        int i=-1;
        PushMessage pushMessage= findMessage(key_id);
        pushMessage.setReadStatus(readStatus);
        try {
            i=iPushMessage.update(pushMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  i;
    }

    public  int updateMessage(PushMessage pushMessage){
        int i=-1;
        try {
            i=iPushMessage.update(pushMessage);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  i;
    }




    public  int updateMessage(PushMsg pushMsg){
        int i=-1;
        PushMessage pushMessage= findMessage(pushMsg.key_id);

        if(pushMessage==null){
            pushMessage= new PushMessage();
            pushMessage.setSend_time(pushMsg.send_time);
            pushMessage.setContent(pushMsg.content);
            pushMessage.setUsername(pushMsg.username);
            pushMessage.setKey_id(pushMsg.key_id);
        }
        pushMessage.setReadStatus("1");
        try {
            i=iPushMessage.createOrUpdate(pushMessage).getNumLinesChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  i;
    }




    public  PushMessage findMessage(int id){
        PushMessage pushMessage=null;
        try {
            pushMessage= iPushMessage.queryForId(id);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  pushMessage;
    }

    public  PushMessage findMessage(String  key_id){
        PushMessage pushMessage=null;
        try {
            pushMessage= iPushMessage.queryBuilder().where().eq("key_id", key_id).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  pushMessage;
    }

    public int deleteMessage(int id){
        int i=-1;
        try {
            i=iPushMessage.deleteById(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    public List<PushMessage> getList(){
        List<PushMessage> list=null;
        try {
            list= iPushMessage.queryBuilder().orderBy("send_time",false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Collections.reverse(list);
        return list;
    }

    public long getStatus(){

        long count=0;
        try {
            count= iPushMessage.queryBuilder().where().eq("readStatus","0").countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }


}
