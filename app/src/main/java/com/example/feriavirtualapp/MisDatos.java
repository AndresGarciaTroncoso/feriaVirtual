package com.example.feriavirtualapp;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.example.feriavirtualapp.bd.DatabaseHelper;

import java.io.IOException;
import java.util.ArrayList;

public class MisDatos extends AppCompatActivity {
String usuario;
String nombre,email,rol;
TextView txtNombre,txtEmail,txtRol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_misdatos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtNombre=findViewById(R.id.txtNombre);
        txtEmail=findViewById(R.id.txtEmail);
        txtRol=findViewById(R.id.txtRol);


        usuario= getIntent().getExtras().getString("usuario");
        obtenerDetalleUsuario(usuario);


        txtNombre.setText(nombre);

        txtEmail.setText(email);

        txtRol.setText(rol);



    }

    public void back(View view)
    {
        super.onBackPressed();
    }

    public void obtenerDetalleUsuario(String usuario)
    {

        DatabaseHelper bd = new DatabaseHelper(this);
        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd.openDataBase();


        String q="select  nombre , email , rol from MantUsuario where usuario='"+usuario+"'";
        Cursor cursor = bd.myDataBase.rawQuery(q, null);


        while (cursor.moveToNext()) {

        //    rut = cursor.getString(0);
            nombre = cursor.getString(0);
            email = cursor.getString(1);
         Integer  var_rol = cursor.getInt(2);
         if(var_rol.equals(2))
         {
             rol="Productor";
         }else{
             rol="Cliente Externo";
         }


        }
        bd.myDataBase.close();

    }


}
