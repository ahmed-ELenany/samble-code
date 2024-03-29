package com.formalin.formalin.myAppFormalin.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "sabil.db";
    public static final String COLUMN_ID = "id";
    public static final String POST_JSONOBJECT = "post_data";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_Networks = "posts";
    private static final String DATABASE_CREATE = "create table "
            + TABLE_Networks + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + POST_JSONOBJECT + " text not null" + ");";  //create table
    private final String TAG = "Database Helper";
    private final Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d(TAG, "Creating Database");
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Upgrading Database");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_Networks);
        onCreate(db);
    }
}