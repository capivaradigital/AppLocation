package com.example.tmili.applocation;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;

public class Filtro extends AppCompatActivity implements GoogleMap.OnMyLocationButtonClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        enableMyLocation();
    }

    GoogleMap mMap;

    String petS ="Filtro/Pet_Shop";
    String vet ="Filtro/Veterinario";
    String petF ="Filtro/Pet_Friendly";

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    public void Vet(View view) {
        Intent tm = new Intent(Filtro.this, MainActivity.class);
        tm.putExtra("filtro",vet);
        startActivity(tm);
     }

    public void PetS(View view) {
        Intent tm = new Intent(Filtro.this, MainActivity.class);
        tm.putExtra("filtro",petS);
        startActivity(tm);
    }

    public void PetF(View view) {
        Intent tm = new Intent(Filtro.this, MainActivity.class);
        tm.putExtra("filtro",petF);
        startActivity(tm);
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            PermitirLocalizacao.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
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
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            enableMyLocation();


        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
}