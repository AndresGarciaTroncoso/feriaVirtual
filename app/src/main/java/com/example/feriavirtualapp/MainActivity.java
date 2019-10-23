package com.example.feriavirtualapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.feriavirtualapp.bd.DatabaseHelper;

import java.io.IOException;


public class MainActivity extends AppCompatActivity {

    Button btnProceso;
    String usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

       limpiarProcesoVenta();

        btnProceso = findViewById(R.id.btnProceso);
        usuario= getIntent().getExtras().getString("usuario");

    }


    public void MisDatos(View view)
    {


        Intent i = new Intent(MainActivity.this,MisDatos.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }
    public void proceso(View view)
    {


        Intent i = new Intent(MainActivity.this,ProcesoVentaActivity.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }

    public void limpiarProcesoVenta(){
        DatabaseHelper bd = new DatabaseHelper(this);
        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd.openDataBase();

        bd.myDataBase.execSQL("DELETE FROM ProcesoVenta ");
        bd.myDataBase.close();
    }



}
