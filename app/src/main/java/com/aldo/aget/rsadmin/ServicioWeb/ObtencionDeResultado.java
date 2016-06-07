package com.aldo.aget.rsadmin.ServicioWeb;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by Work on 23/05/16.
 */
public class ObtencionDeResultado extends AsyncTask<String, Void, JSONObject> {
    private ProgressDialog progressDialog;
    private Context context;
    View view;
    String peticion;
    String[] columnasFiltro,datosColumnas,columnasArecuperar;

    JSONObject objetoJSON;

    private HttpClient httpclient;

    String tabla;
    EditText[] edittext;
    TextView txtId;

//    String idDeLaTabla="";

    public ObtencionDeResultado(Context context, View view, String[] nombresColumnas, String[] datosColumnas,String tabla, EditText[] edittext, String[] columnasArecuperar) {
        this.context = context;
        this.view = view;
        this.columnasFiltro = nombresColumnas;
        this.datosColumnas = datosColumnas;
        this.tabla=tabla;
        this.edittext=edittext;
        this.columnasArecuperar = columnasArecuperar;

    }


//    public ObtencionDeResultado(Context context, View view, String[] nombresColumnas, String[] datosColumnas,String tabla, EditText[] edittext, String[] columnasArecuperar, TextView txtId) {
//        this.context = context;
//        this.view = view;
//        this.nombresColumnas = nombresColumnas;
//        this.datosColumnas = datosColumnas;
//        this.tabla=tabla;
//        this.edittext=edittext;
//        this.columnasArecuperar = columnasArecuperar;
//        this.txtId = txtId;
//
//    }


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
        progressDialog = ProgressDialog.show(
                context, "Por favor espere", "Procesando...");
    }

    /**
     * Cuando se termina de ejecutar, cierra el progressDialog y retorna el resultado a la interfaz
     **/
    @Override
    protected void onPostExecute(JSONObject resul) {
        progressDialog.dismiss();

        //setJSON(resul);
        try {
            parserJson(resul);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setJSON(JSONObject result){
        this.objetoJSON = result;

    }
    public JSONObject getJSON(){
        return this.objetoJSON;
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
            jsonObject.put(columnasFiltro[i], datosColumnas[i]);
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
        return object;
    }





    public void parserJson( JSONObject result) throws JSONException {
        //String temporal="";
        JSONObject temporalobj = result.getJSONObject(tabla);
        for (int i = 0 ; i < result.getJSONObject(tabla).length(); i++){
            if(i < edittext.length){
                edittext[i].setText(result.getJSONObject(tabla).getString(columnasArecuperar[i]));
            }else{
                //txtId.setText(result.getJSONObject(tabla).getString(columnasArecuperar[i]));
                Configuracion.idActual = result.getJSONObject(tabla).getString(columnasArecuperar[i]);
            }
        //    edittext[i].setText(temporal);
        }

    }

}

