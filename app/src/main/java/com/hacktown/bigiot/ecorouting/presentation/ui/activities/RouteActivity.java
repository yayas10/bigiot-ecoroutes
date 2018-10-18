package com.hacktown.bigiot.ecorouting.presentation.ui.activities;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hacktown.bigiot.ecorouting.R;
import com.hacktown.bigiot.ecorouting.domain.executor.impl.ThreadExecutor;
import com.hacktown.bigiot.ecorouting.presentation.model.BikeSharingContentProvider;
import com.hacktown.bigiot.ecorouting.presentation.model.BikeSharingContentProvider.AccesResponseCallback;
import com.hacktown.bigiot.ecorouting.presentation.model.BikeStation;
import com.hacktown.bigiot.ecorouting.presentation.presenter.QueryRoutePresenter;
import com.hacktown.bigiot.ecorouting.presentation.presenter.impl.QueryRoutePresenterImpl;
import com.hacktown.bigiot.ecorouting.presentation.util.PermissionManager;
import com.hacktown.bigiot.ecorouting.storage.model.RouteData;
import com.hacktown.bigiot.ecorouting.storage.repository.RouteRepositoryImpl;
import com.hacktown.bigiot.ecorouting.threading.MainThreadImpl;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.bigiot.lib.offering.AccessResponse;
import org.eclipse.bigiot.lib.offering.OfferingCore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RouteActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, QueryRoutePresenter.View {

    private static final String TAG = RouteActivity.class.getSimpleName();

    private GoogleMap mMap;


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;

    private LocationListener locationListener;

    private final static long REQUEST_INTERVAL = 30*1000; //300 seconds
    private final static int REQUEST_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
    private final static long REQUEST_TIMEOUT = 45000;

    private QueryRoutePresenter queryRoutePresenter;

    PlaceAutocompleteFragment fromPlaceFragment;
    private Place fromPlaceSelected;
    private Marker fromMarker;

    PlaceAutocompleteFragment toPlaceFragment;
    private Place toPlaceSelected;
    private Marker toMarker;

    private Location lastknownLocation;

    private List<Marker> mapstationMarkers = new ArrayList<>();
    private List<Polyline> polylineList = new ArrayList<>();

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "requestinglocations";
    private boolean mRequestingLocationUpdates = false;

    private AccesResponseCallback accessCallback = new AccesResponseCallback() {
        @Override
        public void onResponse(OfferingCore offeringCore, AccessResponse accessResponse) {
            //TODO: put markers

            JSONArray jr = null;
            List<BikeStation> bikeStationList = new ArrayList<>();
            try {
                jr = new JSONArray(accessResponse.getBody());
                for (int i = 0; i < jr.length(); i++){
                    JSONObject jb = (JSONObject)jr.getJSONObject(i);
                    BikeStation bikeStation = new GsonBuilder().create().fromJson(jb.toString(), BikeStation.class);
                    bikeStationList.add(bikeStation);
                }
                paintStationList(bikeStationList);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    private MarkerOptions createMarker(BikeStation station) {
        if(!station.getStatus().equals("OPN") || station.getBikes() <= 0){
            return new MarkerOptions().position(new LatLng(station.getLatitude(), station.getLongitude()))
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }else {
            return new MarkerOptions().position(new LatLng(station.getLatitude(), station.getLongitude()))
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        }
    }

    private void paintStationList(List<BikeStation> bikeStationList) {
        for (Marker marker :
            mapstationMarkers) {
            marker.remove();
        }
        mapstationMarkers.clear();
        for (BikeStation bikeStation :
            bikeStationList){
            if(bikeStation.getType().equals("BIKE")) {
                mapstationMarkers.add(mMap.addMarker(createMarker(bikeStation)));
            }
        }
    }

    @OnClick(R.id.place_autocomplete_route_button)
    void onRouteClicked(){
    }

    /*
    <LifeCycle>
     */

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
        try {
            BikeSharingContentProvider.getInstance(this, accessCallback).onAccessOffering();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        initPresenters();

        initMap();

        initLocationCallback();

        initLocationProvider();

        setAutocompleteListeners();
    }

    /**
     * Manipulates the map once available. This callback is triggered when the map is ready to be
     * used. This is where we can add markers or lines, add listeners or move the camera. In this
     * case, we just add a marker near Sydney, Australia. If Google Play services is not installed
     * on the device, the user will be prompted to install it inside the SupportMapFragment. This
     * method will only be triggered once the user has installed Google Play services and returned
     * to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng barcelona = new LatLng(41.402716, 2.178810);
        moveCameroToPosition(barcelona, null, 12);

        mMap.setMyLocationEnabled(mRequestingLocationUpdates);
    }

    /*
    </LifeCycle>
     */

    // UseCases

    private void initPresenters(){
        queryRoutePresenter = new QueryRoutePresenterImpl(
            ThreadExecutor.getInstance(),
            MainThreadImpl.getInstance(),
            this,
            new RouteRepositoryImpl()
        );
    }

    // Save Instance state
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
            mRequestingLocationUpdates);
        // ...
        super.onSaveInstanceState(outState);
    }

    private ProgressDialog progressDialog;

    public void showProgress(){
        progressDialog = new ProgressDialog(this);
        // Setting Title
        progressDialog.setTitle("Route Calculation");
        // Setting Message
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void hideProgress(){
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //Autocomplete utils
    private void setAutocompleteListeners(){
        ImageButton routeButton = (ImageButton) this.findViewById(R.id.place_autocomplete_route_button);
        routeButton.bringToFront();
        routeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fromPlaceSelected != null && toPlaceSelected != null) {
                    showProgress();
                    queryRoutePresenter
                        .getRoute(fromPlaceSelected.getLatLng(), toPlaceSelected.getLatLng());
                }

            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
            .build();

        fromPlaceFragment = (PlaceAutocompleteFragment)
            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_from);

        fromPlaceFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                fromPlaceSelected = place;
                if(fromMarker != null) {
                    fromMarker.remove();
                }
                fromMarker = mMap.addMarker(getFromMarker(fromPlaceSelected.getLatLng()));

                LatLngBounds bounds = null;
                if(toPlaceSelected != null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(toPlaceSelected.getLatLng());
                    builder.include(fromPlaceSelected.getLatLng());
                    bounds = builder.build();
                }
                moveCameroToPosition(fromPlaceSelected.getLatLng(), bounds, null);

            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                fromPlaceSelected = null;
                if(fromMarker != null) {
                    fromMarker.remove();
                }
            }
        });

        fromPlaceFragment.setFilter(typeFilter);

        toPlaceFragment = (PlaceAutocompleteFragment)
            getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment_to);

        toPlaceFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                toPlaceSelected = place;
                if(toMarker != null) {
                    toMarker.remove();
                }
                toMarker = mMap.addMarker(getToMarker(toPlaceSelected.getLatLng()));

                LatLngBounds bounds = null;
                if(toPlaceSelected != null) {
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(toPlaceSelected.getLatLng());
                    builder.include(fromPlaceSelected.getLatLng());
                    bounds = builder.build();
                }
                moveCameroToPosition(toPlaceSelected.getLatLng(), bounds, null);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
                toPlaceSelected = null;
                if(toMarker != null) {
                    toMarker.remove();
                }
            }
        });

        toPlaceFragment.setFilter(typeFilter);

    }

    private void moveCameroToPosition(LatLng position, LatLngBounds bounds, Integer zoom) {

        if(bounds != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));
        }else {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(position)      // Sets the center of the map to location user
                .zoom(zoom == null ? 17 : zoom)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
                .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
    }

    //Location utils
    private boolean checkGeolocationPermission(){
        if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            PermissionManager.checkPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
            return true;
        }
        mRequestingLocationUpdates = true;
        return false;
    }

    private void initLocationProvider() {
        if(!checkGeolocationPermission()) {
            buildGoogleApiClient();
        }
    }

    private void initLocationCallback(){
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location == null) {
                    return;
                }
                // Update UI with location data
                // ...
                if(lastknownLocation == null || location.getTime() > lastknownLocation.getTime()) {
                    lastknownLocation = location;
                }
            }
        };
