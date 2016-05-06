package com.huawei.stellarfmr.stellarfmr.myalarmservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.huawei.stellarfmr.stellarfmr.currentlocation.GPSTracker;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by ${Azahar} on 1/6/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    GPSTracker gps;
    String token;
    SharedPreferences sP;


    @Override
    public void onReceive(Context context, Intent intent) {

        // For our recurring task, we'll just display a message
       // Toast.makeText(context, "Location Tracking", Toast.LENGTH_SHORT).show();
//        Intent i = new Intent(context, GPSTracker.class);
//        context.startService(i);
         gps = new GPSTracker(context);
        sP = context.getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");

       // Toast.makeText(context, "Lat"+gps.getLatitude()+"Long "+gps.getLongitude(), Toast.LENGTH_SHORT).show();
        try{
            Log.d("mylocation", " lat " + gps.getLatitude() + "Log " + gps.getLongitude());
            if(gps.getLatitude()!=0) {
                setMyLocation(context);
            }
        }
        catch (IndexOutOfBoundsException e)
        {e.printStackTrace();}
        catch (Exception e){e.printStackTrace();}
    }

    private void setMyLocation(Context context) {

        Ion.with(context)
                .load(Config.BASE_URL + "api/Location/PostLocation")
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("SetMethod", "INSERT ")//"test@email.com")//username)  username)//
                .setBodyParameter("CreatedDate", Config.userDetails.get(14))//"Test@123")//password)  password)//
                .setBodyParameter("LocationID", "0")
                .setBodyParameter("FieldManagerEmailID", Config.userDetails.get(10))
                .setBodyParameter("Latitude", gps.getLatitude() + "")
                .setBodyParameter("Longitude", gps.getLongitude() + "")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            Log.d("coordinatesresponse", result);
                        }
                        catch (Exception e1){e1.printStackTrace();}

                    }
                });
    }
}