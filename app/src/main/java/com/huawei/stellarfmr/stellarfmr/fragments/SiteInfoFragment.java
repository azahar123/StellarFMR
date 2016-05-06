package com.huawei.stellarfmr.stellarfmr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.CameraActivity;
import com.huawei.stellarfmr.stellarfmr.activity.HomePageActivity;
import com.huawei.stellarfmr.stellarfmr.adapter.DropDownJourneyAdapter;
import com.huawei.stellarfmr.stellarfmr.listdata.DropDownJourneyList;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.internal.zzip.runOnUiThread;

public class SiteInfoFragment extends Fragment implements View.OnClickListener {

    public static ImageView image;
    static LinearLayout insertPoint;
    String myRetailerId;
    static int count[] = {2, 9, 9, 9, 9, 8, 9, 7, 7, 8,1,9,8,4, 9, 9, 9, 9, 8, 9, 7, 7, 8,1,9,8,4};
    static EditText ed;
    static List<EditText> allEds = new ArrayList<EditText>();
    TextView header, update, photo, journeyLoc;
    EditText name, number, comments;
    ProgressDialog newpDialog;
    String sitevisitID;
    JSONArray jsonArray, jsonArray1;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String token;
    String siteVisitID;
    String imageCaption;
    JSONObject jsonObject, imagePath;
    String TAG_QUERY = "INSERT";
    String TAG_SITEVISIT_ID = "0";
    public static int jsonMainLoop;
    int totaldeviceimages;
    public static int totalInternetImages=0;
    public static int resetvalue=0;
    ProgressDialog pDialog,pd,pDialogUploadImage;
    int flag;
    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    ListView listView_J;
    Spinner spinner;
    int initposition=0;
    boolean Spinnerflag=false;



    public SiteInfoFragment() {
        // Required empty public constructor
    }

