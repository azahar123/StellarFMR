package com.huawei.stellarfmr.stellarfmr.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;

public class Config

{
    //-----------------------------------------------------------
    //live url below
    //public static String BASE_URL = "http://vaahancheckvm.cloudapp.net:122/";
    //----------------------------------------------
    //Development url below
    public static String BASE_URL = "http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/";
    public static String URL_BASE_LOGIN = "token?username=test@email.com&password=Test@123&grant_type=password";
   // public static String URL_JOURNEY = "api/Journey/GetJourneyByUserID?strUserName=clientlogin@gmail.com&strJourneyId=0&strJourneyDate=";
    public static ArrayList<String> siteInfoImages = new ArrayList<>();
    public static ArrayList<String> shelfInfoImages = new ArrayList<>();
    public static ArrayList<String> posmImages = new ArrayList<>();
    public static ArrayList<String> competitorinfoImages = new ArrayList<>();
    public static ArrayList<String> brandingImages = new ArrayList<>();
    public static ArrayList<String> journeyList = new ArrayList<>();
    public static int selectedJourney = 0;
    public static ArrayList<String> userDetails = new ArrayList<>();
    public static Boolean siteInfo = false;
    public static Boolean shelfSpace = false;
    public static Boolean posm = false;
    public static Boolean branding = false;
    public static Boolean competitorinfo = false;
    public static ArrayList<String> SalesInfoImages = new ArrayList<>();
    public static Boolean SalesSpace = false;
    public static String networkAlert = "Network not Available, Please Connect to internet";
    public static String FORGOT_PASSWORD = "api/Account/ForgotPassword";
    public static String CHANGE_PASSWORD = "api/Account/ChangePassword";
    public static String journeyId;
    public static String retailerId;
    public static  int defaultSpinnerSelecter;
    public static  int imageInternetCount;
    public static  int posmImageCount;
    public static  int brandingImageCount;
    public static String ToastAllFieldsRequired="Fill all Fields";

        public static void clearAllData() {
        siteInfoImages.clear();
    }
    public static void dialog(Context c,String value,String title) {
        AlertDialog alertDialog = new AlertDialog.Builder(c).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(value);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
    public static boolean isNetworkAvailable(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}

