package com.freeman.flurryapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alberthe on 2014/12/15.
 */
public class JsonManager {
    private static final String TAG = "JsonManager" ;
    private static JsonManager mInstance;

    private JsonManager(){

    }

    public static JsonManager getManager(){
        if(mInstance == null){
            mInstance = new JsonManager();
        }

        return mInstance;
    }

    public ArrayList<FlurryApplication>  parseToFlurryApplication(InputStream is) throws IOException,JSONException{
        ArrayList<FlurryApplication> data = new ArrayList<FlurryApplication>();
        byte [] jsonData = inputStreamToByte(is);

        Log.d(TAG, new String(jsonData));
        JSONObject flurryObject = new JSONObject(new String(jsonData));

        Object appObject = flurryObject.get("application");
        if(appObject instanceof  JSONObject){
            JSONObject obj = (JSONObject)appObject;
            FlurryApplication app = new FlurryApplication();
            app.name = obj.getString("@name");
            app.apiKey = obj.getString("@apiKey");
            app.createdDate = obj.getString("@createdDate");
            app.platform = obj.getString("@platform");
            data.add(app);
        }else if(appObject instanceof  JSONArray){
            JSONArray jsonArray = (JSONArray) appObject;
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                FlurryApplication app = new FlurryApplication();
                app.name = obj.getString("@name");
                app.apiKey = obj.getString("@apiKey");
                app.createdDate = obj.getString("@createdDate");
                app.platform = obj.getString("@platform");
                data.add(app);
            }
        }
        return data;
    }

    public FlurryData  parseToFlurryData(InputStream is) throws IOException,JSONException{
        FlurryData data = new FlurryData();
        byte [] jsonData = inputStreamToByte(is);

        Log.d(TAG, new String(jsonData));
        JSONObject flurryObject = new JSONObject(new String(jsonData));
        data.metric = flurryObject.getString("@metric");
        data.dayValues = new ArrayList<FlurryDayValue>();

        Object dayObject = flurryObject.get("day");
        if(dayObject instanceof  JSONObject){
            JSONObject obj = (JSONObject)dayObject;
            FlurryDayValue dayValue = new FlurryDayValue();
            dayValue.date = obj.getString("@date");
            dayValue.value = obj.getString("@value");
            data.dayValues.add(dayValue);
        }else if(dayObject instanceof  JSONArray){
            JSONArray jsonArray = (JSONArray) dayObject;
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                FlurryDayValue dayValue = new FlurryDayValue();
                dayValue.date = obj.getString("@date");
                dayValue.value = obj.getString("@value");
                data.dayValues.add(dayValue);
            }
        }
        return data;
    }

    private byte[] inputStreamToByte(InputStream is) throws IOException{
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] jsonData = new byte[1024];
        int len = 0;
        while((len = is.read(jsonData)) != -1){
            os.write(jsonData,0,len);
        }
        is.close();
        os.close();
        return os.toByteArray();
    }

}
