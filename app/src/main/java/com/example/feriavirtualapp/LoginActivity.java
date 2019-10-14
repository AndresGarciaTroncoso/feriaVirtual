package com.example.feriavirtualapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.feriavirtualapp.bd.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends  AppCompatActivity {

    String var_rut;
    Button btnAcceder;
    EditText txtRut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        txtRut = findViewById(R.id.txtUser);


        var_rut=txtRut.getText().toString();



        System.out.println("RUT: "+txtRut+"-"+"FORMAT RUT:"+var_rut);
        btnAcceder =findViewById(R.id.btnLogin);



    }

    public  void acceder (View view) {

        final String rut_format;
        rut_format = txtRut.getText().toString().substring(0, 8);

        if (txtRut.getText().toString().equalsIgnoreCase("")) {

            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
            alertDialog.setTitle("Notificacion");
            alertDialog.setMessage("Debe Ingresar Rut");
            alertDialog.setButton(alertDialog.BUTTON_NEGATIVE, "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // metodo camara
                    //inciarCaptura();
                    dialog.dismiss();
                }
            });

            alertDialog.show();
        }else{


            if(validaLogin(rut_format))
            {
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                i.putExtra("usuario",rut_format);
                startActivity(i);
                //    var_idJefeFaena=obtenerJefeFaena(var_user,var_pass);

            }else {


                //            String url = "http://10.0.2.2:54330/api/v1/usuario/GetById/" + rut_format;
                String url = "http://10.0.2.2:54330/api/v1/usuario/GetById/"+rut_format;
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                System.out.println("JSONRESPONSE => " + response.toString());
                                //txtShowTextResult.setText("Response: " + response.toString());


                        try {
                            JSONObject usuario = response.getJSONObject("usuario");
                            usuario.getString("rut"); // usuario
                            usuario.getString("nombreCompleto");
                            usuario.getString("email");
                            usuario.getString("rol");


                            
                            grabarUsuario(usuario.getString("rut"), usuario.getString("nombreCompleto"), 
                                    usuario.getString("email"), usuario.getString("rol"));
                            if(usuario.getString("rol").toString().equalsIgnoreCase("3"))
                            {
                                Toast.makeText(LoginActivity.this,"Ingresando Cliente Externo.",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginActivity.this,"Ingresando Productor.",Toast.LENGTH_SHORT).show();
                            }



                            Intent i = new Intent(LoginActivity.this,MainActivity.class);
                            i.putExtra("usuario",rut_format);
                            startActivity(i);
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                System.out.println("ERROR= > " + error.getLocalizedMessage());
                            }
                        });

                requestQueue.add(jsonObjectRequest);
            }
         }

    }

    private void grabarUsuario(String rut, String nombreCompleto, String email, String rol) {

        DatabaseHelper bd = new DatabaseHelper(this);

        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bd.openDataBase();

        ContentValues registro = new ContentValues();
        registro.put("usuario", rut);
        registro.put("nombre", nombreCompleto);
        registro.put("email", email);
        registro.put("rol", rol);
       // registro.put("vigencia",1);
        bd.myDataBase.insert("MantUsuario", null, registro);
        System.out.println("GRABO USUARIO:"+nombreCompleto);
        bd.myDataBase.close();
    }



    private boolean validaLogin(String rut) {
        Boolean validaUsuario=false;

        DatabaseHelper bd = new DatabaseHelper(this);
        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bd.openDataBase();
        // Cursor fila = bd.rawQuery("select idUsuario, nombreUsuario, encargado, conductor from usuario", null);
        // String q="select userName, psswUser  from MANTUsuario WHERE activo=1 AND userName ="+"'"+user+"'   AND psswUser ="+"'"+pass+"'";
        String q="select usuario  from MantUsuario WHERE  usuario ="+"'"+rut+"'";
        Cursor fila = bd.myDataBase.rawQuery(q, null);

        if (fila.moveToFirst()) {
            validaUsuario=true;
        }

        bd.myDataBase.close();

        return validaUsuario;
    }


    private void call_service(String rut)
    {
        String url = "http://10.0.2.2:54330/api/v1/usuario/GetById/"+rut;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println("JSONRESPONSE => "+response.toString());
                        //txtShowTextResult.setText("Response: " + response.toString());

                        /*
                        try {
                            JSONObject data = response.getJSONObject("data");
                            JSONArray movies = data.getJSONArray("movies");
                            for (int i = 0; i < movies.length(); i++ ) {
                                JSONObject movie = movies.getJSONObject(i);
                                System.out.println("Titulo pelicula: "+movie.getString("title_english")+", rating: "+movie.getDouble("rating"));
                            }
                        }

                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        */
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        System.out.println("ERROR= > "+error.getLocalizedMessage());
                    }
                });

        requestQueue.add(jsonObjectRequest);

    }




}
