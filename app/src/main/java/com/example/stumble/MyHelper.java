package com.example.stumble;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String CREATE_TABLE =
            "CREATE TABLE " +
                    Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.IMAGE_URL + " TEXT, " +
                    Constants.IS_CLOSED + " BOOLEAN, " +
                    Constants.RATING + " DOUBLE, " +
                    Constants.PRICE + " TEXT, " +
                    Constants.LOCATION + " TEXT, " +
                    Constants.LATITUDE + " DOUBLE, " +
                    Constants.LONGITUDE + " DOUBLE, " +
                    Constants.DISTANCE + " DOUBLE);" ;
    private static final String CREATE_SAVED_TABLE =
            "CREATE TABLE " +
                    Constants.SAVED_TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.NAME + " TEXT, " +
                    Constants.TYPE + " TEXT, " +
                    Constants.IMAGE_URL + " TEXT, " +
                    Constants.IS_CLOSED + " BOOLEAN, " +
                    Constants.RATING + " DOUBLE, " +
                    Constants.PRICE + " TEXT, " +
                    Constants.LOCATION + " TEXT, " +
                    Constants.LATITUDE + " DOUBLE, " +
                    Constants.LONGITUDE + " DOUBLE, " +
                    Constants.DISTANCE + " DOUBLE);" ;


    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;
    private static final String DROP_SAVED_TABLE = "DROP TABLE IF EXISTS " + Constants.SAVED_TABLE_NAME;

    public MyHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
            db.execSQL(CREATE_SAVED_TABLE);
        }
        catch (SQLException sqle){
            Log.e("error", "Failed to create table");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            db.execSQL(DROP_SAVED_TABLE);
            onCreate(db);
        }
        catch (SQLException sqle) {
            Log.e("error", "onUpgrade exception");
        }
    }
}
