package hu.braso.giflandme.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import hu.braso.giflandme.database.contract.GifContract;

import static hu.braso.giflandme.database.contract.GifContract.*;

/**
 * Created by Illés László on 2015.12.13..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "giflandme.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + GifEntry.TABLE_NAME + " (" +
                    GifEntry._ID + " INTEGER PRIMARY KEY," +
                    GifEntry.COLUMN_URL + " TEXT NOT NULL, " +
                    GifEntry.COLUMN_XXX + " INTEGER NOT NULL, " +
                    GifEntry.COLUMN_CONTENT + " TEXT NOT NULL)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
}
