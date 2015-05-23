package com.example.android.networkconnect;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final static String task_id = "TASK_NUMBER";
    private TasksFragment tasksFragment;
    private GoogleApiClient mGoogleApiClient;
    private Position mLastPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        buildGoogleApiClient();

        tasksFragment = (TasksFragment)
                getSupportFragmentManager().findFragmentById(R.id.tasks_fragment);
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.map_button:
                goToMapActivity();
                return true;
            case R.id.refresh_button:
                Communicator.refresh();
                sendCurrentPosition();
                tasksFragment.refresh();
                return true;
        }
        return false;
    }

    private void sendCurrentPosition() {
        if (mLastPosition != null) {
            Communicator.postLocation(mLastPosition);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void goToMapActivity() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFragmentInteraction(Task task) {

        Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
        intent.putExtra(task_id, Integer.toString(task.Id));
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        mLastPosition = new Position(mLastLocation.getLatitude(), mLastLocation.getLongitude());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
