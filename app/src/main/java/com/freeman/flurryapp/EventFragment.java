package com.freeman.flurryapp;

/**
 * Created by alberthe on 2014/12/15.
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.freeman.flurryapp.adapter.DayValueListAdapter;
import com.freeman.flurryapp.adapter.EventParamListAdapter;
import com.freeman.flurryapp.callback.RequestCallBack;
import com.freeman.flurryapp.entry.EventParam;
import com.freeman.flurryapp.entry.EventSummary;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.entry.FlurryData;
import com.freeman.flurryapp.manager.RequestManager;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class EventFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_EVENT_NAME = "event_name";

    private ListView mList;
    private EventParamListAdapter mAdapter;
    private String mEventName;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventFragment newInstance(String eventName) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_NAME, eventName);
        fragment.setArguments(args);
        return fragment;
    }

    public EventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flurry_app, container, false);
        mList = (ListView) rootView.findViewById(R.id.list_day_value);
        mAdapter = new EventParamListAdapter(getActivity());
        mList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mEventName = getArguments().getString(ARG_EVENT_NAME);
        RequestManager.getManager().requestEventParam(mEventParamCallBack,mEventName);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RequestManager.getManager().setRequestCallBack(null);
    }

    private RequestCallBack mEventParamCallBack = new RequestCallBack() {
        @Override
        public void handleRequestData(FlurryData data) {

        }

        @Override
        public void handleRequestApplications(ArrayList<FlurryApplication> data) {

        }

        @Override
        public void handleEventSummary(ArrayList<EventSummary> summaryList) {

        }

        @Override
        public void handleEventParam(final ArrayList<EventParam> paramList) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(paramList);
                }
            });
        }
    };
}
