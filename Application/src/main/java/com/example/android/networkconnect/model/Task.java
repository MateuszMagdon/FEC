package com.example.android.networkconnect.model;

import com.example.android.networkconnect.*;
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
}
