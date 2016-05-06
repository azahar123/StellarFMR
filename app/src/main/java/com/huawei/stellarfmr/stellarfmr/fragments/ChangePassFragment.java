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
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.activity.HomePageActivity;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePassFragment extends Fragment {

    TextView header,btnPasswordUpdate;
    EditText old_password,new_password,retype_password;
    JSONObject jsonObject;
    String oldPassword,newPassword,reTypePassword;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String token;


    public ChangePassFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_pass, container, false);
        sP = getActivity().getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        header = (TextView) view.findViewById(R.id.header_text);
        header.setText("CHANGE PASSWORD");
        old_password = (EditText) view.findViewById(R.id.old_password);
        new_password = (EditText) view.findViewById(R.id.new_password);
        retype_password = (EditText) view.findViewById(R.id.retype_password);
        btnPasswordUpdate=(TextView)view.findViewById(R.id.update);
        btnPasswordUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                oldPassword= old_password.getText().toString();
                newPassword=new_password.getText().toString();
                reTypePassword=retype_password.getText().toString();
                if(oldPassword.equals("")||newPassword.equals("")||reTypePassword.equals(""))
                {
                   // Toast.makeText(getActivity(), Config.ToastAllFieldsRequired,Toast.LENGTH_LONG).show();
                    Config.dialog(getActivity(), Config.ToastAllFieldsRequired, "Alert");
                }
                else
                {
                    if(newPassword.contains(reTypePassword)) {
                        if(Config.isNetworkAvailable(getActivity())) {
                            changePassword(oldPassword, newPassword, reTypePassword);
                        }else {
                            Config.dialog(getActivity(),Config.networkAlert,"Alert");}
                    }
                    else
                    {
                        //Toast.makeText(getActivity(), "Confirmed Password must be matched with new Password",Toast.LENGTH_LONG).show();
                        Config.dialog(getActivity(),"Confirmed Password must be matched with new Password","Alert");

                    }

                }
            }
        });
        return view;
    }

    private void changePassword(String oldPassword, String newPassword, String reTypePassword) {

        // Toast.makeText(ForgotPasswordActivity.this, "API not ready", Toast.LENGTH_LONG).show();
        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Sending Request...");
        pDialog.show();
        pDialog.setCancelable(false);

        Ion.with(getActivity())
                .load(Config.BASE_URL + Config.CHANGE_PASSWORD)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("OldPassword", oldPassword)//"test@email.com")//username)  username)//
                .setBodyParameter("NewPassword", newPassword)
                .setBodyParameter("ConfirmPassword", reTypePassword)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        try {
                            jsonObject = new JSONObject(result);
                            Log.d("changepasswordresponse",result);
                            //String status=jsonObject(0).getString("Status");
                            if(result.contains("Success"))
                            {
                                showAlertDialog("Password Successfully Changed",true);

                            }else
                            {
                                showAlertDialog("Passwords must have at least one uppercase ,one special character and one numeric",false);

                            }



                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        pDialog.hide();
                    }
                });


    }
    public void showAlertDialog(String data, final boolean status) {
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
