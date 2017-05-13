package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static models.Person.*;

/**
 * Created by RENT on 2017-05-10.
 */


public class SqlHellper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mojaBaza.db";
    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_CREATE =
            "create table "
                    + TABLE_NAME + "( "
                    + ID + " integer primary key autoincrement, "
                    + NAME + " text not null,"
                    + RATING + " real not null,"
                    + PHONE + " text not null"
                    + ");";

    private static final String DATABASE_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SqlHellper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DATABASE_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}

