package com.freeman.flurryapp;

import android.app.Application;

import com.freeman.flurryapp.db.DbManager;

/**
 * Created by alberthe on 2014/12/19.
 */
public class ApplicationImpl extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DbManager.init(this);
        RequestManager.getManager().requestApplications(null,RequestData.API_ACCESS_CODE);
    }
}
