package com.example.tmili.applocation;

/**
 * Created by tmili on 26/07/2017.
 */

public class Local {

    private String nome;
    private double Lat;
    private double Lon;
    private String Key;

    public String getNome(){

        return nome;
    }
    public void setNome(String nome){

        this.nome = nome;
    }

    public double getLat(){
        return Lat;
    }

    public void setLat(double lat) {
        this.Lat = Lat;
    }

    public double getLon(){
        return Lon;
    }
    public void setLon(){
        this.Lon = Lon;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        this.Key = key;
    }

}
