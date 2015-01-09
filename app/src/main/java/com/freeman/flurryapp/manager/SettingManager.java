package com.freeman.flurryapp.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by alberthe on 2015/1/4.
 */
public class SettingManager {

    public static final String PREF_FLURRY = "Flurry_Pref";
    public static final String KEY_FIRST_INIT = "Key_first_init";
    public static final String KEY_API_CODE = "Key_api_code";

    public static boolean getBooleanSetting(Context c,String pref,String key,boolean def){
        SharedPreferences sp = c.getSharedPreferences(pref,Context.MODE_PRIVATE);
        Log.e("hjy","" + sp.getBoolean(key,def) );
        return sp.getBoolean(key,def);
    }

    public static void setBooleanSetting(Context c,String pref, String key, boolean value){
        SharedPreferences sp = c.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public static String getStringSetting(Context c,String pref,String key,String def){
        SharedPreferences sp = c.getSharedPreferences(pref,Context.MODE_PRIVATE);
        return sp.getString(key,def);
    }

    public static void setStringSetting(Context c,String pref, String key, String value){
        SharedPreferences sp = c.getSharedPreferences(pref,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }
}
