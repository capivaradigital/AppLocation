package com.example.tmili.applocation;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    GoogleMap mMap;
    private final String LOG_TAG = "TestApp";
    private TextView TxtLat;
    private TextView TxtLon;
    private TextView TxtPro;
    private TextView TxtTim;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Circle circle;

    public LatLng myLoc;

    public double tm;
    public double tm1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mMap.clear();
                myLoc = new LatLng(tm, tm1);
                mMap.addMarker(new MarkerOptions()
                        .position(myLoc)
                        .title("Marker in Sydney")
                        .draggable(false));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));

                circle = mMap.addCircle(new CircleOptions()
                        .center(myLoc)
                        .radius(5000)
                        .strokeWidth(10)
                        .fillColor(Color.GREEN)
                        .strokeColor(Color.BLUE));


                TxtLat.setText(Double.toString(tm));
                TxtLon.setText(Double.toString(tm1));
                TxtPro.setText("thiago");
                TxtTim.setText(Double.toString(123));

            }

        });


        TxtLat = (TextView) findViewById(R.id.TxtLat);
        TxtLon = (TextView) findViewById(R.id.TxtLon);
        TxtPro = (TextView) findViewById(R.id.TxtPro);
        TxtTim = (TextView) findViewById(R.id.TxtTim);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest
                , this);


    }


    @Override
    public void onConnectionSuspended(int i) {
      int v = Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");

        Toast.makeText(this, v, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
     int t = Log.i(LOG_TAG, "GoogleApiClient connection has failed");

        Toast.makeText(this, t, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {


        Log.i(LOG_TAG, location.toString());
        //txtOutput.setText(location.toString());

        tm = location.getLatitude();
        tm1 = location.getLongitude();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        myLoc = new LatLng(tm, tm1);

        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));

        circle = mMap.addCircle(new CircleOptions()
                .center(myLoc)
                .radius(1000)
                .strokeWidth(10)
                    .fillColor(Color.GREEN)
                    .strokeColor(Color.BLUE)
            );

    }

    public void loco(View view) {
        mMap.clear();
        myLoc = new LatLng(tm, tm1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));


    }


}