package com.freeman.flurryapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.freeman.flurryapp.entry.FlurryApplication;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by alberthe on 2014/12/19.
 */
public class DbManager {
    private static final String TAG = "DbManager";
    private static DbManager mInstance;

    private AppDbHelper mAppDbHelper;
    private DbObserver observer;

    public static void init(Context c){
        DbManager mgr = getManager();
        mgr.mAppDbHelper = new AppDbHelper(c);
    }

    private DbManager(){

    }

    public static DbManager getManager(){
        if (mInstance == null) {
            mInstance = new DbManager();
        }

        return mInstance;
    }

    public boolean updateAppList(ArrayList<FlurryApplication> data){
        HashSet<String> keySet = queryAppSet();
        HashSet<String> newKeySet = new HashSet<>();

        SQLiteDatabase db = mAppDbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            for(FlurryApplication app : data){
                ContentValues values = new ContentValues();
                values.put(AppEntry.COLUMN_NAME_NAME, app.name);
                values.put(AppEntry.COLUMN_NAME_APIKEY, app.apiKey);
                values.put(AppEntry.COLUMN_NAME_CREATE_DATE, app.createdDate);
                values.put(AppEntry.COLUMN_NAME_PLATFORM, app.platform);

                if(keySet.contains(app.apiKey)){
                    String selection = AppEntry.COLUMN_NAME_APIKEY + "=? ";
                    String[] selectionArgs = { app.apiKey };
                    db.update(AppEntry.TABLE_NAME, values, selection, selectionArgs);
                    Log.d(TAG,"update app " + app.name);
                }else {
                    values.put(AppEntry.COLUMN_NAME_VISIBLE, "1");
                    db.insert(AppEntry.TABLE_NAME, null, values);
                    Log.d(TAG,"insert app " + app.name);
                }

                newKeySet.add(app.apiKey);
            }

            for(String key : keySet){
                if(!newKeySet.contains(key)){
                    // 没了则删除
                    String where = AppEntry.COLUMN_NAME_APIKEY + "=? ";
                    String[] whereArgs = { key };

                    db.delete(AppEntry.TABLE_NAME,where,whereArgs);
                }
            }

            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }finally {
            db.endTransaction();
        }

        return true;
    }

    private HashSet<String> queryAppSet(){
        HashSet<String> data = new HashSet<String>();
        SQLiteDatabase db = mAppDbHelper.getReadableDatabase();

        String[] projection = {
                AppEntry.COLUMN_NAME_APIKEY,
        };

        Cursor c = null;
        try{
             c = db.query(
                    AppEntry.TABLE_NAME,
                    projection,
                    null,
                    null,
                    null,
                    null,
                    null
            );
            if(c != null && c.moveToFirst()) {
                do {
                    data.add(c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_APIKEY)));
                }while(c.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }

        Log.d(TAG,"queryAppSet success");

        return data;
    }

    public void queryAppList(boolean queryAll){
        ArrayList<FlurryApplication> data = new ArrayList<>();
        SQLiteDatabase db = mAppDbHelper.getReadableDatabase();

        String[] projection = {
                AppEntry._ID,
                AppEntry.COLUMN_NAME_NAME,
                AppEntry.COLUMN_NAME_APIKEY,
                AppEntry.COLUMN_NAME_CREATE_DATE,
                AppEntry.COLUMN_NAME_PLATFORM,
                AppEntry.COLUMN_NAME_VISIBLE,
        };

        String selection = AppEntry.COLUMN_NAME_VISIBLE + "=? ";
        String[] selectionArgs = { "1" };

        if(queryAll){
            selection = null;
            selectionArgs = null;
        }

        String sortOrder =
                AppEntry.COLUMN_NAME_NAME + " ASC";

        Cursor c = null;
        try{
            c = db.query(
                    AppEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
            );
            if(c != null && c.moveToFirst()) {
                do {
                    FlurryApplication app = new FlurryApplication();
                    app.name = c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_NAME));
                    app.apiKey = c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_APIKEY));
                    app.createdDate = c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_CREATE_DATE));
                    app.platform = c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_PLATFORM));
                    String visible = c.getString(c.getColumnIndex(AppEntry.COLUMN_NAME_VISIBLE));
                    app.visible = "1".equals(visible)? true : false;
                    data.add(app);
                    Log.d(TAG,app.name + " " + visible);
                }while(c.moveToNext());
            }
        }catch (Exception e){
            Log.e(TAG,e.toString());
        }finally {
            if(c != null && !c.isClosed()){
                c.close();
            }
        }

        Log.d(TAG,"queryAppList success");

        if(observer != null){
            observer.onQueryAppList(data);
        }
    }

    public void setAppVisible(FlurryApplication app, boolean visible){
        SQLiteDatabase db = mAppDbHelper.getWritableDatabase();

        try {
            db.beginTransaction();

            ContentValues values = new ContentValues();
            values.put(AppEntry.COLUMN_NAME_NAME, app.name);
            values.put(AppEntry.COLUMN_NAME_APIKEY, app.apiKey);
            values.put(AppEntry.COLUMN_NAME_CREATE_DATE, app.createdDate);
            values.put(AppEntry.COLUMN_NAME_PLATFORM, app.platform);
            values.put(AppEntry.COLUMN_NAME_VISIBLE, visible? "1" : "0");

            String selection = AppEntry.COLUMN_NAME_APIKEY + "=? ";
            String[] selectionArgs = { app.apiKey };
            db.update(AppEntry.TABLE_NAME, values, selection, selectionArgs);
            Log.d(TAG,"update app " + app.name);

            db.setTransactionSuccessful();
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }finally {
            db.endTransaction();
        }
    }

    public void setDBObserver(DbObserver o){
       this.observer = o;
    }
}
