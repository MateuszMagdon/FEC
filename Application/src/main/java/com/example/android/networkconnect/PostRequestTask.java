package com.example.android.networkconnect;

import android.os.AsyncTask;

import com.example.android.common.logger.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
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
class PostRequestTask extends AsyncTask<String, String, String> {

    public static final String TAG = "Network Connect";

    private List<NameValuePair> headersList = new ArrayList<>();
    private String content;
    private String result;
    private JSONObject token;

    public PostRequestTask(JSONObject t){
        token = t;
    }

    public void addHeader(String name, String value){
        headersList.add(new BasicNameValuePair(name, value));
    }

    public void addContent(String content){
        this.content = content;
    }

    public String getResult() {
        return result;
    }

    @Override
    protected String doInBackground(String... uri) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response;
        String responseString = null;
        try {
            HttpPost post = new HttpPost(uri[0]);

            if (token != null){
                post.addHeader("Authorization", "Bearer " + token.getString("access_token"));
            }

            for (NameValuePair pair : headersList){
                post.addHeader(pair.getName(), pair.getValue());
            }

            //String content = prepareContent();
            post.setEntity(new StringEntity(content));

            post.addHeader("Content-Type", "application/json");
            post.addHeader("Accept", "application/json");

            response = httpclient.execute(post);
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
            //TODO Handle problems..
        } catch (IOException e) {
            Log.i(TAG, e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return responseString;
    }

//        private String prepareContent() {
//            String body = "";
//            for (NameValuePair pair: contentList) {
//                if (!body.equals("")){
//                    body += "&";
//                }
//                body += pair.getName() + "=" + pair.getValue();
//            }
//            return body;
//        }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        this.result = result;
        Log.i(TAG, result);
    }
}
