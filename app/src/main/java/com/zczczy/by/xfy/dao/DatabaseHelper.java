package com.zczczy.by.xfy.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Leo on 2015/3/9.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    // 数据库名称
    private static final String DATABASE_NAME = "message.db";
    // 数据库version
    private static final int DATABASE_VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // 可以用配置文件来生成 数据表，有点繁琐，不喜欢用
        // super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }
    public DatabaseHelper(Context context, String databaseName,	SQLiteDatabase.CursorFactory factory, int databaseVersion) {
        super(context, databaseName, factory, databaseVersion);
        // TODO Auto-generated constructor stub
    }

    //建表
    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, PushMessage.class);
            Log.e(TAG,"数据库创建成功！！！！！！！！！！！！！！！！！！！！！！");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVersion,int newVersion) {
        try {
            TableUtils.clearTable(connectionSource,  PushMessage.class);
            Log.e(TAG, "数据库清空成功！！！！！！！！！！！！！！！！！！！！！！");
            onCreate(sqliteDatabase,connectionSource);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
    }

    public  void clearTab(){
        try {
            TableUtils.clearTable(getConnectionSource(), PushMessage.class);
            Log.e(TAG,"数据库清空成功！！！！！！！！！！！！！！！！！！！！！！");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG,e.getMessage());
        }
    }
}
