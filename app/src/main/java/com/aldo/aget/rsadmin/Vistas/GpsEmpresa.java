package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;

import java.util.ArrayList;


public class GpsEmpresa extends AppCompatActivity {

    AppCompatSpinner spinner;
    SpinnerAdapter adaptador;

    BroadcastReceiver receptorMensaje;
    private ProgressBar progressBar;

    String idEmpresa;

    ArrayList datos;

    final static String peticionListarGpsLibres = Configuracion.PETICION_LISTAR_GPS_LIBRES;
    final static String tabla = "gps";
    final static String columnas[] = {"imei","numero"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsempresa);
        agregarToolbar();

        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        progressBar = (ProgressBar) findViewById(R.id.barra);

        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem() == "Estado de la empresa") {

                } else {
                    Toast.makeText(GpsEmpresa.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mostrarProgreso(true);
        new ObtenerAsincrono(GpsEmpresa.this,tabla,columnas)
                .execute(peticionListarGpsLibres);

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO empresa");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if(restado){
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
            }
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_GPS_EMPRESA);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtroSync);

        if (Configuracion.cambio) {
            mostrarProgreso(true);
            Configuracion.cambio = false;
            new ObtenerAsincrono(GpsEmpresa.this, tabla, columnas)
                    .execute(peticionListarGpsLibres);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
    }


    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        ArrayList descripciones = new ArrayList();

        for(int i = 0 ; i < datos.size()-1; i++){
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            descripciones.add((String) ((ArrayList) datos.get(i)).get(1));
        }

        adaptador = new SpinnerAdapter(this, android.R.layout.simple_list_item_1);
        adaptador.addAll(descripciones);
        adaptador.add("Elige un Gps a agregar");
        spinner.setAdapter(adaptador);
        spinner.setSelection(adaptador.getCount());
        adaptador.notifyDataSetChanged();
    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }



}
