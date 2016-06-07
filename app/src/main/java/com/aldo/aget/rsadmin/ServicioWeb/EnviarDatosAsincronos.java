package com.aldo.aget.rsadmin.ServicioWeb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.aldo.aget.rsadmin.Control.Convertidor;
//import com.aldo.aget.rsadmin.arecycler.MyRestFulGP;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Work on 23/05/16.
 */
public class EnviarDatosAsincronos extends AsyncTask<String, Void, String> {
    private ProgressDialog progressDialog;
    private Context context;
    View view;
    String peticion;
    String[] nombresColumnas,datosColumnas;

    private HttpClient httpclient;

    public EnviarDatosAsincronos(Context context, View view, String[] nombresColumnas, String[] datosColumnas ) {
        this.context = context;
        this.view = view;
        this.nombresColumnas = nombresColumnas;
        this.datosColumnas = datosColumnas;
    }

    @Override
    protected String doInBackground(String... params) {

        //MyRestFulGP myRestFulGP = new MyRestFulGP();
        try {
            return addEventPost(params);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Antes de comenzar la tarea muestra el progressDialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(
                context, "Por favor espere", "Procesando...");
    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     **/
    @Override
    protected void onPostExecute(String resul) {
        progressDialog.dismiss();

        Snackbar.make(view, resul, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }


    public String addEventPost(String[]paramatros) throws ClientProtocolException, IOException, JSONException {
        httpclient = new DefaultHttpClient();
        String uuid = UUID.randomUUID().toString();
        //url y tipo de contenido
        HttpPost httppost = new HttpPost(peticion);
        httppost.addHeader("Content-Type", "application/json");
        //forma el JSON y tipo de contenido
        JSONObject jsonObject = new JSONObject();

        for(int i = 0 ; i < nombresColumnas.length; i++){
            jsonObject.put(nombresColumnas[i], datosColumnas[i]);
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
        //if( object.getString("Result").equals("200"))
//        if( object.getString("mensaje").equals("Registro con exito!"))
//        {
//            return "Petición POST: Exito";
//        }
//        return "Petición POST: Fracaso";
        return object.getString("mensaje");
    }

}
