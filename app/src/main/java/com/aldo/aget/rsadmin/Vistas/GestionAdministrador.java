package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;

public class GestionAdministrador extends AppCompatActivity {


    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    BroadcastReceiver receptorMensaje;

    ArrayList data;

    MenuItem menuOk, menuEditar, menuEliminar;

//    TextInputLayout mcr_clave;


    EditText edtNombre, edtApPaterno, edtApMaterno, edtTelefono, edtCorreo, edtClave;

    String idAdministrador = "1";


    boolean datosRecibidos = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_administrador);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Administrador");

        edtNombre = (EditText) findViewById(R.id.edt_nombre_administrador);
        edtApPaterno = (EditText) findViewById(R.id.edt_apellido_paterno_administrador);
        edtApMaterno = (EditText) findViewById(R.id.edt_apellido_materno_administrador);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_administrador);
        edtCorreo = (EditText) findViewById(R.id.edt_correo_administrador);
        edtClave = (EditText) findViewById(R.id.edt_pass_administrador);

//        mcr_clave = (TextInputLayout) findViewById(R.id.mcr_edt_pass_usuario);

        progressBar = (ProgressBar) findViewById(R.id.barra);


        mostrarProgreso(true);

        peticionDatos();

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean MostrarMensaje = true;
                Log.v("AGET", "BROAD RECIBIDO GTN-Usuarios");
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
                    menuEliminar.setVisible(false);
                    habilitarComponentes(false);
                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Url mal formada")) {
                    mensaje = "error en dato, probablemente con ID";

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

                Snackbar.make(findViewById(R.id.xml_gestion_administrador),
                        mensaje, Snackbar.LENGTH_SHORT).show();
                mostrarProgreso(false);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor datos
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GESTION_ADMINISTRADOR);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insercion_administrador, menu);
        this.menuEliminar = menu.findItem(R.id.accion_eliminar);
        this.menuEditar = menu.findItem(R.id.accion_editar);
        this.menuOk = menu.findItem(R.id.accion_confirmar);
        // Verificación de visibilidad acción eliminar

        menuEditar.setVisible(true);
        menuEliminar.setVisible(false);
        menuOk.setVisible(false);

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

                break;
            case R.id.accion_editar:
                tipoPeticion = "put";
                menuOk.setVisible(true);
                menuEditar.setVisible(false);
                setTitle("Editar");
                habilitarComponentes(true);
                //insertar();
                break;
            case android.R.id.home:
                if (edtNombre.isEnabled()) {
                    regresar();
                } else {
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void peticionDatos() {
        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_ADMINISTRADOR_ID};
        String[] valorFiltro = {"1"};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_ADMINISTRADOR_NOMBRE,
                Configuracion.COLUMNA_ADMINISTRADOR_AP_PATERNO,
                Configuracion.COLUMNA_ADMINISTRADOR_AP_MATERNO,
                Configuracion.COLUMNA_ADMINISTRADOR_TELEFONO,
                Configuracion.COLUMNA_ADMINISTRADOR_CORREO,
                Configuracion.COLUMNA_ADMINISTRADOR_CONTRASE_NA,
                Configuracion.COLUMNA_ADMINISTRADOR_ID};

        //Datos
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_ADMINISTRADOR, columnasFiltro,
                valorFiltro, Configuracion.TABLA_ADMINISTRADOR, columnasArecuperar, false)
                .execute(Configuracion.PETICION_ADMINISTRADOR_LISTAR_UNO, tipoPeticion);

        Log.v("AGET-Recuparar", "PeticionDatos:" + columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);
    }

    public void eliminar(boolean smsEnviados) {
        if (smsEnviados)
            Log.v("AGET-ELIMINADO", "SI Eliminado");
        else
            Log.v("AGET-ELIMINADO", "NO Eliminado");

//NOTA: LAS DOS LINEAS COMENTADAS DEBEN DE EJECUTARCE DEPUES DE MANDAS EL MENSAJE A LOS NUMEROS
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_ADMINISTRADOR, null, null,
                Configuracion.TABLA_ADMINISTRADOR, null, false)
                .execute(Configuracion.PETICION_ADMINISTRADOR_MODIFICAR_ELIMINAR + idAdministrador, "delete");
    }

    private void insertar() {
        // Extraer datos de UI
        String nombre = edtNombre.getText().toString();
        String apPaterno = edtApPaterno.getText().toString();
        String apMaterno = edtApMaterno.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String correo = edtCorreo.getText().toString();
        String clave = edtClave.getText().toString();


        // Validaciones
        if (!esNombreValido(nombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_nombre_administrador);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            String[] columnasFiltro = {
                    Configuracion.COLUMNA_ADMINISTRADOR_NOMBRE,
                    Configuracion.COLUMNA_ADMINISTRADOR_AP_PATERNO,
                    Configuracion.COLUMNA_ADMINISTRADOR_AP_MATERNO,
                    Configuracion.COLUMNA_ADMINISTRADOR_TELEFONO,
                    Configuracion.COLUMNA_ADMINISTRADOR_CORREO,
                    Configuracion.COLUMNA_ADMINISTRADOR_CONTRASE_NA
            };

            if (!esNombreValido(correo)) {
                TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_correo_administrador);
                mascaraCampoNombre.setError("Este campo no puede quedar vacío");
            } else {
                mostrarProgreso(true);
                String[] valorFiltro = {nombre, apPaterno, apMaterno, telefono, correo, clave};
                for(int i = 0 ; i<valorFiltro.length;i++){
                    Log.v("VE","valor: "+valorFiltro[i]);
                }
                new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_ADMINISTRADOR,
                        columnasFiltro, valorFiltro, Configuracion.TABLA_ADMINISTRADOR, null, false)
                        .execute(Configuracion.PETICION_ADMINISTRADOR_MODIFICAR_ELIMINAR + idAdministrador, tipoPeticion);
                Log.v("AGET-ID", "Editar/eliminar:" + idAdministrador);
            }
        }
    }

    private boolean esNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre);
    }

    private void cargarViews(ArrayList data) {
        this.data = data;
        if (data.size() < 0) {
            return;
        }

        edtNombre.setText(String.valueOf(data.get(0)));
        edtApPaterno.setText(String.valueOf(data.get(1)));
        edtApMaterno.setText(String.valueOf(data.get(2)));
        edtTelefono.setText(String.valueOf(data.get(3)));
        edtCorreo.setText(String.valueOf(data.get(4)));
        edtClave.setText(String.valueOf(data.get(5)));

        habilitarComponentes(false);

        setTitle("Administrador " + String.valueOf(data.get(0)));
    }

    void habilitarComponentes(Boolean habilitado) {

        edtNombre.setEnabled(habilitado);
        edtApPaterno.setEnabled(habilitado);
        edtApMaterno.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtCorreo.setEnabled(habilitado);
        edtClave.setEnabled(habilitado);
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    void regresar() {

        edtNombre.setText(String.valueOf(data.get(0)));
        edtApPaterno.setText(String.valueOf(data.get(1)));
        edtApMaterno.setText(String.valueOf(data.get(2)));
        edtTelefono.setText(String.valueOf(data.get(3)));
        edtCorreo.setText(String.valueOf(data.get(4)));
        edtClave.setText(String.valueOf(data.get(5)));


        habilitarComponentes(false);

        setTitle("Administrador " + String.valueOf(data.get(0)));
        menuOk.setVisible(false);


    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

}
