package com.freeman.flurryapp;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;

import com.freeman.flurryapp.manager.RequestManager;

public class FlurryEventActivity extends ActionBarActivity
        implements EventDrawerFragment.EventDrawerCallbacks {

    private static final String TAG = "FlurryEventActivity";

    public static final String ARG_API_KEY = "arg_apiKey";
    public static final String ARG_APP_NAME = "arg_appName";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private EventDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String appName;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flurry_event);

        mNavigationDrawerFragment = (EventDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        appName = getIntent().getStringExtra(ARG_APP_NAME);
        mTitle = appName;
        apiKey = getIntent().getStringExtra(ARG_API_KEY);
        RequestManager.getManager().setApiKey(apiKey);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(String eventName) {

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, EventFragment.newInstance(eventName))
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
