package com.example.android.networkconnect;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.common.logger.Log;
import com.example.android.common.logger.LogFragment;
import com.example.android.common.logger.LogView;
import com.example.android.common.logger.LogWrapper;
import com.example.android.common.logger.MessageOnlyLogFilter;
import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;

import java.util.ArrayList;
import java.util.List;

/**
 * Sample application demonstrating how to connect to the network and fetch raw
 * HTML. It uses AsyncTask to do the fetch on a background thread. To establish
 * the network connection, it uses HttpURLConnection.
 *
 * This sample uses the logging framework to display log output in the log
 * fragment (LogFragment).
 */
public class MainActivity extends FragmentActivity {

    private LogFragment mLogFragment;

    private List<Location> locations = new ArrayList<Location>();
    private List<Task> tasks = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        // Initialize text fragment that displays intro text.
//        TasksFragment tasksFragment = (TasksFragment)
//                    getSupportFragmentManager().findFragmentById(R.id.tasks_fragment);
        //introFragment.getTextView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16.0f);

        // Initialize the logging framework.
        initializeLogging();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LogView logView = mLogFragment.getLogView();
        switch (item.getItemId()) {
            case R.id.map:
                logView.setText("Fetching locations...");
                Communicator.getLocations();
                return true;
//            case R.id.create_task:
//                logView.setText("Creating new task...");
//                postTask(new Task(1,"name","desc",TaskState.OPEN,new Position(0.123f, 0.456f)));
//                return true;
            case R.id.tasks:
                logView.setText("Fetching tasks...");
                Communicator.getTasks();
                return true;
        }
        return false;
    }

    public Position fetchCurrentPosition(){
        throw new UnsupportedOperationException();
    }

    /** Create a chain of targets that will receive log data */
    public void initializeLogging() {
        // Wraps Android's native log framework
        LogWrapper logWrapper = new LogWrapper();
        Log.setLogNode(logWrapper);

        // A filter that strips out everything except the message text.
        MessageOnlyLogFilter msgFilter = new MessageOnlyLogFilter();
        logWrapper.setNext(msgFilter);

        // On screen logging via a fragment with a TextView.
        mLogFragment =
                (LogFragment) getSupportFragmentManager().findFragmentById(R.id.log_fragment);
        msgFilter.setNext(mLogFragment.getLogView());
    }
}
