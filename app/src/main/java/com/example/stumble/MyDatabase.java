package com.example.stumble;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MyDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final MyHelper helper;

    public MyDatabase(Context c) {
        context = c;
        helper = new MyHelper(context);
    }

    public long insertData (String name, String type, String location) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.LOCATION, location);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }
    public Cursor getData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.LOCATION};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    public List<Listing> getSelectedData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE};

        String selection = Constants.TYPE + "='" + type + "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
//            int index3 = cursor.getColumnIndex(Constants.LOCATION);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
//            String activityLocation = cursor.getString(index3);
            queriedRows.add(new Listing(activityName, activityType, "activityLocation"));
//            buffer.append(activityName + " " + activityType + " " + activityLocation + "\n");
        }
//        return buffer.toString();
        return queriedRows;
    }

    public int deleteRowByName(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(Constants.TABLE_NAME, Constants.NAME + "=?", whereArgs);
        return count;
    }

    class Listing {
        public String lName;
        public String lType;
        public String lLocation;
        Listing(String name, String type, String location) {
            lName = name;
            lType = type;
            lLocation = location;
        }
    }
}
