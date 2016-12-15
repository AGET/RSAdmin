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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Mensajes;
import com.aldo.aget.rsadmin.Control.SQLHelper;
import com.aldo.aget.rsadmin.Modelo.ManagerDB;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GpsDepartamentoDetalle extends AppCompatActivity {

    ProgressBar progressBar;

    MenuItem menuEditar, menuDesvincular;

    EditText edtImei, edtTelefono, edtDescripcion;

    String idEmpresa, empresaNombre, empresaStatus, idDepartamento, departamentoNombre, id, imei,
            telefono, descripcion, autorastreo, departamento_id;


    String tipoPeticion = "";

    String comandoAutotrack;

    BroadcastReceiver receptorMensaje, receptorEnlace;

    //INICIO AUTOTRACK

    //Autotrack
    CheckBox cbx_habilitado;
    Spinner spinnerTiempo;
    SeekBar seekBarTiempo;
    TextView textResultadoSeekBarTiempo;
    SeekBar seekBarCantidad;
    TextView textResultadoSeekBarCantidad;
    SpinnerAdapter adaptadorSpinner;
    int tiempoMaximo = 0;
    String tipoTiempo = "";
    String autorrastreoBD = "";
    String rastreoAnulado = "nofix";

    //FIN AUTOTRACK

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_departamento_detalle);
        agregarToolbar();
        setTitle("Detalle del Gps");

        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);
        empresaNombre = bundle.getString(Configuracion.COLUMNA_EMPRESA_NOMBRE);
        empresaStatus = bundle.getString(Configuracion.COLUMNA_EMPRESA_STATUS);
        idDepartamento = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);
        id = bundle.getString(Configuracion.COLUMNA_GPS_ID);
        imei = bundle.getString(Configuracion.COLUMNA_GPS_IMEI);
        telefono = bundle.getString(Configuracion.COLUMNA_GPS_NUMERO);
        descripcion = bundle.getString(Configuracion.COLUMNA_GPS_DESCRIPCION);
        autorastreo = bundle.getString(Configuracion.COLUMNA_GPS_AUTORASTREO);
        departamento_id = bundle.getString(Configuracion.COLUMNA_GPS_DEPARTAMENTO);


        edtImei = (EditText) findViewById(R.id.edt_imei_gps);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_gps);
        edtDescripcion = (EditText) findViewById(R.id.edt_descripcion_gps);

        progressBar = (ProgressBar) findViewById(R.id.barra);

        cargarComponentes();
        habilitarComponentes(false);

        //INICIO AUTOTRACK

        cbx_habilitado = (CheckBox) findViewById(R.id.cbx_rastreo_auto);
        spinnerTiempo = (Spinner) findViewById(R.id.spinnerTiempo);

        seekBarTiempo = (SeekBar) findViewById(R.id.seekBarTiempo);
        textResultadoSeekBarTiempo = (TextView) findViewById(R.id.textResultadoSeekBarTiempo);


        seekBarCantidad = (SeekBar) findViewById(R.id.seekBarCantidad);
        textResultadoSeekBarCantidad = (TextView) findViewById(R.id.textResultadoSeekBarCantidad);

        seekBarCantidad.setEnabled(false);
        seekBarTiempo.setEnabled(false);

        mostrarAutotrack();

        if(cbx_habilitado.isChecked()){
            spinnerTiempo.setEnabled(false);
        }else{
            spinnerTiempo.setEnabled(true);
        }

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
                        seekBarTiempo.setEnabled(true);
                        seekBarCantidad.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

            public void establecer(String s) {
                if (s.equalsIgnoreCase("Segundos") || s.equalsIgnoreCase("s")) {
                    tiempoMaximo = 59;
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

        adaptadorSpinner = new SpinnerAdapter(GpsDepartamentoDetalle.this, android.R.layout.simple_list_item_1);
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

        cbx_habilitado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbx_habilitado.isChecked()) {
                    if (tipoTiempo == null || tipoTiempo == "") {
                        mensaje("Establesca los datos");
                        cbx_habilitado.setChecked(false);
                    } else {
                        spinnerTiempo.setEnabled(false);
                        seekBarTiempo.setEnabled(false);
                        seekBarCantidad.setEnabled(false);

                        spinnerTiempo.setEnabled(false);

                        enviarAutorrastreo(true);
                    }

                } else {
                    spinnerTiempo.setEnabled(true);

                    enviarAutorrastreo(false);
                    tipoTiempo = "";
                    textResultadoSeekBarCantidad.setText("");
                    textResultadoSeekBarTiempo.setText("");
                    spinnerTiempo.setEnabled(true);
//                    seekBarTiempo.setEnabled(true);
//                    seekBarCantidad.setEnabled(true);
                }
            }
        });

        //FIN AUTOTRACK


        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean MostrarMensaje = true;
                Log.v("AGET", "BROAD RECIBIDO GPS");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
