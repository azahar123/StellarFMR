package com.huawei.stellarfmr.stellarfmr.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView header, submit;
    EditText emailId;
    JSONObject jsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        header = (TextView) findViewById(R.id.header_text);
        submit = (TextView) findViewById(R.id.fp_submit);

        emailId = (EditText) findViewById(R.id.fp_enter_email);

        header.setText("FORGOT PASSWORD?");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {

                    String email = emailId.getText().toString();
                    if (email.equals("") | email.equals(null)) {
                        showAlertDialog("Please enter Username");
                    } else if (!isValidMail(email)) {
                        showAlertDialog("Username not valid");
                    } else {
                        SubmitCheck(email);
                    }

                } else {


                    showAlertDialog(Config.networkAlert);
                }
            }
        });
    }

    public void SubmitCheck(String email)
    {

       // Toast.makeText(ForgotPasswordActivity.this, "API not ready", Toast.LENGTH_LONG).show();
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Sending email...");
        pDialog.show();

        Ion.with(ForgotPasswordActivity.this)
                .load(Config.BASE_URL + Config.FORGOT_PASSWORD)
                .setBodyParameter("Email", email)//"test@email.com")//username)  username)//
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        try {
                            Log.d("forgotpasswordresult", result.toString());
                            jsonObject = new JSONObject(result);
                            String status=jsonObject.getString("Status");
                            String err=jsonObject.getString("Error");

                            if (status.contains("Success")) {
                                showAlertDialog("Email send Successfully.");
                            }
                            else
                            {
                                showAlertDialog(err);

                            }


                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                        pDialog.hide();
                    }
                });

    }

    public void showAlertDialog(String data) {
        AlertDialog alertDialog = new AlertDialog.Builder(ForgotPasswordActivity.this).create();

        alertDialog.setMessage(data);
        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
