package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;;
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

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;

import android.support.v7.widget.AppCompatSpinner;

public class EmpresaCliente extends AppCompatActivity {

    EditText edtNombre, edtTelefono, edtCorreo;
    FloatingActionButton fab_usuarios, fab_gps;

    String idEmpresa;
    String ID = "";
    AppCompatSpinner spinner;
    //Spinner spinner;
    String[] datosSpinner = {
            "Habilitada",
            "Inhabilitada"
    };
    String estado = "0";

    ObtencionDeResultadoBcst resultado;

    BroadcastReceiver receptorMensaje;

    private ProgressBar progressBar;
    MenuItem menuOk, menuEditar, menuEliminar;
    ArrayList data;

    String tabla = Configuracion.TABLA_EMPRESA_CLIENTE;

    String tipoPeticion = "post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empresa_cliente);
        agregarToolbar();
        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);

        fab_usuarios = (FloatingActionButton) findViewById(R.id.fab_usuarios);
        fab_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                lanzarGpss();
            }
        });
        fab_gps = (FloatingActionButton) findViewById(R.id.fab_dispositivos_gps);
        fab_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                lanzarGpss();
            }
        });
        fab_usuarios.setVisibility(View.GONE);
        fab_gps.setVisibility(View.GONE);

        edtNombre = (EditText) findViewById(R.id.edt_nombre_empresa);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_empresa);
        edtCorreo = (EditText) findViewById(R.id.edt_correo_empresa);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner_status);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, datosSpinner);
        SpinnerAdapter adapter = new SpinnerAdapter(this, android.R.layout.simple_list_item_1);
        adapter.addAll(datosSpinner);
        adapter.add("Estado de la empresa");
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getCount());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem() == "Estado de la empresa") {

                } else {
                    if (spinner.getSelectedItem().toString().equalsIgnoreCase("Habilitada"))
                        estado = "1";
                    else
                        estado = "0";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        progressBar = (ProgressBar) findViewById(R.id.barra);

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_ID};
        String[] valorFiltro = {idEmpresa};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_EMPRESA_NOMBRE,
                Configuracion.COLUMNA_EMPRESA_TELEFONO,
                Configuracion.COLUMNA_EMPRESA_CORREO,
                Configuracion.COLUMNA_EMPRESA_STATUS,
                Configuracion.COLUMNA_EMPRESA_ID};

        if (idEmpresa != null) {
            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, columnasFiltro, valorFiltro, tabla, columnasArecuperar);
            resultado.execute(Configuracion.PETICION_LISTAR_EMPRESAS_POR_ID, tipoPeticion);
        } else {
            setTitle(R.string.titulo_actividad_agregar_empresa);
        }

        Log.v("AGET", columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO empresa");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    cargarViews(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                    //}else if(mensaje.equalsIgnoreCase("Registro exitoso!")){
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
                if (mensaje.equalsIgnoreCase("cargado")) {
                    return;
                }
                Snackbar.make(findViewById(R.id.xmlactivity_empresa_cliente),
                        mensaje, Snackbar.LENGTH_SHORT).show();
                mostrarProgreso(false);
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_EMPRESA_CLIENTE);
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
        if (idEmpresa != null) {
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
                eliminar();
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
        String telefono = edtTelefono.getText().toString();
        String correo = edtCorreo.getText().toString();
        // Validaciones
        if (!esNombreValido(nombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_nombre_empresa);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            mostrarProgreso(true);
            String[] columnasFiltro = {Configuracion.COLUMNA_EMPRESA_NOMBRE, Configuracion.COLUMNA_EMPRESA_TELEFONO
                    , Configuracion.COLUMNA_EMPRESA_CORREO, Configuracion.COLUMNA_EMPRESA_STATUS};
            String[] valorFiltro = {nombre, telefono, correo, estado};
            resultado = new ObtencionDeResultadoBcst(this, columnasFiltro, valorFiltro, tabla, null);
            if (ID.isEmpty()) {
                resultado.execute(Configuracion.PETICION_EMPRESAS_REGISTRO, tipoPeticion);
            } else {
                resultado.execute(Configuracion.PETICION_EMPRESAS_MODIFICAR_ELIMINAR + ID, tipoPeticion);
            }
        }
    }

    private boolean esNombreValido(String nombre) {
        return !TextUtils.isEmpty(nombre);
    }

    private void eliminar() {
        if (idEmpresa != null) {
            resultado = new ObtencionDeResultadoBcst(this, null, null, tabla, null);
            resultado.execute(Configuracion.PETICION_EMPRESAS_MODIFICAR_ELIMINAR + ID, tipoPeticion);
        }
    }

    private void cargarViews(ArrayList data) {
        this.data = data;
        if (data.size() < 0) {
            return;
        }
        // Asignar valores a UI
        edtNombre.setText(String.valueOf(data.get(0)));
        edtTelefono.setText(String.valueOf(data.get(1)));
        edtCorreo.setText(String.valueOf(data.get(2)));
        if (String.valueOf(data.get(3)).equalsIgnoreCase("0")) {
            spinner.setSelection(1);
            fab_usuarios.setVisibility(View.GONE);
            fab_gps.setVisibility(View.GONE);
        } else {
            spinner.setSelection(0);
            fab_usuarios.setVisibility(View.VISIBLE);
            fab_gps.setVisibility(View.VISIBLE);
        }
        ID = String.valueOf(data.get(data.size() - 1));


        habilitarComponentes(false);

        setTitle(String.valueOf(data.get(0)));
    }

    void habilitarComponentes(Boolean habilitado) {
        edtNombre.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtCorreo.setEnabled(habilitado);
        spinner.setEnabled(habilitado);
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    void regresar() {
        if (idEmpresa != null) {
            edtNombre.setText(String.valueOf(data.get(0)));
            edtTelefono.setText(String.valueOf(data.get(1)));
            edtCorreo.setText(String.valueOf(data.get(2)));
            if (String.valueOf(data.get(3)).equalsIgnoreCase("0"))
                spinner.setSelection(1);
            else
                spinner.setSelection(0);

            habilitarComponentes(false);

            setTitle(String.valueOf(data.get(0)));
            menuOk.setVisible(false);

        } else {
            finish();
        }
    }

    void lanzarGpss() {
        Intent inten = new Intent(this, GpsEmpresa.class);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_ID,idEmpresa);
        startActivity(inten);
    }

    void lanzarUsuarios() {
        Intent inten = new Intent(this, GpsEmpresa.class);
        startActivity(inten);
    }
}