package com.freeman.flurryapp.callback;

import com.freeman.flurryapp.entry.FlurryApplication;

import java.util.ArrayList;

/**
 * Created by alberthe on 2014/12/17.
 */
public interface RequestAppCallback {
    public void handleRequestApplications(ArrayList<FlurryApplication> data);
}
