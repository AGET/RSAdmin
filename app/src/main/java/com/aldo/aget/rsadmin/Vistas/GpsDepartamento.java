package com.aldo.aget.rsadmin.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
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

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class GpsDepartamento extends AppCompatActivity implements AdapterView.OnItemClickListener, DialogoAgregarGpsDep.DialogListener {

    AppCompatSpinner spinner;
    SpinnerAdapter adaptadorSpinner;
    ListView lista;
    ArrayAdapter adaptadorLista;
    Dialog dialogo;

    BroadcastReceiver receptorMensajeGpsSpinner, receptorMensajeGpsLista;
    private ProgressBar progressBar;

    String idDepartamento, idEmpresa, empresaNombre, empresaStatus, departamentoNombre, tipoPeticion = "post";

    ArrayList datosLista, datosSpinner;

    final static String peticionListarGpsLibres = Configuracion.PETICION_GPS_LISTAR_LIBRES;
    final static String columnas[] = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION};

    boolean datosRecibidos = false;
    String ID_Spinner = "";
    String textDescripcion = null;

    Activity activity;
    Context contex;
    View testingView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsempresa);
        agregarToolbar();

        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        progressBar = (ProgressBar) findViewById(R.id.barra);
        lista = (ListView) findViewById(R.id.lista_gps_de_empresa);
        lista.setOnItemClickListener(this);

        activity = this;
        contex = this;

        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);
        empresaNombre = bundle.getString(Configuracion.COLUMNA_EMPRESA_NOMBRE);
        empresaStatus = bundle.getString(Configuracion.COLUMNA_EMPRESA_STATUS);
        idDepartamento = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);

        setTitle("GPS de " + departamentoNombre);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcado = "";
                String imei = "";
                String numero = "";
                String descripcion = "";

                if (spinner.getSelectedItem() == "Elige un Gps a agregar") {
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

//                        dialogo =  crearDialogo();
//                        dialogo.show();
//                        dialogo.get

                        DialogoAgregarGpsDep dialog = new DialogoAgregarGpsDep(contex, activity);
                        testingView = dialog.inflar();
                        FragmentTransaction ft = getFragmentManager().beginTransaction();
//                      dialog.onCreateDialogo(bn).show(ft,"");
                        dialog.show(ft, "");

//                        if (textDescripcion != null) {
//                            if (textDescripcion != "") {
//                                mostrarProgreso(true);
//                                //String[] columnasFiltro = {Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_DEPARTAMENTO};
//
//                                String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_DEPARTAMENTO_ID};
//                                String[] valorFiltro = {ID_Spinner, idDepartamento};
//                                tipoPeticion = "post";
//                                new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
//                                        .execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR+ID_Spinner, tipoPeticion);
//
//                                Snackbar.make(view, descripcion, Snackbar.LENGTH_INDEFINITE).show();
//                                spinner.setSelection(adaptadorSpinner.getCount());
//                            } else {
//                                Toast.makeText(GpsDepartamento.this, "la descripcion no dee ser vacia", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            Toast.makeText(GpsDepartamento.this, "Elija una descripcion", Toast.LENGTH_SHORT).show();
//                        }

                        /*
                        mostrarProgreso(true);
                        //String[] columnasFiltro = {Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_DEPARTAMENTO};
                        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_DEPARTAMENTO_ID};
                        String[] valorFiltro = {ID_Spinner, idDepartamento};
                        tipoPeticion = "post";
                        new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                                .execute(Configuracion.PETICION_GPS_ASIGNAR_DEPARTAMENTO, tipoPeticion);
*/
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
                if(mensaje.equalsIgnoreCase("Registro actualizado correctamente")){
                    Snackbar.make(findViewById(R.id.xml_activity_gps_empresa), "Se ha agregado la descripcion", Snackbar.LENGTH_SHORT).show();
//                    mostrarProgreso(false);
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
//            descripciones.add((String) ((ArrayList) datosSpinner.get(i)).get(2) + "   " + (String) ((ArrayList) datosSpinner.get(i)).get(3));
            descripciones.add("Numero: " + (String) ((ArrayList) datosSpinner.get(i)).get(2));
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


        mostrarGpsDepartamentoDetalle(position);
    }

    private void mostrarGpsDepartamentoDetalle(int position) {

        Intent actividad = new Intent(GpsDepartamento.this, GpsDepartamentoDetalle.class);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, empresaNombre);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_STATUS, empresaStatus);
        actividad.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_ID, idDepartamento);
        actividad.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE, departamentoNombre);
        Log.v("YYY","GpsDepDet: "+ empresaNombre);
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

    private Dialog crearDialogo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


//        <ImageView
//        android:src="@drawable/ic_description_white_24dp"
//        android:layout_width="match_parent"
//        android:layout_height="64dp"
//        android:scaleType="center"
//        android:background="#2196F3"
//        android:contentDescription="@string/app_name" />

        final ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.ic_description_white_24dp);

        final String[] descripcion = {""};
        String desc = "";

        final EditText textoBusqueda = new EditText(this);
        builder.setTitle(R.string.desc_gps_dep);   // Título
        builder.setView(textoBusqueda);
        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Log.i("texto", textoBusqueda.getText().toString());
                descripcion[0] = (String) textoBusqueda.getText().toString();
            }
        });
        builder.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                descripcion[0] = (String) textoBusqueda.getText().toString();
            }
        });
        desc = descripcion[0];

        builder.create();
        builder.show();
        return builder.create();
//        return desc;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        EditText edtDescripcion = (EditText) testingView.findViewById(R.id.descripcion_gps);
        textDescripcion = edtDescripcion.getText().toString();


        if (textDescripcion != null) {
            if (textDescripcion != "") {
                mostrarProgreso(true);
                //String[] columnasFiltro = {Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_DEPARTAMENTO};

                String[] columnasFiltro = {Configuracion.COLUMNA_GPS_DESCRIPCION};
                String[] valorFiltro = {textDescripcion};
                tipoPeticion = "put";
                new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                        .execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR + ID_Spinner, tipoPeticion);


                Snackbar.make(findViewById(R.id.xml_activity_gps_empresa), textDescripcion + " ID: " + ID_Spinner, Snackbar.LENGTH_INDEFINITE).show();
                spinner.setSelection(adaptadorSpinner.getCount());
//
                String[] columnasFiltroAsignar = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_DEPARTAMENTO_ID};
                String[] valorFiltroAsignar = {ID_Spinner, idDepartamento};
                tipoPeticion = "post";
                new ObtencionDeResultadoBcst(GpsDepartamento.this, Configuracion.INTENT_GPS_LIBRES,
                        columnasFiltroAsignar , valorFiltroAsignar , Configuracion.TABLA_GPS, null, false)
                        .execute(Configuracion.PETICION_GPS_ASIGNAR_DEPARTAMENTO, tipoPeticion);
            } else {
                Toast.makeText(GpsDepartamento.this, "la descripcion no dee ser vacia", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(GpsDepartamento.this, "Elija una descripcion", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }
}