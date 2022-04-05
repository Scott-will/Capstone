package com.example.cyclesafejava;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.cyclesafejava.Services.MapsService;
import com.example.cyclesafejava.Tasks.DownloadTask;
import com.example.cyclesafejava.ViewModels.StatisticsViewModel;
import com.example.cyclesafejava.data.Events.Event;
import com.example.cyclesafejava.data.Events.LocationEvent;
import com.example.cyclesafejava.data.Statistics;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // checks
    private boolean MarkerEnabled = false;
    private boolean StartedRide = false;

    // Map stuff
    private GoogleMap mMap;

    private boolean locationPermissionGranted;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int PERMISSIONS_REQUEST_ACCESS_BACKGROUND_LOCATION = 2;

    //UI
    private TextView speedText;
    private TextView distanceText;
    private TextView fastestSpeedTest;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;
    private CameraPosition cameraPosition;

    // Keys for storing activity state.
    // [START maps_current_place_state_keys]
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;

    //Container
    private AppContainer appContainer;

    //Services
    private MapsService mapsService;

    //ViewModels
    private StatisticsViewModel statisticsViewModel;

    //Data
    private Statistics statistics;

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            Logger.debug("Loading saved Map State");
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.debug("Creating Maps Activity");
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            Logger.debug("Using saved state");
        }
        //Init appContainer
        this.appContainer = ((CycleSafe) getApplication()).appContainer;

        //Statistics
        this.statisticsViewModel = new StatisticsViewModel(appContainer.statisticsService);
        this.statistics = this.statisticsViewModel.LoadStatistics(this.getApplicationContext());

        // Retrieve the content view that renders the map.
        setContentView(R.layout.activity_maps2);
        this.speedText = (TextView) findViewById(R.id.Speed) ;
        this.distanceText = (TextView) findViewById(R.id.Distance) ;
        this.fastestSpeedTest = (TextView) findViewById(R.id.FastestSpeed) ;
        this.fastestSpeedTest.setText("Fastest Speed: " + statistics.FastestSpeed.toString());
        this.distanceText.setText("Distance: " + statistics.TotalDistance.toString());
        this.speedText.setText("Speed: " + statistics.LongestRide.toString());

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), getString(R.string.maps_api_key));
        placesClient = Places.createClient(this);

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void StartRide(View view){
        Button startButton = findViewById(R.id.StartRide);
        Intent intent = new Intent(this, MapsService.class);
        if (startButton.getText().equals("Start")) {
            this.getApplicationContext().startService(intent);
            startButton.setText("Stop");
            //logic for trip finished here
        }
        else{
            this.getApplicationContext().stopService(intent);
            startButton.setText("Start");
        }

        return;
    }

    public void GoBackToMain(View view){
        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        try{
            EventBus.getDefault().register(this);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
            EventBus.getDefault().register(this);
        }
        catch(Exception e){

        }
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent(Event e){
        if(e.getmResultValue().equals(Event.LOCATION)){
            Location previousLocation = lastKnownLocation;
            this.getDeviceLocation();
            Location currentLocation = lastKnownLocation;
            EventBus.getDefault().post(new LocationEvent(currentLocation, previousLocation));
        }
        else if(e.getmResultValue().equals(Event.BATTERY)){
            AlertDialog alert = new AlertDialog.Builder(MapsActivity.this).create();
            alert.setTitle("CycleSafe Battery Low");
            alert.setMessage("Your battery is low for the CycleSafe equipment");
            alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }
        else if(e.getmResultValue().equals(Event.STATSTICS)){
            Logger.debug("Stats event recieved");
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    Logger.debug("I am Running to update text");
                    speedText.setText("Speed: " + Double.toString(Math.round(e.speed*100.0)/100.0));
                    distanceText.setText("Distance: " + Double.toString(Math.round(e.distance*100.0)/100.0));
                    fastestSpeedTest.setText("Fastest Speed: " + Double.toString(Math.round(e.fastestSpeed*100.0)/100.0));
                }
            });
            this.statistics.CurrentSpeed = e.speed;
            this.statistics.Distance = e.distance;
            this.statistics.FastestSpeed = e.fastestSpeed;
            this.statistics.TotalDistance += e.distance;
            this.statisticsViewModel.SaveStatistics(this.getApplicationContext(), this.statistics);
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            Logger.debug("permission granted for location");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            Logger.debug("permission granted for location");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_BACKGROUND_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                Logger.debug("permission granted for location");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        if (requestCode
                == PERMISSIONS_REQUEST_ACCESS_BACKGROUND_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
                Logger.debug("permission granted for location");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }

    /**
     * Sets up the options menu.
     * @param menu The options menu.
     * @return Boolean.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.current_place_menu, menu);
        Logger.debug("Loading Options Menu");
        return true;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;
        Logger.debug("Loading Window Apapter");
        this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // Inflate the layouts for the info window, title and snippet.
                View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                        (FrameLayout) findViewById(R.id.map), false);

                TextView title = infoWindow.findViewById(R.id.title);
                title.setText(marker.getTitle());

                TextView snippet = infoWindow.findViewById(R.id.snippet);
                snippet.setText(marker.getSnippet());
                Logger.debug("Loaded info contents");
                return infoWindow;
            }
        });
        Logger.debug("Requesting Location Permissions");
        getLocationPermission();
        // ...

        // Turn on the My Location layer and the related control on the map.
        Logger.debug("Updating Location UI");
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        Logger.debug("Getting Device Location");
        getDeviceLocation();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.clear();
                MarkerOptions marker = new MarkerOptions()
                        .position(new LatLng(point.latitude, point.longitude))
                        .title("New Marker");
                mMap.addMarker(marker);
                System.out.println(point.latitude + "---" + point.longitude);
                LatLng currentLocation = new LatLng(lastKnownLocation.getLongitude(), lastKnownLocation.getLongitude());
                GetDirections(currentLocation, marker.getPosition());
            }
        });


    }

    private void updateLocationUI() {
        if (mMap == null) {
            Logger.error("Map is null");
            return;
        }
        try {
            if (locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                lastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Logger.error(e.getMessage());
        }
    }

    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            Logger.debug("Getting Device Location");
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {

                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Logger.debug("Task to get location was successful");
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if(Looper.getMainLooper().getThread() == Thread.currentThread()){
                                Logger.debug("Im in main thread");
                            }
                            if (lastKnownLocation != null) {
                                Logger.debug("Last known locaiton known, moving camera");
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                            else{
                                Logger.debug("Last known location is null");
                                Logger.debug( "Current location is null. Using defaults.");
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            }
                        } else {
                            Logger.debug( "Task to get location Failed", task.getException());
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Logger.error(e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }

    private void showCurrentPlace() {
        if (mMap == null) {
            Logger.error("Map is null");
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < M_MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = M_MAX_ENTRIES;
                        }

                        int i = 0;
                        String[] likelyPlaceNames = new String[count];
                        String[] likelyPlaceAddresses = new String[count];
                        List[] likelyPlaceAttributions = new List[count];
                        LatLng[] likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            Logger.debug(likelyPlaceNames[i]);
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        Logger.debug("Showing users likely places");
                        //MapsActivity.this.openPlacesDialog();
                    }
                    else {
                        Logger.error( task.getException().toString());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Logger.debug( "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            mMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            getLocationPermission();
        }
    }

    public void SetMarker(View view){
        MarkerEnabled = true;
    }

    public void GetDirections(LatLng currentLocation, LatLng destination){
        String url = GetUrl(currentLocation, destination);
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String GetUrl(LatLng currentLocation, LatLng destination){
        String origin = "origin=" + Double.toString(currentLocation.latitude) +","+ Double.toString(currentLocation.longitude);
        String dest = "destination=" + Double.toString(destination.latitude) +","+ Double.toString(destination.longitude);
        String mode = "mode=bicycling";
        String parameters = origin + "&" + dest + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.maps_api_key);
        return url;
    }


    /**
     * A method to download json data from url
     */

}