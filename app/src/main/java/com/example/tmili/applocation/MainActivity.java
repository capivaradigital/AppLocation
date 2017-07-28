package com.example.tmili.applocation;

import android.Manifest;
import android.content.Intent;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback{

    FirebaseDatabase database;
    GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Circle circle;

    private TextView TxtLat;
    private TextView TxtLon;
    private TextView TxtTim;

    public LatLng myLoc;
    public LatLng novoP;

    public double tm;
    public double tm1;
    private int radiu=5000;
    private String ref="";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;
    private final String LOG_TAG = "TestApp";

    //41102
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

         database = FirebaseDatabase.getInstance();

        Intent filtros =getIntent();
        ref = filtros.getStringExtra("filtro");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mMap.clear();


                myLoc = new LatLng(tm, tm1);
                mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));

                final DatabaseReference myRef = database.getReference(ref);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                        for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                            Local value = dataSnapshot1.getValue(Local.class);
                            novoP = new LatLng(value.getLat(), value.getLon());
                            Distancia d = new Distancia(myLoc, novoP);
                            if (d.getDistancia() < radiu) {
                                mMap.addMarker(new MarkerOptions()
                                        .position(novoP)
                                        .flat(true)
                                        .title(value.getNome())
                                        .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                        .snippet(dataSnapshot1.getKey())

                                );
                            }
                        }



                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                    }
                });

                circle = mMap.addCircle(new CircleOptions()
                        .center(myLoc)
                        .radius(radiu)
                        .strokeWidth(10)
                        .strokeColor(Color.GREEN)
                        .fillColor(Color.argb(128,255,0,0))
                        .clickable(true));

                TxtLat.setText(Double.toString(tm));
                TxtLon.setText(Double.toString(tm1));
                TxtTim.setText(Boolean.toString(true));

            }

        });//fim

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        TxtLat = (TextView) findViewById(R.id.TxtLat);
        TxtLon = (TextView) findViewById(R.id.TxtLon);
        TxtTim = (TextView) findViewById(R.id.TxtTim);

    }//dist ini

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermitirLocalizacao.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermitirLocalizacao.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
          enableMyLocation();
        }
    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    private void showMissingPermissionError() {
        PermitirLocalizacao.PermissionDeniedDialog
                .newInstance(true).show(getFragmentManager(), "dialog");
    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        enableMyLocation();
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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }enableMyLocation();
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
      int v = Log.i(LOG_TAG, "GoogleApiClient connection has been suspend");

        Toast.makeText(this, v, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
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

        final DatabaseReference myRef = database.getReference(ref);
        mMap.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();

                for (DataSnapshot dataSnapshot1 : dataSnapshots) {
                    Local value = dataSnapshot1.getValue(Local.class);
                    novoP = new LatLng(value.getLat(), value.getLon());
                    Distancia d = new Distancia(myLoc, novoP);
                    if (d.getDistancia() < radiu) {
                        mMap.addMarker(new MarkerOptions()
                                .position(novoP)
                                .flat(true)
                                .title(value.getNome())
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                                .snippet(dataSnapshot1.getKey())

                        );
                    }
                }




            }
            @Override
            public void onCancelled(DatabaseError error) {
            }
        });



        // Add a marker in Sydney and move the camera
        myLoc = new LatLng(tm, tm1);

        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));

        circle = mMap.addCircle(new CircleOptions()
                .center(myLoc)
                .radius(radiu)
                .strokeWidth(10)
                    .fillColor(Color.GREEN)
                    .strokeColor(Color.BLUE)
            );
        enableMyLocation();
    }

    public void loco(View view) {
        mMap.clear();
        myLoc = new LatLng(tm, tm1);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
        mMap.addMarker(new MarkerOptions().position(myLoc).title("Marker in Sydney"));







    }


    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}


//ZONA FANTASMA


    /*public boolean distancia(LatLng latlon1,LatLng latlon2) {  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = latlon2.latitude * Math.PI / 180 - latlon1.latitude * Math.PI / 180;
        double dLon = latlon2.longitude * Math.PI / 180 - latlon1.longitude * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(latlon1.latitude * Math.PI / 180) * Math.cos(latlon2.latitude * Math.PI / 180) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        if(d*1000<radiu){
            return true;
        }else{
            return false;
        }
    }*///dist fim