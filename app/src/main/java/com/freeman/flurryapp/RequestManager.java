package com.freeman.flurryapp;

import android.util.Log;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by alberthe on 2014/12/15.
 */
public class RequestManager {
    private static final String TAG = "RequestManager";
    private static RequestManager mInstance;

    private RequestManager(){

    }

    public static RequestManager getManager(){
        if (mInstance == null) {
            mInstance = new RequestManager();
        }

        return mInstance;
    }

    private static final String REQUEST_URL = "http://api.flurry.com/appMetrics/%s?apiAccessCode=%s&apiKey=%s&startDate=%s&endDate=%s";
    private static final String REQUEST_ALL_APPLICATIONS = "http://api.flurry.com/appInfo/getAllApplications?apiAccessCode=%s";
    private String apiKey = "HP7PSZ2VYZMX5V5K8MS7";

    public void setApiKey(String key){
        apiKey = key;
    }

    private String constructURL(RequestData request){
        if(request == null){
            Log.e(TAG,"request is null!");
            return null;
        }
        String url = String.format(REQUEST_URL,request.metricName,request.apiAccessCode,request.apiKey,request.startDate,request.endDate);
        return url;
    }

    public void requestApplications(RequestAppCallback c, String apiCode){
        setRequestAppCallback(c);
        ThreadManager.executeOnWorkerThread(new RequestApplication(apiCode));
    }

    public void requestData(RequestCallBack c, String metric){
        RequestData request = new RequestData();
        request.metricName =  metric;
        request.apiAccessCode = RequestData.API_ACCESS_CODE;
        request.apiKey = apiKey;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
        request.endDate = sdf.format(calendar.getTime());
        calendar.add(Calendar.DAY_OF_YEAR, -7);
        request.startDate = sdf.format(calendar.getTime());
        setRequestCallBack(c);
        ThreadManager.executeOnWorkerThread(new RequestRunnable(request));
    }

    public class RequestApplication implements Runnable{
        private String apiCode;

        public RequestApplication(String c){
            apiCode = c;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(String.format(REQUEST_ALL_APPLICATIONS,apiCode));
                urlConnection = (HttpURLConnection) url.openConnection();
                ArrayList<FlurryApplication> data = JsonManager.getManager().parseToFlurryApplication(urlConnection.getInputStream());
                if(mAppCallback != null){
                    mAppCallback.handleRequestApplications(data);
                }
            }catch (MalformedURLException ex){
                Log.e(TAG, ex.toString());
            }catch (IOException ex){
                Log.e(TAG,ex.toString());
            }catch(JSONException e){
                Log.e(TAG,e.toString());
            } finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
        }
    }

    public class RequestRunnable implements Runnable{

        private RequestData mRequest;

        public RequestRunnable(RequestData r){
            mRequest = r;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(constructURL(mRequest));
                urlConnection = (HttpURLConnection) url.openConnection();
                FlurryData data = JsonManager.getManager().parseToFlurryData(urlConnection.getInputStream());
                if(mCallBack != null){
                    mCallBack.handleRequestData(data);
                }
            }catch (MalformedURLException ex){
                Log.e(TAG, ex.toString());
            }catch (IOException ex){
                Log.e(TAG,ex.toString());
            }catch(JSONException e){
                Log.e(TAG,e.toString());
            } finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
        }
    }

    public interface RequestCallBack{
        public void handleRequestData(FlurryData data);
    }

    private RequestCallBack mCallBack = null;
    public void setRequestCallBack(RequestCallBack c){
        mCallBack = c;
    }

    private RequestAppCallback mAppCallback = null;
    public void setRequestAppCallback(RequestAppCallback c){mAppCallback = c;}
}
