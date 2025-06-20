package com.qppd.fifov2.Libs.Sqlz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private int DATABASE_VERSION;
    private String DATABASE_NAME;

    private SqlHandler sqlHandler;

    public DB(Context context, int DATABASE_VERSION, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.DATABASE_VERSION = DATABASE_VERSION;
        this.DATABASE_NAME = DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //db.execSQL();
        this.db = db;
        sqlHandler = new SqlHandler(db);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        sqlHandler = new SqlHandler(db);
        //db.execSQL("DROP TABLE IF EXISTS " + SERVER_TABLE);
        onCreate(db);
    }

}
