package com.huawei.stellarfmr.stellarfmr.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TrainingFragment extends Fragment implements View.OnClickListener {

    ImageView upArrowLeft, upArrowRight;
    TextView header, required, completed;
    TextView booleanTrainingRequired,booleanTrainingNotRequired,txtBooleanTrainingCompleted;
    EditText add_comment,trainnerName,trainerNameComment;
    TextView trainingUpdate;
    RelativeLayout req_rel, compl_rel;
    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    String s;
    ProgressDialog pDialog;
    int counter,btn_counter;
RelativeLayout rel_comp;
    SharedPreferences sP;
    String token,strTrainnerName,strTrainnerNameComment,strAdd_comment;
    String strTrue="true",strFalse="false",strTrainingReq,strTrainingNotReq,strTrainingCompleted;
    Spinner spinner;

    String TAG_METHOD = "INSERT";
    String TrainingId;
    public static String shelfRetailerId, shelfJourneyId;

    public TrainingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);

        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("TRAINING INFO");

        required = (TextView) view.findViewById(R.id.tab_1);
        completed = (TextView) view.findViewById(R.id.tab_2);
        trainingUpdate = (TextView) view.findViewById(R.id.update);
        trainingUpdate.setText("NEXT");
        rel_comp = (RelativeLayout) view.findViewById(R.id.rel_comp);
        booleanTrainingRequired=(TextView)view.findViewById(R.id.training_req);
        booleanTrainingNotRequired=(TextView)view.findViewById(R.id.training_not_req);
        txtBooleanTrainingCompleted=(TextView)view.findViewById(R.id.txtBooleanTrainingCompleted);
        required.setText("REQUIRED");
        completed.setText("COMPLETED");
        txtBooleanTrainingCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notrequiredgreenicon,0,0,0);
        upArrowLeft = (ImageView) view.findViewById(R.id.uparrow_left);
        upArrowRight = (ImageView) view.findViewById(R.id.uparrow_right);
        trainnerName=(EditText)view.findViewById(R.id.training_text);
        trainerNameComment=(EditText)view.findViewById(R.id.training_add_comments2);
        add_comment = (EditText) view.findViewById(R.id.training_add_comments);
        req_rel = (RelativeLayout) view.findViewById(R.id.rel_reqired);
        compl_rel = (RelativeLayout) view.findViewById(R.id.rel_completed);

        required.setOnClickListener(this);
        completed.setOnClickListener(this);
        trainingUpdate.setOnClickListener(this);
        txtBooleanTrainingCompleted.setOnClickListener(this);
        booleanTrainingRequired.setOnClickListener(this);
        booleanTrainingNotRequired.setOnClickListener(this);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        strTrainingReq=strFalse;
        strTrainingCompleted=strFalse;
        booleanTrainingRequired.setBackgroundColor(getResources().getColor(R.color.lightGrey));
        txtBooleanTrainingCompleted.setBackgroundColor(getResources().getColor(R.color.lightGrey));

        if(Config.isNetworkAvailable(getActivity())) {
            getData_J();
        }else {Config.dialog(getActivity(), Config.networkAlert, "Alert");}
        return view;
    }

    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

       // Toast.makeText(getActivity(), MyCurrentDate, Toast.LENGTH_LONG).show();
        String j = Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=" + Config.userDetails.get(14);
        //Toast.makeText(getActivity(),j,Toast.LENGTH_LONG).show();
        Log.d("url", j);
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=" + Config.userDetails.get(14))
                        //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", shelfJourneyId)
                .setBodyParameter("strJourneyDate", Config.userDetails.get(14))
                .setBodyParameter("IsActive", "1")
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
                                Config.shelfInfoImages.clear();
                                //shelf_id="0";


                                int imc_met = spinner.getSelectedItemPosition();
                                Config.defaultSpinnerSelecter = imc_met;
                                Config.retailerId = retailerId.get(imc_met);
                                shelfRetailerId = retailerId.get(imc_met);
                                shelfJourneyId = journeyId.get(imc_met);
                                add_comment.setText("");
                                trainnerName.setText("");
                                trainerNameComment.setText("");
                                //totalInternetImages = resetvalue;
                                try {
//                                    insertPoint.removeAllViews();
//                                    insertPointCompetitor.removeAllViews();
//                                    insertPointCamera.removeAllViews();
//                                    allEds.clear();
//                                    allEdsC.clear();
//                                    ModelsAsynTask(shelfRetailerId);

                                    // Toast.makeText(getActivity(), "shelf reta id" + shelfRetailerId, Toast.LENGTH_LONG).show();
                                    // Toast.makeText(getActivity(), "shelf jou id" + shelfJourneyId, Toast.LENGTH_LONG).show();
                                    GetTrainnigDetails(shelfJourneyId, shelfRetailerId);
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

    private void GetTrainnigDetails(String TJourneyId, String TRetailerId) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        final JsonObject json = new JsonObject();
        json.addProperty("JourneyID", TJourneyId);
        json.addProperty("RetailerID", TRetailerId);
        json.addProperty("CompanyID", Config.userDetails.get(11).trim());
        json.addProperty("BrandID", Config.userDetails.get(12).trim());
        json.addProperty("CreatedDate", Config.userDetails.get(13).trim());
        json.addProperty("CreatedDateYYYYMMDD", Config.userDetails.get(13).trim());
        json.addProperty("FMRUserName", Config.userDetails.get(10).trim());
        json.addProperty("TrainingID", "0");
        json.addProperty("LoginUser", Config.userDetails.get(10).trim());
        Log.d("trainningrequest", "TJourneyId " + TJourneyId + "RetailerID " + TRetailerId + "comp id " + Config.userDetails.get(11) + "brand id" + Config.userDetails.get(12) + "created date" + Config.userDetails.get(13).trim());
        Log.d("sendjsonti", json + "");
        Ion.with(this)
                .load(Config.BASE_URL + "api/Training/PostTraining")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("trainningGet", result);
                        pDialog.hide();
                        try {
                            JSONObject jsonobject = new JSONObject(result);

                            TrainingId=jsonobject.getString("TrainingID");
                            String RequiredComment =jsonobject.getString("RequiredComment");
                            String TrainerName =jsonobject.getString("TrainerName");
                            String TrainerComment =jsonobject.getString("TrainerComment");
                            add_comment.setText(RequiredComment);
                            trainnerName.setText(TrainerName);
                            trainerNameComment.setText(TrainerComment);
                           // Toast.makeText(getActivity(),TrainingId+"",Toast.LENGTH_LONG).show();
                            if(TrainingId.contains("null"))
                            {
                                TAG_METHOD="INSERT";
                            }
                            else {
                                TAG_METHOD="UPDATE";
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
            case R.id.tab_1:
                required.setBackgroundColor(v.getResources().getColor(R.color.white));
                completed.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);

                req_rel.setVisibility(View.VISIBLE);
                compl_rel.setVisibility(View.GONE);
                btn_counter = 0;
                trainingUpdate.setText("NEXT");
                break;
            case R.id.tab_2:
                required.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                completed.setBackgroundColor(v.getResources().getColor(R.color.white));

                upArrowRight.setImageResource(R.drawable.uparrow);
                upArrowLeft.setImageResource(R.drawable.blank);

                req_rel.setVisibility(View.GONE);
                compl_rel.setVisibility(View.VISIBLE);
                btn_counter = 1;
                trainingUpdate.setText("UPDATE");
                break;
            case R.id.update:
                btn_counter++;
                Log.d("button1","Clicked");
                if (btn_counter == 1){
                    // Toast.makeText(getActivity(),"inside if"+counter,Toast.LENGTH_SHORT).show();
                    required.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                    completed.setBackgroundColor(v.getResources().getColor(R.color.white));

                    upArrowRight.setImageResource(R.drawable.uparrow);
                    upArrowLeft.setImageResource(R.drawable.blank);

                    req_rel.setVisibility(View.GONE);
                    compl_rel.setVisibility(View.VISIBLE);
                    trainingUpdate.setText("UPDATE");
                }
                if (btn_counter == 2){
                    Log.d("button2","Clicked success");
                    try {
                            strAdd_comment=add_comment.getText().toString();
                            strTrainnerName=trainnerName.getText().toString();
                            strTrainnerNameComment=trainerNameComment.getText().toString();

                            if(strTrainnerNameComment.length()>0) {
                                InsertOrUpdateTraingDetails(shelfJourneyId, shelfRetailerId, strAdd_comment, strTrainnerName, strTrainnerNameComment, strTrainingReq, strTrainingCompleted);
                            }else
                            {
                                Config.dialog(getActivity(),"Please provide comments and then upload photo.","Alert");
                                //Toast.makeText(getActivity(),"Please provide comments and then upload photo.",Toast.LENGTH_LONG).show();
                                btn_counter--;
                            }

                    }
                    catch (NullPointerException e)
                    {
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.training_req:
                strTrainingReq=strTrue;


                booleanTrainingRequired.setBackgroundColor(v.getResources().getColor(R.color.white));
                booleanTrainingNotRequired.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                break;
            case R.id.training_not_req:
                strTrainingReq=strFalse;
                booleanTrainingNotRequired.setBackgroundColor(v.getResources().getColor(R.color.white));

                booleanTrainingRequired.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                break;
            case R.id.txtBooleanTrainingCompleted:


                counter++;
                if(counter%2==0)
                {
                    txtBooleanTrainingCompleted.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                    rel_comp.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));
                    strTrainingCompleted=strFalse;
                    txtBooleanTrainingCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.notrequiredgreenicon, 0, 0, 0);

                }else {
                    txtBooleanTrainingCompleted.setBackgroundColor(v.getResources().getColor(R.color.white));
                    strTrainingCompleted=strTrue;
                    rel_comp.setBackgroundColor(v.getResources().getColor(R.color.white));
                    txtBooleanTrainingCompleted.setCompoundDrawablesWithIntrinsicBounds(R.drawable.completedicon,0,0,0);

                }
                break;



        }
    }

    private void InsertOrUpdateTraingDetails(String TJourneyId, String TRetailerId, String strAdd_comment, String strTrainnerName, String strTrainnerNameComment, String strTrainingReq,String strTrainingCompleted) {

        if(TrainingId.contains("null"))
        {
            TrainingId="0";
        }
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        final JsonObject json = new JsonObject();
        json.addProperty("SetMethod", TAG_METHOD);
        json.addProperty("JourneyID", TJourneyId);
        json.addProperty("RetailerID", TRetailerId);
        json.addProperty("CompanyID", Config.userDetails.get(11).trim());
        json.addProperty("BrandID", Config.userDetails.get(12).trim());
        json.addProperty("CreatedDateYYYYMMDD",  Config.userDetails.get(13).trim());
        json.addProperty("FMRUserName", Config.userDetails.get(10).trim());
        json.addProperty("TrainingID", TrainingId);
        json.addProperty("LoginUser", Config.userDetails.get(10).trim());
        json.addProperty("IsRequired", strTrainingReq);
        json.addProperty("RequiredComment", strAdd_comment);
        json.addProperty("IsCompleted", strTrainingCompleted);
        json.addProperty("TrainerName", strTrainnerName);
        json.addProperty("TrainerComment", strTrainnerNameComment);
        json.addProperty("IsActive", 1);

        //Log.d("trainningrequest", "TJourneyId " + TJourneyId + "RetailerID " + TRetailerId + "comp id " + Config.userDetails.get(11) + "brand id" + Config.userDetails.get(12) + "created date" + Config.userDetails.get(13).trim());
        Log.d("trainingsetsendjson", json + "");
        Ion.with(this)
                .load(Config.BASE_URL + "api/Training/PostUpdateTraining")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(json)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pDialog.hide();

                    Log.d("setJsonResult", result);

                        try {
                            JSONObject jsonobject = new JSONObject(result);
                            String status=jsonobject.getString("Status");
                            if(status.contains("SUCCESS"))
                            {
                                showAlertDialog("Training Information Saved Successfully.", true);

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
