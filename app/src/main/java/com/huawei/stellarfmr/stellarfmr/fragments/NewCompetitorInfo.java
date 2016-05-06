package com.huawei.stellarfmr.stellarfmr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.CameraActivityCompetitorInfo;
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
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.internal.zzip.runOnUiThread;

public class NewCompetitorInfo extends Fragment implements View.OnClickListener {

    TextView selectBrand;
    public static ImageView image;
    private int year, month, day;
    String[] data = {"Sony", "Huawei", "Samsung"};
    Spinner spinner;
    static int count[] = {2, 9, 9, 9, 9, 8, 9, 7, 7, 8,1,9,8,4, 9, 9, 9, 9, 8, 9, 7, 7, 8,1,9,8,4};

    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    String TAG_METHOD="INSERT";
    String s, MyCurrentDate,brName;
    SharedPreferences sP;
    String token;
    static EditText ed,ed1,edC,edImageCaption;

    ProgressDialog pDialog,pd,pDialogUploadImage;
    public static String shelfRetailerId,shelfJourneyId;
    public static String SaleStockOrderID = "0";
    String modelName,orders,sale_qot;

    TextView header, getModel,txtPhoto;
    String stringProperty;
    EditText get_qty;

    List<EditText> array_sale = new ArrayList<EditText>();
    ArrayList<String> ArrayModelsName = new ArrayList<String>();
    ArrayList<String> ArrayModelsID = new ArrayList<String>();
    String CompetitorBrandName;

    String ModelName,ModelAva,ModelReq,ModelId,CompetitorName,CompetitorQty,CompetitorId,CompetitorQtyFlag, BrandName, BrId, bId;
    static  Boolean brandFlag = true;
    static TextView txtMobelName,txtCompetitorName,txtUpdate;
    static int selected=0;

    public  static String CompetitorSale_id = "0";
    ArrayList<String> ArrayCompetitorNames = new ArrayList<String>();
    ArrayList<String> ArrayCompetitorBrandID = new ArrayList<String>();
    ArrayList<String> ArrayCompetitorBrID = new ArrayList<String>();
    ArrayList<String> Brands = new ArrayList<>();
    JSONArray CompetitorsArray = null;
    public  static String competitor_posm_id = "0";
    static List<EditText> allEdsC = new ArrayList<EditText>();
    static LinearLayout insertPointCompetitor,insertPointCamera;
    JsonObject o;
    static List<EditText> allEds = new ArrayList<EditText>();
    int flag;
    public static int jsonMainLoop;
    int totaldeviceimages;
    public static int totalInternetImages=0;
    static List<EditText> allEdsImages = new ArrayList<EditText>();

    public NewCompetitorInfo() {
    }

//    public static void reloadData(Context context) {
//
//
//        for (int i = 0; i < Config.siteInfoImages.size(); i++) {
//            jsonMainLoop++;
//            try {
//                if (i > (insertPoint.getChildCount()-totalInternetImages) - 1)
//                {
//                    LinearLayout layout2 = new LinearLayout(context);
//                    layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
//                    layout2.setOrientation(LinearLayout.HORIZONTAL);
//                    layout2.setWeightSum(3);
//                    //***** Thumbnail
//                    LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
//                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
//                    lparams2.setMargins(10, 10, 10, 10);
//                    image = new ImageView(context);
//                    image.setLayoutParams(lparams2);
//
//                    final int THUMBSIZE = 256;
//                    Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Config.siteInfoImages.get(i)),
//                            THUMBSIZE, THUMBSIZE);
//
//                    image.setImageBitmap(ThumbImage);
//
//                    //***** Thumbnail
//                    //***** Add Caption
//                    LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
//                            0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
//                    lparams3.setMargins(10, 10, 10, 10);
//
//                    ed = new EditText(context);
//                    ed.setVisibility(View.GONE);
//                    allEds.add(ed);
//
//                    ed.setLayoutParams(lparams3);
//                    ed.setId(123 + count[i]);
//                    ed.setHint("Add caption");
//                    ed.setVisibility(View.INVISIBLE);
//
//                    //***** Add Caption
//
//                    layout2.addView(image);
//                    layout2.addView(ed);
//
//                    insertPoint.addView(layout2);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_competitor_info, container, false);

        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("COMPETITOR INFO");
        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");