//                    cargarViews(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                } else if (mensaje.equalsIgnoreCase("Registro con exito!")) {

                } else if (mensaje.equalsIgnoreCase("Registro actualizado correctamente")) {

                    peticionEliminarEnlaces();
//                    Handler handler = new Handler();
//                    handler.postDelayed(new Runnable() {
//                        public void run() {
//                            finish();
//                        }
//                    }, 2000);
//                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Url mal formada")) {
                    mensaje = "error en dato, probablemente con ID";

                } else if (mensaje.equalsIgnoreCase("El GPS al que intentas acceder no existe")) {
                    mensaje = "Revise los datos, puede no haber modificacion";

                } else if (mensaje.equalsIgnoreCase("Desvinculado correctamente")) {
                    Snackbar.make(findViewById(R.id.actividad_gps_departamento_detalle),
                            mensaje, Snackbar.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    Configuracion.cambio = true;

                } else if (mensaje.equalsIgnoreCase("El enlace al que intenta acceder no existe")) {
                    Snackbar.make(findViewById(R.id.actividad_gps_departamento_detalle),
                            "Desvinculado", Snackbar.LENGTH_SHORT).show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Registro eliminado correctamente")) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            finish();
                        }
                    }, 2000);
                    Configuracion.cambio = true;

                } else if (mensaje.equalsIgnoreCase("Autorrastreo establecido correctamente")) {
                    mostrarProgreso(false);
//                    Se envia el comando SMS al gps
                    new Mensajes(context, 1).enviarSMSEstablecerIntervalo(telefono, comandoAutotrack);
                    Log.v(Utilidades.TAGLOG, "SMS_SEND:" + telefono + ": " + comandoAutotrack);

//                    Se envia peticion para obtener enlaces
                    peticionObtenerEnlaces();
//                    guardarConfiguracionAutorrsatreo();
                } else if (mensaje.equalsIgnoreCase("No asignado")) {
                    mensaje("Problema al registrar el autorrastreo");
                } else if (mensaje.equalsIgnoreCase("Error probablemente no se recibio el dato")) {
                    mensaje("Error, no se enviaron los datos de autorastreo");
                }
                if (mensaje.equalsIgnoreCase("Cargado")) {
                    return;
                }

                mostrarProgreso(false);
            }
        };

        receptorEnlace = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean MostrarMensaje = true;
                Log.v("AGET", "RECEPTOR ENLACES");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    guardarConfiguracionAutorrsatreo(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                } else {
                    mensaje("No se encontraron usuarios enlazados");
                }

                if (mensaje.equalsIgnoreCase("Cargado")) {
                    return;
                }
            }
        };

    }


    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GPS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);

        IntentFilter filtro2 = new IntentFilter(Configuracion.INTENT_ENLACE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorEnlace, filtro2);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorEnlace);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gps_departamento_detalle, menu);
        this.menuDesvincular = menu.findItem(R.id.accion_desvincular);
        this.menuEditar = menu.findItem(R.id.accion_editar);
        // Verificación de visibilidad acción eliminar

        menuEditar.setVisible(false);

