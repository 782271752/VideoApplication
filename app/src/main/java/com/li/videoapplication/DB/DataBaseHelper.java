package com.li.videoapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static int VERSION = 5;
    /**
     * 控制设备的编号
     */

    public DataBaseHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public DataBaseHelper(Context context, String name, int version) {
        this(context, name, null, version);
        VERSION = version;
    }

    public DataBaseHelper(Context context, String name) {
        this(context, name, VERSION);
    }

    //	初始化数据库信息
    @Override
    public void onCreate(SQLiteDatabase db) {
        //下载的视频
        db.execSQL("create table video(id INTEGER PRIMARY KEY AUTOINCREMENT,video_id varchar(50),name varchar(50),imgUrl varchar(100),playUrl varchar(50))");
        //录制的视频
        db.execSQL("create table transcribe(id INTEGER PRIMARY KEY AUTOINCREMENT,name varchar(50),path varchar(100),time varchar(50))");
        //观看记录
        db.execSQL("create table record(id INTEGER PRIMARY KEY AUTOINCREMENT,video_id varchar(50),name varchar(50),flag varchar(100),time varchar(50)," +
                "flower varchar(50),comment varchar(50),view varchar(50))");

    }

    @Override
    // 数据库版本升级
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        System.out.println("Upgrade The Old DataBase... " + oldVersion + " to " + newVersion);
    }

}
