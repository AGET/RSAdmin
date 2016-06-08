package com.aldo.aget.rsadmin.ServicioWeb;

/**
 * Created by Work on 22/05/16.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Convertidor;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import android.widget.ProgressBar;


public class ObtenerAsincrono extends AsyncTask<String, Void, String> {

    private ProgressDialog progressDialog;
    private Context context;
    ListView lista;
    ProgressBar progressBar;

    ArrayAdapter adaptador;

    View view;
    String peticion, tabla;
    String[] columna;

    ArrayList datos;

    //ManipulacionDatos datosLista;

    String intent = "";


    /**
     * Constructor de clase
     */
    //public ObtenerAsincrono(Context context, ListView listView, ProgressBar progressBar, View view, String tabla, String columna) {
    public ObtenerAsincrono(Context context, String tabla, String[] columna) {
        this.context = context;
        this.tabla = tabla;
        this.columna = columna;
        //datosLista = new ManipulacionDatos();

        Log.v("AGET-CONTEXTO-class", String.valueOf(context.getClass()));
        Log.v("AGET-CONTEXTO-paquete", String.valueOf(context.getPackageName()));
        Log.v("AGET-CONTEXTO-Nombre", String.valueOf(context.getClass().getName()));

        if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.INTENT_LISTA_EMPRESA)) {
            Log.v("AGET-COMPRARADA", "Intent de lista enempresa");
            intent = Configuracion.INTENT_LISTA_EMPRESA;
        } else if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.INTENT_LISTA_GPS)) {
            Log.v("AGET-COMPRARADA", "Intent de lista gps");
            intent = Configuracion.INTENT_LISTA_GPS;
        }
    }

    /**
     * Antes de comenzar la tarea muestra el progressDialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressBar.setVisibility(View.VISIBLE);
        //progressDialog = ProgressDialog.show(context, "Por favor espere", "Procesando...");
    }

    protected String doInBackground(String... urls) {
        return POST(urls[0]);
    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     **/
    @Override
    protected void onPostExecute(String result) {
        //progressDialog.dismiss();
//        progressBar.setVisibility(View.GONE);

        //Toast.makeText(Configuracion.context, "Received!", Toast.LENGTH_LONG).show();
        if (result.contentEquals("error en el servidor")) {
            enviarBroadcast(false, "hay un error en el servidor",datos);
        } else {
            try {
                Log.v("AGET-RESULT-1", result);
                JSONObject json = new JSONObject(result);
                Log.v("AGET-RESULT-2", result);
                datos = new ArrayList();
                String str = "";
                JSONArray obj = json.getJSONArray(tabla);


               // ArrayList matrix = new ArrayList();
                datos.add(new ArrayList());

                for (int i = 0; i < json.getJSONArray(tabla).length(); i++) {
                    for(int j = 0 ; j < columna.length; j++)
                     //  datos.add(obj.getJSONObject(i).getString(columna));
                        ((ArrayList)datos.get(i)).add(obj.getJSONObject(i).getString(columna[j]));
                    datos.add(new ArrayList());
                }


                // display contents of matrix
                for(int i = 0; i < datos.size();i++){
                    for(int j = 0; j < ((ArrayList)datos.get(i)).size(); j++){
                        Log.v("AGET-listas",(String)((ArrayList)datos.get(i)).get(j) +"  ");
                    }
                    System.out.println();
                }

                //datosLista.setDatosJson(datos);
                Log.v("AGET-PARSEADO", str);
                //enviarBroadcast(true, "Parseado",datos);
                enviarBroadcast(true, "Parseado",datos);
                //actualizar();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.v("AGET-DATOS", "Error en datos");
                enviarBroadcast(false, "No se encontraron datos, podria deberse a la BD",datos);
            }
        }
    }

    protected void actualizar() {
        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpPost(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = new Convertidor().convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            enviarBroadcast(false, "error en el servidor", datos);
            Log.d("AGET-InputStream", e.getLocalizedMessage());
            return "error en el servidor";
        }
        return result;
    }

    private void enviarBroadcast(boolean estado, String mensaje, ArrayList datos) {
        //Intent intentLocal = new Intent(Intent.ACTION_SYNC);
        Intent intentLocal = new Intent(intent);
        intentLocal.putExtra(Utilidades.EXTRA_RESULTADO, estado);
        intentLocal.putExtra(Utilidades.EXTRA_MENSAJE, mensaje);
        intentLocal.putExtra(Utilidades.EXTRA_DATOS_ALIST, datos);
        LocalBroadcastManager.getInstance(Configuracion.context).sendBroadcast(intentLocal);

//        Intent broadcastIntent = new Intent();
//        //broadcastIntent.setAction("NOMBRE_DE_NUESTRA_ACTION");
//        broadcastIntent.putExtra(Utilidades.EXTRA_RESULTADO, estado);
//        broadcastIntent.putExtra(Utilidades.EXTRA_MENSAJE, mensaje);
//        context.sendBroadcast(broadcastIntent);

        Log.v("AGET", "BROAD ENVIADO");
    }
}
