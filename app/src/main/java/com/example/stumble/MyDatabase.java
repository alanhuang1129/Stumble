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

    public long insertData (String name, String type, String image, boolean isClosed, double rating, String price, String location, double latitude, double longitude, double distance) {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.IMAGE_URL, image);
        contentValues.put(Constants.IS_CLOSED, isClosed);
        contentValues.put(Constants.RATING, rating);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.LATITUDE, latitude);
        contentValues.put(Constants.LONGITUDE, longitude);
        contentValues.put(Constants.DISTANCE, distance);
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }
    public Cursor getData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};
        Cursor cursor = db.query(Constants.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //Get the list of listings by type
    public List<Listing> getSelectedData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

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
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        return queriedRows;
    }
    //For Sensor feature
    public Listing getRandomListing() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.TYPE + "='" + "dining" + "'";
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
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        int listSize = queriedRows.size();
        if (listSize > 0) {
            int rand = (int)(Math.random()*(listSize-1));
            return queriedRows.get(rand);
        }
        else {
            return null;
        }
    }

    public List<Listing> getSelectedDataByName(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.NAME + "='" + name + "'";
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
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
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
    public int deleteRowByType(String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {type};
        int count = db.delete(Constants.TABLE_NAME, Constants.TYPE + "=?", whereArgs);
        return count;
    }

    //Saved Table Methods
    public long insertSavedData (String name, String type, String image, boolean isClosed, double rating, String price, String location, double latitude, double longitude, double distance) {
        db = helper.getWritableDatabase();
        //if it already exists in the database, do not insert (This doesn't work for some reason)
        if (getSelectedSavedData(name).size() > 0) {
            return 0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.IMAGE_URL, image);
        contentValues.put(Constants.IS_CLOSED, isClosed);
        contentValues.put(Constants.RATING, rating);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.LATITUDE, latitude);
        contentValues.put(Constants.LONGITUDE, longitude);
        contentValues.put(Constants.DISTANCE, distance);
        long id = db.insert(Constants.SAVED_TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getSavedData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};
        Cursor cursor = db.query(Constants.SAVED_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //Get the list of listings by type
    public List<Listing> getSelectedSavedData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.TYPE + "='" + type + "'";
        Cursor cursor = db.query(Constants.SAVED_TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE_URL);
            int index4 = cursor.getColumnIndex(Constants.IS_CLOSED);
            int index5 = cursor.getColumnIndex(Constants.RATING);
            int index6 = cursor.getColumnIndex(Constants.PRICE);
            int index7 = cursor.getColumnIndex(Constants.LOCATION);
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        return queriedRows;
    }

    public List<Listing> getSelectedSavedDataByName(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.NAME + "='" + name + "'";
        Cursor cursor = db.query(Constants.SAVED_TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE_URL);
            int index4 = cursor.getColumnIndex(Constants.IS_CLOSED);
            int index5 = cursor.getColumnIndex(Constants.RATING);
            int index6 = cursor.getColumnIndex(Constants.PRICE);
            int index7 = cursor.getColumnIndex(Constants.LOCATION);
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        return queriedRows;
    }
    //Delete row in database by name
    public int deleteSavedRowByName(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(Constants.SAVED_TABLE_NAME, Constants.NAME + "=?", whereArgs);
        return count;
    }
    public int deleteSavedRowByType(String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {type};
        int count = db.delete(Constants.SAVED_TABLE_NAME, Constants.TYPE + "=?", whereArgs);
        return count;
    }




    //Top Picks Table Methods
    public long insertTopPicksData (String name, String type, String image, boolean isClosed, double rating, String price, String location, double latitude, double longitude, double distance) {
        db = helper.getWritableDatabase();
        //if it already exists in the database, do not insert (This doesn't work for some reason)
        if (getSelectedTopPicksData(name).size() > 0) {
            return 0;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constants.NAME, name);
        contentValues.put(Constants.TYPE, type);
        contentValues.put(Constants.IMAGE_URL, image);
        contentValues.put(Constants.IS_CLOSED, isClosed);
        contentValues.put(Constants.RATING, rating);
        contentValues.put(Constants.PRICE, price);
        contentValues.put(Constants.LOCATION, location);
        contentValues.put(Constants.LATITUDE, latitude);
        contentValues.put(Constants.LONGITUDE, longitude);
        contentValues.put(Constants.DISTANCE, distance);
        long id = db.insert(Constants.TOP_PICKS_TABLE_NAME, null, contentValues);
        return id;
    }

    public Cursor getTopPicksData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};
        Cursor cursor = db.query(Constants.TOP_PICKS_TABLE_NAME, columns, null, null, null, null, null);
        return cursor;
    }

    //Get the list of listings by type
    public List<Listing> getSelectedTopPicksData(String type) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.TYPE + "='" + type + "'";
        Cursor cursor = db.query(Constants.TOP_PICKS_TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE_URL);
            int index4 = cursor.getColumnIndex(Constants.IS_CLOSED);
            int index5 = cursor.getColumnIndex(Constants.RATING);
            int index6 = cursor.getColumnIndex(Constants.PRICE);
            int index7 = cursor.getColumnIndex(Constants.LOCATION);
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        return queriedRows;
    }

    public List<Listing> getSelectedTopPicksDataByName(String name) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] columns = {Constants.NAME, Constants.TYPE, Constants.IMAGE_URL,
                Constants.IS_CLOSED, Constants.RATING, Constants.PRICE, Constants.LOCATION,
                Constants.LATITUDE, Constants.LONGITUDE, Constants.DISTANCE};

        String selection = Constants.NAME + "='" + name + "'";
        Cursor cursor = db.query(Constants.TOP_PICKS_TABLE_NAME, columns, selection, null, null, null, null);

        List<Listing> queriedRows = new ArrayList<Listing>();
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(Constants.NAME);
            int index2 = cursor.getColumnIndex(Constants.TYPE);
            int index3 = cursor.getColumnIndex(Constants.IMAGE_URL);
            int index4 = cursor.getColumnIndex(Constants.IS_CLOSED);
            int index5 = cursor.getColumnIndex(Constants.RATING);
            int index6 = cursor.getColumnIndex(Constants.PRICE);
            int index7 = cursor.getColumnIndex(Constants.LOCATION);
            int index8 = cursor.getColumnIndex(Constants.LATITUDE);
            int index9 = cursor.getColumnIndex(Constants.LONGITUDE);
            int index10 = cursor.getColumnIndex(Constants.DISTANCE);
            String activityName = cursor.getString(index1);
            String activityType = cursor.getString(index2);
            String activityImage = cursor.getString(index3);
            boolean activityIsClosed = cursor.getInt(index4) > 0;
            double activityRating = cursor.getDouble(index5);
            String activityPrice = cursor.getString(index6);
            String activityLocation = cursor.getString(index7);
            double activityLatitude = cursor.getDouble(index8);
            double activityLongitude = cursor.getDouble(index9);
            double activityDistance = cursor.getDouble(index10);
            queriedRows.add(new Listing(activityName, activityType, activityImage, activityIsClosed,
                    activityRating, activityPrice, activityLocation, activityLatitude, activityLongitude, activityDistance));
        }
        return queriedRows;
    }
    //Delete row in database by name
    public int deleteTopPicksRowByName(String name){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {name};
        int count = db.delete(Constants.TOP_PICKS_TABLE_NAME, Constants.NAME + "=?", whereArgs);
        return count;
    }
    public int deleteTopPicksRowByType(String type){
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] whereArgs = {type};
        int count = db.delete(Constants.TOP_PICKS_TABLE_NAME, Constants.TYPE + "=?", whereArgs);
        return count;
    }

    //Listing struct modelled towards an individual listing
    class Listing {
        public String lName;
        public String lType;
        public String lImageUrl;
        public boolean lIsClosed;
        public double lRating;
        public String lPrice;
        public String lLocation;
        public double lLatitude;
        public double lLongitude;
        public double lDistance;

        Listing(String name, String type, String imageUrl, boolean isClosed, double rating, String price, String location, double latitude, double longitude, double distance) {
            lName = name;
            lType = type;
            lImageUrl = imageUrl;
            lIsClosed = isClosed;
            lRating = rating;
            lPrice = price;
            lLocation = location;
            lLatitude = latitude;
            lLongitude = longitude;
            lDistance = distance;

            //Round distance
            lDistance = round(lDistance, 2);
        }
        //Rounding method
        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            long factor = (long) Math.pow(10, places);
            value = value * factor;
            long tmp = Math.round(value);
            return (double) tmp / factor;
        }
    }
}