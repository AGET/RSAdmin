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
import android.widget.EditText;
import android.widget.ProgressBar;

import org.json.JSONObject;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Mensajes;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultado;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;


public class Gps extends AppCompatActivity {

    EditText edtImei, edtNumero, edtDescripcion, edtEmpresaPerteneciente;

    String idGps;

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    BroadcastReceiver receptorMensaje;

    MenuItem menuOk, menuEditar, menuEliminar;

    JSONObject json;

    String tipoPeticion = "post";

    String ID = "";

    ArrayList data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);
        agregarToolbar();
        Bundle bundle = getIntent().getExtras();
        idGps = bundle.getString(Configuracion.COLUMNA_GPS_ID);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

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
                Configuracion.COLUMNA_GPS_ID,
                Configuracion.COLUMNA_GPS_IMEI,
                Configuracion.COLUMNA_GPS_NUMERO,
                Configuracion.COLUMNA_GPS_DESCRIPCION,
                Configuracion.COLUMNA_GPS_EMPRESA};

        if (idGps != null) {
            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, columnasFiltro, valorFiltro, Configuracion.TABLA_GPS, columnasArecuperar, false);
            resultado.execute(Configuracion.PETICION_GPS_LISTAR_UNO, tipoPeticion);
        } else {
            setTitle(R.string.titulo_actividad_agregar_gps);
            edtEmpresaPerteneciente.setEnabled(false);
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
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GPS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
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
                prparaEliminacion();
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
        // Validaciones
        if (!esNombreValido(telefono)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_gps_numero);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            mostrarProgreso(true);
            String[] columnasFiltro = {Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO
                    , Configuracion.COLUMNA_GPS_DESCRIPCION, Configuracion.COLUMNA_GPS_EMPRESA};
            String[] valorFiltro = {imei, telefono, descripcion, empresaPerteneciente};
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
    private void prparaEliminacion() {
//        if (idGps != null) {
//            //Datos de busqueda
//            String[] columnasFiltro = {Configuracion.COLUMNA_GPS_ID};
//            String[] valorFiltro = {idGps};
//
//            //Datos a mostrar
//            String[] columnasArecuperar = {
//                    Configuracion.COLUMNA_USUARIO_TELEFONO,
//                    Configuracion.COLUMNA_GPS_NUMERO
//            };
//
//            mostrarProgreso(true);
//            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_EMPRESA_CLIENTE_ENLACE_TELEFONOS_ENLAZADOS, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, columnasArecuperar, true);
//            resultado.execute(Configuracion.PETICION_ENLACE_LISTAR_TELEFONOS, "post");
//        }
        eliminar(true);
    }


    public void eliminar(boolean smsEnviados){
        if(smsEnviados)
            Log.v("AGET-ELIMINADO","SI Eliminado");
        else
            Log.v("AGET-ELIMINADO","NO Eliminado");

//NOTA: LAS DOS LINEAS COMENTADAS DEBEN DE EJECUTARCE DEPUES DE MANDAS EL MENSAJE A LOS NUMEROS
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GPS, null, null, Configuracion.TABLA_GPS, null, false);
        resultado.execute(Configuracion.PETICION_EMPRESA_MODIFICAR_ELIMINAR + ID, tipoPeticion);
    }
    //CHECAR ||

//    private void desvincular(String[][] telefonos) {
//        Mensajes sms = new Mensajes(this, telefonos.length);
//
//        for (int i = 0; i < telefonos.length; i++) {
//            sms.enviarSMSDesvincularUsuario(telefonos[i][0], telefonos[i][1]);
//        }
//
//    }


    void regresar() {
        if (idGps != null) {
            edtImei.setText(String.valueOf(data.get(1)));
            edtNumero.setText(String.valueOf(data.get(2)));
            edtDescripcion.setText(String.valueOf(data.get(3)));
            edtEmpresaPerteneciente.setText(String.valueOf(data.get(4)));

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
        // Asignar valores a UI
        edtImei.setText(String.valueOf(data.get(1)));
        edtNumero.setText(String.valueOf(data.get(2)));
        edtDescripcion.setText(String.valueOf(data.get(3)));
        edtEmpresaPerteneciente.setText(String.valueOf(data.get(4)));
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
        edtEmpresaPerteneciente.setEnabled(false);
    }


    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

}
