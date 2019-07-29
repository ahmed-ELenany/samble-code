package com.formalin.formalin.myAppFormalin.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DataSource {

    private final Context context;
    private final DBHelper dbHelper;
    String TAG = "DataSource";
    // Database fields
    private SQLiteDatabase database;
    private String[] allColumns = {DBHelper.COLUMN_ID, DBHelper.POST_JSONOBJECT};


    public DataSource(Context context) {
        dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void deleteDB() {
        context.deleteDatabase(DBHelper.DATABASE_NAME);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }


}