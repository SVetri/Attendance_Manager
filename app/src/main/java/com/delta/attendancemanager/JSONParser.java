package com.delta.attendancemanager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * Handles the parsing methods for JSON objects
 */
public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    /**
     * Constructor
     */
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method,
                                      JSONObject js) {

        // Making HTTP request
        requestMethod(method, url, js);

        String jsons = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            jsons = sb.toString();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        // try parse the string to a JSON object
        try {
            jObj = new JSONObject(jsons);
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // return JSON String
        return jObj;

    }

    /**
     * Makes a HTTP request
     *
     * @param method which method apply
     * @param url specify the url for the http request
     * @param js Specify the JSON Object to be used
     */
    private void requestMethod(String method, String url, JSONObject js) {
        try {
            String post = "POST";
            String get = "GET";
            // check for request method
            if (method.equals(post)) {
                // request method is POST
                // defaultHttpClient
//                JSONObject js=new JSONObject();
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                StringEntity s = new StringEntity(js.toString());
                httpPost.setEntity(s);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            } else if (method.equals(get)) {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                // String paramString = URLEncodedUtils.format(params, "utf-8");
                // url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }

        } catch (UnsupportedEncodingException e) {
            Log.e("JSONParser", e.toString());
        } catch (ClientProtocolException e) {
            Log.e("JSONParser", e.toString());
        } catch (IOException e) {
            Log.e("JSONParser", e.toString());
        }
    }

}
