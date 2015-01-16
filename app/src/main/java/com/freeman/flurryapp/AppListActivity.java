package com.freeman.flurryapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import com.freeman.flurryapp.adapter.AppListAdapter;
import com.freeman.flurryapp.callback.RequestCallBack;
import com.freeman.flurryapp.db.DbManager;
import com.freeman.flurryapp.db.DbObserver;
import com.freeman.flurryapp.entry.EventParam;
import com.freeman.flurryapp.entry.EventSummary;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.entry.FlurryData;
import com.freeman.flurryapp.entry.RequestData;
import com.freeman.flurryapp.manager.RequestManager;
import com.freeman.flurryapp.manager.SettingManager;
import com.freeman.flurryapp.manager.ThreadManager;

import java.util.ArrayList;

/**
 * Created by alberthe on 2014/12/18.
 */
public class AppListActivity extends ActionBarActivity implements AppListAdapter.ItemClickCallBack {

    private ListView mList;
    private AppListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        super.setContentView(R.layout.activity_app_list);

        mList = (ListView) findViewById(R.id.app_list);
        mAdapter = new AppListAdapter(this,false);
        mAdapter.setItemClickCallBack(this);
        mList.setAdapter(mAdapter);

        if(SettingManager.getBooleanSetting(this, SettingManager.PREF_FLURRY, SettingManager.KEY_FIRST_INIT, true)){
            createInputDlg();
        }else{
            RequestData.API_ACCESS_CODE = SettingManager.getStringSetting(AppListActivity.this,SettingManager.PREF_FLURRY,SettingManager.KEY_API_CODE,"");
        }
    }

    private void createInputDlg(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dlg_title);
        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setPositiveButton(R.string.dlg_positive_btn,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String apiCode = editText.getText().toString();
                RequestData.API_ACCESS_CODE = apiCode;
                RequestManager.getManager().requestApplications(mAppRequestCallback,apiCode);
            }
        });
        builder.setNegativeButton(R.string.dlg_negative_btn,new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        DbManager.getManager().setDBObserver(mDbObserver);
        ThreadManager.executeOnWorkerThread(new Runnable() {
            @Override
            public void run() {
                DbManager.getManager().queryAppList(false);
            }
        });
    }

    @Override
    protected void onPause() {
        DbManager.getManager().setDBObserver(null);
        super.onPause();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.flurry_app, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this,AppSettingActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private RequestCallBack mAppRequestCallback = new RequestCallBack() {
        @Override
        public void handleRequestData(FlurryData data) {

        }

        @Override
        public void handleRequestApplications(final ArrayList<FlurryApplication> data) {
            if(isFinishing())
                return;

            SettingManager.setBooleanSetting(AppListActivity.this,SettingManager.PREF_FLURRY,SettingManager.KEY_FIRST_INIT,false);
            SettingManager.setStringSetting(AppListActivity.this,SettingManager.PREF_FLURRY,SettingManager.KEY_API_CODE,RequestData.API_ACCESS_CODE);

            ThreadManager.executeOnWorkerThread(new Runnable() {
                @Override
                public void run() {
                    DbManager.getManager().queryAppList(false);
                }
            });
        }

        @Override
        public void handleEventSummary(ArrayList<EventSummary> summaryList) {

        }

        @Override
        public void handleEventParam(ArrayList<EventParam> paramList) {

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
