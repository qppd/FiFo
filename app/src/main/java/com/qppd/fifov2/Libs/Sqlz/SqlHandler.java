package com.qppd.fifov2.Libs.Sqlz;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SqlHandler {

    private SQLiteDatabase db;

    public SqlHandler(SQLiteDatabase db){
        this.db = db;
    }

    public long INSERT(String tablename, ContentValues contentValues) {

        long result = 0;

        try {
            result = db.insert(tablename, null, contentValues);
            db.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public long UPDATE(String tablename, ContentValues contentValues, String whereClause,
                        String[] whereArgs){

        long result = 0;

        try{
            result = db.update(tablename,contentValues,whereClause,whereArgs);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public long DELETE(String tablename, String whereClause, String[] whereArgs){

        long result = 0;

        try{
            result = db.delete(tablename, whereClause, whereArgs);
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }

        return result;
    }

    public Cursor SELECT(String query){

        Cursor cursor = null;

        try {
            cursor = db.rawQuery( query, null );
        } catch (Exception e){
            e.printStackTrace();
        }

        return cursor;
    }

}

//            if (cursor.getCount() > 0) {
//                while (cursor.moveToNext()) {
//                    info = cursor.getString( cursor.getColumnIndex( best_seller_item_code ) );
//                    list.add(info);
//
//                }
//            }
