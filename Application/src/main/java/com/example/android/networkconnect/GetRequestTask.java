package com.example.android.networkconnect;

import android.os.AsyncTask;

import com.example.android.common.logger.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mateu_000 on 2015-05-17.
 */
class GetRequestTask extends AsyncTask<String, String, String> {

    public static final String TAG = "Network Connect";

    private List<NameValuePair> headersList = new ArrayList<>();
    private String result;
    private JSONObject token;

    public GetRequestTask(JSONObject t){
        token = t;
    }

    public void addHeader(String name, String value){
        headersList.add(new BasicNameValuePair(name, value));
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            HttpGet get = new HttpGet(uri[0]);

            if (token != null){
                get.addHeader("Authorization", "Bearer " + token.getString("access_token"));
            }

            for (NameValuePair pair : headersList){
                get.addHeader(pair.getName(), pair.getValue());
            }

            get.addHeader("Content-Type", "application/json");
            get.addHeader("Accept", "application/json");

            response = httpclient.execute(get);
            StatusLine statusLine = response.getStatusLine();
            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                response.getEntity().writeTo(out);
                responseString = out.toString();
                out.close();
            } else{
                //Closes the connection.
                response.getEntity().getContent().close();
                throw new IOException(statusLine.getReasonPhrase());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.result = result;
        Log.i(TAG, result);
    }
}
