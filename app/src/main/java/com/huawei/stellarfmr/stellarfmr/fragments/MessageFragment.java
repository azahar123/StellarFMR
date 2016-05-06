package com.huawei.stellarfmr.stellarfmr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.MessageActivity;
import com.huawei.stellarfmr.stellarfmr.adapter.JourneyAdapter;
import com.huawei.stellarfmr.stellarfmr.adapter.MessageAdapter;
import com.huawei.stellarfmr.stellarfmr.listdata.JourneyList;
import com.huawei.stellarfmr.stellarfmr.listdata.MessageList;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MessageFragment extends Fragment implements View.OnClickListener {

    ImageView profilePic, upArrowLeft, upArrowRight;
    TextView journey, message, profile_name, profile_location, loginTime;
    // String REGISTER_URL = "http://vaahancheckvm.cloudapp.net:122/api/Journey/GetJourneyByUserID?strUserName=clientlogin@gmail.com&strJourneyId=0&strJourneyDate=02-10-2015";
    String DEMO_URL = "http://www.infotrans.co.in/projects/boonspoon/php/getSQLData.php";
    String REGISTER_URL = "http://vaahancheckvm.cloudapp.net:122/api/Message/GetMessageByUserID?strUserName=semadmin5@gmail.com&strMessageId=0&strMessageDate=01-01-2015";
    ArrayList<String> name = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();

    ArrayList<String> name1 = new ArrayList<>();
    ArrayList<String> address1 = new ArrayList<>();
    ArrayList<String> time1 = new ArrayList<>();
    ArrayList<String> attachments = new ArrayList<>();
    ArrayList<String> msgId = new ArrayList<>();

    ArrayList<JourneyList> myList_J = new ArrayList<>();
    ArrayList<MessageList> myList_M = new ArrayList<>();
    ListView listView_J, listView_M;
    // ProgressDialog pDialog;
    ProgressDialog pDialog;
    JSONObject jsonObject;
    JSONArray jsonArray;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String token;
    private String jsonResponse;



    public MessageFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        profilePic = (ImageView) view.findViewById(R.id.profile_image);
        new DownloadImageTask(profilePic).execute(Config.userDetails.get(9));
        journey = (TextView) view.findViewById(R.id.tab_1);
        message = (TextView) view.findViewById(R.id.tab_2);
        message.setText("MESSAGES");

        upArrowLeft = (ImageView) view.findViewById(R.id.uparrow_left);
        upArrowRight = (ImageView) view.findViewById(R.id.uparrow_right);

        listView_J = (ListView) view.findViewById(R.id.journey_list);
        listView_M = (ListView) view.findViewById(R.id.message_list);

        profile_name = (TextView) view.findViewById(R.id.profile_name);
        profile_location = (TextView) view.findViewById(R.id.profile_location);
        loginTime = (TextView) view.findViewById(R.id.profile_login_time);

        profile_name.setText(Config.userDetails.get(0) + " " + Config.userDetails.get(2));
        profile_location.setText(""+Config.userDetails.get(4));
        //Toast.makeText(getActivity(),"value 4 "+Config.userDetails.get(5),Toast.LENGTH_LONG).show();
        String time = Config.userDetails.get(8);
        String[] timeArray = time.split(",");
        //loginTime.setText("Login Time: " + time.substring(25, 36));
        loginTime.setText("Login Time: " + timeArray[2].substring(5));
        try {
            String tag = getArguments().getString("tag");
            if (tag.contains("0")) {
                //Toast.makeText(getActivity(), tag, Toast.LENGTH_LONG).show();
                journey.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                message.setBackgroundColor(getResources().getColor(R.color.white));
                listView_J.setVisibility(View.GONE);
                listView_M.setVisibility(View.VISIBLE);
            } else {
                //Toast.makeText(getActivity(), tag, Toast.LENGTH_LONG).show();
                journey.setBackgroundColor(getResources().getColor(R.color.white));
                message.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                listView_J.setVisibility(View.VISIBLE);
                listView_M.setVisibility(View.GONE);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //bundle null
        }
        if (isNetworkAvailable()) {
            getData_J();
            getData_M();
        } else {
            showAlertDialog(Config.networkAlert,true);
        }

        journey.setOnClickListener(this);
        message.setOnClickListener(this);

        return view;
    }

    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        //Toast.makeText(getActivity(),MyCurrentDate,Toast.LENGTH_LONG).show();
        String j =Config.BASE_URL +"api/Journey/GetJourneyByUserID?strUserName="+Config.userDetails.get(10)+"&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14);
        //Toast.makeText(getActivity(),j,Toast.LENGTH_LONG).show();
        Log.d("url",j);
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14))
                //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", "0")
                .setBodyParameter("strJourneyDate", Config.userDetails.get(14))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        Log.d("JourneyList",result);
                        try {

                            jsonObject = new JSONObject(result);
                            jsonArray = jsonObject.getJSONArray("OutPutName");
                            if(jsonArray.length()<1)
                            {
                                for(int i=0;i<3;i++) {
                                    Toast toast = Toast.makeText(getActivity(), "No Journey Plan Available", Toast.LENGTH_LONG);
                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                    toast.show();
                                }
                            }
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject retailer = jsonArray.getJSONObject(i).getJSONObject("JourneyRetailerMBO");
                                JSONObject area = jsonArray.getJSONObject(i).getJSONObject("JourneyAreaMBO");
                                String city = jsonArray.getJSONObject(i).getJSONObject("JourneyCityMBO").getString("CityName");
                                String state = jsonArray.getJSONObject(i).getString("StateName");
                                JSONObject JdateTime = jsonArray.getJSONObject(i);
                                JSONObject JidObject = jsonArray.getJSONObject(i);
                                String complete_address = retailer.getString("RetailerAddress") + ", " + area.getString("AreaName") + ", "
                                        + city + ", " + state + " - " + area.getString("PinCode");
                                Log.d("RetailerAddress",retailer.getString("RetailerAddress"));
                                Log.d("city",city);
                                Log.d("state",state);

                                name.add(i, retailer.getString("RetailerName"));
                                address.add(i, complete_address);
                                String jdatetime = JdateTime.getString("strJourneyDate") + "/" + JdateTime.getString("JourneyTimeString");
                                time.add(i, jdatetime);
                                String journeyId1 = JidObject.getString("JourneyID");
                                String RetailerId1 = JidObject.getString("RetailerID");
                                Log.d("Retaid", RetailerId1);

                                journeyId.add(i, journeyId1);
                                retailerId.add(i, RetailerId1);

                                String journeyLoc = (retailer.getString("RetailerName") + ", " + retailer.getString("RetailerAddress") + ", " + area.getString("AreaName"));
                                Config.journeyList.add(i, journeyLoc.trim());

                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        getListViewData_J();
                        listView_J.setAdapter(new JourneyAdapter(getActivity(), myList_J));
                        listView_J.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Config.selectedJourney = position;
                                Config.journeyId = journeyId.get(position);
                                Config.retailerId = retailerId.get(position);
                                Config.defaultSpinnerSelecter=position;
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                Fragment fragment;
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                                String SystemCurrentDate = df.format(c.getTime());
                                fragment = new SiteInfoFragment();
                                transaction.replace(R.id.main_content, fragment);
                                //transaction.addToBackStack(null);
                                transaction.commit();
                            }
                        });
                        pDialog.hide();
                    }
                });
    }

    public void getData_M() {

//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
//        String SystemCurrentDate = df.format(c.getTime());
        Ion.with(getActivity())
                //.load("http://vaahancheckvm.cloudapp.net:122/api/Message/GetMessageByUserID?strUserName=fmr@gmail.com&strMessageId=0&strMessageDate=01-10-2015")
                .load(Config.BASE_URL + "api/Message/GetMessageByUserID?strUserName=" + Config.userDetails.get(10).trim() + "&strMessageId=0&strMessageDate="+Config.userDetails.get(14))
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                        .setBodyParameter("strMessageId", "0")
                        .setBodyParameter("strMessageDate", Config.userDetails.get(14))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {


                                try {
                                    Log.d("smsmyjson", result);
                                    jsonObject = new JSONObject(result);
                                    jsonArray = jsonObject.getJSONArray("OutPutMessage");

                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject c = jsonArray.getJSONObject(i);
                                        //JSONObject att = jsonArray.getJSONObject(i).getJSONObject("MessageAttachBO");
                                        //attachments.add(i,att.getString("MessageFilePath"));
                                        String smsid= c.getString("MessageID");
                                        msgId.add(i, smsid);
                                        name1.add(i, c.getString("MessageTitle"));
                                        address1.add(i, c.getString("MessageDescription"));
                                        time1.add(i, c.getString("strMessageDate"));
                                    }

                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }catch (NullPointerException ex)
                                {
                                    ex.printStackTrace();
                                }

                                getListViewData_M();
                                listView_M.setAdapter(new MessageAdapter(getActivity(), myList_M));
                                listView_M.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Intent intent = new Intent(getActivity(), MessageActivity.class);
                                        intent.putExtra("name", name1.get(position));
                                        intent.putExtra("description", address1.get(position));
                                        intent.putExtra("date", time1.get(position));
                                        intent.putExtra("smsid", msgId.get(position));
                                        getActivity().startActivity(intent);
                                    }
                                });

                            }
                        });
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {

            if (result != null) bmImage.setImageBitmap(result);
        }
    }

    private void getListViewData_J() {
        for (int i = 0; i < name.size(); i++) {
            JourneyList ld = new JourneyList();

            ld.setName(name.get(i));
            ld.setAddress(address.get(i));
            ld.setTime(time.get(i));
            ld.setjId(journeyId.get(i));
            ld.setrId(retailerId.get(i));

            myList_J.add(ld);
        }
    }

    private void getListViewData_M() {
        for (int i = 0; i < name1.size(); i++) {
            MessageList ld = new MessageList();

            ld.setName(name1.get(i));
            ld.setAddress(address1.get(i));
            ld.setTime(time1.get(i));
            ld.setSmsid(msgId.get(i));


            myList_M.add(ld);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_1:
                journey.setBackgroundColor(v.getResources().getColor(R.color.white));
                message.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);

                listView_J.setVisibility(View.VISIBLE);
                listView_M.setVisibility(View.GONE);

                break;
            case R.id.tab_2:
                message.setBackgroundColor(v.getResources().getColor(R.color.white));
                journey.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowRight.setImageResource(R.drawable.uparrow);
                upArrowLeft.setImageResource(R.drawable.blank);

                listView_J.setVisibility(View.GONE);
                listView_M.setVisibility(View.VISIBLE);
                break;

        }
    }

    public boolean hasActiveInternetConnection() {
        if (isNetworkAvailable()) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                //Log.e(LOG_TAG, "Error checking internet connection", e);
            }
        } else {
            //Log.d(LOG_TAG, "No network available!");
        }
        return false;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void detectNetwork() {
        if (!isNetworkAvailable()) {
            showAlertDialog(Config.networkAlert,false);
        }
    }

    public void showAlertDialog(String data, final boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setMessage(data);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        alertDialog.show();
    }
}
