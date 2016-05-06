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
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.CameraActivityBranding;
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

public class BrandingFragment extends Fragment implements View.OnClickListener {

    ImageView upArrowLeft,upArrowCenter, upArrowRight;
    TextView header, posm, competitior, comments,selectBrand, photo;
    EditText add_comment;
    RelativeLayout comm_rel, posm_rel, comp_rel;
    static TextView txtMobelName,txtCompetitorName,txtUpdate;
    ListView listView;
    public static ImageView image;
    String ModelName,ModelAva,ModelReq,ModelId,CompetitorName,CompetitorQty,CompetitorId,CompetitorQtyFlag, BrandName, BrId, bId;
    static LinearLayout insertPoint,insertPointCompetitor,insertPointCamera;
    static int count[] = {2, 9, 9, 9, 9, 8, 9, 7, 7, 8,12, 19, 19, 19, 19, 18, 19, 17, 17, 18,22, 29, 29, 29, 29, 28, 29, 27, 27, 28};
    static EditText ed,ed1,edC,edImageCaption;
    static  Boolean brandFlag = true;
    static int selected=0;
    int CounterAvailableData=0,CounterRequiredIntData=0;
    JsonObject o;
    int modelRefreshFirst=0;

    String TAG_METHOD="INSERT";
    String stringProperty,JsonStringModels="",JsonStringCompetitor="",stringModel,stringCompetitors, brName;


    int flag,counter,available_counter,required_counter;
    static List<EditText> allEds = new ArrayList<EditText>();
    static List<EditText> allEds1 = new ArrayList<EditText>();
    static List<EditText> allEdsC = new ArrayList<EditText>();
    static List<EditText> allEdsImages = new ArrayList<EditText>();
    int totaldeviceimages;

    ArrayList<String> ArrayModelsName = new ArrayList<String>();
    ArrayList<String> ArrayModelsID = new ArrayList<String>();
    ArrayList<String> ArrayCompetitorNames = new ArrayList<String>();
    ArrayList<String> ArrayCompetitorBrandID = new ArrayList<String>();
    ArrayList<String> ArrayCompetitorBrID = new ArrayList<String>();

    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    public static int totalInternetImages=0;
    public static int resetvalue=0;
    public static int jsonMainLoop;
    public static String posmRetailerId,posmJourneyId;
    String imageCaption;
    ProgressDialog pDialog,pd,pDialogUploadImage;
    SharedPreferences sP;
    String token;
    public  static String posm_id = "0";
    Spinner spinner;
    ArrayList<String> Brands = new ArrayList<>();
    String[] data ;//= {"Sony", "Huawei", "Samsung"};
    CheckBox chkInsentiveScheme;

    JSONArray CompetitorsArray = null;
    public BrandingFragment() {
        // Required empty public constructor
    }

    public static void reloadData(Context context) {
        Log.d("reload", "success");


        for (int i = 0; i < Config.brandingImages.size(); i++) {
            //jsonMainLoop++;
            Log.d("forloop",""+Config.brandingImages.size());
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
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Config.brandingImages.get(i)),
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posm, container, false);
        CounterAvailableData=0;
        CounterRequiredIntData=0;
        brandFlag = true;

        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");

        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("BRANDING");
        TextView posm_title=(TextView)view.findViewById(R.id.posm_title);
        posm_title.setText("BRANDING");
        TextView posm_model=(TextView)view.findViewById(R.id.posm_model);
        posm_model.setText("BRANDING");

        posm = (TextView) view.findViewById(R.id.tab_1);
        competitior = (TextView) view.findViewById(R.id.tab_2);
        comments = (TextView) view.findViewById(R.id.tab_3);
        txtUpdate=(TextView)view.findViewById(R.id.update);
        txtUpdate.setText("NEXT");
        posm.setText("BRANDING STATUS");

        upArrowLeft = (ImageView) view.findViewById(R.id.uparrow_left);
        upArrowCenter = (ImageView) view.findViewById(R.id.uparrow_center);
        upArrowRight = (ImageView) view.findViewById(R.id.uparrow_right);
        photo = (TextView) view.findViewById(R.id.posm_photo);

        add_comment = (EditText) view.findViewById(R.id.posm_add_comments);
        listView = (ListView) view.findViewById(R.id.posm_list);
        comm_rel = (RelativeLayout) view.findViewById(R.id.posm_comm_rel);
        posm_rel = (RelativeLayout) view.findViewById(R.id.posm_tab1_rel);
        comp_rel = (RelativeLayout) view.findViewById(R.id.posm_tab2_rel);