//            menuEditar.setVisible(true);
//            menuEliminar.setVisible(true);
//            menuOk.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.accion_editar:
                tipoPeticion = "put";
                setTitle("Editar");
                habilitarComponentes(true);
                //insertar();
                break;
            case R.id.accion_desvincular:
                peticionDesvincular();
                break;
            case android.R.id.home:
                if (edtTelefono.isEnabled()) {
                    cargarComponentes();
                    habilitarComponentes(false);
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void cargarComponentes() {
        edtImei.setText(imei);
        edtTelefono.setText(telefono);
        edtDescripcion.setText(descripcion);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    private void guardarConfiguracionAutorrsatreo(ArrayList datos) {
        ManagerDB db = new ManagerDB(this);

        if (!db.existeDato(SQLHelper.TABLA_EMPRESA_CLIENTE, SQLHelper.COLUMNA_EMPRESA_ID_REMOTO, idEmpresa)) {
            db.insercionMultiple(SQLHelper.TABLA_EMPRESA_CLIENTE, new String[]{
                            SQLHelper.COLUMNA_EMPRESA_ID_REMOTO, SQLHelper.COLUMNA_EMPRESA_NOMBRE, SQLHelper.COLUMNA_EMPRESA_STATUS},
                    new String[]{idEmpresa, empresaNombre, empresaStatus});
        }

        if (!db.existeDato(SQLHelper.TABLA_DEPARTAMENTO, SQLHelper.COLUMNA_DEPARTAMENTO_ID_REMOTO, idDepartamento)) {
            db.insercionMultiple(SQLHelper.TABLA_DEPARTAMENTO, new String[]{SQLHelper.COLUMNA_DEPARTAMENTO_ID_REMOTO, SQLHelper.COLUMNA_DEPARTAMENTO_NOMBRE},
                    new String[]{idDepartamento, departamentoNombre});
        }

        if (!db.existeDato(SQLHelper.TABLA_GPS, SQLHelper.COLUMNA_GPS_ID_REMOTO, id)) {
            db.insercionMultiple(SQLHelper.TABLA_GPS, new String[]{SQLHelper.COLUMNA_GPS_ID_REMOTO, SQLHelper.COLUMNA_GPS_IMEI, SQLHelper.COLUMNA_GPS_TELEFONO, SQLHelper.COLUMNA_GPS_DESCRIPCION,
                    SQLHelper.COLUMNA_GPS_AUTORASTREO, SQLHelper.COLUMNA_GPS_DEPARTAMENTO_ID}, new String[]{id, imei, telefono, descripcion, autorastreo, departamento_id});
        }

        if (datos.size() > 0) {

            for (int i = 0; i < datos.size() - 1; i++) {
                String valor = (String) ((ArrayList) datos.get(i)).get(0);
                if (!db.existeDato(SQLHelper.TABLA_ENLACE, SQLHelper.COLUMNA_ENLACE_ID_REMOTO, valor)) {

                    String enlace_id = (String) ((ArrayList) datos.get(i)).get(0);
                    String usuario_id = (String) ((ArrayList) datos.get(i)).get(1);
                    String gps_id = (String) ((ArrayList) datos.get(i)).get(2);
                    String nombre = (String) ((ArrayList) datos.get(i)).get(3);
                    String ap_paterno = (String) ((ArrayList) datos.get(i)).get(4);
                    String ap_materno = (String) ((ArrayList) datos.get(i)).get(5);
                    db.insercionMultiple(SQLHelper.TABLA_ENLACE, new String[]{SQLHelper.COLUMNA_ENLACE_ID_REMOTO, SQLHelper.COLUMNA_ENLACE_USUARIO_ID,
                            SQLHelper.COLUMNA_ENLACE_GPS_ID, SQLHelper.COLUMNA_ENLACE_USUARIO_NOMBRE, SQLHelper.COLUMNA_ENLACE_USUARIO_AP_PATERNO,
                            SQLHelper.COLUMNA_ENLACE_USUARIO_AP_MATERNO}, new String[]{
                            enlace_id, usuario_id, gps_id, nombre, ap_paterno, ap_paterno
                    });

                    //todo configurado y datos guardados ...
                }
            }
            mensaje("La configuracion de autorrastreo ha sido exitosa!");
        } else {
            mensaje("Gps sin enlaces");
        }


//        idEmpresa
//        empresaNombre
//        empresaStatus
//        idDepartamento
//        departamentoNombre
//        id
//        imei
//        telefono
//        descripcion
//        autorastreo
//        departamento_id

    }

    void habilitarComponentes(Boolean habilitado) {
        edtImei.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtDescripcion.setEnabled(habilitado);
    }

    public void peticionDesvincular() {
        mostrarProgreso(true);
        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO
                , Configuracion.COLUMNA_GPS_DESCRIPCION};
        String[] valorFiltro = {imei, telefono, descripcion};
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                .execute(Configuracion.PETICION_GPS_MODIFICAR_ELIMINAR + id, "put");
        enviarAutorrastreo(false);
    }

    public void peticionEliminarEnlaces() {
        mostrarProgreso(true);
        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID};
        String[] valorFiltro = {id};
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                .execute(Configuracion.PETICION_GPS_DESVINCULAR, "post");
    }

    private void peticionObtenerEnlaces() {
        mostrarProgreso(true);
        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID};
        String[] valorFiltro = {id};
        String[] columnasRecuperar = {Configuracion.COLUMNA_ENLACE_ID, Configuracion.COLUMNA_ENLACE_USUARIO,
                Configuracion.COLUMNA_ENLACE_GPS, Configuracion.COLUMNA_USUARIO_NOMBRE, Configuracion.COLUMNA_USUARIO_AP_PATERNO,
                Configuracion.COLUMNA_USUARIO_AP_MATERNO};
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_ENLACE, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, columnasRecuperar, true)
                .execute(Configuracion.PETICION_ENLACE_LISTAR_BASE_GPS, "post");
    }

    public void enviarAutorrastreo(boolean autorrastreo) {
        String tiempo = "";
        String cantidad = "";
        if (autorrastreo) {
            tiempo = textResultadoSeekBarTiempo.getText().toString();
            if (Integer.parseInt(tiempo) < 10) {
                tiempo = "00" + tiempo;
            } else if (Integer.parseInt(tiempo) < 100) {
                tiempo = "0" + tiempo;
            }

            cantidad = textResultadoSeekBarCantidad.getText().toString();
            if (Integer.parseInt(cantidad) < 10) {
                cantidad = "00" + cantidad;
            } else if (Integer.parseInt(cantidad) < 100) {
                cantidad = "0" + cantidad;
            }

            comandoAutotrack = "fix" + tiempo + tipoTiempo + cantidad + "n123456";
            Log.v("AGET-AUTOTRACK", comandoAutotrack);
            mensaje(comandoAutotrack);
        } else {
            comandoAutotrack = "nofix123456";
            mensaje(comandoAutotrack);
            Log.v("AGET-AUTOTRACK", comandoAutotrack);
        }

        mostrarProgreso(true);
        String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_GPS_AUTORASTREO};
        String[] valorFiltro = {id, comandoAutotrack};
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, null, false)
                .execute(Configuracion.PETICION_GPS_ESTABLECER_AUTORRASTREO, "post");

    }

    public void mensaje(String mensaje) {
        Snackbar.make(findViewById(R.id.actividad_gps_departamento_detalle),
                mensaje, Snackbar.LENGTH_SHORT).show();
    }

    public void mostrarAutotrack() {
        if (autorastreo.equalsIgnoreCase("notn") || autorastreo.equalsIgnoreCase("notn123456")
                || autorastreo.equalsIgnoreCase("nofix") || autorastreo.equalsIgnoreCase("nofix123456")) {
            seekBarCantidad.setProgress(0);
            textResultadoSeekBarCantidad.setText("");

            seekBarTiempo.setProgress(0);
            textResultadoSeekBarTiempo.setText("");
        } else {
            cbx_habilitado.setChecked(true);
            String tiempo = "", cantidad = "", tipoTiempo = "";
            tiempo = autorastreo.substring(3, 5);
            tipoTiempo = autorastreo.substring(5, 6);
            cantidad = autorastreo.substring(7, 9);

            Log.v("AGET-AUTOTRACK", "tiempo: " + tiempo);
            Log.v("AGET-AUTOTRACK", "cantidad: " + cantidad);
            Log.v("AGET-AUTOTRACK", "tipoTiempo: " + tipoTiempo);
            if (tipoTiempo.equalsIgnoreCase("h")) {
                seekBarTiempo.setMax(59);
            } else if (tipoTiempo.equalsIgnoreCase("m")) {
                seekBarTiempo.setMax(59);
            } else if (tipoTiempo.equalsIgnoreCase("h")) {
                seekBarTiempo.setMax(23);
            }
            seekBarTiempo.setProgress(Integer.parseInt(tiempo));
            textResultadoSeekBarTiempo.setText("" + Integer.parseInt(tiempo));
            seekBarCantidad.setProgress(Integer.parseInt(cantidad));
            textResultadoSeekBarCantidad.setText("" + Integer.parseInt(cantidad));
        }
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }
}