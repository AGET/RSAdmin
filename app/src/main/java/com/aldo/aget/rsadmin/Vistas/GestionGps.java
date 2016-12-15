package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import org.json.JSONObject;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.ControlGps;
import com.aldo.aget.rsadmin.Control.Mensajes;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;

import android.widget.CheckBox;

public class GestionGps extends AppCompatActivity {

    EditText edtImei, edtNumero, edtDescripcion, edtEmpresaPerteneciente;

    String idGps;

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    BroadcastReceiver receptorMensaje, receptorEnlaceTelefonosEnlazados;

    MenuItem menuOk, menuEditar, menuEliminar;

    JSONObject json;

    String tipoPeticion = "post";

    String ID = "";

    ArrayList data;

    //Autotrack
//    CheckBox cbx_Deshabilitado;
//    Spinner spinnerTiempo;
//    SeekBar seekBarTiempo;
//    TextView textResultadoSeekBarTiempo;
//    SeekBar seekBarCantidad;
//    TextView textResultadoSeekBarCantidad;
//    SpinnerAdapter adaptadorSpinner;
//    int tiempoMaximo = 0;
//    String tipoTiempo = "";
//    String autorrastreoBD = "";
//    String rastreoAnulado = "nofix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_gps);
        agregarToolbar();
        Bundle bundle = getIntent().getExtras();
        idGps = bundle.getString(Configuracion.COLUMNA_GPS_ID);
/*
        cbx_Deshabilitado = (CheckBox) findViewById(R.id.cbx_rastreo_auto);
        spinnerTiempo = (Spinner) findViewById(R.id.spinnerTiempo);
        seekBarTiempo = (SeekBar) findViewById(R.id.seekBarTiempo);
        textResultadoSeekBarTiempo = (TextView) findViewById(R.id.textResultadoSeekBarTiempo);

        seekBarCantidad = (SeekBar) findViewById(R.id.seekBarCantidad);
        textResultadoSeekBarCantidad = (TextView) findViewById(R.id.textResultadoSeekBarCantidad);

        final String[] seleccionado = {""};
        final String[] tiempo = new String[1];
        final String[] cantidad = new String[1];
        final String[] datos = {"Segundos", "Minutos", "Horas"};


        spinnerTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerTiempo.getSelectedItem() == "Elija") {
                } else {
                    //Toast.makeText(GestionGps.this, spinnerTiempo.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    if (adaptadorSpinner.getCount() == position) {
                    } else {
                        //realizar
                        seleccionado[0] = "" + parent.getItemAtPosition(position).toString();
                        Log.v("AGET-deleccionado", seleccionado[0]);
                        establecer(parent.getItemAtPosition(position).toString());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void establecer(String s) {
                if (s.equalsIgnoreCase("Segundos") || s.equalsIgnoreCase("s")) {
                    tiempoMaximo = 39;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "s";
                    Log.v("AGET", "TIEMPO: s");
                }
                if (s.equalsIgnoreCase("Minutos") || s.equalsIgnoreCase("M")) {
                    tiempoMaximo = 59;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "m";
                    Log.v("AGET", "TIEMPO: m");
                }

                if (s.equalsIgnoreCase("Horas") || s.equalsIgnoreCase("H")) {
                    tiempoMaximo = 23;
                    seekBarTiempo.setMax(tiempoMaximo);
                    tipoTiempo = "h";
                    Log.v("AGET", "TIEMPO: h");
                }
            }
        });

        adaptadorSpinner = new SpinnerAdapter(GestionGps.this, android.R.layout.simple_list_item_1);
        adaptadorSpinner.addAll(datos);
        adaptadorSpinner.add("Elija");
        spinnerTiempo.setAdapter(adaptadorSpinner);
        spinnerTiempo.setSelection(adaptadorSpinner.getCount());

        seekBarTiempo.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seleccionado[0].equalsIgnoreCase(datos[0])) {
                    textResultadoSeekBarTiempo.setText(progress + 20 + "");
                } else {
                    textResultadoSeekBarTiempo.setText(progress + "");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        seekBarCantidad.setMax(300);
        seekBarCantidad.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textResultadoSeekBarCantidad.setText(progress + "");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        */

