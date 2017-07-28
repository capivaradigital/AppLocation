package com.example.tmili.applocation;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Filtro extends AppCompatActivity {


    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);
    }

    String petS ="Filtro/Pet_Shop";
    String vet ="Filtro/Veterinario";
    String petF ="Filtro/Pet_Friendly";

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

}
