package com.zczczy.by.xfy.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by zczczy on 2015/11/17.
 */
@DatabaseTable(tableName="tab_push_message")
public class PushMessage  implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(generatedId=true,useGetSet=true)
    private  int id;

    @DatabaseField(useGetSet=true,canBeNull=false)
    private String username;

    @DatabaseField(useGetSet=true,canBeNull=true)
    private String content;

    @DatabaseField(useGetSet=true,canBeNull=false)
    private String send_time;

    @DatabaseField(useGetSet=true,canBeNull=false)
    private String key_id;

    @DatabaseField(useGetSet=true,canBeNull=false,defaultValue = "0")
    private String readStatus;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSend_time() {
        return send_time;
    }

    public void setSend_time(String send_time) {
        this.send_time = send_time;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