        //Fin autotrack


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        edtImei = (EditText) findViewById(R.id.edt_gps_imei);
        edtNumero = (EditText) findViewById(R.id.edt_gps_numero);
        edtDescripcion = (EditText) findViewById(R.id.edt_gps_descripcion);
        edtEmpresaPerteneciente = (EditText) findViewById(R.id.edt_gps_empresa);

        progressBar = (ProgressBar) findViewById(R.id.barra);

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID};
        String[] valorFiltro = {idGps};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_NUMERO,
                Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_AUTORASTREO,
                Configuracion.COLUMNA_GPS_DEPARTAMENTO,
                Configuracion.COLUMNA_GPS_ID};

        if (idGps != null) {
            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, false);
            resultado.execute(Configuracion.PETICION_GPS_LISTAR_UNO, tipoPeticion);
        } else {
            setTitle(R.string.titulo_actividad_agregar_gps);
            edtEmpresaPerteneciente.setEnabled(false);
//            seekBarCantidad.setEnabled(true);
//            seekBarTiempo.setEnabled(true);
//            spinnerTiempo.setEnabled(true);
//            cbx_Deshabilitado.setEnabled(true);
        }

        Log.v("AGET", columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);


        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean MostrarMensaje = true;
                Log.v("AGET", "BROAD RECIBIDO GPS");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    cargarViews(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                } else if (mensaje.equalsIgnoreCase("Registro con exito!")) {
                    //menuOk.setEnabled(false);
                    menuOk.setVisible(false);
                    menuEditar.setVisible(true);
                    menuEliminar.setVisible(true);
                    habilitarComponentes(false);
                    vincular();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Registro actualizado correctamente")) {
                    menuOk.setVisible(false);
                    menuEditar.setVisible(true);
                    menuEliminar.setVisible(true);
                    habilitarComponentes(false);
                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Url mal formada")) {
                    mensaje = "error en dato, probablemente con ID";
                } else if (mensaje.equalsIgnoreCase("El GPS al que intentas acceder no existe")) {
                    mensaje = "Revise los datos, puede no haber modificacion";
                } else if (mensaje.equalsIgnoreCase("Registro eliminado correctamente")) {
                    Mensajes sms = new Mensajes(context,1);
                    sms.enviarRestaurarGps(edtNumero.getText().toString());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    Configuracion.cambio = true;
                }
                if (mensaje.equalsIgnoreCase("Cargado")) {
                    return;
                }

                Snackbar.make(findViewById(R.id.actividadgps),
                        mensaje, Snackbar.LENGTH_SHORT).show();
                mostrarProgreso(false);
            }
        };