        selectBrand= (TextView) view.findViewById(R.id.cs_select_brand);
        txtPhoto=(TextView)view.findViewById(R.id.site_photo);
        txtPhoto.setOnClickListener(this);
        selectBrand.setOnClickListener(this);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        insertPointCompetitor = (LinearLayout) view.findViewById(R.id.lin_cs_m);
        txtUpdate=(TextView)view.findViewById(R.id.update);
        txtUpdate.setOnClickListener(this);
        insertPointCamera = (LinearLayout) view.findViewById(R.id.competitor_info_insert_point_image);
        totalInternetImages=0;


        reloadData(getActivity());
        Config.competitorinfoImages.clear();

        if(Config.isNetworkAvailable(getActivity())) {
            getData_J();
        }else {Config.dialog(getActivity(), Config.networkAlert, "Alert");}

        return view;
    }

    public static void reloadData(Context context) {
        Log.d("reload", "success");


        for (int i = 0; i < Config.competitorinfoImages.size(); i++) {
            //jsonMainLoop++;
            Log.d("forloop",""+Config.competitorinfoImages.size());
            jsonMainLoop++;
            if (i > (insertPointCamera.getChildCount()-totalInternetImages) - 1) {
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
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Config.competitorinfoImages.get(i)),
                        THUMBSIZE, THUMBSIZE);

                image.setImageBitmap(ThumbImage);

//***** Thumbnail
                //***** Add Caption
                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                lparams3.setMargins(10, 10, 10, 10);

                edImageCaption = new EditText(context);
                //edImageCaption.setVisibility(View.GONE);
                allEdsImages.add(edImageCaption);
                edImageCaption.setLayoutParams(lparams3);
                edImageCaption.setId(123 + count[i]);
                edImageCaption.setHint("Add caption");
                //edImageCaption.setVisibility(View.GONE);

//***** Add Caption

                layout2.addView(image);
                layout2.addView(edImageCaption);

                insertPointCamera.addView(layout2);
            }
        }
    }

    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        //MyCurrentDate = df.format(c.getTime());
        // Toast.makeText(getActivity(), MyCurrentDate, Toast.LENGTH_LONG).show();
        String j = Config.BASE_URL +"api/Journey/GetJourneyByUserID?strUserName="+Config.userDetails.get(10)+"&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14);
        //Toast.makeText(getActivity(),j,Toast.LENGTH_LONG).show();
        Log.d("url", j);
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14))
                        //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", shelfJourneyId)
                .setBodyParameter("strJourneyDate", Config.userDetails.get(14))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            Log.d("output", "" + jsonArray);
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
                                if(jdate.equals(Config.userDetails.get(14))) {
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
                        try {

                            spinner.setSelection(0);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {

                                TAG_METHOD = "INSERT";
                                ArrayModelsName.clear();
                                ArrayModelsID.clear();
                                ArrayCompetitorNames.clear();
                                ArrayCompetitorBrandID.clear();
                                CompetitorsArray = null;
//                                try {

//                                    insertPointCompetitor.removeAllViews();
//                                    insertPointCamera.removeAllViews();
//                                } catch (Exception e1) {
//                                    e1.printStackTrace();
//                                }
                                // Config.shelfInfoImages.clear();
//                                shelf_id = "0";


                                int imc_met = spinner.getSelectedItemPosition();
                                Config.defaultSpinnerSelecter = imc_met;
                                Config.retailerId = retailerId.get(imc_met);
                                shelfRetailerId = retailerId.get(imc_met);
                                shelfJourneyId = journeyId.get(imc_met);
//                                totalInternetImages = resetvalue;
                                try {
                                    // insertPoint.removeAllViews();
//                                    insertPointCompetitor.removeAllViews();
//                                    insertPointCamera.removeAllViews();
//                                    allEds.clear();
//                                    allEdsC.clear();

                                    CompetitorAsynTask(shelfRetailerId);

                                    // Toast.makeText(getActivity(),"shelf reta id"+shelfRetailerId,Toast.LENGTH_LONG).show();
                                    // Toast.makeText(getActivity(),"shelf jou id"+shelfJourneyId,Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                //Toast.makeText(getActivity(), "Total Images" + totalInternetImages, Toast.LENGTH_LONG).show();
                                //SpinnerLoadingAndCheckingPreviousData(Config.retailerId, Config.userDetails.get(10), MyCurrentDate);

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

    private void CompetitorAsynTask(String retailer_id) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        //pDialog.setCancelable(false);
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intPOSMId", "0");
        getjson.addProperty("intRetailerId", retailer_id);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Log.d("competitorinfogetjson", getjson + "");
        Log.d("competitorinfosgeturl", Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + retailer_id + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10));

        Ion.with(this)
                .load(Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + retailer_id + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        String response = result.toString();
                        Log.d("competitorinforesponse", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            try {
                                File root = new File(Environment.getExternalStorageDirectory(), "JsonResult");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File gpxfile = new File(root, "newcompetitiorinforesponse.txt");
                                FileWriter writer = new FileWriter(gpxfile);
                                writer.append(response);
                                writer.flush();
                                writer.close();
                                // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e2) {
                                e2.printStackTrace();

                            }
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            competitor_posm_id = jsonArray.getJSONObject(0).getString("CompetitorModelID");
                            //ArrayCompetitorNames.clear();
                            //ArrayCompetitorBrandID.clear();
                            //ArrayCompetitorBrID.clear();
                            //posm_id = jsonArray.getString("ShelfID");
                            if (competitor_posm_id.contains("null")) {
                                competitor_posm_id = "0";
                                TAG_METHOD = "INSERT";

                            } else {
                                TAG_METHOD = "UPDATE";

                            }
//
//                            Brands.clear();
//                            ArrayCompetitorBrID.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject CompetitorsObject = jsonArray.getJSONObject(i);
                                if (CompetitorsArray == null) {
                                    CompetitorsArray = CompetitorsObject.getJSONArray("objCompetitorModelModelItem");
                                }

                                if (CompetitorsArray != null) {
                                    Log.d("POSMCompetitors", CompetitorsArray.length() + "");
//
                                    insertPointCompetitor.removeAllViews();
                                    for (int j = 0; j < CompetitorsArray.length(); j++)
                                    {

                                        BrandName = CompetitorsArray.getJSONObject(j).getString("CompetitorBrandName");
                                        BrId = CompetitorsArray.getJSONObject(j).getString("CompetitorBrandID");
                                        ArrayCompetitorBrID.add(BrId);
                                        Brands.add(BrandName);
                                        if (brandFlag) {
                                            selectBrand.setText(Brands.get(0));
                                            brName = Brands.get(0);
                                            bId = ArrayCompetitorBrID.get(0);
                                        }

                                        //  JSONObject CompObject = CompetitorsArray.getJSONObject(j);
                                        //  if(CompArray == null)
                                        JSONArray CompArray = CompetitorsArray.getJSONObject(j).getJSONArray("CompetitorModelModelList");

                                        if (j == selected)
                                        {

                                            for (int k = 0; k < CompArray.length(); k++)
                                            {


                                                CompetitorName = CompArray.getJSONObject(k).getString("CompetitorModelName");
                                                CompetitorQty = CompArray.getJSONObject(k).getString("Comment");
                                                //CompetitorQtyFlag = CompArray.getJSONObject(k).getString("Quantity");
////                                        posm_id = CompetitorsArray.getJSONObject(j).getString("ShelfID");
////                                        if (posm_id.contains("null")) {
////                                            posm_id = "0";
////                                        }
////                                        if (posm_id.contains("null")) {
////                                            //TAG_METHOD = "INSERT";
////                                            CompetitorQty = "";
////                                        } else {
////                                            //TAG_METHOD = "UPDATE";
////                                        }
                                                if (CompetitorQty.contains("null"))
                                                {
                                                    CompetitorQty = "";
                                                } else {
                                                }
                                                CompetitorId = CompArray.getJSONObject(k).getString("CompetitorModelID");
                                                ArrayCompetitorNames.add(k, CompetitorName);
                                                ArrayCompetitorBrandID.add(k, CompetitorId);
                                                LinearLayout layout2 = new LinearLayout(getActivity());
                                                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                layout2.setOrientation(LinearLayout.VERTICAL);

////***** Thumbnail
                                                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                lparams2.setMargins(10, 10, 10, 10);
                                                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                                                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                                lparams3.setMargins(10, 10, 10, 10);

                                                edC = new EditText(getActivity());
                                                edC.setSingleLine(true);
                                                edC.setLines(3);
                                                edC.setTag(BrandName + CompetitorName);
                                                allEdsC.add(edC);
                                                edC.setLayoutParams(lparams3);
                                                edC.setId(111 + k);


                                                if (CompetitorQty.length() > 0) {
                                                    edC.setText(CompetitorQty);
                                                    edC.setEnabled(false);
                                                    edC.setClickable(false);
                                                    edC.setFocusable(false);
                                                } else {
                                                    edC.setText(CompetitorQty);
                                                }

                                                txtCompetitorName = new TextView(getActivity());
                                                txtCompetitorName.setLayoutParams(lparams2);
                                                txtCompetitorName.setText(CompetitorName);
                                                layout2.addView(txtCompetitorName);
                                                layout2.addView(edC);
                                                try {
                                                    insertPointCompetitor.addView(layout2);
                                                }
                                                catch (Exception e1)
                                                {
                                                    e1.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }
//
                            }
                            //-----end json loop-----


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (NullPointerException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        SetUpOldBackImages();

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



    private void SetUpOldBackImages() {
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intPOSMId", "0");
        getjson.addProperty("intRetailerId", shelfRetailerId);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Log.d("competitorinfogetjson", getjson + "");
        Log.d("competitorinfosgeturl", Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + shelfRetailerId + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10));

        Log.v("ImageLink", "nskdjfhnk");
        Ion.with(this)
                .load(Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + shelfRetailerId + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        try {
                            Log.v("ImageLink", result.toString());

                            try {
                                File root = new File(Environment.getExternalStorageDirectory(), "JsonResult");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File gpxfile = new File(root, "myjson.txt");
                                FileWriter writer = new FileWriter(gpxfile);
                                writer.append(result.toString());
                                writer.flush();
                                writer.close();
                                // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e2) {
                                e2.printStackTrace();

                            }

                            JSONObject jsonObject = new JSONObject(result.toString());
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            Log.d("cmjsonArray",jsonArray+"");
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject ShelfPhotosObject = jsonArray.getJSONObject(i);
                                JSONArray ShelfPhotoArray = ShelfPhotosObject.getJSONArray("CompetitorModelPhotos");
                                //totalInternetImages=resetvalue;
                                Log.d("cmphotoarray",ShelfPhotoArray+"");
                                totalInternetImages=ShelfPhotoArray.length();
                                Log.d("totalInternetImages",totalInternetImages+"");
                                Config.imageInternetCount=ShelfPhotoArray.length();
                                if (ShelfPhotoArray != null)
                                {
                                    for(int k =0;k <ShelfPhotoArray.length();k++) {
                                        JSONObject my_back_image_path = ShelfPhotoArray.getJSONObject(k);
                                        String imgPath = my_back_image_path.getString("PhotoDisplayPath");
                                        String imgCaption = my_back_image_path.getString("PhotoCaption");
                                        Log.d("ImageLink",imgPath);
                                        new DownloadImage().execute(imgPath, imgCaption);


                                    }
                                }

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                });

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        String d_imageCaption = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(String... URL) {


            String imageURL = URL[0];
            //imageCaption = URL[1];
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {

            try {
                runOnUiThread(new Runnable() {
                    public void run() {
                        pd.dismiss();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (insertPointCamera.getChildCount() >= 0 ){

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

                edImageCaption = new EditText(getActivity());
                allEdsImages.add(edImageCaption);
                edImageCaption.setLayoutParams(lparams3);
                edImageCaption.setId(123 + count[jsonMainLoop]);

                edImageCaption.setText(d_imageCaption);
                if(edImageCaption.getText().toString().contains(""))
                {
                    edImageCaption.setClickable(false);
                    edImageCaption.setEnabled(false);
                    edImageCaption.setFocusable(false);
                    edImageCaption.setFocusableInTouchMode(false);
                    edImageCaption.setOnKeyListener(null);

                }else
                {
                    edImageCaption.setClickable(false);
                    edImageCaption.setEnabled(false);
                    edImageCaption.setFocusable(false);
                    edImageCaption.setFocusableInTouchMode(false);
                    edImageCaption.setOnKeyListener(null);
                }
                //edImageCaption.setVisibility(View.INVISIBLE);
                image = new ImageView(getActivity());
                image.setLayoutParams(lparams2);
                result = Bitmap.createScaledBitmap(result, 256, 256, true);
                image.setImageBitmap(result);
                layout2.addView(image);
                layout2.addView(edImageCaption);
                insertPointCamera.addView(layout2);
            }
            pDialog.hide();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cs_select_brand:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Brand");
                final String datas[] = new String[Brands.size()];
                for(int i=0; i<Brands.size();i++){
                    datas[i] = Brands.get(i);
                }
                builder.setItems(datas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        try{
                            insertPointCamera.removeAllViews();
                        }catch (Exception e){}
                        if(Config.isNetworkAvailable(getActivity())) {
                            updatepageData();
                        }else {Config.dialog(getActivity(),Config.networkAlert,"Alert");}
                        selectBrand.setText(datas[which]);
                        selected = which;
                        Brands.clear();
                        brName = datas[which];
                        bId = ArrayCompetitorBrID.get(which);
                        ArrayCompetitorBrID.clear();
                        brandFlag=false;
                        //Config.posmImages.clear();
                        //totalInternetImages = resetvalue;

                        try {
                            //insertPoint.removeAllViews();
                            insertPointCompetitor.removeAllViews();
                            //insertPointCamera.removeAllViews();
                            //allEds.clear();
                            //allEds1.clear();
                            //allEdsC.clear();
                            CompetitorAsynTask(shelfRetailerId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.update:

                try
                {
                    if(Config.isNetworkAvailable(getActivity())) {
                        updateData();
                    }
                    else {Config.dialog(getActivity(),Config.networkAlert,"Alert");}                    } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.site_photo:

//                if (((Config.imageInternetCount+1)+Config.competitorinfoImages.size()) > Integer.parseInt(Config.userDetails.get(15))){
//                    Config.dialog(getActivity(),"You can take maximum of 10 photos only.","Error");
//                }else{
                    //Log.d("image_size",""+Config.competitorinfoImages.size()+""+Config.imageInternetCount);
                    Intent intent = new Intent(getActivity(), CameraActivityCompetitorInfo.class);
                    startActivity(intent);

               // }

                break;


        }
    }

    private void updateData() {

        if(TAG_METHOD.contains("INSERT"))
        {
            stringProperty = "{\"IsActive\":\"1\",\"SetMethod\":\""+TAG_METHOD+"\",\"CreatedDate\":\""+Config.userDetails.get(13)+"\",\"CreatedDateYYYYMMDD\":\""+Config.userDetails.get(13)+"\",\"FMRUserName\":\""+Config.userDetails.get(10)+"\",\"CompetitorModelID\":\"0\",\"JourneyId\":\""+shelfJourneyId+"\",\"RetailerId\":\""+shelfRetailerId+"\",\"BrandID\":\""+Config.userDetails.get(12)+"\",\"CompanyID\":\""+Config.userDetails.get(11)+"\",\"LoginUser\":\""+Config.userDetails.get(10)+""+"\",";
            Log.d("stringPropertyci",""+stringProperty);
        }
        else
        {
            stringProperty = "{\"IsActive\":\"1\",\"SetMethod\":\""+TAG_METHOD+"\",\"CreatedDate\":\""+Config.userDetails.get(13)+"\",\"CreatedDateYYYYMMDD\":\""+Config.userDetails.get(13)+"\",\"FMRUserName\":\""+Config.userDetails.get(10)+"\",\"CompetitorModelID\":\""+competitor_posm_id+"\",\"JourneyId\":\""+shelfJourneyId+"\",\"RetailerId\":\""+shelfRetailerId+"\",\"BrandID\":\""+Config.userDetails.get(12)+"\",\"CompanyID\":\""+Config.userDetails.get(11)+"\",\"LoginUser\":\""+Config.userDetails.get(10)+""+"\",";
            Log.d("stringPropertyci",""+stringProperty);

        }
        Log.d("stringProperty",stringProperty);
        updatepageData();
        String compData1 =   CompetitorsArray.toString();
        Log.d("competitorsalessendjson",compData1);
        String FinalJson=stringProperty+"\"objCompetitorModelItem\""+" :"+compData1+",\"POSMPhotos\":[]}";
        Log.d("competitorsalessendjson",FinalJson);
        sendingUpdatedData(FinalJson);
    }
    private void sendingUpdatedData(String finalJson) {

        try {

            JsonParser parser = new JsonParser();
            o = parser.parse(finalJson).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        //pDialog.setCancelable(false);
        Log.d("competitorsetdata",""+o);
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "JsonResult");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, TAG_METHOD+"competitorsendmyjson.txt");
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(o+"");
            writer.flush();
            writer.close();
            // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e2) {
            e2.printStackTrace();

        }
        // String SelfUpdateSendFromUser = "api/Shelf/InputGetShelfDetailsByUserORShelfIDValidation?CreatedDate="+Config.userDetails.get(13)+"&strFMRUserName="+Config.userDetails.get(10)+"&intShelfId=0&intRetailerId=1&intBrandID="+Config.userDetails.get(12)+"&intCompanyID="+Config.userDetails.get(11)+"&LoginUser="+Config.userDetails.get(10);
        Log.d("cinfoseturl",Config.BASE_URL + "api/CompetitorModel/SetCompetitorModel");
        Ion.with(this)
                //.load(Config.BASE_URL + SelfUpdateSendFromUser)
                .load(Config.BASE_URL + "api/CompetitorModel/SetCompetitorModel")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(o)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        Log.d("newcompetitormyvalue", result.toString());
                        String response = result.toString();
                        try {
                            File root = new File(Environment.getExternalStorageDirectory(), "JsonResult");
                            if (!root.exists()) {
                                root.mkdirs();
                            }
                            File gpxfile = new File(root, TAG_METHOD + "nresponsecompetitormyvalue.txt");
                            FileWriter writer = new FileWriter(gpxfile);
                            writer.append(response);
                            writer.flush();
                            writer.close();
                            // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        } catch (IOException e2) {
                            e2.printStackTrace();

                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            competitor_posm_id = jsonObject.getJSONObject("OutPutName").getString("CompetitorModelID");
                            String status = jsonObject.getJSONObject("OutPutName").getString("Status");
                            uploadpictures(competitor_posm_id, status);

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

/////

                    }
                });
    }

    private void uploadpictures(String posm_id, String status) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        int d = allEdsImages.size();
        String[] strings = new String[d];
        //----------image upload multi part using ion library---------
        totaldeviceimages=Config.competitorinfoImages.size();
        if(Config.competitorinfoImages.size()>0)
        {
            //Toast.makeText(getActivity(),"Image prasent",Toast.LENGTH_LONG).show();

        }
        else
        {

            pDialog.hide();
            if(status.contains("SUCCESS"))
            {
                showAlertDialog("Competitor information saved Successfully.",true);


            }else {
                showAlertDialog("Failed",false);
            }
        }
        for (int i = 0; i < Config.competitorinfoImages.size(); i++)
        {
            //  Toast.makeText(getActivity(),"For loop executing "+i,Toast.LENGTH_LONG).show();
            Log.d("InternetImagesupload",totalInternetImages+"");
            strings[i] = allEdsImages.get(i+totalInternetImages).getText().toString();
            Log.d("Competitorcaption" + i, strings[i]);


            Bitmap bitmap = BitmapFactory.decodeFile(Config.competitorinfoImages.get(i));
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
            File f = new File(getActivity().getCacheDir(), "CompetitorModel_" + PosmFragment.posm_id + "_"+ts+(i + 1) + ".jpeg");
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

            Log.d("posm_idbeforeupload",posm_id);
            Log.d("posm_idbeforeupload",Config.BASE_URL + "api/Upload/PostFileDetail?strTypeID=3&strID=" + posm_id + "&strTitle=" + strings[i] + "&strUserName=" + Config.userDetails.get(10).trim());
            pDialogUploadImage = new ProgressDialog(getActivity());
            pDialogUploadImage.setMessage("Uploading Image...");
            pDialogUploadImage.setCancelable(false);
            pDialogUploadImage.show();
            Ion.with(this)
                    //.load("http://vaahancheckvm.cloudapp.net:122/Api/Upload/PostFileDetail?strFile=Nilesh")
                    .load(Config.BASE_URL + "api/Upload/PostFileDetail?strTypeID=3&strID=" + posm_id + "&strTitle=" + strings[i] + "&strUserName=" + Config.userDetails.get(10).trim())
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
                            pDialog.hide();
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
                                Log.d("cinfoimageresponse", response);
                                JSONObject jsonObject = new JSONObject(response);
                                //Toast.makeText(getActivity(),"loop 1"+jsonObject,Toast.LENGTH_LONG).show();
                                JSONObject jsonObjectsite = jsonObject.getJSONObject("OutPutName");
                                //Toast.makeText(getActivity(), jsonObjectsite.getString("SiteVisitID"), Toast.LENGTH_LONG).show();
                                String success = jsonObjectsite.getString("ErrorDesc");
                                if (success.contains("Record has been save successfully")) {
                                    flag++;

                                    //Config.dialog(getActivity(), "Data save successfully", "Task");
                                    //getActivity().getSupportFragmentManager().popBackStack();
                                    if (flag == totaldeviceimages) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage("Data save successfully.");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //dialog.dismiss();
                                                        startActivity(new Intent(getActivity(), HomePageActivity.class));
                                                        try {
                                                            File myDirectory = new File(Environment.getExternalStorageDirectory(), "StellarFMR");
                                                            if (myDirectory.isDirectory()) {
                                                                String[] children = myDirectory.list();
                                                                for (int i = 0; i < children.length; i++) {
                                                                    new File(myDirectory, children[i]).delete();
                                                                }
                                                            }
                                                            Config.competitorinfoImages.clear();
                                                        } catch (Exception e1) {
                                                        }
                                                    }
                                                });
                                        alertDialog.show();


                                        // getActivity().finish();

                                    }
                                } else {

                                    Config.dialog(getActivity(), "Some Error", "Task");
                                }


                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }catch (NullPointerException e1) {
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

    public class MyAdapter extends ArrayAdapter<String> {

        Context context;
        LayoutInflater inflater;


        public MyAdapter(Context context, int textViewResourceId
        ) {
            super(context, textViewResourceId);
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            // LayoutInflater inflater = LayoutInflater.from(context);
            // inflater = getActivity().getLayoutInflater();

            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.custom_spinner_layout, parent, false);
            TextView txtItem = (TextView) row.findViewById(R.id.spinner_text);
            txtItem.setText(data[position]);

            return row;
        }
    }


/*
    private  void updatepageData() {


        String NewBrandName = null;
        JSONArray CompArray = null;
        try {
            NewBrandName = CompetitorsArray.getJSONObject(selected).getString("BrandName");
            CompArray = CompetitorsArray.getJSONObject(selected).getJSONArray("POSMBrandActivityList");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i < allEdsC.size(); i++)
        {
            try {


                String CompetitorName1 = allEdsC.get(i).getTag().toString().trim();
                String CompetitorQty12 = allEdsC.get(i).getText().toString().trim();

                // Log.d("stringsModelQty",stringsModelAva[i]);
                for(int j=0; j < CompArray.length(); j++)
                    try {
                        String  CompetitorName = CompArray.getJSONObject(j).getString("ActivityName");
                        String CompetitorQty = CompArray.getJSONObject(j).getString("Available");
                        String srt = NewBrandName + CompetitorName;
                        if (srt.equals(CompetitorName1)) {

                            //if( ! allEds1.get(j).getText().toString().trim().equals(CompetitorQty)) {
                            String CompetitorQty1 = allEdsC.get(i).getText().toString();
                            JSONObject Available =  CompArray.getJSONObject(j);//.getJSONObject("Available");
                            Available.remove("Available");
                            Available.put("Available", CompetitorQty1);

                            // CompArray.getJSONObject(i).optString("Available", CompetitorQty1);
                            // }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //}

            //allEds1.clear();
//           allEdsC.clear();
        }
        try {

            JSONObject POSMBrandActivityList =  CompetitorsArray.getJSONObject(selected);//.getJSONArray("POSMBrandActivityList");
            POSMBrandActivityList.remove("POSMBrandActivityList");
            POSMBrandActivityList.put("POSMBrandActivityList", CompArray);
            // CompetitorsArray.getJSONObject(selected).optString("POSMBrandActivityList", String.valueOf(CompArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        allEdsC.clear();


    }
*/

    private  void updatepageData()
    {


        String NewBrandName = null;
        JSONArray CompArray = null;
        try {
            NewBrandName = CompetitorsArray.getJSONObject(selected).getString("CompetitorBrandName");
            CompArray = CompetitorsArray.getJSONObject(selected).getJSONArray("CompetitorModelModelList");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i=0; i < allEdsC.size(); i++)
        {
            try {


                String CompetitorName1 = allEdsC.get(i).getTag().toString().trim();
                String CompetitorQty12 = allEdsC.get(i).getText().toString().trim();

                // Log.d("stringsModelQty",stringsModelAva[i]);
                for(int j=0; j < CompArray.length(); j++)
                    try {
                        String  CompetitorName = CompArray.getJSONObject(j).getString("CompetitorModelName");
                        String CompetitorQty = CompArray.getJSONObject(j).getString("Comment");
                        String srt = NewBrandName + CompetitorName;
                        if (srt.equals(CompetitorName1)) {

                            //if( ! allEds1.get(j).getText().toString().trim().equals(CompetitorQty)) {
                            String CompetitorQty1 = allEdsC.get(i).getText().toString();
                            JSONObject Available =  CompArray.getJSONObject(j);//.getJSONObject("Available");
                            Available.remove("Comment");
                            Available.put("Comment", CompetitorQty1);

                            // CompArray.getJSONObject(i).optString("Available", CompetitorQty1);
                            // }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //}

            //allEds1.clear();
//           allEdsC.clear();
        }
        try {

            JSONObject POSMBrandActivityList =  CompetitorsArray.getJSONObject(selected);//.getJSONArray("POSMBrandActivityList");
            POSMBrandActivityList.remove("CompetitorModelModelList");
            POSMBrandActivityList.put("CompetitorModelModelList", CompArray);
            // CompetitorsArray.getJSONObject(selected).optString("POSMBrandActivityList", String.valueOf(CompArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        allEdsC.clear();


    }
}
