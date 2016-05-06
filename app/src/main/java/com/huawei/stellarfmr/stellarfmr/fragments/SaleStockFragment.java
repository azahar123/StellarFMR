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
import com.huawei.stellarfmr.stellarfmr.activity.CameraActivitySales;
import com.huawei.stellarfmr.stellarfmr.activity.HomePageActivity;
import com.huawei.stellarfmr.stellarfmr.adapter.DropDownJourneyAdapter;
import com.huawei.stellarfmr.stellarfmr.adapter.SalesAdapter;
import com.huawei.stellarfmr.stellarfmr.listdata.DropDownJourneyList;
import com.huawei.stellarfmr.stellarfmr.listdata.StocksListData;
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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.google.android.gms.internal.zzip.runOnUiThread;


public class SaleStockFragment extends Fragment implements View.OnClickListener {

    public static int jsonMainLoop = 0;
    public static String shelf_id = "0";
    public static String salesRetailerId;
    public static int resetvalue = 0;
    public static int totalInternetImages = 0;
    public static ImageView image;
    public static String SaleStockOrderID = "0";
    static List<EditText> allEds = new ArrayList<EditText>();
    static EditText ed, edC, edImageCaption;
    static List<EditText> allEdsImage = new ArrayList<EditText>();
    static int count[] = {2, 9, 9, 9, 9, 8, 9, 7, 7, 8, 9, 9, 9, 9, 8, 9, 7, 7, 8, 17, 18, 19, 19, 19, 19, 18, 19, 17, 17, 18};
    static LinearLayout insertPoint, insertPointCompetitor, insertPointCamera;
    ListView listView;
    ArrayList<StocksListData> myList = new ArrayList<>();
    ImageView upArrowLeft, upArrowRight;
    TextView sso, comments, header, update, getModel, photo;
    EditText add_comment, getSale, getStock, getOrder;
    SharedPreferences sP;
    RelativeLayout comm_rel;
    LinearLayout sale_stock_order_layout, sale_stock_order_quantity_layout, sso_layout;
    int year, month, day;
    JSONArray jsonArray;
    String[] model = new String[]{"Mate7 Gold", "Honor 4C", "S7 Youth 2-3G 16GB"};
    String[] sale = new String[]{"20", "22", "10"};
    String[] stock = new String[]{"15", "10", "15"};
    String[] order = new String[]{"30", "15", "15"};
    Spinner spinner;
    String ModelId, token, salesJourneyId, TAG_METHOD = "INSERT";
    String stringProperty, JsonStringSales = "", JsonStringStock = "", JsonStringOrder = "", finalJsonStringOrder = "", stringSale, stringStock, stringOrder, stringCompetitors;
    ArrayList<String> listname = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> journeyId = new ArrayList<>();
    ArrayList<String> retailerId = new ArrayList<>();
    List<EditText> array_sale = new ArrayList<EditText>();
    ArrayList<EditText> array_stock = new ArrayList<EditText>();
    ArrayList<EditText> array_order = new ArrayList<EditText>();
    ArrayList<String> ArrayModelsName = new ArrayList<String>();
    ArrayList<String> ArrayModelsID = new ArrayList<String>();
    JsonObject o;
    ArrayList<DropDownJourneyList> myList_J = new ArrayList<>();
    String RetailerID = "1";
    ProgressDialog pDialog,pDialogUploadImage;
    String modelName, stock_qot, sale_qot, orders;
    int totaldeviceimages;
    String imageCaption;
    EditText sales_stock_add_comments;
    int flag,salesCounter,stockCounter,orderCounter,counter;
    int CounterAvailableSales=0,CounterAvailableStock=0,CounterAvailableOrder=0;
    private ProgressDialog progressDialog,pd;
    int imageuploadflag=0;

    public SaleStockFragment() {
        // Required empty public constructor
    }

