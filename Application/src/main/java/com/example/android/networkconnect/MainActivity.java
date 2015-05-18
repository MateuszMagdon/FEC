package com.example.android.networkconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private final static String task_json = "TASK_JSON";
    private TasksFragment tasksFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        tasksFragment = (TasksFragment)
                getSupportFragmentManager().findFragmentById(R.id.tasks_fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_button:
                goToMapActivity();
                return true;
            case R.id.refresh_button:
                Communicator.refresh();
                Communicator.postLocation(fetchCurrentPosition());
                return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private Position fetchCurrentPosition() {
        throw new UnsupportedOperationException();
    }

    private void goToMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Task task) {

        Intent intent = new Intent(this, TaskDetailsActivity.class);
        intent.putExtra(task_json, task.toJSON());
        startActivity(intent);
    }
}
