package com.aldo.aget.rsadmin.Vistas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultado;


public class Gps extends AppCompatActivity {

    EditText edtImei,edtEnlace,edtNumero;

    String  imeipasado;

    ObtencionDeResultado resultado;

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        imeipasado = bundle.getString(Configuracion.COLUMNA_GPS_IMEI);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        edtImei = (EditText) findViewById(R.id.edtimei);
        edtEnlace = (EditText) findViewById(R.id.edtenlacegps);
        edtNumero = (EditText) findViewById(R.id.edtnumero);

        String[] columnas = {Configuracion.COLUMNA_GPS_IMEI};
        String[] datos = {imeipasado};

        String tabla =Configuracion.TABLA_GPS;
        EditText[] editores = {edtImei,edtEnlace,edtNumero};
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_EMPRESA,
                Configuracion.COLUMNA_GPS_NUMERO};

        resultado = new ObtencionDeResultado(this,findViewById(R.id.actividadgps),columnas,datos,tabla, editores,columnasArecuperar);
        resultado.execute(Configuracion.PETICION_GPS_LISTAR_UNO);

        //json = resultado.getJSON();
        //new HttpAsyncTaskGps().execute();
    }


//
//
//
//
//    private class HttpAsyncTaskGps extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... urls)            {
//
//            return POST(urls[0]);
//        }
//        @Override
//        protected void onPostExecute(String result) {
//            Toast.makeText(Configuracion.context, "Received!", Toast.LENGTH_LONG).show();
//            try {
//                JSONObject json = new JSONObject(result);
//                Log.v("RESULT", result);
//                String str = "";
//                JSONArray gps = json.getJSONArray(Configuracion.TABLA_GPS);
//                str += "articles length = " + json.getJSONArray(Configuracion.TABLA_GPS).length();
//                str += "names: " + gps.getJSONObject(0).names();
//                for (int i = 0 ; i < json.getJSONArray("usuario").length(); i++){
//                    imei = gps.getJSONObject(i).getString(Configuracion.COLUMNA_IMEI);
//                    enlace = gps.getJSONObject(i).getString(Configuracion.COLUMNA_ENLACE);
//                    numero = gps.getJSONObject(i).getString(Configuracion.COLUMNA_NUMERO);
//                }
//                Log.v("PARSEADO", str);
//
//                edtImei.setText(imei);
//                edtEnlace.setText("el GPS esta disponible, no tiene empresas asociadas");
//                edtNumero.setText(numero);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//    }
//
//    public static String POST(String url) {
//        InputStream inputStream = null;
//        String result = "";
//        try {
//
//            // create HttpClient
//            HttpClient httpclient = new DefaultHttpClient();
//
//            // make GET request to the given URL
//            HttpResponse httpResponse = httpclient.execute(new HttpPost(url));
//
//            // receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // convert inputstream to string
//            if (inputStream != null)
//                result = new Convertidor().convertInputStreamToString(inputStream);
//            else
//                result = "Did not work!";
//
//        } catch (Exception e) {
//            Log.d("InputStream", e.getLocalizedMessage());
//        }
//
//        return result;
//    }

}
