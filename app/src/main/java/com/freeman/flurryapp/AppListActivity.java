package com.freeman.flurryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by alberthe on 2014/12/18.
 */
public class AppListActivity extends Activity implements AppListAdapter.ItemClickCallBack {

    private ListView mList;
    private AppListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_app_list);

        mList = (ListView) findViewById(R.id.app_list);
        mAdapter = new AppListAdapter(this);
        mAdapter.setItemClickCallBack(this);
        mList.setAdapter(mAdapter);

        RequestManager.getManager().requestApplications(mAppRequestCallback,RequestData.API_ACCESS_CODE);
    }

    private RequestAppCallback mAppRequestCallback = new RequestAppCallback() {
        @Override
        public void handleRequestApplications(final ArrayList<FlurryApplication> data) {
            if(isFinishing())
                return;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.setData(data);
                }
            });
        }
    };

    @Override
    public void onItemClick(int position) {
        FlurryApplication app = (FlurryApplication)mAdapter.getItem(position);
        Intent intent = new Intent(this, FlurryAppActivity.class);
        intent.putExtra(FlurryAppActivity.ARG_API_KEY, app.apiKey);
        intent.putExtra(FlurryAppActivity.ARG_APP_NAME, app.name);
        startActivity(intent);
    }
}
