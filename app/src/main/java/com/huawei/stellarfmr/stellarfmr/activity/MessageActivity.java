package com.huawei.stellarfmr.stellarfmr.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.stellarfmr.stellarfmr.R;
import com.huawei.stellarfmr.stellarfmr.utils.Config;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MessageActivity extends AppCompatActivity {

    TextView header, name, desc, date;

    JSONObject jsonObject;
    JSONArray jsonArray;
    SharedPreferences sP;
    SharedPreferences.Editor editor;
    String token;
    String smsid;
    int totalAttachment;
    String attachmentfinalurl;
    // Progress Dialog
    private ProgressDialog pDialog;
    String FileSavedPath,filename;
    TextView AttachmentHeading;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        AttachmentHeading=(TextView)findViewById(R.id.message_attachment);
        AttachmentHeading.setVisibility(View.INVISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        header = (TextView) findViewById(R.id.header_text);
        header.setText("MESSAGE DETAILS");

        name = (TextView) findViewById(R.id.message_title);
        desc = (TextView) findViewById(R.id.message_desc);
        date = (TextView) findViewById(R.id.message_date);

        Bundle extra = getIntent().getExtras();
        name.setText("Sub: " + extra.getString("name"));
        desc.setText(extra.getString("description"));
        smsid=extra.getString("smsid");
        Toast.makeText(MessageActivity.this,extra.getString("smsid"),Toast.LENGTH_LONG).show();
        String m_date = extra.getString("date");
        date.setText(m_date);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String MyCurrentDate = df.format(c.getTime());
        sP = getSharedPreferences("TOKEN", Context.MODE_PRIVATE);
        token = sP.getString("accessToken", "null");
        Ion.with(this)
                .load(Config.BASE_URL + "api/Message/GetMessageByUserID?strUserName=" + Config.userDetails.get(10).trim() + "&strMessageId="+smsid+"&strMessageDate="+MyCurrentDate)
                .setHeader("authorization", "bearer " + token)
                .setBodyParameter("strUserName", Config.userDetails.get(10))
                .setBodyParameter("strMessageId", smsid)
                .setBodyParameter("strJourneyDate", MyCurrentDate)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        LinearLayout layout = (LinearLayout) findViewById(R.id.attachmentLayout);

                        try {
                            try {
                                jsonObject = new JSONObject(result);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                            //Toast.makeText(MessageActivity.this, result, Toast.LENGTH_LONG).show();
                            Log.d("smsatt",result);
                            jsonArray = jsonObject.getJSONArray("OutPutMessage");
                            if(jsonArray!=null) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONArray  atturl = jsonArray.getJSONObject(i).getJSONArray("MessageAttachBO");
                                    if(atturl!=null)
                                    {
                                        AttachmentHeading.setVisibility(View.VISIBLE);
                                        totalAttachment=atturl.length();


                                        for(int j = 0; j < atturl.length();j++)
                                        {
                                            JSONObject urlObject = atturl.getJSONObject(j);
                                            Log.d("innerurl", urlObject.getString("MessageFilePath"));
                                            attachmentfinalurl =urlObject.getString("MessageFilePath");
                                            final TextView dynamicTextView = new TextView(MessageActivity.this);
                                            final TextView dynamicTextViewName = new TextView(MessageActivity.this);
                                            dynamicTextView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                                            dynamicTextViewName.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                                            String fileName = attachmentfinalurl.substring( attachmentfinalurl.lastIndexOf('/')+1, attachmentfinalurl.length() );

                                            //dynamicTextView.setText("Attachment"+fileName+(j+1));
                                            dynamicTextView.setText(attachmentfinalurl);
                                            dynamicTextViewName.setText((j+1)+". "+fileName+"\n");
                                            dynamicTextViewName.setLinksClickable(true);
                                            dynamicTextView.setVisibility(View.GONE);

                                            dynamicTextView.setLinksClickable(true);
                                            dynamicTextView.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String fileName = attachmentfinalurl.substring(attachmentfinalurl.lastIndexOf('/') + 1, attachmentfinalurl.length());
                                                   // Toast.makeText(MessageActivity.this, fileName, Toast.LENGTH_LONG).show();
                                                    //DownloadFileFromInternet(attachmentfinalurl,fileName);
                                                    DownloadFileFromInternet(dynamicTextView.getText().toString(), dynamicTextView.getText().toString().substring(dynamicTextView.getText().toString().lastIndexOf('/') + 1, dynamicTextView.getText().toString().length()));

                                                }

                                            });
                                            layout.addView(dynamicTextView);
                                            dynamicTextViewName.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    String fileName = attachmentfinalurl.substring( attachmentfinalurl.lastIndexOf('/')+1, attachmentfinalurl.length() );
                                                   // Toast.makeText(MessageActivity.this,fileName,Toast.LENGTH_LONG).show();
                                                    //DownloadFileFromInternet(attachmentfinalurl,fileName);
                                                    DownloadFileFromInternet(dynamicTextView.getText().toString(),dynamicTextView.getText().toString().substring(dynamicTextView.getText().toString().lastIndexOf('/') + 1, dynamicTextView.getText().toString().length()));

                                                }

                                            });
                                            layout.addView(dynamicTextViewName);
                                        }

                                    }


                                }
                                //end json loop

                            }

                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }

                    }
                });

    }

    private void DownloadFileFromInternet(String attachmentfinalurl, String fileName) {
        new DownloadFileFromURL().execute(attachmentfinalurl,fileName);
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

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }
    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                filename=f_url[1];
                Log.d("fileurl1",attachmentfinalurl);
                Log.d("filename1",filename);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                FileSavedPath= Environment.getExternalStorageDirectory().getPath()+ "/";

                // Output stream to write file

                OutputStream output = new FileOutputStream(FileSavedPath+filename);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            //Toast.makeText(MessageActivity.this,FileSavedPath+filename,Toast.LENGTH_LONG).show();
            try {
//                Intent intent = new Intent();
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.setAction(Intent.ACTION_VIEW);
//                String type = "application/application";
//                intent.setDataAndType(Uri.fromFile(new File(FileSavedPath + filename)), type);
//                startActivity(intent);
//                Intent myIntent = new Intent(Intent.ACTION_VIEW);
//                myIntent.setData(Uri.fromFile(new File(FileSavedPath + filename)));
//                Intent j = Intent.createChooser(myIntent, "Choose an application to open with:");
//                startActivity(j);

//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(FileSavedPath + filename)));
//                intent.setType("multipart/");
//                startActivity(intent);

                MimeTypeMap myMime = MimeTypeMap.getSingleton();
                Intent newIntent = new Intent(Intent.ACTION_VIEW);
                String mimeType = myMime.getMimeTypeFromExtension(fileExt(FileSavedPath + filename).substring(1));
                newIntent.setDataAndType(Uri.fromFile(new File(FileSavedPath + filename)),mimeType);
                newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(newIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(MessageActivity.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                }


            }catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(MessageActivity.this,"No App Available to open file",Toast.LENGTH_LONG).show();
            }

        }

    }

    private String fileExt(String url) {

        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf("."));
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();
        }

    }
}