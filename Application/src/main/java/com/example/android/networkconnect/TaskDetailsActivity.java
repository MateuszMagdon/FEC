package com.example.android.networkconnect;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.networkconnect.model.Task;
import com.example.android.networkconnect.model.TaskState;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class TaskDetailsActivity extends FragmentActivity implements OnFragmentInteractionListener {

    private final static String task_id = "TASK_NUMBER";
    public Task task;
    private TaskDetailsFragment detailsFragment;
    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Bundle extras = getIntent().getExtras();
        int taskId = 0;
        if (extras != null) {
            taskId = Integer.parseInt(extras.getString(task_id));
        }
        task = getTaskById(taskId);

        detailsFragment = (TaskDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.task_details_fragment);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tasks_details_map_fragment);
        map = mapFragment.getMap();
        map.setMyLocationEnabled(true);

        setMarkerOnMap();
    }

    public void requestBackup(View view) {
        detailsFragment.requestBackup(view);
    }

    private void setMarkerOnMap() {
        LatLng latLng = null;

        latLng = new LatLng(task.Position.Longitude, task.Position.Latitude);

        int icon = R.drawable.pin;
        if (task.TaskState == TaskState.OPEN) {
            icon = R.drawable.task_red;
        } else if (task.TaskState == TaskState.IN_PROGRESS) {
            icon = R.drawable.task_orange;
        } else if (task.TaskState == TaskState.FINISHED) {
            icon = R.drawable.task_green;
        }

        Marker m = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title(task.Name)
                .icon(BitmapDescriptorFactory
                        .fromResource(icon)));


        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(15.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }

    private Task getTaskById(int taskId) {
        Task result = null;
        for (Task t : Communicator.Tasks) {
            if (t.Id == taskId) {
                result = t;
                break;
            }
        }
        return result;
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
