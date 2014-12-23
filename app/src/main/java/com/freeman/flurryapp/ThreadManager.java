package com.freeman.flurryapp;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * Created by alberthe on 2014/12/11.
 */
public class ThreadManager {
    private static HandlerThread WORKER_THREAD;

    private static Handler WORKER_THREAD_HANDLER;

    public static HandlerThread getWorkerThread(){
        if(WORKER_THREAD == null){
            getWorkerThreadHandler();
        }

        return WORKER_THREAD;
    }

    public static Handler getWorkerThreadHandler(){
        if (WORKER_THREAD == null){
            synchronized (ThreadManager.class){
                WORKER_THREAD = new HandlerThread("Network");
                WORKER_THREAD.start();
                WORKER_THREAD_HANDLER = new Handler(WORKER_THREAD.getLooper());
            }
        }

        return WORKER_THREAD_HANDLER;
    }

    public static Looper getWorkerThreadLooper(){
        return getWorkerThreadHandler().getLooper();
    }

    public static void executeOnWorkerThread(Runnable runnable){
        getWorkerThreadHandler().post(runnable);
    }
}
