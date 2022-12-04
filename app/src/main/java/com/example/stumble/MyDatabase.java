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

    public long insertData (String name, String type, String image, boolean isClosed, double rating, String price, String location, double distance) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.IMAGE_URL, image);
        contentValues.put(Constants.IS_CLOSED, isClosed);
        contentValues.put(Constants.RATING, rating);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.DISTANCE, distance);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }
    public Cursor getData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.UID, Constants.NAME, Constants.TYPE, Constants.LOCATION};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //Get the list of listings by type
    public List<Listing> getSelectedData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
        Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION, Constants.DISTANCE};

        String selection = Constants.TYPE + "='" + type + "'";
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE_URL);
            int index4 = cursor.getColumnIndex(Constants.IS_CLOSED);
            int index5 = cursor.getColumnIndex(Constants.RATING);
            int index6 = cursor.getColumnIndex(Constants.PRICE);
            int index7 = cursor.getColumnIndex(Constants.LOCATION);
            int index8 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            String activityIsClosed = cursor.getString(index4);
            String activityRating = cursor.getString(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            String activityDistance = cursor.getString(index8);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityDistance));
        }
        return queriedRows;
    }

    //Delete row in database by name
    public int deleteRowByName(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(Constants.TABLE_NAME, Constants.NAME + "=?", whereArgs);
        return count;
    }

    //Listing struct modelled towards an individual listing
    class Listing {
        public String lName;
        public String lType;
        public String lImageUrl;
        public String lIsClosed;
        public String lRating;
        public String lPrice;
        public String lLocation;
        public String lDistance;

        Listing(String name, String type, String imageUrl, String isClosed, String rating, String price, String location, String distance) {
            lName = name;
            lType = type;
            lImageUrl = imageUrl;
            lIsClosed = isClosed;
            lRating = rating;
            lPrice = price;
            lLocation = location;
            lDistance = distance;
        }
    }
}