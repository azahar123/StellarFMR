package com.huawei.stellarfmr.stellarfmr.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPassword;
    EditText username, password;
    Boolean userFlag = false, passFlag = false;
    ImageView loginButton;
    String user, pass;
    JSONObject jsonObject;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String myIMEINumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        forgotPassword = (TextView) findViewById(R.id.forgotPass);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (ImageView) findViewById(R.id.loginButton);
        final LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Config.clearAllData();
        Config.userDetails.clear();
       username.setText("vivek.patil@axisvation.in");
       password.setText("pass@123");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isNetworkAvailable()) {
                    user = username.getText().toString().trim();
                    pass = password.getText().toString().trim();
                    int currentapiVersion = android.os.Build.VERSION.SDK_INT;
                    if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP)
                    {
                        if (ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.READ_PHONE_STATE)
                                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(LoginActivity.this,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast toast = Toast.makeText(LoginActivity.this,"No Permission Granted, Please enable All Permission.\n Goto Setting->App->Steller FMR->Permissions->Enable it", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return;

                        }
                        else
                        {
                            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                            myIMEINumber=telephonyManager.getDeviceId();
//                            Toast toast = Toast.makeText(LoginActivity.this,"os version 22 ,23 Mobile IMEI no. "+myIMEINumber, Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();

                            Log.d("myimei",myIMEINumber);

                        }


                    }else
                    {
                        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        myIMEINumber=telephonyManager.getDeviceId();
//                        Toast toast = Toast.makeText(LoginActivity.this,"os version less then 22  Mobile IMEI no. "+myIMEINumber, Toast.LENGTH_LONG);
//                        toast.setGravity(Gravity.CENTER, 0, 0);
//                        toast.show();
                        Log.d("myimei",myIMEINumber);
                    }

                    if (user.equals("") | user.equals(null) && pass.equals("") | pass.equals(null)) {
                        showAlertDialog("Please enter Username and Password");
                    } else if (user.equals("") | user.equals(null)) {
                        showAlertDialog("Please enter Username");
                    } else if (!isValidMail(user)) {
                        showAlertDialog("Username not valid");
                    } else if (pass.equals("") | pass.equals(null)) {
                        showAlertDialog("Please enter Password");
                    } else {
                        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                            try {
                                LoginCheck(user, pass);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }else{

                            showAlertDialog("Please enable GPS.");
                        }

                    }
                } else {
                    showAlertDialog(Config.networkAlert);
                }
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

//        setCustomFont();
//        focusListemers();
//        hideShowCursor();
    }

    public void LoginCheck(final String username, String password) {
        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Logging In...");
        pDialog.setCancelable(false);
        pDialog.show();

        Ion.with(LoginActivity.this)
                .load(Config.BASE_URL +"token?username="+username+"&password="+password+"&grant_type=password")
                .setBodyParameter("username", username)//"test@email.com")//username)  username)//
                .setBodyParameter("password", password)//"Test@123")//password)  password)//
                .setBodyParameter("grant_type", "password")
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        Log.d("Login_details",result);
                        //Toast.makeText(LoginActivity.this, result, Toast.LENGTH_LONG).show();
                        try {
                            jsonObject = new JSONObject(result);

                            if (result.contains("error")) {
                                showAlertDialog("Username or Password is Incorrect");
                            }
                            String myImeiServer = jsonObject.getString("IMEINumber");
                            String token = jsonObject.getString("access_token");
                            Config.userDetails.add(jsonObject.getString("FirstName"));      //0
                            Config.userDetails.add(jsonObject.getString("MiddleName"));     //1
                            Config.userDetails.add(jsonObject.getString("LastName"));       //2
                            Config.userDetails.add(jsonObject.getString("ArearName"));       //3
                            Config.userDetails.add(jsonObject.getString("CityName"));       //4
                            Config.userDetails.add(jsonObject.getString("BrandName"));       //5
                            Config.userDetails.add(jsonObject.getString("CompanyLogoPath"));   //6
                            Config.userDetails.add(jsonObject.getString("BrandLogoPath"));     //7
                            Config.userDetails.add(jsonObject.getString("SingInUTC")); //8
                            Config.userDetails.add(jsonObject.getString("ProfilePicturePath"));//9
                            Config.userDetails.add(username);//10
                            Config.userDetails.add(jsonObject.getString("CompanyID"));//11
                            Config.userDetails.add(jsonObject.getString("BrandID"));//12
                            Config.userDetails.add(jsonObject.getString("DateYYYYMMDD"));//13
                            Config.userDetails.add(jsonObject.getString("CreatedDate"));//14
                            Config.userDetails.add(jsonObject.getString("PhotoLimit"));//15
                            Config.userDetails.add(jsonObject.getString("LocationInterval"));//16
                            Config.userDetails.add(jsonObject.getString("RoleName"));//17
                            Log.d("myimeinumber",myImeiServer);
                            if ((token != null && myIMEINumber.equals(myImeiServer))||(token != null && myImeiServer.equals("0000000000"))) {
                                sP = getSharedPreferences("TOKEN", MODE_PRIVATE);
                                editor = sP.edit();
                                editor.putString("accessToken", token);
                                editor.commit();

                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(intent);
                            }else
                            {
                                Toast toast = Toast.makeText(LoginActivity.this,"IMEI Not Matched,Acces denied", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();

                            }


//                            String error = jsonObject.getString("error");
//                            if(error != null){
//
//                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }catch(NullPointerException ex)
                        {
                            ex.printStackTrace();
                        }


                        pDialog.hide();
                    }
                });
    }

    public void showAlertDialog(String data) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();

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


    private void focusListemers() {
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                username.setHint("");
                userFlag = false;
                return false;
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    username.setHint("USERNAME");
                    userFlag = true;
                }
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                password.setHint("");
                passFlag = false;
                return false;
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    password.setHint("PASSWORD");
                    passFlag = true;
                }
            }
        });
    }

    private void hideShowCursor() {
        final View activityRootView = findViewById(R.id.login_root);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    if (username != null && userFlag == true) {
                        username.setCursorVisible(true);
                    }
                    if (password != null && passFlag == true) {
                        password.setCursorVisible(true);
                    }
                } else {
                    if (username != null) {
                        username.setCursorVisible(false);
                    }
                    if (password != null) {
                        password.setCursorVisible(false);
                    }
                }
            }
        });
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}