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

    public Task(int id, String name, String descriprion, int taskState, double latitude, double longitude) {
        Id = id;
        Name = name;
        Descriprion = descriprion;
        switch (taskState) {
            case 0:
                TaskState = com.example.android.networkconnect.model.TaskState.OPEN;
                break;
            case 1:
                TaskState = com.example.android.networkconnect.model.TaskState.IN_PROGRESS;
                break;
            case 2:
                TaskState = com.example.android.networkconnect.model.TaskState.FINISHED;
                break;
        }
        Position = new Position(latitude, longitude);
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

    public String toJSONupdate() {
        JSONObject object = new JSONObject();
        try {
            object.put("Id", this.Id);
            object.put("Description", this.Descriprion);
            object.put("TaskState", TaskState.ordinal());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    @Override
    public String toString() {
        return Name + "\n" + Descriprion;
    }
}
