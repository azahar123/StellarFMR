package com.huawei.stellarfmr.stellarfmr.utils;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.view.Gravity;
import android.widget.Toast;

import com.huawei.stellarfmr.stellarfmr.activity.LoginActivity;

import static com.google.android.gms.internal.zzip.runOnUiThread;

/**
 * Created by ${Azahar} on 1/13/2016.
 */
public class ErrorControllerClass extends Application {
    // uncaught exception handler variable
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    private Thread.UncaughtExceptionHandler defaultUEH;

    // handler listener
    private Thread.UncaughtExceptionHandler _unCaughtExceptionHandler =
            new Thread.UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread thread, Throwable ex) {

                    // here I do logging of exception to a db
                    Toast toast = Toast.makeText(getBaseContext(),"Slow Internet Connection Issue", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    try {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getBaseContext(),"Slow Internet Connection Issue", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }
                        });
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    PendingIntent myActivity = PendingIntent.getActivity(getBaseContext(),
                            192837, new Intent(getBaseContext(), LoginActivity.class),
                            PendingIntent.FLAG_ONE_SHOT);


                    AlarmManager alarmManager;
                    alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                            15000, myActivity);
                    System.exit(2);



                    // re-throw critical exception further to the os (important)
                    defaultUEH.uncaughtException(thread, ex);
                }
            };

    public ErrorControllerClass() {
        defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        try {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getBaseContext(),"Slow Internet Connection Issue", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            });
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        // setup handler for uncaught exception
        Thread.setDefaultUncaughtExceptionHandler(_unCaughtExceptionHandler);
    }
}
