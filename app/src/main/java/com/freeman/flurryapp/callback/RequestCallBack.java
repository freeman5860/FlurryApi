package com.freeman.flurryapp.callback;

import com.freeman.flurryapp.entry.EventParam;
import com.freeman.flurryapp.entry.EventSummary;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.entry.FlurryData;

import java.util.ArrayList;

/**
 * Created by freeman on 2015/1/16.
 */
public abstract interface RequestCallBack {
    public void handleRequestData(FlurryData data);
    public void handleRequestApplications(ArrayList<FlurryApplication> data);
    public void handleEventSummary(ArrayList<EventSummary> summaryList);
    public void handleEventParam(ArrayList<EventParam> paramList);
}
