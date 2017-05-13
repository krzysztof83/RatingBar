package com.example.rc.samples.contentProvider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import helper.SqlHellper;
import models.Person;

/**
 * Created by RENT on 2017-05-13.
 */

public class PersonContentProvider extends ContentProvider {

    private SqlHellper database;

    private static final int PEOPLE=10;
    private static final int PERSON=20;

    private static final String ATHORITY="com.example.rc.samples.contentProvider";
    private static final String BASE_PATH= Person.TABLE_NAME;
    public static final Uri CONTENT_URI=Uri.parse("content://"+ATHORITY+"/"+BASE_PATH);

    public static final String CONTENT_TYPE= ContentResolver.CURSOR_DIR_BASE_TYPE+"/"+Person.TABLE_NAME;
    public static final String CONTENT_ITEM_TYPE=ContentResolver.CURSOR_ITEM_BASE_TYPE+"/person";

    private static final UriMatcher sURIMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(ATHORITY, BASE_PATH, PEOPLE);
        sURIMatcher.addURI(ATHORITY, BASE_PATH + "/#", PERSON);
    }

    @Override
    public boolean onCreate() {
        database=new SqlHellper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Person.TABLE_NAME);

        int uriType=sURIMatcher.match(uri);

        switch (uriType) {
            case PEOPLE:
                break;
            case PERSON:
                // adding the ID to the original query
                queryBuilder.appendWhere(Person.ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case PEOPLE:
                id = sqlDB.insert(Person.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case PEOPLE:
                rowsDeleted = sqlDB.delete(Person.TABLE_NAME, selection,
                        selectionArgs);
                break;
            case PERSON:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            Person.TABLE_NAME,
                            Person.ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            Person.TABLE_NAME,
                            Person.ID+ "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case PEOPLE:
                rowsUpdated = sqlDB.update(Person.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                break;
            case PERSON:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(Person.TABLE_NAME,
                            values,
                            Person.ID+ "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(Person.TABLE_NAME,
                            values,
                            Person.ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
