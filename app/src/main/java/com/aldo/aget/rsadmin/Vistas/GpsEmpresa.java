package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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


public class GpsEmpresa extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AppCompatSpinner spinner;
    SpinnerAdapter adaptadorSpinner;
    ListView lista;
    ArrayAdapter adaptadorLista;

    BroadcastReceiver receptorMensajeGps, receptorMensajeGpsAgregados;
    private ProgressBar progressBar;

    String idEmpresa, tipoPeticion = "post";

    ArrayList datosLista, datosSpinner;

    final static String peticionListarGpsLibres = Configuracion.PETICION_GPS_LISTAR_LIBRES;
    final static String columnas[] = {Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION};

    boolean datosRecibidos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsempresa);
        agregarToolbar();

        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        progressBar = (ProgressBar) findViewById(R.id.barra);
        lista = (ListView) findViewById(R.id.lista_gps_de_empresa);
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
                String marcado = "";
                String imei = "";
                String numero = "";
                String descripcion = "";

                if (spinner.getSelectedItem() == "Estado de la empresa") {
                } else {
                    Toast.makeText(GpsEmpresa.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("AGET-SPINNER-GPSS", "" + ((ArrayList) datosSpinner).size());
                    Log.v("AGET-SPINNER-ITEMS", "" + ((ArrayList) datosSpinner.get(0)).size());
                    Log.v("AGET-SPINNER-POSICION", "" + position);
                    Log.v("AGET-SPINNER-ADAPTADOR", "" + adaptadorSpinner.getCount());

                    if (adaptadorSpinner.getCount() == position) {

                    } else {
                        imei = (String) ((ArrayList) datosSpinner.get(position)).get(0);
                        numero = (String) ((ArrayList) datosSpinner.get(position)).get(1);
                        descripcion = (String) ((ArrayList) datosSpinner.get(position)).get(2);

                        marcado = (String) ((ArrayList) datosSpinner.get(position)).get(0) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(1) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        Log.v("AGET-Enviado", marcado);
                        Log.v("AGET-IMEI", imei);
                        Log.v("AGET-NUMERO", numero);
                        Log.v("AGET-DESCRIPCION", descripcion);

                        mostrarProgreso(true);
                        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_EMPRESA};
                        String[] valorFiltro = {numero, descripcion, idEmpresa};
                        tipoPeticion = "put";
                        new ObtencionDeResultadoBcst(GpsEmpresa.this,Configuracion.INTENT_GPS_EMPRESA, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false).execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR
                                + imei, tipoPeticion);

                        spinner.setSelection(adaptadorSpinner.getCount());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mostrarProgreso(true);
        new ObtenerAsincrono(GpsEmpresa.this,Configuracion.INTENT_GPS_EMPRESA, Configuracion.TABLA_GPS, columnas)
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

        new ObtencionDeResultadoBcst(GpsEmpresa.this,Configuracion.INTENT_GPS_EMPRESA, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, true)
                .execute(Configuracion.PETICION_GPS_LISTAR_EMPRESA, tipoPeticion);

        receptorMensajeGps = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS LIBRES");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    actualizarSpinner(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
            }
        };

        receptorMensajeGpsAgregados = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS AGREADOS");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    actualizarLista(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                } else {

                    if (mensaje.equalsIgnoreCase("Registro actualizado correctamente")) {
                        mostrarProgreso(true);
                        new ObtenerAsincrono(GpsEmpresa.this,Configuracion.INTENT_GPS_EMPRESA, Configuracion.TABLA_GPS, columnas)
                                .execute(peticionListarGpsLibres);

                        String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_ID};
                        String[] valorFiltro = {idEmpresa};

                        String[] columnasArecuperar = {
                                Configuracion.COLUMNA_GPS_IMEI,
                                Configuracion.COLUMNA_GPS_NUMERO,
                                Configuracion.COLUMNA_GPS_DESCRIPCION,
                                Configuracion.COLUMNA_GPS_EMPRESA};
                        tipoPeticion = "post";
                        new ObtencionDeResultadoBcst(GpsEmpresa.this,Configuracion.INTENT_GPS_EMPRESA, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, true)
                                .execute(Configuracion.PETICION_GPS_LISTAR_EMPRESA, tipoPeticion);

                        Snackbar.make(findViewById(R.id.xml_activity_gps_empresa),
                                "Se ha guuardado", Snackbar.LENGTH_SHORT).show();
                    }
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
        this.datosSpinner = datosMultiples;
        ArrayList descripciones = new ArrayList();

        for (int i = 0; i < datosSpinner.size() - 1; i++) {
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datosSpinner.get(i)).get(0));
            descripciones.add((String) ((ArrayList) datosSpinner.get(i)).get(1));
        }

        adaptadorSpinner = new SpinnerAdapter(GpsEmpresa.this, android.R.layout.simple_list_item_1);
        adaptadorSpinner.addAll(descripciones);
        adaptadorSpinner.add("Elige un Gps a agregar");
        spinner.setAdapter(adaptadorSpinner);
        spinner.setSelection(adaptadorSpinner.getCount());

        // adaptador.notifyDataSetChanged();
    }

    protected void actualizarLista(ArrayList datosMultiples) {
        this.datosLista = datosMultiples;
        Log.v("AGET-LISTA:", "" + datosLista.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datosLista.get(0)).size());

        for (int i = 0; i < datosLista.size() - 1; i++) {
            // Log.v("AGET-include:","" + i);
            //Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) ((ArrayList) datosLista.get(i)).get(0) + " - " + ((ArrayList) datosLista.get(i)).get(1));
        }

        adaptadorLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        lista.setAdapter(adaptadorLista);

        Log.v("AGET","Lista actualizada");
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
        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        Log.v("AGET-LISTA-CONTADOR", "" + ((ArrayList) datosLista.get(position)).size());
        Log.v("AGET-LISTA-ITEM", "" + ((ArrayList) datosLista).size());
        Log.v("AGET-POSICION-LISTA", "" + position);
        Log.v("AGET-ADAPTADOR-LISTA", "" + adaptadorLista.getCount());
        marcado = (String) ((ArrayList) datosLista.get(position)).get(0) + " - " + ((ArrayList) datosLista.get(position)).get(1);
        Log.v("AGET-Enviado", marcado);
        //actividadEmpresa(marcado);
        Intent actividad = new Intent(GpsEmpresa.this, GpsEmpresaDetalle.class);
        actividad.putExtra(Configuracion.COLUMNA_GPS_IMEI, (String) ((ArrayList) datosLista.get(position)).get(0) );
        actividad.putExtra(Configuracion.COLUMNA_GPS_NUMERO, (String) ((ArrayList) datosLista.get(position)).get(1) );
        actividad.putExtra(Configuracion.COLUMNA_GPS_DESCRIPCION, (String) ((ArrayList) datosLista.get(position)).get(2) );
        startActivity(actividad);
    }
}