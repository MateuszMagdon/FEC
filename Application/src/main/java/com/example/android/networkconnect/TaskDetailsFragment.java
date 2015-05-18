package com.example.android.networkconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.networkconnect.model.Task;

import org.json.JSONException;
import org.json.JSONObject;


public class TaskDetailsFragment extends Fragment {

    private final static String task_json = "TASK_JSON";
    ArrayAdapter<CharSequence> adapter;
    private Task Task;
    private TextView name;
    private TextView description;
    private Spinner state;

    public TaskDetailsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    private void prepareSpinner(View view) {
        state = (Spinner) view.findViewById(R.id.task_state);
        adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.task_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        state.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            Task = Communicator.parseTask(new JSONObject(getArguments().getString(task_json)));
            name.setText(Task.Name);
            description.setText(Task.Descriprion);
            state.setSelection(adapter.getPosition(Task.TaskState.name()));
        } catch (JSONException e) {
        }


        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        name = (TextView) view.findViewById(R.id.task_name);
        description = (TextView) view.findViewById(R.id.task_descr);

        prepareSpinner(view);

        return view;
    }
}
