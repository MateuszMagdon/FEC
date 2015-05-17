package com.example.android.networkconnect;

import android.os.AsyncTask;

import com.example.android.common.logger.Log;
import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mateu_000 on 2015-05-17.
 */
public class Communicator {
    public static final String URI = "http://floodemergencycoordinator.apphb.com";

    private static JSONObject token = null;

    private static String prepareContent(List<NameValuePair> contentList) {
        String body = "";
        for (NameValuePair pair: contentList) {
            if (!body.equals("")){
                body += "&";
            }
            body += pair.getName() + "=" + pair.getValue();
        }
        return body;
    }

    public static JSONObject logIn(String username, String password){
        List<NameValuePair> contentList = new ArrayList<>();
        contentList.add(new BasicNameValuePair("grant_type", "password"));
        contentList.add(new BasicNameValuePair("username", username));
        contentList.add(new BasicNameValuePair("password", password));

        PostRequestTask loginTask = new PostRequestTask(null);

        loginTask.addContent(prepareContent(contentList));
        String url = "/Token";

        token = executeAsyncTakAndReturnResult(loginTask, url);
        return token;
    }

    public static void testConnection(){
        GetRequestTask task = new GetRequestTask(token);
        task.execute("/api/serviceUnit/testMessage");
    }

    public static void getLocations(){
        GetRequestTask task = new GetRequestTask(token);
        JSONArray result = executeAsyncTakAndReturnResultInArray(task, "/api/serviceUnit/locations");
    }

    public static void postLocation(Position position){
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(position.toJSON().toString());
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/location");
    }

    public static void getTasks() {
        GetRequestTask task = new GetRequestTask(token);
        JSONArray result = executeAsyncTakAndReturnResultInArray(task, "/api/serviceUnit/tasks");
    }

    public static void postTask(Task taskToPost){
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(taskToPost.toJSON());
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/task");
    }

    public static void postBackupRequest(int taskId){
        PostRequestTask task = new PostRequestTask(token);
        task.addContent("{TaskId: " + taskId + " }");
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/backupRequest");
    }

    private static JSONObject executeAsyncTakAndReturnResult(AsyncTask<String, String, String> task, String url) {
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

    private static JSONArray executeAsyncTakAndReturnResultInArray(AsyncTask<String, String, String> task, String url) {
        String result;
        JSONArray jObject = null;

        try {
            result = task.execute(URI + url).get();
            jObject = new JSONArray(result);

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

}