    public static void reloadData(Context context) {
        Log.d("reload", "success");


        for (int i = 0; i < Config.SalesInfoImages.size(); i++) {
            //jsonMainLoop++;
            Log.d("forloop", "" + Config.SalesInfoImages.size());
            jsonMainLoop++;
            if (i > (insertPointCamera.getChildCount() - totalInternetImages) - 1) {
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
                Bitmap ThumbImage = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(Config.SalesInfoImages.get(i)),
                        THUMBSIZE, THUMBSIZE);

                image.setImageBitmap(ThumbImage);

//***** Thumbnail
                //***** Add Caption
                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                lparams3.setMargins(10, 10, 10, 10);
                edImageCaption = new EditText(context);
                //edImageCaption.setVisibility(View.GONE);
                allEdsImage.add(edImageCaption);
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

        View view = inflater.inflate(R.layout.fragment_sale_stock, container, false);

        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        insertPoint = (LinearLayout) view.findViewById(R.id.sale_stock_order_layout);
        // sale_stock_order_quantity_layout = (LinearLayout) view.findViewById(R.id.sale_stock_order_quantity_layout);
        sso_layout = (LinearLayout) view.findViewById(R.id.sso_layout);
        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("SALES, STOCK AND ORDERS");
        sso = (TextView) view.findViewById(R.id.tab_1);
        comments = (TextView) view.findViewById(R.id.tab_2);
        sales_stock_add_comments=(EditText)view.findViewById(R.id.sales_stock_add_comments);
        insertPointCamera = (LinearLayout) view.findViewById(R.id.site_insert_point_image);
        //insertPoint = (LinearLayout) view.findViewById(R.id.site_insert_point);
        //  createDynamicView(getActivity());
        sso.setText("SALES, STOCK & ORDERS");
        comments.setText("COMMENTS");
        photo = (TextView) view.findViewById(R.id.ss_photo);


        upArrowLeft = (ImageView) view.findViewById(R.id.uparrow_left);
        upArrowRight = (ImageView) view.findViewById(R.id.uparrow_right);
        spinner = (Spinner) view.findViewById(R.id.spinner);
        update = (TextView) view.findViewById(R.id.update);
        update.setText("NEXT");
        add_comment = (EditText) view.findViewById(R.id.sales_stock_add_comments);
        listView = (ListView) view.findViewById(R.id.sale_stock_list);
//        listView.setItemsCanFocus(true);
//        listView.setClickable(false);

        comm_rel = (RelativeLayout) view.findViewById(R.id.sale_stock_comm_rel);

        listView.setItemsCanFocus(true);
        getListViewData();
        listView.setAdapter(new SalesAdapter(getActivity(), myList));

        sso.setOnClickListener(this);
        comments.setOnClickListener(this);
        update.setOnClickListener(this);
        photo.setOnClickListener(this);
        Config.SalesInfoImages.clear();
        reloadData(getActivity());
        if(Config.isNetworkAvailable(getActivity())) {
            getData_J();
        }else {Config.dialog(getActivity(),Config.networkAlert,"Error");}
        //  ModelsAsynTask("1");

        return view;
    }

    private void getListViewData() {
        for (int i = 0; i < model.length; i++) {
            // Create a new object for each list item
            StocksListData ld = new StocksListData();
            ld.setModel(model[i]);
            ld.setSale(sale[i]);
            ld.setStock(stock[i]);
            ld.setOrder(order[i]);
            myList.add(ld);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_1:
                sso.setBackgroundColor(v.getResources().getColor(R.color.white));
                comments.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);
                //listView.setVisibility(View.VISIBLE);
                comm_rel.setVisibility(View.GONE);
                sso_layout.setVisibility(View.VISIBLE);
                counter = 0;
                update.setText("NEXT");
                break;
            case R.id.tab_2:
                comments.setBackgroundColor(v.getResources().getColor(R.color.white));
                sso.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                upArrowRight.setImageResource(R.drawable.uparrow);
                upArrowLeft.setImageResource(R.drawable.blank);
                listView.setVisibility(View.GONE);
                sso_layout.setVisibility(View.GONE);
                comm_rel.setVisibility(View.VISIBLE);
                counter = 1;
                update.setText("UPDATE");

                break;
            case R.id.update:
                counter++;
                if (counter == 1){
                    // Toast.makeText(getActivity(),"inside if"+counter,Toast.LENGTH_SHORT).show();
                    comments.setBackgroundColor(v.getResources().getColor(R.color.white));
                    sso.setBackgroundColor(v.getResources().getColor(R.color.lightGrey));

                    upArrowRight.setImageResource(R.drawable.uparrow);
                    upArrowLeft.setImageResource(R.drawable.blank);
                    listView.setVisibility(View.GONE);
                    sso_layout.setVisibility(View.GONE);
                    comm_rel.setVisibility(View.VISIBLE);
                    update.setText("UPDATE");
                }
                if (counter == 2){
                    try {
                        if(Config.isNetworkAvailable(getActivity())) {
                            updateData();
                        }else {Config.dialog(getActivity(),Config.networkAlert,"Error");}
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.ss_photo:
                String photo_cm = add_comment.getText().toString();
                if (photo_cm.toString().length() <= 0) {
                    // Toast.makeText(getActivity(), "plz fill comment", Toast.LENGTH_LONG).show();
                    Config.dialog(getActivity(),"Please provide comments and then upload photo.","Alert");

                } else {

                    if (((Config.imageInternetCount+1)+Config.SalesInfoImages.size()) > Integer.parseInt(Config.userDetails.get(15))){
                        Config.dialog(getActivity(),"You can take maximum of "+ Config.userDetails.get(15)+" photos only.","Alert");
                    }else{

                        Intent intent = new Intent(getActivity(), CameraActivitySales.class);
                        startActivity(intent);

                    }
                }

                break;

        }
    }
    private void updateData(){
        String[] stringsSaleQty = new String[array_sale.size()];
        String[] stringsStockQty = new String[array_stock.size()];
        String[] stringsOrderQty = new String[array_order.size()];
        int ACounterAvailableSales=0;
        int ACounterAvailableStock=0;
        int ACounterAvailableOrder=0;
        for (int i = 0; i < array_sale.size(); i++)
        {

            try {
                if (array_sale.get(i).getText().toString().trim().equals("")) {
                    stringsSaleQty[i] = "";
                } else {
                    stringsSaleQty[i] = array_sale.get(i).getText().toString();
                    salesCounter++;
                    ACounterAvailableSales++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < array_stock.size(); i++)
        {

            try {
                if (array_stock.get(i).getText().toString().trim().equals("")) {
                    stringsStockQty[i] = "";
                } else {
                    stringsStockQty[i] = array_stock.get(i).getText().toString();
                    stockCounter++;
                    ACounterAvailableStock++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < array_order.size(); i++) {

            try {
                if (array_order.get(i).getText().toString().trim().equals("")) {
                    stringsOrderQty[i] = "";
                } else {
                    stringsOrderQty[i] = array_order.get(i).getText().toString();
                    orderCounter++;
                    ACounterAvailableOrder++;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }


        if(ACounterAvailableSales==ACounterAvailableStock&&ACounterAvailableSales==ACounterAvailableOrder&&ACounterAvailableSales!=0&&ACounterAvailableStock!=0&&ACounterAvailableOrder!=0)
        {

            if (TAG_METHOD.contains("INSERT")) {
                stringProperty = "{\"IsActive\":1,\"setMethod\":\"" + TAG_METHOD + "\",\"CreatedDateYYYYMMDD\":\"" + Config.userDetails.get(13) + "\",\"FMRUserName\":\"" + Config.userDetails.get(10) + "\",\"SaleStockOrderID\":" + "0" + ",\"RetailerId\":\"" + salesRetailerId + "\",\"BrandID\":\"" + Config.userDetails.get(12) + "\",\"CompanyID\":\"" + Config.userDetails.get(11) + "\",\"LoginUser\":\"" + Config.userDetails.get(10) + "" + "\",\"Comments\":\"" +sales_stock_add_comments.getText().toString()+ "\",";

            } else {
                stringProperty = "{\"IsActive\":1,\"setMethod\":\"" + TAG_METHOD + "\",\"CreatedDateYYYYMMDD\":\"" + Config.userDetails.get(13) + "\",\"FMRUserName\":\"" + Config.userDetails.get(10) + "\",\"RetailerId\":\"" + salesRetailerId + "\",\"SaleStockOrderID\":" + SaleStockOrderID + ",\"BrandID\":\"" + Config.userDetails.get(12) + "\",\"CompanyID\":\"" + Config.userDetails.get(11) + "\",\"LoginUser\":\"" + Config.userDetails.get(10) + "" + "\",\"Comments\":\"" + sales_stock_add_comments.getText().toString() + "\",";

            }
            for (int i = 0; i < array_sale.size(); i++)
            {
                Log.d("array_size",""+array_sale.size());
                //  if (salesCounter == stockCounter && stockCounter == orderCounter){
                //  if(CounterAvailableSales==CounterAvailableStock&&CounterAvailableSales==CounterAvailableOrder&&CounterAvailableSales!=0&&CounterAvailableStock!=0&&CounterAvailableOrder!=0) {

                if (stringsSaleQty[i].toString().length() != 0) {

                    stringSale = "{\"ModelName\":\"" + ArrayModelsName.get(i) + "\",\"Sale\":\"" + stringsSaleQty[i] + "\",\"Stock\":" + stringsStockQty[i] + ",\"Orders\":" + stringsOrderQty[i] + ",\"ModelID\":" + ArrayModelsID.get(i) + "},";
                    JsonStringSales = JsonStringSales + stringSale;
                } else {

                }/*
            }else
            {
                CounterAvailableSales=0;
                CounterAvailableStock=0;
                CounterAvailableOrder=0; sso.setBackgroundColor(getResources().getColor(R.color.white));
                comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);
                //listView.setVisibility(View.VISIBLE);
                comm_rel.setVisibility(View.GONE);
                sso_layout.setVisibility(View.VISIBLE);
                counter = 0;
                update.setText("NEXT");
                return;
            }*/
            /*}else{

                salesCounter = 0;
                stockCounter = 0;
                orderCounter = 0;
                // startActivity(new Intent(getActivity(),HomePageActivity.class));
                Config.dialog(getActivity(), "Please fill sales ,stock and order field.", "Error");
                return;
            }*/


            }
            // stringSale = "{\"ModelName\":\"" + ArrayModelsName.get(i) + "\",\"Sale\":\"" + stringsSaleQty[i] + "\",\"SaleStockOrderID\":\"" + "0" + "\",\"ModelID\":" + ArrayModelsID.get(i) + "},";


            // finalJsonStringOrder = JsonStringSales.substring(0, JsonStringSales.length() - 1)+JsonStringStock.substring(0, JsonStringStock.length() - 1)+JsonStringOrder.substring(0, JsonStringOrder.length() - 1);
            // if (salesCounter == stockCounter && stockCounter == orderCounter){
            Log.d("allcounter",""+salesCounter+stockCounter+orderCounter);

            //  if(CounterAvailableSales==CounterAvailableStock&&CounterAvailableSales==CounterAvailableOrder&&CounterAvailableSales!=0&&CounterAvailableStock!=0&&CounterAvailableOrder!=0) {

            String FinalJson = stringProperty + "\"SaleStockModelOrderModelList\":" + " [" + JsonStringSales.substring(0, JsonStringSales.length() - 1) + "],\"SaleStockModelOrderPhotosList\":[]}";

            Log.d("finaljson", FinalJson);
            sendingUpdatedData(FinalJson);
//                        Toast toast = Toast.makeText(getActivity(),"Sending...........", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();


        }else
        {
            Toast toast = Toast.makeText(getActivity(),"Please Fill Proper...........", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            sso.setBackgroundColor(getResources().getColor(R.color.white));
            comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

            upArrowLeft.setImageResource(R.drawable.uparrow);
            upArrowRight.setImageResource(R.drawable.blank);
            //listView.setVisibility(View.VISIBLE);
            comm_rel.setVisibility(View.GONE);
            sso_layout.setVisibility(View.VISIBLE);
            counter = 0;
            update.setText("NEXT");
        }

        //                Toast toast = Toast.makeText(getActivity(),"Sending...........", Toast.LENGTH_LONG);
//            toast.setGravity(Gravity.CENTER, 0, 0);
//            toast.show();

           /* }else
            {
                CounterAvailableSales=0;
                CounterAvailableStock=0;
                CounterAvailableOrder=0;
                sso.setBackgroundColor(getResources().getColor(R.color.white));
                comments.setBackgroundColor(getResources().getColor(R.color.lightGrey));

                upArrowLeft.setImageResource(R.drawable.uparrow);
                upArrowRight.setImageResource(R.drawable.blank);
                //listView.setVisibility(View.VISIBLE);
                comm_rel.setVisibility(View.GONE);
                sso_layout.setVisibility(View.VISIBLE);
                counter = 0;
                update.setText("NEXT");
                return;
            }
*/
        /*}else{
            salesCounter = 0;
            stockCounter = 0;
            orderCounter = 0;
            // startActivity(new Intent(getActivity(),HomePageActivity.class));
            Config.dialog(getActivity(), "Please fill sales ,stock and order field.", "Error");
            return;
        }*/
    }

    public void getData_J() {
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");

        //Toast.makeText(getActivity(), MyCurrentDate, Toast.LENGTH_LONG).show();
        String j = Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=" + Config.userDetails.get(14);
        //Toast.makeText(getActivity(),j,Toast.LENGTH_LONG).show();
        Log.d("url", j);
        //Toast.makeText(getActivity(), formattedDate, Toast.LENGTH_LONG).show();
        Ion.with(getActivity())
                .load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=" + Config.userDetails.get(14))
                        //.load(Config.BASE_URL + "api/Journey/GetJourneyByUserID?strUserName=" + Config.userDetails.get(10) + "&strJourneyId=0&strJourneyDate=19-Dec-2015"/*+MyCurrentDate*/)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strJourneyId", salesJourneyId)
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
                            spinner.setSelection(Config.defaultSpinnerSelecter);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            @Override
                            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                                       int arg2, long arg3) {

                                // Toast.makeText(getActivity(), "spinner", Toast.LENGTH_SHORT).show();
                                TAG_METHOD = "INSERT";
                                ArrayModelsName.clear();
                                ArrayModelsID.clear();
                                allEdsImage.clear();

                                try {
                                    insertPoint.removeAllViews();
                                    insertPointCamera.removeAllViews();
                                    // sale_stock_order_quantity_layout.removeAllViews();
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                Config.SalesInfoImages.clear();
                                SaleStockOrderID = "0";


                                int imc_met = spinner.getSelectedItemPosition();
                                Config.defaultSpinnerSelecter=imc_met;
                                Config.retailerId = retailerId.get(imc_met);
                                // ModelsAsynTask(retailerId.get(imc_met));
                                salesRetailerId = retailerId.get(imc_met);
                                Log.d("retailer", salesRetailerId);
                                salesJourneyId = journeyId.get(imc_met);

                                totalInternetImages = resetvalue;
                                Config.imageInternetCount = 0;
                                try {
                                    insertPoint.removeAllViews();
                                    // sale_stock_order_quantity_layout.removeAllViews();
                                    insertPointCamera.removeAllViews();

                                    array_sale.clear();
                                    array_stock.clear();
                                    array_order.clear();
                                    ModelsAsynTask(salesRetailerId);

                                    // Toast.makeText(getActivity(), "shelf reta id" + salesRetailerId, Toast.LENGTH_LONG).show();
                                    // Toast.makeText(getActivity(), "shelf jou id" + salesJourneyId, Toast.LENGTH_LONG).show();

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
        // Log.d("sendingurlparameter", "createdate" + Config.userDetails.get(13) + "strFMRUserName"+Config.userDetails.get(10)+"shelf id"+shelf_id+"reta id"+retailer_id+"bran id"+Config.userDetails.get(12)+"comp id"+Config.userDetails.get(11)+"login user"+Config.userDetails.get(10));


        Ion.with(this)
                .load(Config.BASE_URL + "api/SaleStock/PostSaleStock")
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("CreatedDate", Config.userDetails.get(13))
                .setBodyParameter("FMRUserName", Config.userDetails.get(10))
                .setBodyParameter("SaleStockOrderID", SaleStockOrderID)
                .setBodyParameter("RetailerID", retailer_id)
                .setBodyParameter("BrandID", Config.userDetails.get(12))
                .setBodyParameter("CompanyID", Config.userDetails.get(11))
                .setBodyParameter("LoginUser", Config.userDetails.get(10))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        pDialog.hide();


                        try {

                            //jsonArray = new JSONArray(result);
                            Log.d("responseget", result);
                            try {
                                File root = new File(Environment.getExternalStorageDirectory(), "JsonResult");
                                if (!root.exists()) {
                                    root.mkdirs();
                                }
                                File gpxfile = new File(root, "salesstockget.txt");
                                FileWriter writer = new FileWriter(gpxfile);
                                writer.append(result);
                                writer.flush();
                                writer.close();
                                // Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                            } catch (IOException e2) {
                                e2.printStackTrace();

                            }
                            // JSONObject object = jsonArray.getJSONObject(0);
                            JSONObject object = new JSONObject(result);
                            SaleStockOrderID = object.getString("SaleStockOrderID");
                            String Comments=object.getString("Comments");
                            sales_stock_add_comments.setText(Comments);


                            JSONArray ssm = object.getJSONArray("SaleStockModelOrderModelList");
                            for (int i = 0; i < ssm.length(); i++) {

                                JSONObject output = ssm.getJSONObject(i);
                                modelName = output.getString("ModelName");
                                sale_qot = output.getString("Sale");
                                stock_qot = output.getString("Stock");
                                orders = output.getString("Orders");
                                String orderId = output.getString("SaleStockOrderID");

                                if (orderId.contains("null")) {
                                    //SaleStockOrderID = "0";

                                } else {

                                    // SaleStockOrderID = orderId1;
                                    TAG_METHOD = "UPDATE";
                                }

                                ModelId = output.getString("ModelID");
                                ArrayModelsID.add(i, ModelId);

                                LinearLayout layout = new LinearLayout(getActivity());
                                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                layout.setOrientation(LinearLayout.HORIZONTAL);
                                layout.setWeightSum(5);


                                LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.WRAP_CONTENT,2f);
                                lparams.setMargins(10, 10, 10, 10);
                                getModel = new TextView(getActivity());
                                getModel.setText(modelName);
                                ArrayModelsName.add(i, modelName);
                                getModel.setLayoutParams(lparams);

                                LinearLayout.LayoutParams lparams1 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
                                lparams1.setMargins(10, 10, 10, 2);
                                getSale = new EditText(getActivity());

                                getSale.setInputType(InputType.TYPE_CLASS_NUMBER);

                                getSale.setGravity(Gravity.RIGHT);
                                int maxLength = 4;
                                InputFilter[] FilterArray = new InputFilter[1];
                                FilterArray[0] = new InputFilter.LengthFilter(maxLength);
                                getSale.setFilters(FilterArray);

                                array_sale.add(getSale);
                                if (sale_qot.equals("null")) {
                                    getSale.setText("");
                                } else {
                                    if (sale_qot.length() > 0) {
                                        getSale.setText(sale_qot);
                                        getSale.setEnabled(false);
                                        getSale.setFocusable(false);
                                    }
                                    getSale.setText(sale_qot);
                                }
                                getSale.setLayoutParams(lparams1);


                                LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
                                lparams2.setMargins(10, 10, 10, 2);
                                getStock = new EditText(getActivity());
                                getStock.setInputType(InputType.TYPE_CLASS_NUMBER);

                                getStock.setGravity(Gravity.RIGHT);
                                getStock.setFilters(FilterArray);
                                array_stock.add(getStock);
                                if (stock_qot.equals("null")) {
                                    getStock.setText("");
                                } else {
                                    if (stock_qot.length() > 0) {
                                        getStock.setText(stock_qot);
                                        getStock.setEnabled(false);
                                        getStock.setFocusable(false);
                                    }
                                    getStock.setText(stock_qot);
                                }
                                getStock.setLayoutParams(lparams2);


                                LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                                        0, LinearLayout.LayoutParams.WRAP_CONTENT,1f);
                                lparams3.setMargins(10, 10, 10, 2);
                                getOrder = new EditText(getActivity());
                                getOrder.setInputType(InputType.TYPE_CLASS_NUMBER);
                                getOrder.setGravity(Gravity.RIGHT);
                                getOrder.setFilters(FilterArray);
                                array_order.add(getOrder);
                                if (orders.equals("null")) {
                                    getOrder.setText("");
                                } else {
                                    if (orders.length() > 0) {
                                        getOrder.setText(orders);
                                        getOrder.setEnabled(false);
                                        getOrder.setFocusable(false);
                                    }
                                    getOrder.setText(orders);
                                }
                                getOrder.setLayoutParams(lparams3);

                                layout.addView(getModel);
                                layout.addView(getSale);
                                layout.addView(getStock);
                                layout.addView(getOrder);

                                insertPoint.addView(layout);
                                // sale_stock_order_quantity_layout.addView(layout);
                            }
                            //   JSONObject ssm = jsonObject.getJSONObject("SaleStockModelOrderModelList");

                            //   Log.d("sales",response);


                            //shelf_id = jsonArray.getJSONObject(0).getString("ShelfID");
                            //shelf_id = jsonArray.getString("ShelfID");
//                            String comment=jsonArray.getJSONObject(0).getString("comment");
//                            add_comment.setText(comment);
                           /* if (shelf_id.contains("null")) {
                                shelf_id = "0";
                                TAG_METHOD = "INSERT";

                            } else {
                                TAG_METHOD = "UPDATE";

                            }*/
/*

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject ModelsObject = jsonArray.getJSONObject(i);
                                JSONArray ModelsArray = ModelsObject.getJSONArray("SaleStockModelOrderModelList");
                                if (ModelsArray != null) {
                                    Log.d("ModelsArrayLength", ModelsArray.length() + "");
                                    insertPoint.removeAllViews();
                                    for (int j = 0; j < ModelsArray.length(); j++) {
                                        ModelName = ModelsArray.getJSONObject(j).getString("ModelName");
                                        ModelQty = ModelsArray.getJSONObject(j).getString("Quantity");
//                                        shelf_id = ModelsArray.getJSONObject(j).getString("ShelfID");
//                                        if (shelf_id.contains("null")) {
//                                            shelf_id = "0";
//                                        }
                                        if (ModelQty.contains("null") || ModelQty.contains("0")) {
                                            ModelQty = "";
                                        }
                                        ModelId = ModelsArray.getJSONObject(j).getString("ModelID");

                                        ArrayModelsID.add(j, ModelId);
                                        ArrayModelsName.add(j, ModelName);

                                        LinearLayout layout2 = new LinearLayout(getActivity());
                                        layout2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                        layout2.setOrientation(LinearLayout.HORIZONTAL);
                                        layout2.setWeightSum(3);
/*//***** Thumbnail
                             LinearLayout.LayoutParams lparams2 = new LinearLayout.LayoutParams(
                             300, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
                             lparams2.setMargins(10, 10, 10, 10);
                             LinearLayout.LayoutParams lparams3 = new LinearLayout.LayoutParams(
                             150, LinearLayout.LayoutParams.WRAP_CONTENT, 2f);
                             lparams3.setMargins(10, 10, 10, 10);

                             ed = new EditText(getActivity());
                             ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                             allEds.add(ed);
                             ed.setLayoutParams(lparams3);
                             ed.setId(999 + j);
                             if (ModelQty.length() > 0) {
                             ed.setText(ModelQty);
                             ed.setEnabled(false);
                             ed.setFocusable(false);
                             ed.setClickable(false);
                             } else {
                             ed.setText(ModelQty);
                             }
                             txtMobelName = new TextView(getActivity());
                             txtMobelName.setLayoutParams(lparams2);
                             txtMobelName.setText(ModelName);
                             layout2.addView(txtMobelName);
                             layout2.addView(ed);
                             insertPoint.addView(layout2);


                             }
                             CompetitorAsynTask(salesRetailerId);
                             }

                             }*/
                            //-----end json loop-----


                        } catch (NullPointerException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                        pDialog.hide();
                        SetUpOldBackImages();

                    }
                });

    }

    private void SetUpOldBackImages() {
        Ion.with(this)
                .load(Config.BASE_URL + "api/SaleStock/PostSaleStock")
                        // .load("http://vaahancheckvm.cloudapp.net:124/WebService/MMSWebApi/api/Shelf/GetShelfDetailsByUserORShelfID?CreatedDate=20151217&strFMRUserName=pmr@gmail.com&intShelfId=1&intRetailerId=1&intBrandID=1&intCompanyID=5098&LoginUser=pmr@gmail.com")
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("CreatedDate", Config.userDetails.get(13))
                .setBodyParameter("FMRUserName", Config.userDetails.get(10))
                .setBodyParameter("SaleStockOrderID", SaleStockOrderID)
                .setBodyParameter("RetailerID", salesRetailerId)
                .setBodyParameter("BrandID", Config.userDetails.get(12))
                .setBodyParameter("CompanyID", Config.userDetails.get(11))
                .setBodyParameter("LoginUser", Config.userDetails.get(10))
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        try {
                            // JSONArray array = new JSONArray(result);
                            JSONObject object = new JSONObject(result);
                            JSONArray image_array = object.getJSONArray("SaleStockModelOrderPhotosList");

                            totalInternetImages = resetvalue;
                            totalInternetImages = image_array.length();
                            Config.imageInternetCount = image_array.length();
                            Log.d("totalInternetImages", totalInternetImages + "");

                            if (image_array != null) {
                                for (int k = 0; k < image_array.length(); k++) {
                                    String imgPath = null;
                                    String imgCaption = null;

                                    JSONObject my_back_image_path = image_array.getJSONObject(k);
                                    imgPath = my_back_image_path.getString("PhotoDisplayPath");
                                    imgCaption = my_back_image_path.getString("PhotoCaption");

                                    Log.d("img_path",imgPath +" image_caption"+imgCaption);
                                    new DownloadImage().execute(imgPath, imgCaption);


                                }
                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                    }
                });

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
        Ion.with(this)
                //.load(Config.BASE_URL + SelfUpdateSendFromUser)
                .load(Config.BASE_URL + "api/SaleStock/PostUpdateSaleStock")
                .setHeader("authorization", "bearer " + token)
                .setJsonObjectBody(o)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.hide();
                        // Log.d("myvalue", result.toString());
                        String response = result.toString();

                        Log.d("response_last", response);
                        try {

                            JSONObject object = new JSONObject(response);
                            SaleStockOrderID = object.getString("SaleStockOrderID");
                            String status= object.getString("Status");

                            uploadpictures(SaleStockOrderID,status);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                        // Toast.makeText(getActivity(), "inset-response-id" + SaleStockOrderID, Toast.LENGTH_SHORT).show();



                    }

                });


    }

    private void uploadpictures(String saleStockOrderID, String status) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();

        int d = allEdsImage.size();
        String[] strings = new String[d];
        //----------image upload multi part using ion library---------
        totaldeviceimages = Config.SalesInfoImages.size();
        Log.d("totaldeviceimages", "" + totaldeviceimages);
        Log.d("stotalinternetimages", "" + totalInternetImages);
        if(Config.SalesInfoImages.size()>0)
        {
            //Toast.makeText(getActivity(),"Image prasent",Toast.LENGTH_LONG).show();

        }
        else
        {

            pDialog.hide();
            if(status.contains("SUCCESS"))
            {
                showAlertDialog("Sales Stock information saved Successfully.",true);


            }else {
                showAlertDialog("Failed",false);
            }
        }
        for (int i = 0; i < Config.SalesInfoImages.size(); i++) {

            strings[i] = allEdsImage.get(i+totalInternetImages).getText().toString();
            Log.d("caption" + i, strings[i]);


            Bitmap bitmap = BitmapFactory.decodeFile(Config.SalesInfoImages.get(i));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            int srcWidth = bitmap.getWidth();
            int srcHeight = bitmap.getHeight();
            int dstWidth, dstHeight;
            dstWidth = (int) (srcWidth * 0.5f);
            dstHeight = (int) (srcHeight * 0.5f);
            bitmap = Bitmap.createScaledBitmap(bitmap, dstWidth, dstHeight, true);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] bitmapdata = byteArrayOutputStream.toByteArray();
            Long tsLong = System.currentTimeMillis() / 1000;
            String ts = tsLong.toString();
            File f = new File(getActivity().getCacheDir(), "Sales_" + saleStockOrderID + "_" + ts + "_" + (i + 1) + ".jpeg");
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
                    .load(Config.BASE_URL + "api/Upload/PostFileDetail?strTypeID=6&strID=" + saleStockOrderID + "&strTitle=" + strings[i] + "&strUserName=" + Config.userDetails.get(10).trim())
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
                                //   JSONObject jsonObjectsite = jsonObject.getJSONObject("OutPutName");
                                //Toast.makeText(getActivity(), jsonObjectsite.getString("SiteVisitID"), Toast.LENGTH_LONG).show();
                                String success = jsonObject.getString("OutPutName");
                                if (success.contains("SUCCESS")) {
                                    flag++;

                                    //Config.dialog(getActivity(), "Data save successfully", "Task");
                                    //getActivity().getSupportFragmentManager().popBackStack();
                                    if (flag == totaldeviceimages) {
                                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                                        alertDialog.setTitle("Alert");
                                        alertDialog.setMessage("Data save successfully");
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
                                                            Config.SalesInfoImages.clear();
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

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        String d_imageCaption = null;


        @Override
        protected void onPreExecute() {
            pDialog.show();
            super.onPreExecute();
            //showProgressDialog("Please wait...", "Images Downloading");
            //pd = ProgressDialog.show(getActivity(), "", "images Loading...", true, false);

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

                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            pd.dismiss();
                        }
                    });

                } catch (Exception e) {
                }



//            if(progressDialog != null && progressDialog.isShowing())
//            {
//                progressDialog.dismiss();
//            }
            if (insertPointCamera.getChildCount() >= 0) {

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
                //edImageCaption.setVisibility(View.GONE);
                allEdsImage.add(edImageCaption);
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
                image = new ImageView(getActivity());
                image.setLayoutParams(lparams2);
                result = Bitmap.createScaledBitmap(result, 256, 256, true);
                //edImageCaption.setVisibility(View.GONE);
                image.setImageBitmap(result);

                layout2.addView(image);
                layout2.addView(edImageCaption);
                insertPointCamera.addView(layout2);


            }
        }
    }


    private void showProgressDialog(String title, String message)
    {
        progressDialog = new ProgressDialog(getActivity());

//        progressDialog.setTitle(title); //title
//
//        progressDialog.setMessage(message); // message
//
//        progressDialog.setCancelable(false);
//
//        progressDialog.show();
        progressDialog = ProgressDialog.show(getActivity(), title, message, true, false);
    }
    public void showAlertDialog(final String data,final boolean status) {
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