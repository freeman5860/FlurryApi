package com.freeman.flurryapp.callback;

import com.freeman.flurryapp.entry.EventSummary;

import java.util.ArrayList;

/**
 * Created by alberthe on 2015/1/7.
 */
public interface RequestEventCallback {
    public void handleEventSummary(ArrayList<EventSummary> summaryList);
}