        selectBrand= (TextView) view.findViewById(R.id.posm_select_brand);
        chkInsentiveScheme=(CheckBox)view.findViewById(R.id.checkBoxInsentive);
        chkInsentiveScheme.setVisibility(View.GONE);

        posm.setOnClickListener(this);
        competitior.setOnClickListener(this);
        comments.setOnClickListener(this);
        selectBrand.setOnClickListener(this);
        photo.setOnClickListener(this);
        txtUpdate.setOnClickListener(this);

        insertPoint = (LinearLayout) view.findViewById(R.id.posm_insert_point);
        insertPointCompetitor = (LinearLayout) view.findViewById(R.id.posm_insert_pointCompetitor);
        insertPointCamera=(LinearLayout) view.findViewById(R.id.posm_insert_point_image);
        reloadData(getActivity());
        //ModelsAsynTask();
        Config.brandingImages.clear();
        spinner = (Spinner) view.findViewById(R.id.spinner);
        if(Config.isNetworkAvailable(getActivity())) {
            getData_POSM();
        }else
        {
            Config.dialog(getActivity(),Config.networkAlert,"Error");
        }

        return view;
    }


    public void getData_POSM() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        // Toast.makeText(getActivity(), MyCurrentDate, Toast.LENGTH_LONG).show();
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14))
                        //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", posmJourneyId)
                .setBodyParameter("strJourneyDate", Config.userDetails.get(14))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {

                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            Log.d("output",""+jsonArray);
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
                        try {
                            spinner.setSelection(Config.defaultSpinnerSelecter);
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
                                ArrayCompetitorBrID.clear();
                                CompetitorsArray = null;
                                totalInternetImages = 0;
                                Config.brandingImageCount = 0;
                                allEdsImages.clear();

                                try {
                                    insertPoint.removeAllViews();
                                    insertPointCompetitor.removeAllViews();
                                    insertPointCamera.removeAllViews();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                Config.brandingImages.clear();
                                posm_id="0";


                                int imc_met = spinner.getSelectedItemPosition();
                                Config.defaultSpinnerSelecter=imc_met;
                                Config.retailerId = retailerId.get(imc_met);
                                posmRetailerId=retailerId.get(imc_met);
                                posmJourneyId=journeyId.get(imc_met);
                                //totalInternetImages = resetvalue;
                                try {
                                    insertPoint.removeAllViews();
                                    insertPointCompetitor.removeAllViews();
                                    insertPointCamera.removeAllViews();
                                    allEds.clear();
                                    allEdsC.clear();
                                    allEds1.clear();
                                    ModelsAsynTask(posmRetailerId);

                                    // Toast.makeText(getActivity(),"shelf reta id"+posmRetailerId,Toast.LENGTH_LONG).show();
                                    // Toast.makeText(getActivity(),"shelf jou id"+posmJourneyId,Toast.LENGTH_LONG).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                // Toast.makeText(getActivity(), "Total Images" + totalInternetImages, Toast.LENGTH_LONG).show();
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


    private void ModelsAsynTask(String retailer_id) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        pDialog.setCancelable(false);
        Log.d("sendingurlparameter", "createdate" + Config.userDetails.get(13) + "strFMRUserName" + Config.userDetails.get(10) + "shelf id" + posm_id + "reta id" + retailer_id + "bran id" + Config.userDetails.get(12) + "comp id" + Config.userDetails.get(11) + "login user" + Config.userDetails.get(10));

        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intBrandingId", "0");
        getjson.addProperty("intRetailerId", retailer_id);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Ion.with(this)
                .load(Config.BASE_URL + "api/Branding/GetBrandingDetailsByUserORBrandingID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intBrandingId=0&intRetailerId="+retailer_id+"&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        String response = result.toString();
                        Log.d("modelresponse", response);



                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            posm_id=jsonArray.getJSONObject(0).getString("BrandingID");
                            //posm_id = jsonArray.getString("ShelfID");
                            String comment=jsonArray.getJSONObject(0).getString("Comments");
                            add_comment.setText(comment);
                            if (posm_id.contains("null"))
                            {
                                posm_id = "0";
                                TAG_METHOD="INSERT";

                            }else
                            {
                                TAG_METHOD="UPDATE";

                            }


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ModelsObject = jsonArray.getJSONObject(i);
                                JSONArray ModelsArray = ModelsObject.getJSONArray("BrandingItems");
                                if (ModelsArray != null) {
                                    Log.d("ModelsArrayLength", ModelsArray.length() + "");
                                    if(modelRefreshFirst==0) {
                                        insertPoint.removeAllViews();

                                        for (int j = 0; j < ModelsArray.length(); j++) {


                                            ModelName = ModelsArray.getJSONObject(j).getString("ActivityName");
                                            ModelAva = ModelsArray.getJSONObject(j).getString("Available");
                                            ModelReq = ModelsArray.getJSONObject(j).getString("Required");
//                                        posm_id = ModelsArray.getJSONObject(j).getString("ShelfID");
//                                        if (posm_id.contains("null")) {
//                                            posm_id = "0";
//                                        }
                                            if (ModelAva.contains("null") || ModelAva.contains("0")) {
                                                ModelAva = "";
                                            }
                                            if (ModelReq.contains("null") || ModelReq.contains("0")) {
                                                ModelReq = "";
                                            }
                                            ModelId = ModelsArray.getJSONObject(j).getString("ActivityID");

                                            ArrayModelsID.add(j, ModelId);
                                            ArrayModelsName.add(j, ModelName);

                                            LinearLayout layout2 = new LinearLayout(getActivity());
                                            layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                            layout2.setOrientation(LinearLayout.HORIZONTAL);
                                            layout2.setWeightSum(4);
                                            //layout2.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
                                            //     layout2.setGravity(Gravity.CENTER_VERTICAL);
//***** Thumbnail
                                            LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                                            lparams2.setMargins(10, 10, 10, 10);

                                            LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                                                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                            lparams3.setMargins(10, 10, 10, 10);

                                            LinearLayout.LayoutParams lparams4 = new LinearLayout.LayoutParams(
                                                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                            lparams4.setMargins(10, 10, 10, 10);


                                            int maxLength = 4;
                                            InputFilter[] FilterArray = new InputFilter[1];
                                            FilterArray[0] = new InputFilter.LengthFilter(maxLength);

                                            ed = new EditText(getActivity());
                                            ed.setFilters(FilterArray);
                                            ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            ed.setGravity(Gravity.RIGHT);
                                            allEds.add(ed);
                                            ed.setLayoutParams(lparams3);
                                            ed.setId(999 + j);
                                            if (ModelAva.length() > 0) {
                                                ed.setText(ModelAva);
                                                ed.setEnabled(false);
                                                ed.setFocusable(false);
                                                ed.setClickable(false);
                                            } else {
                                                ed.setText(ModelAva);
                                            }

                                            ed1 = new EditText(getActivity());
                                            ed1.setFilters(FilterArray);
                                            ed1.setInputType(InputType.TYPE_CLASS_NUMBER);
                                            allEds1.add(ed1);
                                            ed1.setLayoutParams(lparams4);
                                            ed1.setId(999 + j);
                                            if (ModelReq.length() > 0) {
                                                ed1.setText(ModelReq);
                                                ed1.setEnabled(false);
                                                ed1.setFocusable(false);
                                                ed1.setClickable(false);
                                            } else {
                                                ed1.setText(ModelReq);
                                            }

                                            txtMobelName = new TextView(getActivity());
                                            txtMobelName.setLayoutParams(lparams2);
                                            txtMobelName.setText(ModelName);
                                            layout2.addView(txtMobelName);
                                            layout2.addView(ed);
                                            layout2.addView(ed1);
                                            insertPoint.addView(layout2);

                                        }
                                    }
                                    CompetitorAsynTask(posmRetailerId);
                                }

                            }
                            //-----end json loop-----


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (NullPointerException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });

    }

    private void CompetitorAsynTask(String retailer_id) {
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intBrandID", "0");
        getjson.addProperty("intRetailerId", retailer_id);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Log.d("posm", getjson + "");
        //Log.d("posmurl", Config.BASE_URL + "api/POSM/GetPOSMDetailsByUserORPOSMID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + retailer_id + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10));

        Ion.with(this)
                .load(Config.BASE_URL + "api/Branding/GetBrandingDetailsByUserORBrandingID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intBrandingId=0&intRetailerId="+retailer_id+"&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        String response = result.toString();
                        Log.d("competitorresponseposm", response);
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            posm_id=jsonArray.getJSONObject(0).getString("BrandingID");
                            ArrayCompetitorNames.clear();
                            ArrayCompetitorBrandID.clear();
                            ArrayCompetitorBrID.clear();
                            //posm_id = jsonArray.getString("ShelfID");
                            if (posm_id.contains("null"))
                            {
                                posm_id = "0";
                                TAG_METHOD="INSERT";

                            }else
                            {
                                TAG_METHOD="UPDATE";

                            }

                            Brands.clear();
                            ArrayCompetitorBrID.clear();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject CompetitorsObject = jsonArray.getJSONObject(i);
                                if(CompetitorsArray == null) {
                                    CompetitorsArray = CompetitorsObject.getJSONArray("BrandingCompetitors");
                                }

                                if (CompetitorsArray != null) {
                                    Log.d("BrandingCompetitors", CompetitorsArray.length() + "");

                                    insertPointCompetitor.removeAllViews();
                                    for (int j = 0; j < CompetitorsArray.length(); j++) {

                                        BrandName = CompetitorsArray.getJSONObject(j).getString("BrandName");
                                        BrId = CompetitorsArray.getJSONObject(j).getString("BrandID");
                                        ArrayCompetitorBrID.add(BrId);
                                        Brands.add(BrandName);
                                        if(brandFlag){
                                            selectBrand.setText(Brands.get(0));
                                            brName = Brands.get(0);
                                            bId = ArrayCompetitorBrID.get(0);
                                        }

                                        //  JSONObject CompObject = CompetitorsArray.getJSONObject(j);
                                        //  if(CompArray == null)
                                        JSONArray CompArray =  CompetitorsArray.getJSONObject(j).getJSONArray("BrandingBrandActivityList");

                                        if (j == selected)
                                        {

                                            for (int k = 0; k < CompArray.length(); k++) {


                                                CompetitorName = CompArray.getJSONObject(k).getString("ActivityName");
                                                CompetitorQty = CompArray.getJSONObject(k).getString("Available");
                                                CompetitorQtyFlag = CompArray.getJSONObject(k).getString("Available");
//                                        posm_id = CompetitorsArray.getJSONObject(j).getString("ShelfID");
//                                        if (posm_id.contains("null")) {
//                                            posm_id = "0";
//                                        }
//                                        if (posm_id.contains("null")) {
//                                            //TAG_METHOD = "INSERT";
//                                            CompetitorQty = "";
//                                        } else {
//                                            //TAG_METHOD = "UPDATE";
//                                        }
                                                if (CompetitorQty.contains("null")) {
                                                    CompetitorQty = "";
                                                } else {
                                                }
                                                CompetitorId = CompArray.getJSONObject(k).getString("ActivityID");
                                                ArrayCompetitorNames.add(k, CompetitorName);
                                                ArrayCompetitorBrandID.add(k, CompetitorId);
                                                LinearLayout layout2 = new LinearLayout(getActivity());
                                                layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                                layout2.setOrientation(LinearLayout.HORIZONTAL);
                                                layout2.setWeightSum(3);
//***** Thumbnail
                                                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                                                lparams2.setMargins(10, 10, 10, 10);
                                                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                                                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                                                lparams3.setMargins(10, 10, 10, 10);


                                                int maxLength = 4;
                                                InputFilter[] FilterArray = new InputFilter[1];
                                                FilterArray[0] = new InputFilter.LengthFilter(maxLength);

                                                lparams3.gravity = Gravity.RIGHT;
                                                edC = new EditText(getActivity());
                                                edC.setGravity(Gravity.RIGHT);
                                                edC.setFilters(FilterArray);
                                                edC.setSingleLine(true);
                                                edC.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                edC.setGravity(Gravity.RIGHT);
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
                                                insertPointCompetitor.addView(layout2);
                                            }
                                        }
                                    }
                                }

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

    private void SetUpOldBackImages() {
        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intBrandID", "0");
        getjson.addProperty("intRetailerId", posmRetailerId);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Log.v("ImageLink", "nskdjfhnk");
        Ion.with(this)
                .load(Config.BASE_URL + "api/Branding/GetBrandingDetailsByUserORBrandingID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intBrandingId=0&intRetailerId="+posmRetailerId+"&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
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
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject ShelfPhotosObject = jsonArray.getJSONObject(i);
                                JSONArray ShelfPhotoArray = ShelfPhotosObject.getJSONArray("BrandingPhotos");
                                //totalInternetImages=resetvalue;
                                totalInternetImages=ShelfPhotoArray.length();
                                Log.d("totalInternetImages",totalInternetImages+"");
                                Config.brandingImageCount = ShelfPhotoArray.length();
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
            case R.id.posm_select_brand:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Brand");
                final String datas[] = new String[Brands.size()];
                for(int i=0; i<Brands.size();i++){
                    datas[i] = Brands.get(i);
                }
                builder.setItems(datas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        modelRefreshFirst++;
                        try{
                            insertPointCamera.removeAllViews();
                        }catch (Exception e){}
                        updatepageData();
                        selectBrand.setText(datas[which]);
                        selected = which;
                        Brands.clear();
                        brName = datas[which];
                        bId = ArrayCompetitorBrID.get(which);
                        ArrayCompetitorBrID.clear();
                        brandFlag=false;
                        //Config.brandingImages.clear();
                        //totalInternetImages = resetvalue;

                        try {
                            //insertPoint.removeAllViews();
                            insertPointCompetitor.removeAllViews();
                            //insertPointCamera.removeAllViews();
                            //allEds.clear();
                            //allEds1.clear();
                            //allEdsC.clear();
                            ModelsAsynTask(posmRetailerId);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;

            case R.id.tab_1:
                posm.setBackgroundColor(v.getResources().getColor(R.color.white));
                competitior.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                comments.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowCenter.setImageResource(R.drawable.blank);
                upArrowRight.setImageResource(R.drawable.blank);

                posm_rel.setVisibility(View.VISIBLE);
                comm_rel.setVisibility(View.GONE);
                comp_rel.setVisibility(View.GONE);
                counter = 0;
                txtUpdate.setText("NEXT");
                break;
            case R.id.tab_2:
                competitior.setBackgroundColor(v.getResources().getColor(R.color.white));
                posm.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                comments.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.blank);
                upArrowCenter.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);

                posm_rel.setVisibility(View.GONE);
                comm_rel.setVisibility(View.GONE);
                comp_rel.setVisibility(View.VISIBLE);
                counter = 1;
                txtUpdate.setText("NEXT");
                break;

            case R.id.tab_3:
                comments.setBackgroundColor(v.getResources().getColor(R.color.white));
                posm.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                competitior.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.blank);
                upArrowCenter.setImageResource(R.drawable.blank);
                upArrowRight.setImageResource(R.drawable.uparrow);

                posm_rel.setVisibility(View.GONE);
                comm_rel.setVisibility(View.VISIBLE);
                comp_rel.setVisibility(View.GONE);
                counter = 2;
                txtUpdate.setText("UPDATE");
                break;

            case R.id.posm_photo:

                String photo_cm = add_comment.getText().toString();
                if (photo_cm.toString().length() <= 0) {
                   // Toast.makeText(getActivity(), "plz fill comment", Toast.LENGTH_LONG).show();
                    Config.dialog(getActivity(), "Please provide comments before taking photos.", "Alert");

                } else {

                    Log.d("imagecountbranding","config:"+Config.brandingImageCount+"\nbrandingImage:"+Config.brandingImages.size()+"\napicount:"+Integer.parseInt(Config.userDetails.get(15)));

                    if (((Config.brandingImageCount+1)+Config.brandingImages.size()) > Integer.parseInt(Config.userDetails.get(15))){
                        Config.dialog(getActivity(),"You can take maximum of "+ Config.userDetails.get(15)+" photos only.","Alert");
                    }else{
                        Log.d("image_size",""+Config.brandingImages.size()+""+Config.imageInternetCount);
                        Intent intent = new Intent(getActivity(), CameraActivityBranding.class);
                        startActivity(intent);

                    }
                }
                break;

            case R.id.update:
                counter++;

                if (counter == 1){
                    competitior.setBackgroundColor(v.getResources().getColor(R.color.white));
                    posm.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                    comments.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                    upArrowLeft.setImageResource(R.drawable.blank);
                    upArrowCenter.setImageResource(R.drawable.uparrow);
                    upArrowRight.setImageResource(R.drawable.blank);
                    posm_rel.setVisibility(View.GONE);
                    comm_rel.setVisibility(View.GONE);
                    comp_rel.setVisibility(View.VISIBLE);
                    txtUpdate.setText("NEXT");
                }
                if (counter == 2){
                    comments.setBackgroundColor(v.getResources().getColor(R.color.white));
                    posm.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                    competitior.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                    upArrowLeft.setImageResource(R.drawable.blank);
                    upArrowCenter.setImageResource(R.drawable.blank);
                    upArrowRight.setImageResource(R.drawable.uparrow);
                    posm_rel.setVisibility(View.GONE);
                    comm_rel.setVisibility(View.VISIBLE);
                    comp_rel.setVisibility(View.GONE);
                    txtUpdate.setText("UPDATE");
                }
                if (counter == 3){
                    try {
                        if(Config.isNetworkAvailable(getActivity())){
                        updateData();
                        }else
                        {
                            Config.dialog(getActivity(),Config.networkAlert,"Alert");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;

        }
    }

    private void updateData() {

        String[] stringsModelAva = new String[allEds.size()];
        String[] stringsModelReq = new String[allEds1.size()];
        String[] stringsCompetitorQty = new String[allEdsC.size()];
        int CounterAvailableData=0;
        int CounterRequiredIntData=0;
        for(int i=0; i < allEds.size(); i++)
        {
            try {
                if(allEds.get(i).getText().toString().trim().equals("")) {
                    stringsModelAva[i] = "";
                }else
                {
                    stringsModelAva[i] = allEds.get(i).getText().toString();
                    available_counter++;
                    CounterAvailableData++;
                }
                if(allEds1.get(i).getText().toString().trim().equals("")) {
                    stringsModelReq[i] = "";
                }else
                {
                    stringsModelReq[i] = allEds1.get(i).getText().toString();
                    required_counter++;
                    CounterRequiredIntData++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            Log.d("stringsModelQty",stringsModelAva[i]);
        }

        //-----------------------------------------------------
        if(CounterAvailableData==CounterRequiredIntData&&CounterAvailableData!=0&&CounterRequiredIntData!=0)
        {
            Log.d("aaaa", allEdsC.size()+"");
            Log.d("bbbb", allEds.size()+"");
            for(int i=0; i < allEdsC.size(); i++)
            {
                try
                {
                    if(allEdsC.get(i).getText().toString().trim().equals(""))
                    {
                        stringsCompetitorQty[i] = "";
                    }else
                    {
                        stringsCompetitorQty[i] = allEdsC.get(i).getText().toString();
                    }
                } catch (ArrayIndexOutOfBoundsException e)
                {
                    e.printStackTrace();
                }
                Log.d("stringsCompetitorQty",stringsCompetitorQty[i]);

            }

            if(TAG_METHOD.contains("INSERT"))
            {
                stringProperty = "{\"IsActive\":\"1\",\"SetMethod\":\""+TAG_METHOD+"\",\"CreatedDate\":\""+Config.userDetails.get(13)+"\",\"CreatedDateYYYYMMDD\":\""+Config.userDetails.get(13)+"\",\"FMRUserName\":\""+Config.userDetails.get(10)+"\",\"BrandingID\":\"0\",\"JourneyId\":\""+posmJourneyId+"\",\"RetailerId\":\""+posmRetailerId+"\",\"BrandID\":\""+Config.userDetails.get(12)+"\",\"CompanyID\":\""+Config.userDetails.get(11)+"\",\"LoginUser\":\""+Config.userDetails.get(10)+""+"\",\"Comments\":\""+add_comment.getText().toString()+"\",";

            }
            else
            {
                stringProperty = "{\"IsActive\":\"1\",\"SetMethod\":\""+TAG_METHOD+"\",\"CreatedDate\":\""+Config.userDetails.get(13)+"\",\"CreatedDateYYYYMMDD\":\""+Config.userDetails.get(13)+"\",\"FMRUserName\":\""+Config.userDetails.get(10)+"\",\"BrandingID\":\""+posm_id+"\",\"JourneyId\":\""+posmJourneyId+"\",\"RetailerId\":\""+posmRetailerId+"\",\"BrandID\":\""+Config.userDetails.get(12)+"\",\"CompanyID\":\""+Config.userDetails.get(11)+"\",\"LoginUser\":\""+Config.userDetails.get(10)+""+"\",\"Comments\":\""+add_comment.getText().toString()+"\",";

            }
            Log.d("stringProperty",stringProperty);

            for(int i =0;i<allEds.size();i++)
            {
                Log.d("stringsModelQty"+i,stringsModelAva[i].toString().length()+"");

                // if (available_counter == required_counter){
                Log.d("checkvalue1", CounterAvailableData + " " + CounterRequiredIntData);
                //  if(CounterRequiredIntData==CounterAvailableData && CounterRequiredIntData!=0&&CounterAvailableData!=0) {
                if (stringsModelAva[i].toString().length() != 0) {
                    stringModel = "{\"ActivityName\":\"" + ArrayModelsName.get(i) + "\",\"Available\":\"" + stringsModelAva[i] + "\",\"Required\":\"" + stringsModelReq[i] + "\",\"BrandingID\":\"" + posm_id + "\",\"ActivityID\":" + ArrayModelsID.get(i) + "},";
                    JsonStringModels = JsonStringModels + stringModel;
                } else {
                }



            }
            Log.d("posmsetmodeldata",JsonStringModels);


            String compData = "{\"POSMID\":\""+posm_id+"\","+ "\"BrandID\""+":"+bId+","+"\"BrandName\""+":\""+brName+"\","+"\"BrandingBrandActivityList\""+":[";

//                for(int k =0;k<allEdsC.size();k++)
//                {
//
//                    if(stringsCompetitorQty[k].toString().length()!=0) {
            // stringCompetitors = "{\"ActivityName\":\"" + ArrayCompetitorNames.get(k) + "\",\"Available\":\"" + stringsCompetitorQty[k] + "\",\"ActivityID\":" + ArrayCompetitorBrandID.get(k) + "},";
            // JsonStringCompetitor = //JsonStringCompetitor + //+ stringCompetitors;
//                    }else {}


//                }
            updatepageData();
            String compData1 =   CompetitorsArray.toString();



            Log.d("checkvalue2", CounterAvailableData + " " + CounterRequiredIntData);
            String FinalJson=stringProperty+"\"BrandingItems\":"+" ["+JsonStringModels.substring(0, JsonStringModels.length() - 1)+"],"+"\"BrandingCompetitors\""+" :"+compData1+",\"POSMPhotos\":[]}";
            Log.d("finaljsonlog", FinalJson);
            sendingUpdatedData(FinalJson);
//                        Toast toast = Toast.makeText(getActivity(),"Sending...........", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
            Log.d("checkvalue","Send");



        }else
        {
            Toast toast = Toast.makeText(getActivity(),"Please Fill Proper Data", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            posm.setBackgroundColor(getResources().getColor(R.color.white));
            competitior.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            upArrowLeft.setImageResource(R.drawable.uparrow);
            upArrowCenter.setImageResource(R.drawable.blank);
            upArrowRight.setImageResource(R.drawable.blank);

            posm_rel.setVisibility(View.VISIBLE);
            comm_rel.setVisibility(View.GONE);
            comp_rel.setVisibility(View.GONE);
            counter = 0;
            txtUpdate.setText("NEXT");
        }





//            Toast toast = Toast.makeText(getActivity(),"Sending...........", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

        /*}
        else {
            CounterRequiredIntData = 0;
            CounterAvailableData=0;
            Config.dialog(getActivity(), "Please fill available and required quantity.", "Alert");
            posm.setBackgroundColor(getResources().getColor(R.color.white));
            competitior.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            upArrowLeft.setImageResource(R.drawable.uparrow);
            upArrowCenter.setImageResource(R.drawable.blank);
            upArrowRight.setImageResource(R.drawable.blank);

            posm_rel.setVisibility(View.VISIBLE);
            comm_rel.setVisibility(View.GONE);
            comp_rel.setVisibility(View.GONE);
            counter = 0;
            txtUpdate.setText("NEXT");
            Log.d("checkvalue","Fail");
            return;
//            Toast toast = Toast.makeText(getActivity(),"Please Fill Proper Data", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();
        }
*/
      /* }else{

           available_counter = 0;
           required_counter = 0;
           Config.dialog(getActivity(), "Please fill available and required quantity.", "Alert");
           posm.setBackgroundColor(getResources().getColor(R.color.white));
           competitior.setBackgroundColor(getResources().getColor(R.color.lightGrey));
           comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

           upArrowLeft.setImageResource(R.drawable.uparrow);
           upArrowCenter.setImageResource(R.drawable.blank);
           upArrowRight.setImageResource(R.drawable.blank);

           posm_rel.setVisibility(View.VISIBLE);
           comm_rel.setVisibility(View.GONE);
           comp_rel.setVisibility(View.GONE);
           counter = 0;
           txtUpdate.setText("NEXT");
           return;
       }
*/


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
        pDialog.setCancelable(false);
        Log.d("posmsetdata", "" + o);
        // String SelfUpdateSendFromUser = "api/Shelf/InputGetShelfDetailsByUserORShelfIDValidation?CreatedDate="+Config.userDetails.get(13)+"&strFMRUserName="+Config.userDetails.get(10)+"&intShelfId=0&intRetailerId=1&intBrandID="+Config.userDetails.get(12)+"&intCompanyID="+Config.userDetails.get(11)+"&LoginUser="+Config.userDetails.get(10);
        Ion.with(this)
                //.load(Config.BASE_URL + SelfUpdateSendFromUser)
                .load(Config.BASE_URL + "api/Branding/SetBrandingMaster")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(o)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        Log.d("brandingmyvalue", result.toString());
                        String response = result.toString();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            posm_id = jsonObject.getJSONObject("OutPutName").getString("BrandingID");
                            String status= jsonObject.getJSONObject("OutPutName").getString("Status");
                            uploadpictures(posm_id,status);
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
        totaldeviceimages=Config.brandingImages.size();
        if(Config.brandingImages.size()>0)
        {
            //Toast.makeText(getActivity(),"Image prasent",Toast.LENGTH_LONG).show();

        }
        else
        {

            pDialog.hide();
            if(status.contains("SUCCESS"))
            {
                showAlertDialog("Branding information saved Successfully.",true);


            }else {
                showAlertDialog("Failed",false);
            }
        }
        for (int i = 0; i < Config.brandingImages.size(); i++)
        {
          //  Toast.makeText(getActivity(),"For loop executing "+i,Toast.LENGTH_LONG).show();

            strings[i] = allEdsImages.get(i+totalInternetImages).getText().toString();
            Log.d("brandingcaption" + i, strings[i]);


            Bitmap bitmap = BitmapFactory.decodeFile(Config.brandingImages.get(i));
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
            File f = new File(getActivity().getCacheDir(), "POSM_" + PosmFragment.posm_id + "_"+ts+(i + 1) + ".jpeg");
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
            pDialogUploadImage = new ProgressDialog(getActivity());
            pDialogUploadImage.setMessage("Uploading Image...");
            pDialogUploadImage.setCancelable(false);
            pDialogUploadImage.show();

            Ion.with(this)
                    //.load("http://vaahancheckvm.cloudapp.net:122/Api/Upload/PostFileDetail?strFile=Nilesh")
                    .load(Config.BASE_URL + "api/Upload/PostFileDetail?strTypeID=10&strID=" + posm_id + "&strTitle=" + strings[i] + "&strUserName=" + Config.userDetails.get(10).trim())
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
                                Log.d("imageresponse", response);
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
                                                            Config.brandingImages.clear();
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

    private  void updatepageData()
    {


        String NewBrandName = null;
        JSONArray CompArray = null;
        try {
            NewBrandName = CompetitorsArray.getJSONObject(selected).getString("BrandName");
            CompArray = CompetitorsArray.getJSONObject(selected).getJSONArray("BrandingBrandActivityList");
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

            JSONObject BrandingBrandActivityList =  CompetitorsArray.getJSONObject(selected);//.getJSONArray("BrandingBrandActivityList");
            BrandingBrandActivityList.remove("BrandingBrandActivityList");
            BrandingBrandActivityList.put("BrandingBrandActivityList", CompArray);
            // CompetitorsArray.getJSONObject(selected).optString("BrandingBrandActivityList", String.valueOf(CompArray));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        allEdsC.clear();


    }
/*
    private JSONArray  getCompetitorsArray() {
        JSONArray newCompetitorsArray = CompetitorsArray;
        try {
            for (int j = 0; j < newCompetitorsArray.length(); j++) {
                JSONArray CompArray = null;

                CompArray = newCompetitorsArray.getJSONObject(j).getJSONArray("BrandingBrandActivityList");


                for (int k = 0; k < CompArray.length(); k++) {


                    CompetitorName = CompArray.getJSONObject(k).getString("ActivityName");
                    CompetitorQty = CompArray.getJSONObject(k).getString("Available");
                    if (CompetitorQty.contains("null")) {
                        //JSONObject Available = CompArray.getJSONObject(j);//.getJSONObject("Available");
                        CompArray.remove(j);

                    }

                    try {

                        JSONObject BrandingBrandActivityList =  newCompetitorsArray.getJSONObject(selected);//.getJSONArray("BrandingBrandActivityList");
                        BrandingBrandActivityList.remove("BrandingBrandActivityList");
                        BrandingBrandActivityList.put("BrandingBrandActivityList", CompArray);
                        // CompetitorsArray.getJSONObject(selected).optString("BrandingBrandActivityList", String.valueOf(CompArray));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newCompetitorsArray;
    }
*/
public void showAlertDialog(String data,final boolean status) {
    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

    alertDialog.setMessage(data);
    alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.cancel();
            if (status == true) {
                startActivity(new Intent(getActivity(), HomePageActivity.class));
            }

        }
    });
    alertDialog.show();
}

}