package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
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

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;

import java.util.ArrayList;


public class GpsDepartamento extends AppCompatActivity implements AdapterView.OnItemClickListener {

    AppCompatSpinner spinner;
    SpinnerAdapter adaptadorSpinner;
    ListView lista;
    ArrayAdapter adaptadorLista;

    BroadcastReceiver receptorMensajeGpsSpinner, receptorMensajeGpsLista;
    private ProgressBar progressBar;

    String idDepartamento, idEmpresa, departamentoNombre, tipoPeticion = "post";

    ArrayList datosLista, datosSpinner;

    final static String peticionListarGpsLibres = Configuracion.PETICION_GPS_LISTAR_LIBRES;
    final static String columnas[] = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION};

    boolean datosRecibidos = false;
    String ID_Spinner = "";

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
        idDepartamento = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);

        setTitle("GPS de " + departamentoNombre);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcado = "";

                String imei = "";
                String numero = "";
                String descripcion = "";

                if (spinner.getSelectedItem() == "Estado de la empresa") {
                } else {
//                    Toast.makeText(GpsDepartamento.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("AGET-SPINNER-GPSS", "" + ((ArrayList) datosSpinner).size());
                    Log.v("AGET-SPINNER-ITEMS", "" + ((ArrayList) datosSpinner.get(0)).size());
                    Log.v("AGET-SPINNER-POSICION", "" + position);
                    Log.v("AGET-SPINNER-ADAPTADOR", "" + adaptadorSpinner.getCount());

                    if (adaptadorSpinner.getCount() == position) {

                    } else {
                        ID_Spinner = (String) ((ArrayList) datosSpinner.get(position)).get(0);
                        imei = (String) ((ArrayList) datosSpinner.get(position)).get(1);
                        numero = (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        descripcion = (String) ((ArrayList) datosSpinner.get(position)).get(3);

                        marcado = (String) ((ArrayList) datosSpinner.get(position)).get(0) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(1) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        Log.v("AGET-Enviado", marcado);
                        Log.v("AGET-IMEI", imei);
                        Log.v("AGET-NUMERO", numero);
                        Log.v("AGET-DESCRIPCION", descripcion);

                        mostrarProgreso(true);
                        //String[] columnasFiltro = {Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_DEPARTAMENTO};
                        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_DEPARTAMENTO_ID};
                        String[] valorFiltro = {ID_Spinner, idDepartamento};
                        tipoPeticion = "post";
                        new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                                .execute(Configuracion.PETICION_GPS_ASIGNAR_DEPARTAMENTO, tipoPeticion);

                        spinner.setSelection(adaptadorSpinner.getCount());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mostrarProgreso(true);
        peticionGPSLibres();
        peticionGPSEnlazados();

        receptorMensajeGpsSpinner = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS LIBRES");
                //mostrarProgreso(false);
                if (datosRecibidos) {

                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    actualizarSpinner(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
                if (mensaje.equalsIgnoreCase("No hay datos")) {
                    mensajesSak("No hay GPS disponibles para agregar");
                }
                if (mensaje.equalsIgnoreCase("Asignado correctamente")) {
                    peticionGPSLibres();
                    peticionGPSEnlazados();
                    mensajesSak("GPS Enlazado");
                }
                Log.v("AGET-spinner", mensaje);
            }
        };

        receptorMensajeGpsLista = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS AGREADOS");
                mostrarProgreso(false);
                if (datosRecibidos) {

                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    actualizarLista(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                } else {

//                    if (mensaje.equalsIgnoreCase("Registro actualizado correctamente")) {
//                        mostrarProgreso(true);
//                        new ObtenerAsincrono(GpsDepartamento.this,Configuracion.INTENT_GPS_LIBRES, Configuracion.TABLA_GPS, columnas)
//                                .execute(peticionListarGpsLibres);
//
//                        String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_ID};
//                        String[] valorFiltro = {idEmpresa};
//
//                        String[] columnasArecuperar = {
//                                Configuracion.COLUMNA_GPS_IMEI,
//                                Configuracion.COLUMNA_GPS_NUMERO,
//                                Configuracion.COLUMNA_GPS_DESCRIPCION,
//                                Configuracion.COLUMNA_GPS_DEPARTAMENTO
//                        };
//                        tipoPeticion = "post";
//                        new ObtencionDeResultadoBcst(GpsDepartamento.this,Configuracion.INTENT_GPS_DEPARTAMENTO, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, true)
//                                .execute(Configuracion.PETICION_GPS_LISTAR_DEPARTAMENTO, tipoPeticion);
////
//                        Snackbar.make(findViewById(R.id.xml_activity_gps_empresa),
//                                "Se ha guuardado", Snackbar.LENGTH_SHORT).show();
//                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_GPS_LIBRES);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensajeGpsSpinner, filtroSync);

        IntentFilter filtroSyncGpsAgregados = new IntentFilter(Configuracion.INTENT_GPS_DEPARTAMENTO);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensajeGpsLista, filtroSyncGpsAgregados);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensajeGpsSpinner);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensajeGpsLista);
    }

    protected void onRestart() {
        super.onRestart();
        peticionGPSEnlazados();
        peticionGPSLibres();
    }



    protected void actualizarSpinner(ArrayList datosMultiples) {
        this.datosSpinner = datosMultiples;
        ArrayList descripciones = new ArrayList();

        for (int i = 0; i < datosSpinner.size() - 1; i++) {
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datosSpinner.get(i)).get(0));
            descripciones.add((String) ((ArrayList) datosSpinner.get(i)).get(2) + "   " + (String) ((ArrayList) datosSpinner.get(i)).get(3));
        }

        adaptadorSpinner = new SpinnerAdapter(GpsDepartamento.this, android.R.layout.simple_list_item_1);
        adaptadorSpinner.addAll(descripciones);
        adaptadorSpinner.add("Elige un Gps a agregar");
        spinner.setAdapter(adaptadorSpinner);
        spinner.setSelection(adaptadorSpinner.getCount());

        // adaptador.notifyDataSetChanged();

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_ID};
        String[] valorFiltro = {idEmpresa};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_NUMERO,
                Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_DEPARTAMENTO};

        //new ObtencionDeResultadoBcst(GpsDepartamento.this,Configuracion.INTENT_GPS_DEPARTAMENTO, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, true)
        //      .execute(Configuracion.PETICION_GPS_LISTAR_DEPARTAMENTO, tipoPeticion);
    }

    protected void actualizarLista(ArrayList datosMultiples) {
        this.datosLista = datosMultiples;
        Log.v("AGET-LISTA:", "" + datosLista.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datosLista.get(0)).size());

        for (int i = 0; i < datosLista.size() - 1; i++) {
            nombres.add((String) ((ArrayList) datosLista.get(i)).get(2) + "   " + ((ArrayList) datosLista.get(i)).get(3));
        }

        adaptadorLista = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);
        lista.setAdapter(adaptadorLista);

        Log.v("AGET", "Lista actualizada");
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
//        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();
        Log.v("AGET-LISTA-CONTADOR", "" + ((ArrayList) datosLista.get(position)).size());
        Log.v("AGET-LISTA-ITEM", "" + ((ArrayList) datosLista).size());
        Log.v("AGET-POSICION-LISTA", "" + position);
        Log.v("AGET-ADAPTADOR-LISTA", "" + adaptadorLista.getCount());
        marcado = (String) ((ArrayList) datosLista.get(position)).get(0) + " - " + ((ArrayList) datosLista.get(position)).get(1);
        Log.v("AGET-Enviado", marcado);
        //actividadEmpresa(marcado);
        Intent actividad = new Intent(GpsDepartamento.this, GpsDepartamentoDetalle.class);
        actividad.putExtra(Configuracion.COLUMNA_GPS_ID, (String) ((ArrayList) datosLista.get(position)).get(0));
        actividad.putExtra(Configuracion.COLUMNA_GPS_IMEI, (String) ((ArrayList) datosLista.get(position)).get(1));
        actividad.putExtra(Configuracion.COLUMNA_GPS_NUMERO, (String) ((ArrayList) datosLista.get(position)).get(2));
        actividad.putExtra(Configuracion.COLUMNA_GPS_DESCRIPCION, (String) ((ArrayList) datosLista.get(position)).get(3));
        actividad.putExtra(Configuracion.COLUMNA_GPS_AUTORASTREO, (String) ((ArrayList) datosLista.get(position)).get(4));
        actividad.putExtra(Configuracion.COLUMNA_GPS_DEPARTAMENTO, (String) ((ArrayList) datosLista.get(position)).get(5));
        startActivity(actividad);
    }

    public void mensajesSak(String mensaje) {
        Snackbar.make(findViewById(R.id.xml_activity_gps_empresa),
                mensaje, Snackbar.LENGTH_SHORT).show();
    }

    public void peticionGPSLibres() {
        new ObtenerAsincrono(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES, Configuracion.TABLA_GPS, columnas)
                .execute(peticionListarGpsLibres);
    }

    public void peticionGPSEnlazados() {
//Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_DEPARTAMENTO_ID};
        String[] valorFiltro = {idDepartamento};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_GPS_ID,
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_NUMERO,
                Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_AUTORASTREO,
                Configuracion.COLUMNA_GPS_DEPARTAMENTO};

        Log.v("AGET-ID_DEPARTAMENTO", "" + idDepartamento);
        new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_DEPARTAMENTO, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, true)
                .execute(Configuracion.PETICION_GPS_LISTAR_DEPARTAMENTO, tipoPeticion);
    }
}