//        receptorEnlaceTelefonosEnlazados = new BroadcastReceiver() {
//
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                boolean MostrarMensaje = true;
//                Log.v("AGET", "RECEPTOR ENLACE");
//                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
//                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
//                if (resultado) {
//                    procesarNumObtenidos(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
//                } else {
//                    mostrarProgreso(false);
//                }
//
//                Snackbar.make(findViewById(R.id.actividadgps), mensaje, Snackbar.LENGTH_SHORT).show();
//            }
//        };
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GPS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);

        IntentFilter filtro2 = new IntentFilter(Configuracion.INTENT_ENLACE_ELIMINAR_TELEFONOS_ENLAZADOS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorEnlaceTelefonosEnlazados, filtro2);

    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorEnlaceTelefonosEnlazados);
    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insercion_empresa, menu);
        this.menuEliminar = menu.findItem(R.id.accion_eliminar);
        this.menuEditar = menu.findItem(R.id.accion_editar);
        this.menuOk = menu.findItem(R.id.accion_confirmar);
        // Verificación de visibilidad acción eliminar
        if (idGps != null) {
            menuEditar.setVisible(true);
            menuEliminar.setVisible(true);
            menuOk.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.accion_confirmar:
                insertar();
                break;
            case R.id.accion_eliminar:
                tipoPeticion = "delete";
//                preparaEliminacion();
                eliminar();
                break;
            case R.id.accion_editar:
                tipoPeticion = "put";
                menuOk.setVisible(true);
                setTitle("Editar");
                habilitarComponentes(true);
                break;
            case android.R.id.home:
                if (edtNumero.isEnabled()) {
                    regresar();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void insertar() {
        // Extraer datos de UI
        String imei = edtImei.getText().toString();
        String telefono = edtNumero.getText().toString();
        String descripcion = edtDescripcion.getText().toString();
        String empresaPerteneciente = edtEmpresaPerteneciente.getText().toString();
        String autorrastreo = "notn";
//        if (cbx_Deshabilitado.isChecked())
//            autorrastreo = "nofix";
//        else
//            autorrastreo = "calcule";

        // Validaciones
        if (!esNombreValido(telefono)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_gps_numero);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            mostrarProgreso(true);
            String[] columnasFiltro = {Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO
                    , Configuracion.COLUMNA_GPS_DESCRIPCION};
            telefono = telefono.replaceAll("\\s", "");
            telefono = telefono.replaceAll("-", "");
            if (telefono.substring(0, 1).equalsIgnoreCase("+")) {
                telefono = telefono.substring(3);
            }
            String[] valorFiltro = {imei, telefono, descripcion};
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false);
            if (ID.isEmpty()) {
                resultado.execute(Configuracion.PETICION_GPS_REGISTRO, tipoPeticion);
            } else {
                resultado.execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR + ID, tipoPeticion);
            }
        }
    }

    private boolean esNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre);
    }

    // || CHECAR
//    private void preparaEliminacion() {
//        if (idGps != null) {
//            //Datos de busqueda
//            String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID};
//            String[] valorFiltro = {idGps};
//
//            //Datos a mostrar
//            String[] columnasArecuperar = {
//                    Configuracion.COLUMNA_ENLACE_AUX_TELEFONO_USUARIO,
//                    Configuracion.COLUMNA_ENLACE_AUX_TELEFONO_GPS
//            };
//
//            mostrarProgreso(true);
//            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_ENLACE_ELIMINAR_TELEFONOS_ENLAZADOS, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, columnasArecuperar, true);
//            resultado.execute(Configuracion.PETICION_ENLACE_LISTAR_NUMEROS_A_ELIMINAR, "post");
//        }
////        eliminar(true);
//    }

//    private void procesarNumObtenidos(ArrayList datos) {
//        int x = (int) datos.size();
//        int y = ((ArrayList) datos).size();
//        String[][] matrizTelefonos = new String[x][y];
//        if (datos.size() > 0) {
//            for (int i = 0; i < datos.size() - 1; i++) {
////            datosEnLista.add((String) ((ArrayList) datos.get(i)).get(1));
//
//                for (int j = 0; i < ((ArrayList) datos).size(); j++) {
//                    matrizTelefonos[i][j] = (String) ((ArrayList) datos.get(i)).get(j);
//                }
//            }
//            desvincular(matrizTelefonos);
//        }
//    }


    //||
//    private void desvincular(String[][] telefonos) {
//        Mensajes sms = new Mensajes(this, telefonos.length);
//
//        for (int i = 0; i < telefonos.length; i++) {
////            sms.enviarSMSDesvincularUsuario(telefonos[i][0], telefonos[i][1]);
//            Log.v(Utilidades.TAGLOG,telefonos[i][0] + " - " + telefonos[i][1]);
//        }
//    }

