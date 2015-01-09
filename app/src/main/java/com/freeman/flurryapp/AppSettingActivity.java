package com.freeman.flurryapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import com.freeman.flurryapp.adapter.AppListAdapter;
import com.freeman.flurryapp.db.DbManager;
import com.freeman.flurryapp.db.DbObserver;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.manager.ThreadManager;

import java.util.ArrayList;

/**
 * Created by alberthe on 2014/12/19.
 */
public class AppSettingActivity extends ActionBarActivity implements AppListAdapter.ItemClickCallBack {
    private ListView mList;
    private AppListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_app_list);

        mList = (ListView) findViewById(R.id.app_list);
        mAdapter = new AppListAdapter(this,true);
        mAdapter.setItemClickCallBack(this);
        mList.setAdapter(mAdapter);

        DbManager.getManager().setDBObserver(mDbObserver);
        ThreadManager.executeOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                DbManager.getManager().queryAppList(true);
            }
        });
        //RequestManager.getManager().requestApplications(mAppRequestCallback,RequestData.API_ACCESS_CODE);
    }

    @Override
    protected void onDestroy() {
        DbManager.getManager().setDBObserver(null);

        super.onDestroy();
    }

    private DbObserver mDbObserver = new DbObserver() {

        @Override
        public void onQueryAppList(final ArrayList<FlurryApplication> data) {
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
        DbManager.getManager().setAppVisible(app, !app.visible);
        mAdapter.setItemVisible(position,!app.visible);
    }
}
