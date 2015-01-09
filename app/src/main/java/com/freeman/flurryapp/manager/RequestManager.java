package com.freeman.flurryapp.manager;

import android.util.Log;

import com.freeman.flurryapp.callback.RequestAppCallback;
import com.freeman.flurryapp.callback.RequestEventCallback;
import com.freeman.flurryapp.callback.RequestEventParamCallback;
import com.freeman.flurryapp.db.DbManager;
import com.freeman.flurryapp.entry.EventParam;
import com.freeman.flurryapp.entry.EventSummary;
import com.freeman.flurryapp.entry.FlurryApplication;
import com.freeman.flurryapp.entry.FlurryData;
import com.freeman.flurryapp.entry.RequestData;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    private static final String REQUEST_EVENT_SUMMARY = "http://api.flurry.com/eventMetrics/Summary?apiAccessCode=%s&apiKey=%s&startDate=%s&endDate=%s";
    private static final String REQUEST_EVENT_DETAIL = "http://api.flurry.com/eventMetrics/Event?apiAccessCode=%s&apiKey=%s&startDate=%s&endDate=%s&eventName=%s";
    // config the key
    private String apiKey = "";

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

    public void requestEventSummary(RequestEventCallback c, String apiCode){
        setRequestEventCallback(c);
        ThreadManager.executeOnWorkerThread(new RequestEventSummary(apiCode));
    }

    public void requestEventParam(RequestEventParamCallback c, String eventName){
        setRequestEventCallback(c);
        ThreadManager.executeOnWorkerThread(new RequestEventParam(eventName));
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

    public class RequestEventParam implements Runnable{
        private String eventName;

        public RequestEventParam(String name){
            eventName = name;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            String endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            String startDate = sdf.format(calendar.getTime());
            try {
                URL url = new URL(String.format(REQUEST_EVENT_DETAIL,RequestData.API_ACCESS_CODE,apiKey,startDate,endDate,eventName));
                urlConnection = (HttpURLConnection) url.openConnection();
                ArrayList<EventParam> data = JsonManager.getManager().parseToEventParams(urlConnection.getInputStream());
                if(mEventParamCallback != null){
                    mEventParamCallback.handleEventParam(data);
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

    public class RequestEventSummary implements Runnable{
        private String apiCode;

        public RequestEventSummary(String c){
            apiCode = c;
        }

        @Override
        public void run() {
            HttpURLConnection urlConnection = null;
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd");
            String endDate = sdf.format(calendar.getTime());
            calendar.add(Calendar.DAY_OF_YEAR, -7);
            String startDate = sdf.format(calendar.getTime());
            try {
                URL url = new URL(String.format(REQUEST_EVENT_SUMMARY,apiCode,apiKey,startDate,endDate));
                urlConnection = (HttpURLConnection) url.openConnection();
                ArrayList<EventSummary> data = JsonManager.getManager().parseToEventSummary(urlConnection.getInputStream());
                if(mEventCallback != null){
                    mEventCallback.handleEventSummary(data);
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
                DbManager.getManager().updateAppList(data);
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

    private RequestEventCallback mEventCallback = null;
    public void setRequestEventCallback(RequestEventCallback c){
        mEventCallback = c;
    }

    private RequestEventParamCallback mEventParamCallback = null;
    public void setRequestEventCallback(RequestEventParamCallback c){
        mEventParamCallback = c;
    }
}