//    public void eliminar(boolean smsEnviados) {
//        if (smsEnviados)
//            Log.v("AGET-ELIMINADO", "SI Eliminado");
//        else
//            Log.v("AGET-ELIMINADO", "NO Eliminado");
//
////NOTA: LAS DOS LINEAS COMENTADAS DEBEN DE EJECUTARCE DEPUES DE MANDAS EL MENSAJE A LOS NUMEROS
//        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, null, null, Configuracion.TABLA_GPS, null, false);
//        resultado.execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR + ID, tipoPeticion);
//    }

    //||
    public void eliminar() {
        Mensajes sms = new Mensajes(this, 1);
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, null, null, Configuracion.TABLA_GPS, null, false);
        resultado.execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR + ID, tipoPeticion);
    }


    //||
    public void vincular() {
        String telefono = edtNumero.getText().toString();
        telefono = telefono.replaceAll("\\s", "");
        telefono = telefono.replaceAll("-", "");
        if (telefono.substring(0, 1).equalsIgnoreCase("+")) {
            telefono = telefono.substring(3);
        }

        new Mensajes(this, 1).enviarSMSAgregarNuevoGPS("7471212313", telefono);


    }


    void regresar() {
        if (idGps != null) {
            if (data != null) {
                edtImei.setText(String.valueOf(data.get(0)));
                edtNumero.setText(String.valueOf(data.get(1)));
                edtDescripcion.setText(String.valueOf(data.get(2)));
//                autorrastreoBD = String.valueOf(data.get(3));
                edtEmpresaPerteneciente.setText(String.valueOf(data.get(4)));
            }


            habilitarComponentes(false);

            //setTitle(String.valueOf(data.get(0)));
            menuOk.setVisible(false);
        } else {
            finish();
        }
    }

    private void cargarViews(ArrayList data) {
        this.data = data;
        if (data.size() < 0) {
            return;
        }
        String[] datos = ControlGps.desmantarAutotrack(String.valueOf(data.get(3)));

        // Asignar valores a UI
        edtImei.setText(String.valueOf(data.get(0)));
        edtNumero.setText(String.valueOf(data.get(1)));
        edtDescripcion.setText(String.valueOf(data.get(2)));
//        autorrastreoBD = String.valueOf(data.get(3));
        if (String.valueOf(data.get(4)).equalsIgnoreCase("null"))
            edtEmpresaPerteneciente.setText("No ha sido asignado");
//        if (String.valueOf(data.get(3)).equalsIgnoreCase("0")) {
//            spinner.setSelection(1);
//            fab_usuarios.setVisibility(View.GONE);
//            fab_gps.setVisibility(View.GONE);
//        } else {
//            spinner.setSelection(0);
//            fab_usuarios.setVisibility(View.VISIBLE);
//            fab_gps.setVisibility(View.VISIBLE);
//        }
        ID = String.valueOf(data.get(data.size() - 1));

        habilitarComponentes(false);

        setTitle(String.valueOf(data.get(2)));
    }

    void habilitarComponentes(Boolean habilitado) {
        edtImei.setEnabled(habilitado);
        edtNumero.setEnabled(habilitado);
        edtDescripcion.setEnabled(habilitado);

//        cbx_Deshabilitado.setEnabled(false);
        edtEmpresaPerteneciente.setEnabled(false);

//        if (autorrastreoBD.equalsIgnoreCase(rastreoAnulado)){
//            cbx_Deshabilitado.setChecked(true);
//            spinnerTiempo.setEnabled(false);
//            seekBarCantidad.setEnabled(false);
//            seekBarTiempo.setEnabled(false);
//        }
    }


    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

}

/*
                tiempo[0] = textResultadoSeekBarTiempo.getText().toString();
                cantidad[0] = textResultadoSeekBarCantidad.getText().toString();
                String aux = "";
                if (tiempo[0].length() == 1) {
                    aux = tiempo[0];
                    tiempo[0] = "00" + aux;
                } else if (tiempo[0].length() == 2) {
                    aux = tiempo[0];
                    tiempo[0] = "0" + aux;
                }

                if (cantidad[0].length() == 1) {
                    aux = cantidad[0];
                    cantidad[0] = "00" + aux;
                } else if (cantidad[0].length() == 2) {
                    aux = cantidad[0];
                    cantidad[0] = "0" + aux;
                }
                cmdGeoPosicionIntervalosAutoTrack = "t" + tiempo[0] + tipoTiempo + cantidad[0] + "n" + password;

*/