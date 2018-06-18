package net.callofdroidy.mapandsearch;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = MainActivity.class.getSimpleName();

    SupportPlaceAutocompleteFragment searchInputField;
    SupportMapFragment worldMap;

    GoogleMap myMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        searchInputField = (SupportPlaceAutocompleteFragment) fragmentManager.findFragmentById(R.id.search_input_field);
        worldMap = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        if (worldMap != null) {
            worldMap.getMapAsync(this);
        }

        searchInputField.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.e(TAG, "onPlaceSelected: " + place.getName());
                if (myMap != null) {
                    myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 10));
                }
            }

            @Override
            public void onError(Status status) {
                Log.e(TAG, "onPlaceError: " + status.getStatusMessage());
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }
}
