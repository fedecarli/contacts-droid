package com.rflpazini.contactlist.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rflpazini on 4/19/16.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, DbInfo.DB_NAME, null, DbInfo.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DbInfo.DbEntry.TABLE + " ( " +
                DbInfo.DbEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DbInfo.DbEntry.CONT_NAME + " TEXT NOT NULL, " +
                DbInfo.DbEntry.CONT_PHONE + " TEXT NOT NULL," +
                DbInfo.DbEntry.CONT_EMAIL + " TEXT NOT NULL);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DbInfo.DbEntry.TABLE);
        onCreate(db);
    }
}
