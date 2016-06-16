package com.damonw.nodejs.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Damon on 29/5/16.
 */
public class sqldb extends SQLiteOpenHelper {
    private static final int VERSION = 1;//資料庫版本

    //建構子
    public sqldb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public sqldb(Context context, String name) {
        this(context, name, null, VERSION);
    }

    public sqldb(Context context, String name, int version) {
        this(context, name, null, version);
    }

    //輔助類建立時運行該方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        String DATABASE_CREATE_TABLE ="create table if not exists token(id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL,token VARCHAR)";
        db.execSQL(DATABASE_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //oldVersion=舊的資料庫版本；newVersion=新的資料庫版本
        db.execSQL("DROP TABLE IF EXISTS token"); //刪除舊有的資料表
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    @Override
    public synchronized void close() {
        super.close();
    }


}