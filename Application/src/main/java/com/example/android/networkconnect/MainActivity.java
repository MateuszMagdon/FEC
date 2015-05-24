package com.example.android.networkconnect;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;

public class MainActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private final static String task_id = "TASK_NUMBER";
    public Position mLastPosition = new Position(19.912905, 50.067862);
    private final LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            mLastPosition = new Position(location.getLongitude(), location.getLatitude());
            sendCurrentPosition();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onProviderDisabled(String provider) {
        }
    };
    private TasksFragment tasksFragment;
    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_main);

        tasksFragment = (TasksFragment)
                getSupportFragmentManager().findFragmentById(R.id.tasks_fragment);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10l,
                1.0f, mLocationListener);
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
}
