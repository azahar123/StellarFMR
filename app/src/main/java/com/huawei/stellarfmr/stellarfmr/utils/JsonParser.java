package com.huawei.stellarfmr.stellarfmr.utils;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JsonParser {

    JSONObject jsonObject;

    public JSONObject getJSONFromUrl(String url) {

        HttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            StatusLine statusLine = response.getStatusLine();
            int statuscode = statusLine.getStatusCode();
            if (statuscode != 200) {
                return null;
            }
            InputStream JsonData = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(JsonData));
            StringBuilder builder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            String jD = builder.toString();

            jsonObject = new JSONObject(jD);

        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        } catch (JSONException e) {
        }

        return jsonObject;
    }
}