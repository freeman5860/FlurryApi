package com.freeman.flurryapp.callback;

import com.freeman.flurryapp.entry.EventParam;

import java.util.ArrayList;

/**
 * Created by alberthe on 2015/1/8.
 */
public interface RequestEventParamCallback {
    public void handleEventParam(ArrayList<EventParam> paramList);
}
