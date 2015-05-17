package com.example.android.networkconnect.model;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by mateu_000 on 2015-04-26.
 */
public class Task {

    public int Id;
    public String Name;
    public String Descriprion;
    public TaskState TaskState;
    public Position Position;

    public Task(int id, String name, String descriprion, TaskState taskState, Position position) {
        Id = id;
        Name = name;
        Descriprion = descriprion;
        TaskState = taskState;
        Position = position;
    }

    public String toJSON(){
        JSONObject object = new JSONObject();
        try {
            object.put("Id", this.Id);
            object.put("Name", this.Name);
            object.put("Description", this.Descriprion);
            object.put("TaskState", TaskState.ordinal());
            object.put("Position", Position.toJSON());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
