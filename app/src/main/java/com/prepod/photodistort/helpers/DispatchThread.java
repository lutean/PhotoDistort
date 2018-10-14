package com.prepod.photodistort.helpers;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import java.util.concurrent.CountDownLatch;

/**
 * Created by Anton on 07.08.2018.
 */
public class DispatchThread extends Thread {

    private volatile Handler handler = null;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    public DispatchThread(final String threadName) {
        setName(threadName);
        start();
    }

    public void postRunnable(Runnable runnable) {
        postRunnable(runnable, 0);
    }

    public void postRunnable(Runnable runnable, long delay) {
        try {
            countDownLatch.await();
            if (delay <= 0)
                handler.post(runnable);
            else
                handler.postDelayed(runnable, delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

            }
        };
        countDownLatch.countDown();
        Looper.loop();
    }
}
