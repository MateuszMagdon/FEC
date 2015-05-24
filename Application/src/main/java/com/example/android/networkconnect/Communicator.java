package com.example.android.networkconnect;

import android.os.AsyncTask;

import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by mateu_000 on 2015-05-17.
 */
public class Communicator {
    public static final String URI = "http://floodemergencycoordinator.apphb.com";
    public static List<Task> Tasks = new ArrayList<>();
    public static List<Position> Locations = new ArrayList<>();
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

    public static void refresh() {
        getTasks();
        getLocations();
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

    public static List<Position> getLocations() {
        GetRequestTask task = new GetRequestTask(token);
        JSONArray result = executeAsyncTakAndReturnResultInArray(task, "/api/serviceUnit/locations");

        Locations = parsePositionsArray(result);
        return Locations;
    }

    private static List<Position> parsePositionsArray(JSONArray jsonArray) {
        LinkedList<Position> result = new LinkedList<>();
        try {
            if (jsonArray == null) return result;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                Position currentTask = parsePosition(jsonobject);
                result.add(currentTask);
            }
        } catch (JSONException e) {

        }
        return result;
    }

    public static Position parsePosition(JSONObject jsonobject) throws JSONException {

        double lat = jsonobject.getDouble("latitude");
        double lon = jsonobject.getDouble("longitude");

        return new Position(lat, lon);
    }

    public static void postLocation(Position position){
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(position.toJSON().toString());
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/location");
    }

    private static List<Task> getTasks() {
        GetRequestTask task = new GetRequestTask(token);
        JSONArray jsonArray = executeAsyncTakAndReturnResultInArray(task, "/api/serviceUnit/tasks");

        Tasks = parseTasksArray(jsonArray);
        return Tasks;
    }

    private static List<Task> parseTasksArray(JSONArray jsonArray) {
        LinkedList<Task> result = new LinkedList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonobject = jsonArray.getJSONObject(i);
                Task currentTask = parseTask(jsonobject);
                result.add(currentTask);
            }
        } catch (JSONException e) {

        }
        return result;
    }

    public static Task parseTask(JSONObject jsonobject) throws JSONException {
        int id = jsonobject.getInt("Id");
        String name = jsonobject.getString("Name");
        String description = jsonobject.getString("Description");
        int state = jsonobject.getInt("TaskState");
        JSONObject jsonPosition = jsonobject.getJSONObject("Position");
        double lat = jsonPosition.getDouble("latitude");
        double lon = jsonPosition.getDouble("longitude");

        return new Task(id, name, description, state, lat, lon);
    }

    public static void postTask(Task taskToPost){
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(taskToPost.toJSON());
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/task");
    }

    public static void updateTask(Task taskToPost) {
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(taskToPost.toJSONupdate());
        JSONObject result = executeAsyncTakAndReturnResult(task, "/api/serviceUnit/task");
    }

    public static void postBackupRequest(Task taskToPost) {
        PostRequestTask task = new PostRequestTask(token);
        task.addContent(taskToPost.toJSONbackupRequest());
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
