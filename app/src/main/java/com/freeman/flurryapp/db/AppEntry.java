package com.freeman.flurryapp.db;

import android.provider.BaseColumns;

/**
 * Created by alberthe on 2014/12/19.
 */
public abstract class AppEntry  implements BaseColumns {

    public static final String TABLE_NAME = "flury_app";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_APIKEY = "apikey";
    public static final String COLUMN_NAME_CREATE_DATE = "createdDate";
    public static final String COLUMN_NAME_PLATFORM = "platform";
    public static final String COLUMN_NAME_VISIBLE = "visible";
}