//        locationListener = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                if (locationResult == null) {
//                    return;
//                }
//                for (Location location : locationResult.getLocations()) {
//                    // Update UI with location data
//                    // ...
//                    if(lastknownLocation == null || location.getTime() > lastknownLocation.getTime()) {
//                        lastknownLocation = location;
//                    }
//                }
//            }
//        };

    }

    private void stopLocationUpdates() {
        mGoogleApiClient.disconnect();
    }


    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .addApi(LocationServices.API)
            .build();

        mRequestingLocationUpdates = true;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, locationListener);

        if (mMap != null){
            mMap.setMyLocationEnabled(mRequestingLocationUpdates);
        }
    }

    //Location Callbacks
    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    // Map utils
    private MarkerOptions getFromMarker(LatLng location) {
        return new MarkerOptions().position(location)
            .draggable(false)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
    }

    private MarkerOptions getToMarker(LatLng location) {
        return new MarkerOptions().position(location)
            .draggable(false)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
    }

    private void initMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Align location button to bottom
        View mapView = mapFragment.getView();
        if (mapView != null) {
            // Get the button view
            @SuppressLint("ResourceType") View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
            // and next place it, on bottom right (as Google Maps app)
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                locationButton.getLayoutParams();
            // position on right bottom
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 0, 30, 30);
        }
    }

    //Permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionManager.MY_PERMISSION_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    buildGoogleApiClient();
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                }else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                break;
            }
        }
    }

    //ROute Callback

    private int getColor(String mode){
        switch (mode) {
            case "walking":
                return Color.BLUE;
            case "cycling":
                return Color.GREEN;

            default:
                return Color.GRAY;

        }
    }

    @Override
    public void onRouteRetrieved(List<RouteData> routeList) {
        //TODO: paint route
        for (Polyline polyline :
            polylineList) {
            polyline.remove();
        }
        polylineList.clear();

        for (RouteData route :
            routeList) {
            PolylineOptions options = new PolylineOptions();

            options.color(getColor(route.getMode()));
            options.width(5);
            options.visible(true);

            for (LatLng location : route.getPointList()) {
                options.add(location);
            }

            polylineList.add(mMap.addPolyline(options));
        }
        hideProgress();
    }

    @Override
    public void onRouteError(Integer errorCode) {
        hideProgress();
    }
}
