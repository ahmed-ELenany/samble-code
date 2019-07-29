package com.formalin.formalin.myAppFormalin.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;

public class DataProvider extends ContentProvider {
    public static final String POST_JSONOBJECT = "post_data";
    private static final String PROVIDER_NAME = "com.formalin.formalin.myAppFormalin.database.DataProvider";
    private static final String URL = "content://" + PROVIDER_NAME + "/sabil";
    public static final Uri CONTENT_URI = Uri.parse(URL);
    private static final int POSTS = 1;
    private static final int POSTS_ID = 2;

    private static final UriMatcher uriMatcher;
    private static final String DATABASE_NAME = "sabil";
    private static final String SABIL_TABLE_NAME = "posts";
    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "id";
    private static final String CREATE_DB_TABLE =
            "create table "
                    + SABIL_TABLE_NAME + "(" + COLUMN_ID
                    + " integer primary key autoincrement, " + POST_JSONOBJECT + " text not null" + ");";  //create table
    private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "movies", POSTS);
        uriMatcher.addURI(PROVIDER_NAME, "movies/#", POSTS_ID);
    }

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);

        db = dbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(SABIL_TABLE_NAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SABIL_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case POSTS:
                qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                break;

            case POSTS_ID:
                qb.appendWhere(COLUMN_ID + "=" + uri.getPathSegments().get(1));
                break;

            default:
        }


        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case POSTS:
                count = db.delete(SABIL_TABLE_NAME, selection, selectionArgs);
                break;

            case POSTS_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(SABIL_TABLE_NAME, COLUMN_ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values,
                      String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case POSTS:
                count = db.update(SABIL_TABLE_NAME, values, selection, selectionArgs);
                break;

            case POSTS_ID:
                count = db.update(SABIL_TABLE_NAME, values, COLUMN_ID + " = " + uri.getPathSegments().get(1) + (!TextUtils.isEmpty(selection) ?
                        "AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case POSTS:
                return "vnd.android.cursor.dir/vnd.example.movies";

            case POSTS_ID:
                return "vnd.android.cursor.item/vnd.example.movies";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SABIL_TABLE_NAME);
            onCreate(db);
        }
    }
}
