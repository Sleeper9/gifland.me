package hu.braso.giflandme.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.androidannotations.annotations.EBean;

import java.sql.SQLException;

import hu.braso.giflandme.database.contract.GifContract;
import hu.braso.giflandme.model.Gif;

/**
 * Created by Illés László on 2015.12.13..
 */
@EBean(scope = EBean.Scope.Singleton)
public class GifDataSource {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public GifDataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void insert(Gif gif) {
        try{
            openWrite();

            ContentValues values = new ContentValues();

            values.put(GifContract.GifEntry._ID, gif.getId());
            values.put(GifContract.GifEntry.COLUMN_URL, gif.getUrl());
            values.put(GifContract.GifEntry.COLUMN_XXX, gif.isXxx() ? 1 : 0);
            values.put(GifContract.GifEntry.COLUMN_CONTENT, gif.getPath());

            database.insert(GifContract.GifEntry.TABLE_NAME, null,  values);
        } catch (SQLException e) {
            Log.e("[DB]", "Error during gif insert", e);
        } finally {
            close();
        }
    }

    public boolean exists(int id){
        try{
            openRead();

            Cursor query = database.query(GifContract.GifEntry.TABLE_NAME,
                    new String[]{GifContract.GifEntry._ID},
                    GifContract.GifEntry._ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    null);

            return query.getCount() > 0;
        } catch (SQLException e) {
            Log.e("[DB]", "Error during gif insert", e);
        } finally {
            close();
        }

        return false;
    }

    private void openWrite() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    private void openRead() throws SQLException {
        database = dbHelper.getReadableDatabase();
    }

    private void close() {
        dbHelper.close();
    }


}
