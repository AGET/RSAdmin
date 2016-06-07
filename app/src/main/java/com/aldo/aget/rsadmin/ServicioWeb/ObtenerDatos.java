package com.aldo.aget.rsadmin.ServicioWeb;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Control.ManipulacionDatos;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Work on 22/05/16.
 */
public class ObtenerDatos {
    ArrayList datos;
    ManipulacionDatos datosLista;
    String columna;
    String tabla;

    ObtenerDatos(){
        datosLista = new ManipulacionDatos();
    }

    void realizarPeticion( String peticion,String tabla,String columna){
//        "http://192.168.0.100/api.rs.com/v1/empresa_cliente/listarVarios"
        this.columna = columna;
        this.tabla = tabla;
        new HttpAsyncTask().execute(peticion);
    }

    public static String POST(String url) {
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
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls)            {

            return POST(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Configuracion.context, "Received!", Toast.LENGTH_LONG).show();
            try {
                JSONObject json = new JSONObject(result);
                Log.v("RESULT", result);
                datos= new ArrayList();
                String str = "";
                //JSONArray articles = json.getJSONArray("usuario");
                JSONArray articles = json.getJSONArray(tabla);
                //str += "articles length = " + json.getJSONArray("usuario").length();
                str += "articles length = " + json.getJSONArray(tabla).length();
                str += "\n--------\n";
                str += "names: " + articles.getJSONObject(0).names();
                str += "\n--------\n";
                for (int i = 0 ; i < json.getJSONArray("usuario").length(); i++){
                    str += "is : " + articles.getJSONObject(i).getString("empresa_id");
                    str += "  noombre: " + articles.getJSONObject(i).getString("nombre");
                    str += "  telefono: " + articles.getJSONObject(i).getString("telefono");
                    str += "  correo: " + articles.getJSONObject(i).getString("correo");
                    str += "  status: " + articles.getJSONObject(i).getString("status");
                    datos.add(articles.getJSONObject(i).getString(columna));
                }
                datosLista.setDatosJson(datos);

//                etResponse.setText(str);
                Log.v("PARSEADO", str);
                //actualizar();
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public List obtenerLista(){
        return datosLista.getDatosJson();
    }














    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;
    }
}
