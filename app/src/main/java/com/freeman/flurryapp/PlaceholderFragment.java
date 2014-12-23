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

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_ACTION = "section_action";

    private ListView mList;
    private DayValueListAdapter mAdapter;
    private int mSectionNum;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber,String action) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putString(ARG_SECTION_ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_flurry_app, container, false);
        mList = (ListView) rootView.findViewById(R.id.list_day_value);
        mAdapter = new DayValueListAdapter(getActivity());
        mList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mSectionNum = getArguments().getInt(ARG_SECTION_NUMBER);
        ((FlurryAppActivity) activity).onSectionAttached(
                mSectionNum);
        String action = getArguments().getString(ARG_SECTION_ACTION);
        RequestManager.getManager().requestData(mCallBack,action);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RequestManager.getManager().setRequestCallBack(null);
    }

    private RequestManager.RequestCallBack mCallBack = new RequestManager.RequestCallBack() {
        @Override
        public void handleRequestData(final FlurryData data) {
            if(getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(data.dayValues);
                    }
                });
            }
        }
    };
}
