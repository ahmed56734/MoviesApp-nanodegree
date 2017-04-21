package com.example.ahmed.moviesapp_nanodegree.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.ahmed.moviesapp_nanodegree.data.MovieContract.MovieEntry;

/**
 * Created by ahmed on 4/19/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

         final String SQL_CREATE = "CREATE TABLE " + MovieEntry.TABLE_NAME +" ("+
                MovieEntry.COLUMN_ID  + " INT PRIMARY KEY, " +
                MovieEntry.COLUMN_TITLE + " TEXT, " +
                MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieEntry.COLUMN_SYNOPSIS + " TEXT, " +
                MovieEntry.COLUMN_VOTE_AVERAGE + " TEXT, " +
                MovieEntry.COLUMN_IMAGE_URL + " TEXT " +
                ");" ;

        Log.d("sql", SQL_CREATE);
        sqLiteDatabase.execSQL(SQL_CREATE);


    }

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public  boolean checkIfRecordAlreadyExists(String id){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MovieEntry.TABLE_NAME,
                null,
                MovieEntry.COLUMN_ID + " =?",
                new String[] {id},
                null,
                null,
                null);

        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();

        return exists;
    }
}
