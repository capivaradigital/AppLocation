package com.example.tmili.applocation;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Djalma on 26/07/2017.
 */

public class Distancia {
    public LatLng origim;
    public LatLng dest;
    double dist;


    public Distancia(LatLng origim, LatLng dest) {
        this.origim = origim;
        this.dest = dest;
    }

    void setDistancia( LatLng nDest){
        //origim=nOrigim;
        dest=nDest;
    }

    double getDistancia() {  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = dest.latitude * Math.PI / 180 - origim.latitude * Math.PI / 180;
        double dLon = dest.longitude * Math.PI / 180 - origim.longitude * Math.PI / 180;
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(origim.latitude * Math.PI / 180) * Math.cos(dest.latitude * Math.PI / 180) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        dist=d*1000;
        return dist;
        }

    }

