package com.example.android.networkconnect;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.networkconnect.model.Task;


public class TaskDetailsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter<CharSequence> adapter;
    private Task task;
    private TextView name;
    private TextView description;
    private Spinner spinner;
    private Button requestBackupButton;
    private TextView requestBackupLabel;
    private TextView requestBackupDate;

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

        handleVisibilityOfBRstuff();
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
        requestBackupButton = (Button) view.findViewById(R.id.request_backup_button);
        requestBackupLabel = (TextView) view.findViewById(R.id.backup_requested_at_label);
        requestBackupDate = (TextView) view.findViewById(R.id.backup_requested_date);

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

    public void handleVisibilityOfBRstuff() {
        if (task.BackupRequested) {
            requestBackupButton.setVisibility(View.GONE);
            requestBackupDate.setVisibility(View.VISIBLE);
            requestBackupLabel.setVisibility(View.VISIBLE);
            requestBackupDate.setText(task.BackupRequestedDate);
        } else {
            requestBackupButton.setVisibility(View.VISIBLE);
            requestBackupDate.setVisibility(View.GONE);
            requestBackupLabel.setVisibility(View.GONE);
        }
    }

    public void requestBackup(View view) {
        Communicator.postBackupRequest(task);
        task.setBackupRequested();
        handleVisibilityOfBRstuff();
    }
}
