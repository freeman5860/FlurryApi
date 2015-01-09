package com.freeman.flurryapp.db;

import com.freeman.flurryapp.entry.FlurryApplication;

import java.util.ArrayList;

/**
 * Created by alberthe on 2014/12/19.
 */
public interface DbObserver {
    public void onQueryAppList(ArrayList<FlurryApplication> data);
}
