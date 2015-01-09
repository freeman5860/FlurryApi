package com.freeman.flurryapp.manager;

import android.util.Log;

import com.freeman.flurryapp.entry.EventParam;
import com.freeman.flurryapp.entry.EventSummary;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.entry.FlurryData;
import com.freeman.flurryapp.entry.FlurryDayValue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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

    public ArrayList<EventSummary>  parseToEventSummary(InputStream is) throws IOException,JSONException{
        ArrayList<EventSummary> data = new ArrayList<>();
        byte [] jsonData = inputStreamToByte(is);

        Log.d(TAG, new String(jsonData));
        JSONObject flurryObject = new JSONObject(new String(jsonData));

        Object appObject = flurryObject.get("event");
        if(appObject instanceof  JSONObject){
            JSONObject obj = (JSONObject)appObject;
            EventSummary event = new EventSummary();
            event.eventName = obj.getString("@eventName");
            data.add(event);
        }else if(appObject instanceof  JSONArray){
            JSONArray jsonArray = (JSONArray) appObject;
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                EventSummary event = new EventSummary();
                event.eventName = obj.getString("@eventName");
                data.add(event);
            }
        }
        return data;
    }

    public ArrayList<EventParam>  parseToEventParams(InputStream is) throws IOException,JSONException{
        ArrayList<EventParam> data = new ArrayList<>();
        byte [] jsonData = inputStreamToByte(is);

        Log.d(TAG, new String(jsonData));
        JSONObject flurryObject = new JSONObject(new String(jsonData));

        Object paraObj = flurryObject.get("parameters");
        if(paraObj instanceof  JSONObject){
            JSONObject paraJson = (JSONObject)paraObj;
            Object keyObj = paraJson.get("key");
            data.addAll(parseKeyJson(keyObj));
        }else if(paraObj instanceof  JSONArray){
            JSONArray jsonArray = (JSONArray) paraObj;
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject paraJson = jsonArray.getJSONObject(i);
                Object keyObj = paraJson.get("key");
                data.addAll(parseKeyJson(keyObj));
            }
        }

        return data;
    }

    private ArrayList<EventParam> parseKeyJson(Object keyObj) throws IOException,JSONException{
        ArrayList<EventParam> retData = new ArrayList<>();

        if (keyObj instanceof JSONObject) {
            JSONObject keyJson = (JSONObject) keyObj;
            String strKeyName = keyJson.getString("@name");
            Object valueObj = keyJson.get("value");
            retData.addAll(parseValueJson(valueObj,strKeyName));
        } else if (keyObj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) keyObj;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                String strKeyName = obj.getString("@name");
                Object valueObj = obj.get("value");
                retData.addAll(parseValueJson(valueObj,strKeyName));
            }
        }
        return retData;
    }

    private ArrayList<EventParam> parseValueJson(Object valueObj, String keyName) throws IOException,JSONException{
        ArrayList<EventParam> retData = new ArrayList<>();

        if (valueObj instanceof JSONObject) {
            EventParam event = new EventParam();
            event.key = keyName;
            event.name = ((JSONObject) valueObj).getString("@name");
            event.totalCount = ((JSONObject) valueObj).getString("@totalCount");
            retData.add(event);
        } else if (valueObj instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) valueObj;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                EventParam event = new EventParam();
                event.key = keyName;
                event.name = obj.getString("@name");
                event.totalCount = obj.getString("@totalCount");
                retData.add(event);
            }
        }
        return retData;
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

    public FlurryData parseToFlurryData(InputStream is) throws IOException,JSONException{
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
