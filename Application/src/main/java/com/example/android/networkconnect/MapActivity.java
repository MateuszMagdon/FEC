package com.example.android.networkconnect;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android.networkconnect.model.Position;
import com.example.android.networkconnect.model.Task;
import com.example.android.networkconnect.model.TaskState;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnFragmentInteractionListener, GoogleMap.OnInfoWindowClickListener {

    private final static String task_id = "TASK_NUMBER";

    private SupportMapFragment mapFragment;
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        map = mapFragment.getMap();
        map.setOnInfoWindowClickListener(this);
        map.setMyLocationEnabled(true);

        setMarkersOnMap();
    }

    private void setMarkersOnMap() {
        LatLng latLng = null;

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Task t : Communicator.Tasks) {
            latLng = new LatLng(t.Position.Longitude, t.Position.Latitude);


            int icon = R.drawable.pin;
            if (t.TaskState == TaskState.OPEN) {
                icon = R.drawable.task_red;
            } else if (t.TaskState == TaskState.IN_PROGRESS) {
                icon = R.drawable.task_orange;
            } else if (t.TaskState == TaskState.FINISHED) {
                icon = R.drawable.task_green;
            }

            Marker m = map.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(t.Name)
                    .icon(BitmapDescriptorFactory
                            .fromResource(icon)));

            builder.include(m.getPosition());
        }

        for (Position l : Communicator.Locations) {
            Marker m = map.addMarker(new MarkerOptions()
                    .position(new LatLng(l.Longitude, l.Latitude))
                    .title(l.Latitude + " " + l.Longitude)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.unit)));

            builder.include(m.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 5; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(14.0f).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.moveCamera(cameraUpdate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh_button:
                Communicator.refresh();
                return true;
            case R.id.tasks_button:
                finishActivity(0);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Task task) {

    }

    private Task getTaskByName(String name) {
        Task result = null;
        for (Task t : Communicator.Tasks) {
            if (t.Name.equals(name)) {
                result = t;
                break;
            }
        }
        return result;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Task t = getTaskByName(marker.getTitle());

        Intent intent = new Intent(getApplicationContext(), TaskDetailsActivity.class);
        intent.putExtra(task_id, Integer.toString(t.Id));
        startActivity(intent);
    }
}
