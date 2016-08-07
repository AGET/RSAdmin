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
import android.widget.TextView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;

public class GestionUsuarios extends AppCompatActivity {

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    String tabla = Configuracion.TABLA_USUARIOS;

    BroadcastReceiver receptorMensaje;

    ArrayList data;

    MenuItem menuOk, menuEditar, menuEliminar;

    EditText edtNombre, edtApPaterno, edtApMaterno, edtTelefono, edtCorreo, edtUsuario, edtClave;

    String idUsuario="", nombreUsuario = "", departamentoId = "", departamentoNombre = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_usuarios);
        agregarToolbar();

        Bundle bundle = getIntent().getExtras();
        idUsuario = bundle.getString(Configuracion.COLUMNA_USUARIO_ID);
        nombreUsuario = bundle.getString(Configuracion.COLUMNA_USUARIO_NOMBRE);
        departamentoId = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);
        setTitle("Usuarios " + departamentoNombre);

        edtNombre = (EditText) findViewById(R.id.edt_nombre_usuario);
        edtApPaterno = (EditText) findViewById(R.id.edt_apellido_paterno);
        edtApMaterno = (EditText) findViewById(R.id.edt_apellido_materno);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_usuario);
        edtCorreo = (EditText) findViewById(R.id.edt_correo_usuario);
        edtUsuario = (EditText) findViewById(R.id.edt_usuario);
        edtClave = (EditText) findViewById(R.id.edt_pass_usuario);

        progressBar = (ProgressBar) findViewById(R.id.barra);

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_ID};
        String[] valorFiltro = {idUsuario};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_USUARIO_NOMBRE,
                Configuracion.COLUMNA_USUARIO_AP_PATERNO,
                Configuracion.COLUMNA_USUARIO_AP_MATERNO,
                Configuracion.COLUMNA_USUARIO_TELEFONO,
                Configuracion.COLUMNA_USUARIO_CORREO,
                Configuracion.COLUMNA_USUARIO_USUARIO,
                Configuracion.COLUMNA_USUARIO_CONTRASE_NA,
                Configuracion.COLUMNA_USUARIO_DEPARTAMENTO_ID,
                Configuracion.COLUMNA_USUARIO_ID};

        if (idUsuario != null) {
            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, columnasArecuperar, false);
            resultado.execute(Configuracion.PETICION_USUARIO_LISTAR_UNO, tipoPeticion);
        } else {
            setTitle(R.string.titulo_actividad_agregar_departamento);
        }

        Log.v("AGET", columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);

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
                    menuEliminar.setVisible(true);
                    habilitarComponentes(false);
                    Configuracion.cambio = true;
                } else if (mensaje.equalsIgnoreCase("Url mal formada")) {
                    mensaje = "error en dato, probablemente con ID";
                } else if (mensaje.equalsIgnoreCase("La empresa a la que intentas acceder no existe")) {
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

                Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
                        mensaje, Snackbar.LENGTH_SHORT).show();
                mostrarProgreso(false);
            }
        };


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);

        //IntentFilter filtroTelefonos = new IntentFilter(Configuracion.INTENT_EMPRESA_CLIENTE_ENLACE_TELEFONOS_ENLAZADOS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insercion_usuario, menu);
        this.menuEliminar = menu.findItem(R.id.accion_eliminar);
        this.menuEditar = menu.findItem(R.id.accion_editar);
        this.menuOk = menu.findItem(R.id.accion_confirmar);
        // Verificación de visibilidad acción eliminar
        if (idUsuario != null) {
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
                preparaEliminacion();
                break;
            case R.id.accion_editar:
                tipoPeticion = "put";
                menuOk.setVisible(true);
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

    private void insertar() {
        // Extraer datos de UI
        String nombre = edtNombre.getText().toString();
        String apPaterno = edtApPaterno.getText().toString();
        String apMaterno = edtApMaterno.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String correo = edtCorreo.getText().toString();
        String usuario = edtUsuario.getText().toString();
        String clave = edtClave.getText().toString();


        // Validaciones
        if (!esNombreValido(nombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_nombre_departamento);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            mostrarProgreso(true);
            String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_NOMBRE, Configuracion.COLUMNA_USUARIO_AP_PATERNO
                    , Configuracion.COLUMNA_USUARIO_AP_MATERNO, Configuracion.COLUMNA_USUARIO_TELEFONO,
                    Configuracion.COLUMNA_USUARIO_CORREO, Configuracion.COLUMNA_USUARIO_USUARIO,Configuracion.COLUMNA_USUARIO_CONTRASE_NA};
            String[] valorFiltro = {nombre, apPaterno, apMaterno, telefono, correo, usuario,clave};

            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, null, false);
            Log.v("AGET-ID",idUsuario);
            if (idUsuario.isEmpty()) {
                resultado.execute(Configuracion.PETICION_USUARIO_REGISTRO, tipoPeticion);
            } else {
                resultado.execute(Configuracion.PETICION_USUARIO_MODIFICAR_ELIMINAR + idUsuario, tipoPeticion);
                Log.v("AGET-ID","Editar/eliminar:"+idUsuario);
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
        // Asignar valores a UI


        edtNombre.setText(String.valueOf(data.get(0)));
        edtApPaterno.setText(String.valueOf(data.get(1)));
        edtApMaterno.setText(String.valueOf(data.get(2)));
        edtTelefono.setText(String.valueOf(data.get(3)));
        edtCorreo.setText(String.valueOf(data.get(4)));
        edtUsuario.setText(String.valueOf(data.get(5)));
        edtClave.setText(String.valueOf(data.get(6)));

        habilitarComponentes(false);

        setTitle(String.valueOf(data.get(0)));
    }

    void habilitarComponentes(Boolean habilitado) {
        edtNombre.setEnabled(habilitado);
        edtApPaterno.setEnabled(habilitado);
        edtApMaterno.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtCorreo.setEnabled(habilitado);
        edtUsuario.setEnabled(habilitado);
        edtClave.setEnabled(habilitado);

    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void preparaEliminacion() {
        if (idUsuario != null) {

            //Datos de busqueda
            String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_ID};
            String[] valorFiltro = {idUsuario};

            //Datos a mostrar
            String[] columnasArecuperar = {
                    Configuracion.COLUMNA_USUARIO_TELEFONO,
                    Configuracion.COLUMNA_GPS_NUMERO
            };

            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_DEPARTAMENTO_ENLACE_TELEFONOS_ENLAZADOS, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, columnasArecuperar, true);
            resultado.execute(Configuracion.PETICION_ENLACE_LISTAR_TELEFONOS, "post");
        }
    }

    void regresar() {
        if (idUsuario != null) {
            edtNombre.setText(String.valueOf(data.get(0)));
            edtApPaterno.setText(String.valueOf(data.get(1)));
            edtApMaterno.setText(String.valueOf(data.get(2)));
            edtTelefono.setText(String.valueOf(data.get(3)));
            edtCorreo.setText(String.valueOf(data.get(4)));
            edtUsuario.setText(String.valueOf(data.get(5)));
            edtClave.setText(String.valueOf(data.get(6)));


            habilitarComponentes(false);

            setTitle(String.valueOf(data.get(0)));
            menuOk.setVisible(false);

        } else {
            finish();
        }
    }


    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
