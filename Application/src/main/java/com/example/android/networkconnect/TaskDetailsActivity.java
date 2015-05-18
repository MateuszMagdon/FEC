package com.example.android.networkconnect;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.networkconnect.model.Task;


public class TaskDetailsActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private final static String task_json = "TASK_JSON";
    private TaskDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        detailsFragment = (TaskDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.task_details_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh_button:
                Communicator.refresh();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Task task) {

    }
}
