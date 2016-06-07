package com.aldo.aget.rsadmin.ServicioWeb;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Convertidor;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Work on 23/05/16.
 */
public class ObtencionDeResultadoBcst extends AsyncTask<String, Void, JSONObject> {
    private ProgressDialog progressDialog;
    private Context context;
    String peticion;
    String[] columnasFiltro,valorFiltro,columnas_a_recuperar;
    ArrayList datos;
    JSONObject objetoJSON;

    private HttpClient httpclient;

    String tabla;
    String intent = "";

    boolean obtener = true;

    public ObtencionDeResultadoBcst(Context context, String[] nombresColumnas, String[] datosColumnas, String tabla, String[] columnas_a_recuperar) {
        this.context = context;
        this.columnasFiltro = nombresColumnas;
        this.valorFiltro = datosColumnas;
        this.tabla=tabla;
        if(columnas_a_recuperar != null) {
            this.columnas_a_recuperar = columnas_a_recuperar;
            obtener = true;
        } else{
            obtener = false;
        }

        datos = new ArrayList();

        if (String.valueOf(context.getClass().getName()).equalsIgnoreCase(Configuracion.IntentEmpresaCliente)) {
            intent = Configuracion.IntentEmpresaCliente;
        }
    }

    @Override
    protected JSONObject doInBackground(String... params) {

        try {
            return addEventPost(params[0]);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Antes de comenzar la tarea muestra el progressDialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(context, "Por favor espere", "Procesando...");
    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     **/
    @Override
    protected void onPostExecute(JSONObject resul) {
//        progressDialog.dismiss();

        //setJSON(resul);
        try {
            parserJson(resul);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject addEventPost(String peticion) throws ClientProtocolException, IOException, JSONException {
        httpclient = new DefaultHttpClient();
        String uuid = UUID.randomUUID().toString();
        //url y tipo de contenido
        HttpPost httppost = new HttpPost(peticion);
        httppost.addHeader("Content-Type", "application/json");
        //forma el JSON y tipo de contenido
        JSONObject jsonObject = new JSONObject();

        for(int i = 0 ; i < columnasFiltro.length; i++){
            jsonObject.put(columnasFiltro[i], valorFiltro[i]);
        }
        StringEntity stringEntity = new StringEntity(jsonObject.toString());
        stringEntity.setContentType((Header) new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
        httppost.setEntity(stringEntity);
        //ejecuta
        HttpResponse response = httpclient.execute(httppost);
        //obtiene la respuesta y transorma a objeto JSON
        String jsonResult = new Convertidor().inputStreamToString(response.getEntity().getContent()).toString();
        JSONObject object = new JSONObject(jsonResult);
        Log.i("jsonResult", jsonResult);
        return object;
    }

    public void parserJson( JSONObject result) throws JSONException {
//        {
//            "estado": 1
//            "mensaje": "Registro con exito!"
//        }

        if(result != null) {
            Log.v("AGET-JSON-NoNulo",result.getString("mensaje").toString());
            if(obtener) {
                for (int i = 0; i < result.getJSONObject(tabla).length(); i++) {
                    datos.add(result.getJSONObject(tabla).getString(columnas_a_recuperar[i]));
                }
                enviarBroadcast(true, "Cargado", datos);
            }else{
                if(result.getString("mensaje").toString().equalsIgnoreCase("Registro con exito!")){
                    enviarBroadcast(false, "Registro exitoso!", datos);

                }
            }
        }else{
            enviarBroadcast(false, "Ha ocurrido un error, con los datos", datos);
        }
    }
    private void enviarBroadcast(boolean estado, String mensaje, ArrayList datos) {
        Intent intentLocal = new Intent(intent);
        intentLocal.putExtra(Utilidades.EXTRA_RESULTADO, estado);
        intentLocal.putExtra(Utilidades.EXTRA_MENSAJE, mensaje);
        intentLocal.putExtra(Utilidades.EXTRA_DATOS_ALIST, datos);
        LocalBroadcastManager.getInstance(Configuracion.context).sendBroadcast(intentLocal);

        Log.v("AGET", "BROAD ENVIADO");
    }
}