    public static void reloadData(Context context) {


        for (int i = 0; i < Config.siteInfoImages.size(); i++) {
            jsonMainLoop++;
            try {
                if (i > (insertPoint.getChildCount()-totalInternetImages) - 1)
                {
                    LinearLayout layout2 = new LinearLayout(context);
                    layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                    layout2.setOrientation(LinearLayout.HORIZONTAL);
                    layout2.setWeightSum(3);
                    //***** Thumbnail
                    LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                    lparams2.setMargins(10, 10, 10, 10);
                    image = new ImageView(context);
                    image.setLayoutParams(lparams2);

                    final int THUMBSIZE = 256;
                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Config.siteInfoImages.get(i)),
                            THUMBSIZE, THUMBSIZE);

                    image.setImageBitmap(ThumbImage);

    //***** Thumbnail
                    //***** Add Caption
                    LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                    lparams3.setMargins(10, 10, 10, 10);

                    ed = new EditText(context);
                    //ed.setVisibility(View.GONE);
                    allEds.add(ed);

                    ed.setLayoutParams(lparams3);
                    ed.setId(123 + count[i]);
                    ed.setHint("Add caption");
                    //ed.setVisibility(View.INVISIBLE);

    //***** Add Caption

                    layout2.addView(image);
                    layout2.addView(ed);

                    insertPoint.addView(layout2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void reloadImage() {
        for (int i = 0; i < Config.siteInfoImages.size(); i++) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_site_info, container, false);
        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        header = (TextView) view.findViewById(R.id.header_text);
        update = (TextView) view.findViewById(R.id.update);
        header.setText("SITE VISIT INFO");
        Config.siteInfoImages.clear();
        allEds.clear();
        spinner = (Spinner) view.findViewById(R.id.spinner);

        //journeyLoc = (TextView) view.findViewById(R.id.location_text);
        //journeyLoc.setText(Config.journeyList.get(Config.selectedJourney));

        // Use this for ...journey Id Config.journeyId
        //Toast.makeText(getActivity(),Config.journeyId+Config.retailerId,Toast.LENGTH_LONG).show();;
        String retailerId = Config.retailerId;
        String username = Config.userDetails.get(10);

        name = (EditText) view.findViewById(R.id.contact_person);
        number = (EditText) view.findViewById(R.id.phone_no);
        number.addTextChangedListener(watch);
        comments = (EditText) view.findViewById(R.id.site_add_comments);

        photo = (TextView) view.findViewById(R.id.site_photo);

        insertPoint = (LinearLayout) view.findViewById(R.id.site_insert_point);
        totalInternetImages=0;


        reloadData(getActivity());


        /*
       Get EditText Data

       String[] strings = new String[](allEds.size());

        for(int i=0; i < allEds.size(); i++){
            string[i] = allEds.get(i).getText().toString();
        }*/
        update.setOnClickListener(this);
        photo.setOnClickListener(this);
        //LoadingAndCheckingPreviousData(retailerId, username, mydate);
        if(Config.isNetworkAvailable(getActivity())) {
            getData_J();
        }
        else
        {
            Config.dialog(getActivity(),Config.networkAlert,"Alert");
        }

        return view;
    }
    TextWatcher watch = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 10){

                Config.dialog(getActivity(),"Please enter valid mobile number.","Alert");

            }

        }
    };
    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Calendar c = Calendar.getInstance();

       // Toast.makeText(getActivity(),MyCurrentDate,Toast.LENGTH_LONG).show();
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

                        try {
                            jsonObject = new JSONObject(result);
                            jsonArray = jsonObject.getJSONArray("OutPutName");
                            Log.d("demo",jsonArray.toString());
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject retailer = jsonArray.getJSONObject(i).getJSONObject("JourneyRetailerMBO");
                                JSONObject area = jsonArray.getJSONObject(i).getJSONObject("JourneyAreaMBO");
                                String city = jsonArray.getJSONObject(i).getJSONObject("JourneyCityMBO").getString("CityName");
                                String state = jsonArray.getJSONObject(i).getString("StateName");
                                JSONObject JdateTime = jsonArray.getJSONObject(i);
                                JSONObject JidObject = jsonArray.getJSONObject(i);
                                String complete_address = retailer.getString("RetailerAddress") + ", " + area.getString("AreaName") + ", "
                                        + city + ", " + state + " - " + area.getString("PinCode");

                                String jdate = JdateTime.getString("strJourneyDate");
                                if(jdate.equals(Config.userDetails.get(14)))
                                {
                                    listname.add(i, retailer.getString("RetailerName"));
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


                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        getListViewData_J();
                        //listView_J.setAdapter(new DropDownJourneyAdapter(getActivity(), myList_J));
                        spinner.setAdapter(new DropDownJourneyAdapter(getActivity(), myList_J));

                        spinner.setSelection(Config.defaultSpinnerSelecter);
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                @Override
                                public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                           int arg2, long arg3) {

                                    TAG_QUERY="INSERT";
                                    Config.siteInfoImages.clear();
                                    Config.defaultSpinnerSelecter=spinner.getSelectedItemPosition();
                                    int imc_met = spinner.getSelectedItemPosition();
                                    Config.retailerId = retailerId.get(imc_met);
                                    myRetailerId=retailerId.get(imc_met);
                                    totalInternetImages = resetvalue;
                                    Config.imageInternetCount=0;
                                    try {
                                        insertPoint.removeAllViews();
                                       // Toast.makeText(getActivity(),"All Child Removed",Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                   // Toast.makeText(getActivity(), "Total Images" + totalInternetImages, Toast.LENGTH_LONG).show();
                                   // Toast.makeText(getActivity(),"Retailer Id="+Config.retailerId,Toast.LENGTH_LONG).show();
                                    SpinnerLoadingAndCheckingPreviousData(myRetailerId, Config.userDetails.get(10), Config.userDetails.get(14));

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> arg0) {
                                    // TODO Auto-generated method stub

                                }
                            });

                        pDialog.hide();
                    }
                });
    }

    private void getListViewData_J() {
        for (int i = 0; i < listname.size(); i++) {
            DropDownJourneyList ld = new DropDownJourneyList();

            ld.setName(listname.get(i));
            ld.setAddress(address.get(i));
            ld.setTime(time.get(i));
            ld.setjId(journeyId.get(i));
            ld.setrId(retailerId.get(i));

            myList_J.add(ld);
        }
    }

    private void LoadingAndCheckingPreviousData(String retailerId, String username, String mydate) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        name.setText("");
        number.setText("");
        comments.setText("");
        try {
            insertPoint.removeAllViews();

        } catch (Exception e) {
            e.printStackTrace();
        }
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("strSiteVisitId", "0");
        getjson.addProperty("RetailerID", retailerId);
        getjson.addProperty("strSiteVisitDate", mydate);
        getjson.addProperty("strUserName", username);
        Ion.with(this)
                .load(Config.BASE_URL + "api/SiteVisit/GetSiteVisitByUserID?strUserName=" + username + "&strSiteVisitId=0&strSiteVisitDate=" + mydate + "&strRetailerID=" + retailerId)
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                        //JsonObject jsonObject = new JsonObject(result);
                        //sitevisitID = result.getAsJsonObject("SiteVisitID").toString();
                        pDialog.hide();
                        try {
                            String response = result.toString();

                            //Toast.makeText(getActivity(), "loop 1" + response, Toast.LENGTH_LONG).show();
                            Log.d("dresponse", response);
                            //Toast.makeText(getActivity(),"value "+sitevisitID,Toast.LENGTH_LONG).show();
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("OutPutName");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ObjectPerson = jsonArray.getJSONObject(i);
                                String SiteContactPerson = ObjectPerson.getString("SiteContactPerson");

                                String Phone = ObjectPerson.getString("Phone");
                                String Comment = ObjectPerson.getString("Comment");
                                Log.d("SiteContactPerson", SiteContactPerson);
                                Log.d("Phone", Phone);
                                Log.d("Comment", Comment);
                                if(SiteContactPerson.contains("null")) {
                                    name.setText("");
                                }else
                                {
                                    name.setText(SiteContactPerson);
                                }
                                if(Phone.contains("null")) {
                                    number.setText("");
                                }else
                                {
                                    number.setText(Phone);
                                }
                                if(Comment.contains("null")) {
                                    comments.setText("");
                                }else{comments.setText(Comment);}
                                imagePath = jsonArray.getJSONObject(i);



                            JSONArray ArrayimagePath = imagePath.getJSONArray("objSiteVisitPhotoBO");
                            if(ArrayimagePath != null)
                            {
                                Config.imageInternetCount=ArrayimagePath.length();
                                totalInternetImages=0;
                                totalInternetImages =ArrayimagePath.length();
                                for(int j = 0; j < ArrayimagePath.length();j++) {
                                    JSONObject my_back_image_path = ArrayimagePath.getJSONObject(j);
                                    if(my_back_image_path != null){

                                        String imgPath = my_back_image_path.getString("PhotoDisplayPath");
                                        String imgCaption = my_back_image_path.getString("PhotoCaption");
                                        siteVisitID = my_back_image_path.getString("SiteVisitID");

                                        TAG_QUERY = "UPDATE";
                                        TAG_SITEVISIT_ID = siteVisitID;
                                        new DownloadImage().execute(imgPath, imgCaption);
                                        Log.d("Path and caption", imgPath + "////" + imgCaption);


                                    }
                                }
                                pDialog.hide();

                            }


                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }catch (NullPointerException e1)
                        {
                            e1.printStackTrace();
                        }


                        //  Log.d("siteid",sitevisitID);
                    }
                });
    }

    private void SpinnerLoadingAndCheckingPreviousData(String retailerId, String username, String mydate) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        name.setText("");
        number.setText("");
        comments.setText("");
        try {
            insertPoint.removeAllViews();
            allEds.clear();
           // Toast.makeText(getActivity(),"Child removed spinner click func",Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("strSiteVisitId", "0");
        getjson.addProperty("RetailerID", retailerId);
        getjson.addProperty("strSiteVisitDate", mydate);
        getjson.addProperty("strUserName", username);
        Log.d("sitevisitgetjson_req",getjson+"");
        Ion.with(this)
                .load(Config.BASE_URL + "api/SiteVisit/GetSiteVisitByUserID?strUserName=" + username + "&strSiteVisitId=0&strSiteVisitDate=" + mydate + "&strRetailerID=" + retailerId)
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                        //JsonObject jsonObject = new JsonObject(result);
                        //sitevisitID = result.getAsJsonObject("SiteVisitID").toString();
                        pDialog.hide();
                        Log.d("sitevisitget_result", result.toString());
                        try {
                            String response = result.toString();

                            //Toast.makeText(getActivity(), "loop 1" + response, Toast.LENGTH_LONG).show();
                            Log.d("siteinforesponse", response);
                            //Toast.makeText(getActivity(),"value "+sitevisitID,Toast.LENGTH_LONG).show();
                            jsonObject = new JSONObject(response);
                            jsonArray = jsonObject.getJSONArray("OutPutName");


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ObjectPerson = jsonArray.getJSONObject(i);
                                String SiteContactPerson = ObjectPerson.getString("SiteContactPerson");
                                TAG_SITEVISIT_ID = ObjectPerson.getString("SiteVisitID");
                                        if(TAG_SITEVISIT_ID.contains("null")){TAG_QUERY = "INSERT";}
                                        else {TAG_QUERY = "UPDATE";}
                                String Phone = ObjectPerson.getString("Phone");
                                String Comment = ObjectPerson.getString("Comment");
                                Log.d("SiteContactPerson", SiteContactPerson);
                                Log.d("Phone", Phone);
                                Log.d("Comment", Comment);
//                                name.setText(SiteContactPerson);
//                                number.setText(Phone);
//                                comments.setText(Comment);
                                if(SiteContactPerson.contains("null")) {
                                    name.setText("");
                                }else
                                {
                                    name.setText(SiteContactPerson);
                                    TAG_QUERY = "UPDATE";
                                    TAG_SITEVISIT_ID = ObjectPerson.getString("SiteVisitID");

                                }
                                if(Phone.contains("null")) {
                                    number.setText("");
                                }else
                                {
                                    number.setText(Phone);
                                }
                                if(Comment.contains("null")) {
                                    comments.setText("");
                                }else{comments.setText(Comment);}
                                imagePath = jsonArray.getJSONObject(i);

                                JSONArray ArrayimagePath = imagePath.getJSONArray("objSiteVisitPhotoBO");
                            if(ArrayimagePath != null) {
                                totalInternetImages = 0;
                                Config.imageInternetCount=ArrayimagePath.length();
                                totalInternetImages = ArrayimagePath.length();
                                for (int j = 0; j < ArrayimagePath.length(); j++) {
                                    JSONObject my_back_image_path = ArrayimagePath.getJSONObject(j);
                                    if (my_back_image_path != null) {

                                        String imgPath = my_back_image_path.getString("PhotoDisplayPath");
                                        String imgCaption = my_back_image_path.getString("PhotoCaption");
                                        siteVisitID = my_back_image_path.getString("SiteVisitID");

                                        TAG_QUERY = "UPDATE";
                                        TAG_SITEVISIT_ID = siteVisitID;
                                        new DownloadImage().execute(imgPath, imgCaption);
                                        Log.d("Path", imgPath + "////" + imgCaption);


                                    }
                                }
                            }
                                pDialog.hide();

                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }catch (NullPointerException e1)
                        {
                            e1.printStackTrace();
                        }catch (Exception e1)
                        {
                            e1.printStackTrace();
                        }


                        //  Log.d("siteid",sitevisitID);
                    }
                });
    }

    public void getUserId() {

    }

    public void sendData() {
        Log.d("siteidbeforesenddata","Query= "+TAG_QUERY+"Site id= "+TAG_SITEVISIT_ID+"Config.retailerId"+Config.retailerId+"CompanyID "+Config.userDetails.get(11).trim()+"BrandID "+Config.userDetails.get(12).trim());
         pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("SetMethod", TAG_QUERY);
        json.addProperty("SiteVisitID", TAG_SITEVISIT_ID);
        json.addProperty("RetailerID", Config.retailerId);
        json.addProperty("CompanyID", Config.userDetails.get(11).trim());
        json.addProperty("BrandID", Config.userDetails.get(12).trim());
        json.addProperty("SiteContactPerson", name.getText().toString());
        json.addProperty("Phone", number.getText().toString());
        json.addProperty("Comment", comments.getText().toString());
        json.addProperty("IsActive", "1");
        //json.addProperty("ContactPersonPath", "xyx");
        json.addProperty("LoginUser", Config.userDetails.get(10).trim());
        Log.d("siteinfosendjson",json+"");
        //json.addProperty("JourneyID", Config.journeyId);

//        JsonObject img = new JsonObject();
//        json.add("imagesList", img);

        //-------------------azr 09-12-2015 code---------------------------------

//        for (int i = 0; i < Config.siteInfoImages.size(); i++)
//        {
//            Bitmap bitmap = BitmapFactory.decodeFile(Config.siteInfoImages.get(0));
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 10, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream .toByteArray();
//            img.addProperty("fileName",Config.siteInfoImages.get(0));
//            img.addProperty("imageData",Base64.encodeToString(byteArray, Base64.DEFAULT));
//        }

        //------------------end code---------------------------------------------
//------------------------sending images 10-12-2015------------------------

//-------------------------------end send image task--------------------------------
        Ion.with(this)
                .load(Config.BASE_URL + "api/SiteVisit/PostSiteVisit")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        //Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_LONG).show();
                        //JsonObject jsonObject = new JsonObject(result);
                        //sitevisitID = result.getAsJsonObject("SiteVisitID").toString();
                        try {
                            String response = result.toString();
                            Log.d("onComplete",response);
                            JSONObject jsonObject = new JSONObject(response);
                            //Toast.makeText(getActivity(),"loop 1"+jsonObject,Toast.LENGTH_LONG).show();
                            JSONObject jsonObjectsite = jsonObject.getJSONObject("OutPutName");
                            sitevisitID = jsonObjectsite.getString("SiteVisitID");
                            Log.d("imageuploading",TAG_QUERY+" sitevisitID= "+sitevisitID);

                            String status= jsonObjectsite.getString("Status");

                            uploadpictures(sitevisitID,status);
                            //Toast.makeText(getActivity(),"value "+sitevisitID,Toast.LENGTH_LONG).show();

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        //  Log.d("siteid",sitevisitID);
                    }
                });


    }

    private void uploadpictures(String sitevisitID, String status) {

        int d = allEds.size();
        String[] strings = new String[d];
        //----------image upload multi part using ion library---------
        totaldeviceimages=Config.siteInfoImages.size();
        if(Config.siteInfoImages.size()>0)
        {

        }
        else
        {

            pDialog.hide();
            if(status.contains("SUCCESS"))
            {
                showAlertDialog("Site visit information saved Successfully.",true);


            }else {
                showAlertDialog("Failed",false);
            }
        }

        for (int i = 0; i < Config.siteInfoImages.size(); i++) {

            strings[i] = allEds.get(i+totalInternetImages).getText().toString();
            Log.d("caption" + i, strings[i]);

            Bitmap bitmap = BitmapFactory.decodeFile(Config.siteInfoImages.get(i));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int srcWidth = bitmap.getWidth();
            int srcHeight = bitmap.getHeight();
            int dstWidth, dstHeight;
            dstWidth = (int) (srcWidth * 0.5f);
            dstHeight = (int) (srcHeight * 0.5f);
            bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            File f = new File(getActivity().getCacheDir(), "Sitevisit_" + sitevisitID + "_"+ts+(i + 1) + ".jpeg");
            try {
                f.createNewFile();


                FileOutputStream fos = new FileOutputStream(f);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //Toast.makeText(getActivity(),"value "+sitevisitID,Toast.LENGTH_LONG).show();

            pDialogUploadImage = new ProgressDialog(getActivity());
            pDialogUploadImage.setMessage("Uploading Image...");
            pDialogUploadImage.setCancelable(false);
            pDialogUploadImage.show();
            Ion.with(this)
                    //.load("http://vaahancheckvm.cloudapp.net:122/Api/Upload/PostFileDetail?strFile=Nilesh")
                    .load(Config.BASE_URL + "api/Upload/PostFileDetail?strTypeID=8&strID=" +sitevisitID + "&strTitle=" + strings[i] + "&strUserName=" + Config.userDetails.get(10).trim())
                    .setHeader("authorization", "bearer " + token)
                    .progressDialog(pDialogUploadImage)
                    .uploadProgressHandler(new ProgressCallback() {
                        @Override
                        public void onProgress(long uploaded, long total) {
                            // Displays the progress bar for the first time.
//                        mNotifyManager.notify(notificationId, mBuilder.build());
//                        mBuilder.setProgress((int) total, (int) uploaded, false);
                        }
                    })
                    .setTimeout(60 * 60 * 1000)
                    .setMultipartFile("upload", "image/jpeg", f)
                    .asJsonObject()
                            // run a callback on completion
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            // When the loop is finished, updates the notification
//                        mBuilder.setContentText("Upload complete")
//                                // Removes the progress bar
//                                .setProgress(0, 0, false);
//                        mNotifyManager.notify(notificationId, mBuilder.build());
                            //pDialog.hide();
                           // pDialogUploadImage.dismiss();
                            try {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        pDialogUploadImage.dismiss();
                                    }
                                });
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                            try {
                                String response = result.toString();
                                Log.d("imageresponse",response);
                                JSONObject jsonObject = new JSONObject(response);
                                //Toast.makeText(getActivity(),"loop 1"+jsonObject,Toast.LENGTH_LONG).show();
                                JSONObject jsonObjectsite = jsonObject.getJSONObject("OutPutName");
                                //Toast.makeText(getActivity(), jsonObjectsite.getString("SiteVisitID"), Toast.LENGTH_LONG).show();
                                String success=jsonObjectsite.getString("ErrorDesc");
                                if(success.contains("Record has been save successfully")) {
                                    flag++;

                                    //Config.dialog(getActivity(), "Data save successfully", "Task");
                                    //getActivity().getSupportFragmentManager().popBackStack();
                                    if (flag == totaldeviceimages) {
                                        pDialog.hide();
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Message");
                                        alertDialog.setMessage("Site visit information saved Successfully.");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //dialog.dismiss();
                                                        startActivity(new Intent(getActivity(), HomePageActivity.class));
                                                        try {
                                                            File myDirectory = new File(Environment.getExternalStorageDirectory(), "StellarFMR");
                                                            if (myDirectory.isDirectory())
                                                            {
                                                                String[] children = myDirectory.list();
                                                                for (int i = 0; i < children.length; i++)
                                                                {
                                                                    new File(myDirectory, children[i]).delete();
                                                                }
                                                            }
                                                            Config.siteInfoImages.clear();
                                                        }catch (Exception e1){}
                                                    }
                                                });
                                        alertDialog.show();


                                        // getActivity().finish();

                                    }
                                }else {

                                        Config.dialog(getActivity(), "Some Error", "Task");
                                    }


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                            // pDialog.hide();
                            if (e != null) {
                                //Toast.makeText(getActivity(), e+"\nError uploading file", Toast.LENGTH_LONG).show();
                                return;
                            }
                            //Toast.makeText(getActivity(), "Success", Toast.LENGTH_LONG).show();

                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update:
                if (isNetworkAvailable()) {

                    String username = name.getText().toString();
                    String phone_number = number.getText().toString();
                    if (username.toString().length() <= 0 || phone_number.toString().length() <= 0) {
                      //Toast toast=  Toast.makeText(getActivity(), "Please Enter Username and Phone number", Toast.LENGTH_LONG);
                       // toast.setGravity(Gravity.CENTER,0,0);
                       // toast.show();
                        Config.dialog(getActivity(),"Please Enter Contact Person name and Phone number.","Alert");
                    } else {
                        try {

                            sendData();
                        }
                        catch (Exception e)
                        {
                            //pDialog.hide();
                        }
                    }


                } else {
                    showAlertDialog(Config.networkAlert,false);
                }
                break;

            case R.id.site_photo:
                //captureImage();
                String username = name.getText().toString();
                String phone_number = number.getText().toString();
                String comment = comments.getText().toString();
                if (username.toString().length() <= 0 || phone_number.toString().length() <= 0) {
                    Config.dialog(getActivity(),"Please provide Contact person Name and Phone number before taking photos.","Alert");

                }else if (comment.length() <= 0){
                    Config.dialog(getActivity(),"Please provide comment before taking photos.","Alert");
                }
                else {

                    if (((Config.imageInternetCount+1)+Config.siteInfoImages.size()) > Integer.parseInt(Config.userDetails.get(15))){
                        Config.dialog(getActivity(),"You can take maximum of "+ Config.userDetails.get(15)+" photos only.","Alert");
                    }else{
                        Log.d("image_size",""+Config.siteInfoImages.size()+""+Config.imageInternetCount);
                        Intent intent = new Intent(getActivity(), CameraActivity.class);
                        startActivity(intent);

                    }
                }
                break;
        }
    }

    public void showAlertDialog(String data,final boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setMessage(data);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (status == true){
                    startActivity(new Intent(getActivity(),HomePageActivity.class));
                }
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        String d_imageCaption = null;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            newpDialog = new ProgressDialog(getActivity());
//            newpDialog.setMessage("Downloading Images Please Wait...");
//            newpDialog.setCancelable(false);
//            newpDialog.show();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {


            String imageURL = URL[0];
            imageCaption = URL[1];
            d_imageCaption = URL[1];
            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd = ProgressDialog.show(getActivity(), "", "images Loading...", true, false);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bitmap bitmap = null;
            try {
                // Download Image from URL
                InputStream input = new java.net.URL(imageURL).openStream();
                // Decode Bitmap
                bitmap = BitmapFactory.decodeStream(input);
            }catch (FileNotFoundException e){}
            catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            pDialog.hide();
            //newpDialog.hide();

            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (insertPoint.getChildCount() >= 0 ){


                LinearLayout layout2 = new LinearLayout(getActivity());
                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout2.setOrientation(LinearLayout.HORIZONTAL);
                layout2.setWeightSum(3);
//***** Thumbnail
                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                lparams2.setMargins(10, 10, 10, 10);
                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                lparams3.setMargins(10, 10, 10, 10);


                ed = new EditText(getActivity());
                allEds.add(ed);
                ed.setLayoutParams(lparams3);
                ed.setId(123 + count[jsonMainLoop]);

                ed.setText(d_imageCaption);
                if(ed.getText().toString().contains(""))
                {
                    ed.setClickable(false);
                    ed.setEnabled(false);
                    ed.setFocusable(false);
                    ed.setFocusableInTouchMode(false);
                    ed.setOnKeyListener(null);

                }else
                {
                    ed.setClickable(false);
                    ed.setEnabled(false);
                    ed.setFocusable(false);
                    ed.setFocusableInTouchMode(false);
                    ed.setOnKeyListener(null);
                }
                //ed.setVisibility(View.INVISIBLE);
                image = new ImageView(getActivity());
                image.setLayoutParams(lparams2);
                result = Bitmap.createScaledBitmap(result, 256, 256, true);
                image.setImageBitmap(result);
                layout2.addView(image);
                layout2.addView(ed);
                insertPoint.addView(layout2);

            }
        }
    }
}