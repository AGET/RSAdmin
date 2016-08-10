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

    String intent = "";

    public ObtenerAsincrono(Context context, String receptor, String tabla, String[] columna) {
        this.context = context;
        this.tabla = tabla;
        this.columna = columna;

   /*     Log.v("AGET-CONTEXTO-class", String.valueOf(context.getClass()));
        Log.v("AGET-CONTEXTO-paquete", String.valueOf(context.getPackageName()));
        Log.v("AGET-CONTEXTO-Nombre", String.valueOf(context.getClass().getName()));*/

        if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.INTENT_LISTA_EMPRESA)) {
            Log.v("AGET-COMPRARADA", "Intent de lista enempresa");
            intent = Configuracion.INTENT_LISTA_EMPRESA;
        } else if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.INTENT_LISTA_GPS)) {
            Log.v("AGET-COMPRARADA", "Intent de lista gps");
            intent = Configuracion.INTENT_LISTA_GPS;
        } else if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.INTENT_GPS_DEPARTAMENTO)) {
            Log.v("AGET-COMPRARADA", "Intent de spinner gps departamento");
            intent = Configuracion.INTENT_GPS_DEPARTAMENTO;
        } else if (Configuracion.INTENT_LISTA_EMPRESA == receptor) {
            intent = Configuracion.INTENT_LISTA_EMPRESA;
        } else if (Configuracion.INTENT_LISTA_GPS == receptor) {
            intent = Configuracion.INTENT_LISTA_GPS;
        }else{
            intent=receptor;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    protected String doInBackground(String... urls) {
        return POST(urls[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.contentEquals("error en el servidor")) {
            enviarBroadcast(false, "hay un error en el servidor", datos);
        } else {
            try {
//                Log.v("AGET-RESULT-1", result);
                JSONObject json = new JSONObject(result);
                Log.v("AGET-RESULT-2", result);
                datos = new ArrayList();
                String str = "";
                JSONArray obj = json.getJSONArray(tabla);

                datos.add(new ArrayList());

                for (int i = 0; i < json.getJSONArray(tabla).length(); i++) {
                    for (int j = 0; j < columna.length; j++)
                        ((ArrayList) datos.get(i)).add(obj.getJSONObject(i).getString(columna[j]));
                    datos.add(new ArrayList());
                }

                for (int i = 0; i < datos.size(); i++) {
                    for (int j = 0; j < ((ArrayList) datos.get(i)).size(); j++) {
                        Log.v("AGET-listas", (String) ((ArrayList) datos.get(i)).get(j) + "  ");
                    }
                    System.out.println();
                }

                Log.v("AGET-PARSEADO", str);

                enviarBroadcast(true, "Parseado", datos);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                Log.v("AGET-DATOS", "Error en datos");
                enviarBroadcast(false, "No se encontraron datos, podria deberse a la BD", datos);
            }
        }
    }

    public String POST(String url) {
        InputStream inputStream = null;
        String result = "";
        try {

            HttpClient httpclient = new DefaultHttpClient();

            HttpResponse httpResponse = httpclient.execute(new HttpPost(url));

            inputStream = httpResponse.getEntity().getContent();


            if (inputStream != null)
                result = new Convertidor().convertInputStreamToString(inputStream);
            else
                result = "no se encontraron datos";

        } catch (Exception e) {
            enviarBroadcast(false, "error en el servidor", datos);
            Log.d("AGET-InputStream", e.getLocalizedMessage());
            return "error en el servidor";
        }
        return result;
    }

    private void enviarBroadcast(boolean estado, String mensaje, ArrayList datos) {
        Intent intentLocal = new Intent(intent);
        intentLocal.putExtra(Utilidades.EXTRA_RESULTADO, estado);
        intentLocal.putExtra(Utilidades.EXTRA_MENSAJE, mensaje);
        intentLocal.putExtra(Utilidades.EXTRA_DATOS_ALIST, datos);
        LocalBroadcastManager.getInstance(Configuracion.context).sendBroadcast(intentLocal);

        Log.v("AGET", "BROAD ENVIADO:" + intent);
    }
}
