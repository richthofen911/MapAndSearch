package net.callofdroidy.mapandsearch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.places.ui.SupportPlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int DEFAULT_MAP_ZOOM_IN_LEVEL = 15;
    private static final int COUNTRY_TYPE_MAP_ZOOM_IN_LEVEL = 4;
    private static final int PROVINCE_TYPE_MAP_ZOOM_IN_LEVEL = 7;
    private static final int CITY_TYPE_MAP_ZOOM_IN_LEVEL = 11;
    private static final int COUNTY_TYPE_MAP_ZOOM_IN_LEVEL = 13;

    @Nullable
    private GoogleMap myMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();

        SupportPlaceAutocompleteFragment searchInputField =
                (SupportPlaceAutocompleteFragment) fragmentManager.findFragmentById(R.id.search_input_field);
        SupportMapFragment worldMap = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);

        if (worldMap != null && searchInputField != null) {

            worldMap.getMapAsync(this);
            searchInputField.setHint(getString(R.string.search_input_field_hint));

            searchInputField.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    Log.i(TAG, "onPlaceSelected: " + place.getName() + ", " + place.getPlaceTypes());
                    if (myMap != null) {
                        // clear the marker for the previous location
                        myMap.clear();

                        // adjust the zoom level based if the location is a country/province/city/county
                        float zoom_level = DEFAULT_MAP_ZOOM_IN_LEVEL;
                        List<Integer> locationType = place.getPlaceTypes();
                        if (locationType.contains(Place.TYPE_COUNTRY)) {
                            zoom_level = COUNTRY_TYPE_MAP_ZOOM_IN_LEVEL;
                        } else if (locationType.contains(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_1) ||
                                locationType.contains(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_2) ||
                                locationType.contains(Place.TYPE_ADMINISTRATIVE_AREA_LEVEL_3)) {
                            zoom_level = PROVINCE_TYPE_MAP_ZOOM_IN_LEVEL;
                        } else if (locationType.contains(Place.TYPE_LOCALITY)) {
                            zoom_level = CITY_TYPE_MAP_ZOOM_IN_LEVEL;
                        } else if (locationType.contains(Place.TYPE_SUBLOCALITY) ||
                                locationType.contains(Place.TYPE_SUBLOCALITY_LEVEL_1) ||
                                locationType.contains(Place.TYPE_SUBLOCALITY_LEVEL_2) ||
                                locationType.contains(Place.TYPE_SUBLOCALITY_LEVEL_3) ||
                                locationType.contains(Place.TYPE_SUBLOCALITY_LEVEL_4) ||
                                locationType.contains(Place.TYPE_SUBLOCALITY_LEVEL_5)) {
                            zoom_level = COUNTY_TYPE_MAP_ZOOM_IN_LEVEL;
                        }

                        LatLng location = place.getLatLng();
                        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), zoom_level));
                        // add marker to indicate the selected location
                        myMap.addMarker(new MarkerOptions().position(location));
                    }
                }

                @Override
                public void onError(Status status) {
                    Log.e(TAG, "onPlaceError: " + status.getStatusMessage());
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
    }
}
