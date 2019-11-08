package com.modifica.creditcollectorapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelperForRoot extends SQLiteOpenHelper {


    public static final String DATABASE_NAME = "collaction_root.db";
    public static final String TABLE_NAME = "collaction_info";
    public static final String COL_01 = "Label";
    public static final String COL_02 = "Info";




    public DatabaseHelperForRoot(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+" (Label TEXT, Info TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);

    }

    public boolean insertFullData(String lable, String info ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_01,lable);
        contentValues.put(COL_02,info);
        long result =- db.insert(TABLE_NAME,null,contentValues);
        db.close();

        if (result == -1){
            return false;
        }else {
            return true;
        }
    }


    public boolean insertOneData(String id, String val){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_02,val);
        int result =- db.update(TABLE_NAME,contentValues,"Label = ?",new String[]{String.valueOf(id)});

        if (result < 0){
            return true;
        }else {
            return false;
        }
    }



    public Cursor getAllData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from "+ TABLE_NAME,null);
        return cursor;
    }
}
