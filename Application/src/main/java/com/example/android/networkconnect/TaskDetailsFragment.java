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


public class TaskDetailsFragment extends Fragment { // implements AdapterView.OnItemSelectedListener {

    private final static String task_json = "TASK_JSON";
    private ArrayAdapter<CharSequence> adapter;
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

    @Override
    public void onResume() {
        super.onResume();
        Task = Communicator.Tasks.get(1);
        name.setText(Task.Name);
        description.setText(Task.Descriprion);
        state.setSelection(adapter.getPosition(Task.TaskState.name()));
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
//        try {
//            Task = Communicator.Tasks.get(0);//parseTask(new JSONObject(getArguments().getString(task_json)));
//            name.setText(Task.Name);
//            description.setText(Task.Descriprion);
//            state.setSelection(adapter.getPosition(Task.TaskState.name()));
//        } catch (Exception e) {
//        }


        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        name = (TextView) view.findViewById(R.id.task_name);
        description = (TextView) view.findViewById(R.id.task_descr);

        prepareSpinner(view);

        return view;
    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//        Object state = parent.getItemAtPosition(position);
//        switch (state) {
//            case 0:
//                Task.TaskState = com.example.android.networkconnect.model.TaskState.OPEN;
//                break;
//            case 1:
//                Task.TaskState = com.example.android.networkconnect.model.TaskState.IN_PROGRESS;
//                break;
//            case 2:
//                Task.TaskState = com.example.android.networkconnect.model.TaskState.FINISHED;
//                break;
//        }
//        Communicator.postTask(Task);
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
}
