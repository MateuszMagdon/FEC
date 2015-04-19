package com.example.android.networkconnect;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private PostRequestTask loginTask;

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
                logIn("testUser", "password");
                return true;
            // Clear the log view fragment.
            case R.id.clear_action:
              mLogFragment.getLogView().setText("");
              return true;
        }
        return false;
    }

    private String logIn(String username, String password){
        loginTask = new PostRequestTask();
        loginTask.addContent("grant_type", "password");
        loginTask.addContent("username", username);
        loginTask.addContent("password", password);

        loginTask.execute(URI + "/Token");

        return loginTask.getResult();
    }

    private void testConnection(){
        GetRequestTask task = new GetRequestTask();
        task.addHeader("Authentication", "Bearer " + loginTask.getResult());

        task.execute(URI + "/api/serviceUnit/testMessage");
    }

    private class GetRequestTask extends AsyncTask<String, String, String>{

        private List<NameValuePair> nameValuePair = new ArrayList<>();
        private String result;

        public void addHeader(String name, String value){
            nameValuePair.add(new BasicNameValuePair(name, value));
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
                HttpGet get = new HttpGet(uri[0]);

                for (NameValuePair pair : nameValuePair){
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
                //TODO Handle problems..
            } catch (IOException e) {
                //TODO Handle problems..
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

                for (NameValuePair pair : headersList){
                    post.addHeader(pair.getName(), pair.getValue());
                }

                post.setEntity(new StringEntity("grant_type=password&username=testUser&password=qwerty"));

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
                //TODO Handle problems..
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


    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {

        // Using Log, front-end to the logging chain, emulates
        // android.util.log method signatures.

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
