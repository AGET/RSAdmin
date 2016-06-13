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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;

import java.util.ArrayList;


public class GpsEmpresa extends AppCompatActivity implements AdapterView.OnItemClickListener{

    AppCompatSpinner spinner;
    SpinnerAdapter adaptador;
    ListView lista;
    ArrayAdapter adaptadorLista;

    BroadcastReceiver receptorMensajeGps,receptorMensajeGpsAgregados;
    private ProgressBar progressBar;

    String idEmpresa,tipoPeticion = "post";

    ArrayList datos;

    final static String peticionListarGpsLibres = Configuracion.PETICION_LISTAR_GPS_LIBRES;
    final static String columnas[] = {Configuracion.COLUMNA_GPS_IMEI,Configuracion.COLUMNA_GPS_NUMERO};

    boolean datosRecibidos = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsempresa);
        agregarToolbar();

        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        progressBar = (ProgressBar) findViewById(R.id.barra);
        lista = (ListView)findViewById(R.id.lista_gps_de_empresa);
        lista.setOnItemClickListener(this);

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
        new ObtenerAsincrono(GpsEmpresa.this,Configuracion.TABLA_GPS,columnas)
                .execute(peticionListarGpsLibres);

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_ID};
        String[] valorFiltro = {idEmpresa};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_NUMERO,
                Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_EMPRESA};

        new ObtencionDeResultadoBcst(GpsEmpresa.this, columnasFiltro, valorFiltro ,Configuracion.TABLA_GPS, columnasArecuperar,true)
                .execute(Configuracion.PETICION_LISTAR_GPS_EMPRESA,tipoPeticion);

        receptorMensajeGps = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS LIBRES");
                if(datosRecibidos) {
                    mostrarProgreso(false);
                }else{
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if(restado){
                    actualizarSpinner(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
            }
        };

        receptorMensajeGpsAgregados = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS AGREADOS");
                if(datosRecibidos) {
                    mostrarProgreso(false);
                }else{
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if(restado){
                    actualizarLista(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_GPS_EMPRESA);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensajeGps, filtroSync);

        IntentFilter filtroSyncGpsAgregados = new IntentFilter(Configuracion.INTENT_GPS_EMPRESA_AGREGADOS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensajeGpsAgregados, filtroSyncGpsAgregados);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensajeGps);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensajeGpsAgregados);
    }

    protected void actualizarSpinner(ArrayList datosMultiples) {
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

    protected void actualizarLista(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:",""+datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:",""+((ArrayList)datos.get(0)).size());

        for(int i = 0 ; i < datos.size()-1; i++){
            Log.v("AGET-include:","" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) ((ArrayList) datos.get(i)).get(0) + " - " + ((ArrayList) datos.get(i)).get(1));
        }

        adaptadorLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        lista.setAdapter(adaptadorLista);
        adaptadorLista.notifyDataSetChanged();
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String marcado = (String) lista.getItemAtPosition(position);
        Snackbar.make(view,  "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        marcado = (String) ((ArrayList) datos.get(position)).get(0) + " - " + ((ArrayList) datos.get(position)).get(1);
        Log.v("AGET-Enviado",marcado);
        //actividadEmpresa(marcado);
    }
}
