package com.example.android.networkconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.networkconnect.model.Task;


public class TaskDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private final static String task_json = "TASK_JSON";
    private ArrayAdapter<CharSequence> adapter;
    private Task task;
    private TextView name;
    private TextView description;
    private Spinner spinner;

    public TaskDetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        task = ((TaskDetailsActivity) getActivity()).task;
        name.setText(task.Name);
        description.setText(task.Descriprion);
        spinner.setSelection(adapter.getPosition(task.TaskState.name()));
    }

    private void prepareSpinner(View view) {
        spinner = (Spinner) view.findViewById(R.id.task_state);
        adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(),
                R.array.task_states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        name = (TextView) view.findViewById(R.id.task_name);
        description = (TextView) view.findViewById(R.id.task_descr);

        task = ((TaskDetailsActivity) getActivity()).task;
        prepareSpinner(view);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        String state = parent.getItemAtPosition(position).toString();
        if (state.equals("OPEN")) {
            task.TaskState = com.example.android.networkconnect.model.TaskState.OPEN;
        } else if (state.equals("IN_PROGRESS")) {
            task.TaskState = com.example.android.networkconnect.model.TaskState.IN_PROGRESS;
        } else if (state.equals("FINISHED")) {
            task.TaskState = com.example.android.networkconnect.model.TaskState.FINISHED;
        }
        Communicator.updateTask(task);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
