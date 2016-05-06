package com.huawei.stellarfmr.stellarfmr.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OnlineAgentsFragment extends Fragment {

    TextView header;
    MapView amMapView;
    private GoogleMap agoogleMap;
    SharedPreferences sP;
    String token;
    double[] Arraylatitude;
    double[] Arraylongitude;
    String[]PlaceName;
    String isLastValue="1";
    private RadioGroup radioMapUserGroup;
    ProgressDialog pDialog;

    public OnlineAgentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_online_agents, container, false);

        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("ONLINE AGENTS");

//        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
        amMapView = (MapView) view.findViewById(R.id.mapView);
        amMapView.onCreate(savedInstanceState);
        amMapView.onResume();
        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        


        radioMapUserGroup = (RadioGroup) view.findViewById(R.id.rg);

        LoadMapFunction(isLastValue);
        radioMapUserGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
                switch (checkedId) {
                    case R.id.one:
                        Log.d("radioclicked", "1");
                        amMapView.invalidate();
                        isLastValue="1";
                        LoadMapFunction(isLastValue);
                        break;
                    case R.id.all:
                        isLastValue="0";
                        amMapView.invalidate();
                        LoadMapFunction(isLastValue);
                        Log.d("radioclicked", "all");
                        break;
                }

            }
        });




        return view;
    }

    private void LoadMapFunction(String isLastValue) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please Wait...");
        pDialog.show();
        agoogleMap = amMapView.getMap();

        Ion.with(this)
                .load(Config.BASE_URL + "api/Location/GetLocation?strUsername=ALL&strCreatedDate=" + Config.userDetails.get(13)+"&strIsLast="+isLastValue)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUsername", "ALL")
                .setBodyParameter("strCreatedDate", Config.userDetails.get(13))
                .setBodyParameter("strIsLast", isLastValue)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            Log.d("onlineagentresponse", result.toString());
                            JSONObject responseObject = new JSONObject(result.toString());
                            JSONArray OutPutNameArray = responseObject.getJSONArray("OutPutName");
                            if (OutPutNameArray != null&&OutPutNameArray.length()>0) {
                                Arraylatitude = new double[OutPutNameArray.length()];
                                Arraylongitude = new double[OutPutNameArray.length()];
                                PlaceName = new String[OutPutNameArray.length()];
                                MarkerOptions marker;

                                agoogleMap.clear();
                                for (int i = 0; i < OutPutNameArray.length(); i++)
                                {
                                    Arraylatitude[i] = Double.parseDouble(OutPutNameArray.getJSONObject(i).getString("Latitude"));
                                    Arraylongitude[i] = Double.parseDouble(OutPutNameArray.getJSONObject(i).getString("Longitude"));
                                    PlaceName[i] = OutPutNameArray.getJSONObject(i).getString("FieldManagerName");
                                     marker = new MarkerOptions().position(
                                             new LatLng(Arraylatitude[i], Arraylongitude[i])).title(PlaceName[i]);

                                    // Changing marker icon
                                    marker.icon(BitmapDescriptorFactory
                                            .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

                                    // adding marker
                                    agoogleMap.addMarker(marker);
                                    CameraPosition cameraPosition = new CameraPosition.Builder()
                                            .target(new LatLng(Arraylatitude[i], Arraylongitude[i])).zoom(10).build();
                                    agoogleMap.animateCamera(CameraUpdateFactory
                                            .newCameraPosition(cameraPosition));
                                }
                                pDialog.hide();
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            pDialog.hide();
                        }catch (Exception e1)
                        {
                            pDialog.hide();
                        }


                    }
                });
    }


}