package com.mobile.agri10x.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "employee.db";
    public static final String LOCATION_TABLE_NAME = "location";
    public static final String LOCATION_COLUMN_ID = "id";
    public static final String LOCATION_COLUMN_NAME = "name";
    public static final String LOCATION_COLUMN_LATITUDE = "latitude";
    public static final String LOCATION_COLUMN_LONGITUDE = "longitude";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer primary key, name text,latitude text, longitude text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertLocation (String name, double latitude, double longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("latitude", latitude);
        contentValues.put("longitude", longitude);
        db.insert("contacts", null, contentValues);
        Log.e("Insert location", "true");
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
        return res;
    }

    public ArrayList<String> getAllData() {
        ArrayList<String> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from contacts", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(LOCATION_COLUMN_NAME)));
            res.moveToNext();
        }
        return array_list;
    }
}
