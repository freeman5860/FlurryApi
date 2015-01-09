package com.freeman.flurryapp;


import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.support.v4.widget.DrawerLayout;

import com.freeman.flurryapp.manager.RequestManager;

public class FlurryAppActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = "FlurryAppActivity";

    public static final String ARG_API_KEY = "arg_apiKey";
    public static final String ARG_APP_NAME = "arg_appName";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private String appName;
    private String apiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flurry_app);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
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
    public void onNavigationDrawerItemSelected(int position) {
        if(position == 7){
            Intent intent = new Intent(this,FlurryEventActivity.class);
            intent.putExtra(FlurryEventActivity.ARG_API_KEY, apiKey);
            intent.putExtra(FlurryEventActivity.ARG_APP_NAME, appName);
            startActivity(intent);
            return;
        }

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1,getAction(position + 1)))
                .commit();
    }

    public void onSectionAttached(int number) {
        mTitle = getAction(number);
    }

    private String getAction(int number){
        switch (number) {
            case 1:
                return getString(R.string.title_section1);
            case 2:
                return getString(R.string.title_section2);
            case 3:
                return getString(R.string.title_section3);
            case 4:
                return getString(R.string.title_section4);
            case 5:
                return getString(R.string.title_section5);
            case 6:
                return getString(R.string.title_section6);
            case 7:
                return getString(R.string.title_section7);
        }

        return null;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
