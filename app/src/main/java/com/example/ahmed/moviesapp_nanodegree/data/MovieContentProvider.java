package com.example.ahmed.moviesapp_nanodegree.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.example.ahmed.moviesapp_nanodegree.data.MovieContract.MovieEntry;

/**
 * Created by ahmed on 4/19/17.
 */

public class MovieContentProvider extends ContentProvider {

    private MovieDbHelper mMovieDbHelper;

    private static final int MOVIES = 100;
    private static final int MOVIES_WITH_ID = 101;

    private static UriMatcher sUriMatcher = builUriMatcher();
    @Override
    public boolean onCreate() {

        mMovieDbHelper = new MovieDbHelper(getContext());
        return true;
    }

    private static UriMatcher  builUriMatcher(){

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIES_WITH_ID);

        return uriMatcher;
    }



    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values){

        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        Uri returnUri;

        switch (sUriMatcher.match(uri)){
            case MOVIES:
                Long id = db.insert(MovieEntry.TABLE_NAME, null, values);
                if(id == -1)
                    throw new android.database.SQLException("failed to insert row into " + uri);
                returnUri = ContentUris.withAppendedId(MovieEntry.CONTENT_URI, id);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = mMovieDbHelper.getReadableDatabase();
        Cursor cursor;
        switch (sUriMatcher.match(uri)){
            case MOVIES:
                cursor = db.query(MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,null,
                        sortOrder);
                break;


            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }





    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = mMovieDbHelper.getWritableDatabase();
        int deletedRows;

        switch (sUriMatcher.match(uri)){
            case MOVIES_WITH_ID:
                selection = MovieEntry.COLUMN_ID + " =?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};

                deletedRows = db.delete(MovieEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException();
        }

        if(deletedRows != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return deletedRows;
    }



    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw new UnsupportedOperationException("Unknown uri: " + uri);
    }


    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
