package com.example.android.networkconnect;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
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
import java.util.concurrent.ExecutionException;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    public static final String TAG = "Network Connect";
    public static final String URI = "http://floodemergencycoordinator.apphb.com";

    public JSONObject token = null;
    // Reference to the fragment showing events, so we can clear it with a button
    // as necessary.
    private LogFragment mLogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
        SimpleTextFragment introFragment = (SimpleTextFragment)
                    getSupportFragmentManager().findFragmentById(R.id.intro_fragment);
        introFragment.setText(R.string.welcome_message);
        introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.fetch_action:
                token = logIn("testUser", "qwerty");
                return true;
            case R.id.clear_action:
                mLogFragment.getLogView().setText("");
                return true;
            case R.id.test_action:
                mLogFragment.getLogView().setText("");
                getLocations();
                return true;
        }
        return false;
    }

    private JSONObject logIn(String username, String password){
        PostRequestTask loginTask = new PostRequestTask();
        loginTask.addContent("grant_type", "password");
        loginTask.addContent("username", username);
        loginTask.addContent("password", password);
        String url = "/Token";

        return executeAsyncTakAndReturnResult(loginTask, url);
    }

    private void testConnection(){
        GetRequestTask task = new GetRequestTask();
        task.execute("/api/serviceUnit/testMessage");
    }

    private void getLocations(){
        GetRequestTask task = new GetRequestTask();
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/locations");
    }

    private void postLocation(){
        PostRequestTask task = new PostRequestTask();
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/location");

    }

    private void getTasks() {
        PostRequestTask task = new PostRequestTask();
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/location");
    }

    private void postTask(){

    }

    private void postBackupRequest(){

    }

    private JSONObject executeAsyncTakAndReturnResult(AsyncTask<String, String, String> task, String url) {
        String result;
        JSONObject jObject = null;

        try {
            result = task.execute(URI + url).get();
            jObject = new JSONObject(result);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return jObject;
    }

    private class GetRequestTask extends AsyncTask<String, String, String>{

        private List<NameValuePair> headersList = new ArrayList<>();
        private String result;

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

    private class PostRequestTask extends AsyncTask<String, String, String>{

        private List<NameValuePair> headersList = new ArrayList<>();
        private List<NameValuePair> contentList = new ArrayList<>();
        private String result;

        public void addHeader(String name, String value){
            headersList.add(new BasicNameValuePair(name, value));
        }

        public void addContent(String name, String value){
            contentList.add(new BasicNameValuePair(name, value));
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
                    post.addHeader("Authentication", "Bearer " + token.getString("access_token"));
                }

                for (NameValuePair pair : headersList){
                    post.addHeader(pair.getName(), pair.getValue());
                }

                String body = prepareContent();
                post.setEntity(new StringEntity(body));

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

        private String prepareContent() {
            String body = "";
            for (NameValuePair pair: contentList) {
                if (!body.equals("")){
                    body += "&";
                }
                body += pair.getName() + "=" + pair.getValue();
            }
            return body;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            this.result = result;
            Log.i(TAG, result);
        }
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
