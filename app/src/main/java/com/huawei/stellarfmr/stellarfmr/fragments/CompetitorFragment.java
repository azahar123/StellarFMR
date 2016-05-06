package com.huawei.stellarfmr.stellarfmr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.HomePageActivity;
import com.huawei.stellarfmr.stellarfmr.adapter.DropDownJourneyAdapter;
import com.huawei.stellarfmr.stellarfmr.listdata.DropDownJourneyList;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class CompetitorFragment extends Fragment implements View.OnClickListener {

    TextView header, selectBrand, selectModel;
    private int year, month, day;
    //String[] data = {"Sony", "Huawei", "Samsung"};
    //String[] data1 = {"A", "B", "C"};
    String s,mCompetitorModelName,mCompetitorModelId;
    Spinner spinner;
    TextView txtUpdate;
    ProgressDialog pDialog;
    SharedPreferences sP;
    String token;
    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    ArrayList<String> listnamebrand = new ArrayList<>();
    ArrayList<String> brandId = new ArrayList<>();
    ArrayList<String> commentslist = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_Ja = new ArrayList<>();
    String CompetitorJourneyId;
    public static String shelfRetailerId,shelfJourneyId;
    String TAG_METHOD="INSERT";
    private String intPOSMId="0";
    public  static String CompetitorModelID = "0";
    JSONArray CompetitorsArray = null;
    String ModelName,ModelAva,ModelReq,ModelId,CompetitorName,CompetitorQty,CompetitorId,CompetitorQtyFlag, BrandName, BrId, bId;
    ArrayList<String> ArrayCompetitorBrID = new ArrayList<String>();
    ArrayList<String> Brands = new ArrayList<>();
    static  Boolean brandFlag = true;
    String stringProperty,JsonStringModels="",JsonStringCompetitor="",stringModel,stringCompetitors, brName;
    static int selected=0;
    public static String posmRetailerId,posmJourneyId;
    EditText comp_add_comments;
    String setCompetitorBrandID,setModelID,setComment;
    public CompetitorFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comp, container, false);

        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("COMPETITOR INFO");

        selectBrand= (TextView) view.findViewById(R.id.comp_select_brand);
        selectModel= (TextView) view.findViewById(R.id.comp_select_model);
        comp_add_comments=(EditText)view.findViewById(R.id.comp_add_comments);
        selectBrand.setOnClickListener(this);
        selectModel.setOnClickListener(this);
        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        spinner = (Spinner) view.findViewById(R.id.spinner);
        txtUpdate=(TextView)view.findViewById(R.id.update);
        txtUpdate.setOnClickListener(this);
        getData_J();
        return view;
    }
    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        Toast.makeText(getActivity(), Config.userDetails.get(14), Toast.LENGTH_LONG).show();
        String j = Config.BASE_URL +"api/Journey/GetJourneyByUserID?strUserName="+Config.userDetails.get(10)+"&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14);
        //Toast.makeText(getActivity(),j,Toast.LENGTH_LONG).show();
        Log.d("url", j);
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate="+Config.userDetails.get(14))
                        //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", CompetitorJourneyId)
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

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        } catch (NullPointerException ex) {
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
//                                ArrayModelsName.clear();
//                                ArrayModelsID.clear();
//                                ArrayCompetitorNames.clear();
//                                ArrayCompetitorBrandID.clear();
//                                try {
//                                    insertPoint.removeAllViews();
//                                    insertPointCompetitor.removeAllViews();
//                                    insertPointCamera.removeAllViews();
//                                } catch (Exception e1) {
//                                    e1.printStackTrace();
//                                }
//                                Config.shelfInfoImages.clear();
//                                shelf_id="0";
//
//
                                int imc_met = spinner.getSelectedItemPosition();
//                                Config.retailerId = retailerId.get(imc_met);
                                shelfRetailerId = retailerId.get(imc_met);
                                shelfJourneyId = journeyId.get(imc_met);
//                                totalInternetImages = resetvalue;
//                                try {
//                                    insertPoint.removeAllViews();
//                                    insertPointCompetitor.removeAllViews();
//                                    insertPointCamera.removeAllViews();
//                                    allEds.clear();
//                                    allEdsC.clear();
                                ModelsAsynTask(shelfRetailerId);
//
//                                    Toast.makeText(getActivity(),"shelf reta id"+shelfRetailerId,Toast.LENGTH_LONG).show();
//                                    Toast.makeText(getActivity(),"shelf jou id"+shelfJourneyId,Toast.LENGTH_LONG).show();
//
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                                Toast.makeText(getActivity(), "Total Images" + totalInternetImages, Toast.LENGTH_LONG).show();
//                                //SpinnerLoadingAndCheckingPreviousData(Config.retailerId, Config.userDetails.get(10), MyCurrentDate);

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

    private void ModelsAsynTask(String CompetitorRetailerId) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Log.d("sendingurlparameter", "createdate" + Config.userDetails.get(13) + "strFMRUserName" + Config.userDetails.get(10) + "shelf id" + intPOSMId + "reta id" + CompetitorRetailerId + "bran id" + Config.userDetails.get(12) + "comp id" + Config.userDetails.get(11) + "login user" + Config.userDetails.get(10));

        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("CreatedDate", Config.userDetails.get(13));
        getjson.addProperty("strFMRUserName", Config.userDetails.get(10));
        getjson.addProperty("intPOSMId", "0");
        getjson.addProperty("intRetailerId", CompetitorRetailerId);
        getjson.addProperty("intBrandID", Config.userDetails.get(12));
        getjson.addProperty("intCompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        Log.d("sendjsonget", getjson + "");
        Log.d("sendjsongeturl",Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + CompetitorRetailerId + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10));
        Ion.with(this)
                .load(Config.BASE_URL + "api/CompetitorModel/GetCompetitorModelByUserORCompetitorModelID?CreatedDate=" + Config.userDetails.get(13) + "&strFMRUserName=" + Config.userDetails.get(10) + "&intPOSMId=0&intRetailerId=" + CompetitorRetailerId + "&intBrandID=" + Config.userDetails.get(12) + "&intCompanyID=" + Config.userDetails.get(11) + "&LoginUser=" + Config.userDetails.get(10))
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        String response = result.toString();
                        Log.d("competitorinfogetrespon", response);
                        try {
                            File root = new
                                    File(Environment.getExternalStorageDirectory(), "JsonResult");
                            if (!root.exists()) {
                                root.mkdirs();
                            }
                            File gpxfile = new File(root, "competitorfragmentgetjson.txt");
                            FileWriter writer = new FileWriter(gpxfile);
                            writer.append(response);
                            writer.flush();
                            writer.close();
                            // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                        } catch (IOException e2) {
                            e2.printStackTrace();

                        }
                        try {
                            JSONObject jsonObject= new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("OutPutName");
                            CompetitorModelID=jsonArray.getJSONObject(0).getString("CompetitorModelID");
                            Log.d("CompetitorModelID",CompetitorModelID);
                            if (CompetitorModelID.contains("null"))
                            {
                                CompetitorModelID = "0";
                                TAG_METHOD="INSERT";

                            }else
                            {
                                TAG_METHOD="UPDATE";

                            }
                            //Main Outputnname Loop
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject CompetitorsObject = jsonArray.getJSONObject(i);
                                if(CompetitorsArray == null) {
                                    CompetitorsArray = CompetitorsObject.getJSONArray("objCompetitorModelModelItem");
                                }
                                if (CompetitorsArray != null)
                                {
                                    for (int j = 0; j < CompetitorsArray.length(); j++)
                                    {
                                        BrandName = CompetitorsArray.getJSONObject(j).getString("CompetitorBrandName");
                                        BrId = CompetitorsArray.getJSONObject(j).getString("CompetitorBrandID");
                                        ArrayCompetitorBrID.add(BrId);
                                        Brands.add(BrandName);
                                        if(brandFlag){
                                            selectBrand.setText(Brands.get(0));
                                            brName = Brands.get(0);
                                            bId = ArrayCompetitorBrID.get(0);
                                        }
                                        JSONArray CompArray =  CompetitorsArray.getJSONObject(j).getJSONArray("CompetitorModelModelList");
                                        Log.d("CompArrayb",CompArray+"");
                                        if (j == selected) {
                                            JSONArray CompArraya =  CompetitorsArray.getJSONObject(j).getJSONArray("CompetitorModelModelList");
                                            Log.d("CompArraya",CompArraya+"");
                                            retailerId = new ArrayList<>();
                                            listnamebrand = new ArrayList<>();
                                            for (int k = 0; k < CompArraya.length(); k++) {

                                                String CompetitorModelName= CompArraya.getJSONObject(k).getString("CompetitorModelName");
                                                String CompetitorModelId= CompArraya.getJSONObject(k).getString("CompetitorModelID");
                                                String CompetitorComments= CompArraya.getJSONObject(k).getString("Comment");
                                                listnamebrand.add(k,CompetitorModelName);
                                                brandId.add(k,CompetitorModelId);
                                                commentslist.add(k,CompetitorComments);
                                                if(k == 0)
                                                {
                                                    selectModel.setText(CompetitorModelName);
                                                    mCompetitorModelName = CompetitorModelName;
                                                    mCompetitorModelId = CompetitorModelId;

                                                }

                                                Log.d("CompetitorName", CompetitorModelName+" "+CompetitorModelId);

                                            }
                                        }
                                    }
                                }

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.comp_select_brand:

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Select Brand");
                final String datas[] = new String[Brands.size()];
                for(int i=0; i<Brands.size();i++){
                    datas[i] = Brands.get(i);
                }
                builder.setItems(datas, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectBrand.setText(datas[which]);
                        selected = which;
                        Brands.clear();
                        brName = datas[which];
                        bId = ArrayCompetitorBrID.get(which);
                        setCompetitorBrandID=ArrayCompetitorBrID.get(which);
                        ArrayCompetitorBrID.clear();
                        brandFlag=false;
                        ModelsAsynTask(shelfRetailerId);
                        //Toast.makeText(getActivity()," clicked "+which+" Brand id "+bId,Toast.LENGTH_LONG).show();
                    }
                });
                builder.show();
                break;


            case R.id.comp_select_model:

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setTitle("Select Model");
                final String datasmodel[] = new String[listnamebrand.size()];
                for(int i=0; i<listnamebrand.size();i++){
                    datasmodel[i] = listnamebrand.get(i);
                }
                builder1.setItems(datasmodel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectModel.setText(datasmodel[which]);
                        mCompetitorModelName = datasmodel[which];
                        mCompetitorModelId = brandId.get(which);
                        setModelID=brandId.get(which);
                        comp_add_comments.setText(commentslist.get(which));
                    }
                });
                builder1.show();
                break;
            case R.id.update:
                setComment =comp_add_comments.getText().toString();
                Log.d("sendcompetitorinfo",setCompetitorBrandID+" "+setModelID+" "+setComment);
                try
                {
                    if(setComment.length()>0)
                    {
                        sendInsertOrUpdateData(setCompetitorBrandID, setModelID, setComment, shelfRetailerId, CompetitorModelID);
                    }else
                    {
                        Config.dialog(getActivity(), "Please provide comments and then upload photo.", "Alert");
                    }

                }
                catch (Exception e){}

                break;

        }
    }

    private void sendInsertOrUpdateData(String setCompetitorBrandID, String setModelID, String setComment, String shelfRetailerId, String competitorModelID) {

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        final JsonObject getjson = new JsonObject();
        //getjson.addProperty("SetMethod", "INSERT");
        getjson.addProperty("SetMethod", TAG_METHOD);
        getjson.addProperty("CompetitorModelID", competitorModelID);
        getjson.addProperty("Comments", setComment);
        getjson.addProperty("CreatedDateYYYYMMDD", Config.userDetails.get(13));
        getjson.addProperty("FMRUserName", Config.userDetails.get(10));
        getjson.addProperty("ModelID", setModelID);
        getjson.addProperty("RetailerId", shelfRetailerId);
        getjson.addProperty("BrandID", Config.userDetails.get(12));
        getjson.addProperty("CompetitorBrandID", setCompetitorBrandID);
        getjson.addProperty("CompanyID", Config.userDetails.get(11));
        getjson.addProperty("LoginUser", Config.userDetails.get(10));
        getjson.addProperty("IsActive", "1");
        Log.d("sendcompetitorinfo", getjson + "");
        Ion.with(this)
                .load(Config.BASE_URL + "api/CompetitorModel/SetCompetitorModel")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(getjson)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result)
                    {
                        pDialog.hide();
                        try {
                            Log.d("competitorinfosetresult", result.toString());
                            JSONObject mainObject= new JSONObject(result.toString());
                            String status= mainObject.getJSONObject("OutPutName").getString("Status");
//                            Toast toast = Toast.makeText(getActivity(),status, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
                            if(status.contains("SUCCESS"))
                            {
                                showAlertDialog("Competitor Information Saved Successfully.", true);

                            }
                            else
                            {
                                showAlertDialog("Failed.", false);

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                });


    }
    public void showAlertDialog(String data, final boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

        alertDialog.setMessage(data);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (status == true) {
                    startActivity(new Intent(getActivity(), HomePageActivity.class));
                }

                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
