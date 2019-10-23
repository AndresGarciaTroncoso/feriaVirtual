package com.example.feriavirtualapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.feriavirtualapp.bd.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ProcesoVentaActivity  extends AppCompatActivity {

    ListView lvProcesos;
    ArrayList<String> lsProcesos;

    String usuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceso_venta);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lvProcesos = (ListView) findViewById(R.id.lvProcesos);
        usuario= getIntent().getExtras().getString("usuario");



        getProcesoVentaAll();
        if(lsProcesos.size()<1)
        {

            String url = "http://10.0.2.2:54330/api/v1/producto/ObtenerDetallePedidoAsignado/" + usuario;
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) {
                            System.out.println("JSONRESPONSE => " + response.toString());
                            //txtShowTextResult.setText("Response: " + response.toString());
                            try {


                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject procesoVenta = response.getJSONObject(i);
                                    String fechaInicio =procesoVenta.getString("fechaInicio");
                                    String tipoVenta =procesoVenta.getString("tipoVenta");
                                    String estado =procesoVenta.getString("estado");
                                    String idPedido =procesoVenta.getString("idPedido");

                                    grabarPrcesoVenta(fechaInicio,tipoVenta,estado,idPedido);


                                    finish();
                                    startActivity(getIntent());
                                }

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

            requestQueue.add(jsonArrayRequest);


        }

            ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1,lsProcesos);
            lvProcesos.setAdapter(adapter);






    }

    private void grabarPrcesoVenta(String fechaInicio, String tipoVenta, String estado, String pedidoId) {

        DatabaseHelper bd = new DatabaseHelper(this);

        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        bd.openDataBase();

        ContentValues registro = new ContentValues();
        registro.put("fechaInicio", fechaInicio);
        registro.put("tipoVenta", tipoVenta);
        registro.put("estado", estado);
        registro.put("pedidoId", pedidoId);
        // registro.put("vigencia",1);
        bd.myDataBase.insert("ProcesoVenta", null, registro);
        System.out.println("GRABO proceso venta:"+estado);
        bd.myDataBase.close();

    }

    public ArrayList<String> getProcesoVentaAll()
    {

        DatabaseHelper bd = new DatabaseHelper(this);
        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd.openDataBase();


        String q="select pedidoId,substr(fechaInicio,0,11) as fecha, tipoVenta , estado from ProcesoVenta";
        Cursor cursor = bd.myDataBase.rawQuery(q, null);

        lsProcesos= new ArrayList<String>();
        while (cursor.moveToNext()) {
            Integer v_idEstructura;


            lsProcesos.add(cursor.getString(0)+"-"+ cursor.getString(3)+" "+cursor.getString(1)+" "+cursor.getString(2));

        }
        bd.myDataBase.close();
        return lsProcesos;
    }


    public Boolean obtenerProcesos(){
        Boolean estado=false;
        DatabaseHelper bd = new DatabaseHelper(this);

        try {
            bd.createDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bd.openDataBase();


        String q="select * from ProcesoVenta ";
        Cursor cursor = bd.myDataBase.rawQuery(q, null);


        while (cursor.moveToNext()) {

            estado=true;

        }
        bd.myDataBase.close();

        return estado;
    }


    public void back(View view)
    {
        Intent i = new Intent(ProcesoVentaActivity.this,MainActivity.class);
        i.putExtra("usuario",usuario);
        startActivity(i);
    }

}
