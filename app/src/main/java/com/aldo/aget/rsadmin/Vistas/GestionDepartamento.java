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

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Mensajes;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;

import java.util.ArrayList;

public class GestionDepartamento extends AppCompatActivity {

    FloatingActionButton fab_usuarios, fab_gps;
    String idEmpresa, empresaNombre,empresaStatus,idDepartamento,departamentoNombre;

    EditText edtNombre;
    EditText edtTelefono;
    EditText edtCorreo;
    EditText edtDireccion;
    TextView txtEstadoEmpresa;

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    String tabla = Configuracion.TABLA_DEPARTAMENTO;

    BroadcastReceiver receptorMensaje,receptorTelefonos;

    ArrayList data;

    MenuItem menuOk, menuEditar, menuEliminar;

    String ID = "";

    String[][] telefonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_departamento);
        agregarToolbar();
        Bundle bundle = getIntent().getExtras();
        idDepartamento =  bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre  = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);
        idEmpresa  = (String) bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);
        empresaNombre  = bundle.getString(Configuracion.COLUMNA_EMPRESA_NOMBRE);
        empresaStatus  = bundle.getString(Configuracion.COLUMNA_EMPRESA_STATUS);

        edtNombre = (EditText) findViewById(R.id.edt_nombre_departamento);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_departamento);
        edtCorreo = (EditText) findViewById(R.id.edt_correo_departamento);
        edtDireccion = (EditText) findViewById(R.id.edt_direccion_departamento);
        txtEstadoEmpresa = (TextView) findViewById(R.id.txt_estado_departamento);

        fab_usuarios = (FloatingActionButton) findViewById(R.id.fab_usuarios);
        fab_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                lanzaUsuarios();
            }
        });
        fab_gps = (FloatingActionButton) findViewById(R.id.fab_dispositivos_gps);
        fab_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                lanzarGpss();
            }
        });

        progressBar = (ProgressBar) findViewById(R.id.barra);

        //Datos de busqueda
        String[] columnasFiltro = {Configuracion.COLUMNA_DEPARTAMENTO_ID};
        String[] valorFiltro = {idDepartamento};

        //Datos a mostrar
        String[] columnasArecuperar = {
                Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE,
                Configuracion.COLUMNA_DEPARTAMENTO_TELEFONO,
                Configuracion.COLUMNA_DEPARTAMENTO_CORREO,
                Configuracion.COLUMNA_DEPARTAMENTO_DIRECCION,
                Configuracion.COLUMNA_DEPARTAMENTO_ID};

        if (idDepartamento!= null) {
            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_DEPARTAMENTO, columnasFiltro, valorFiltro, tabla, columnasArecuperar, false);
            resultado.execute(Configuracion.PETICION_DEPARTAMENTO_LISTAR_POR_ID, tipoPeticion);
        } else {
            setTitle(R.string.titulo_actividad_agregar_departamento);
        }

        Log.v("AGET", columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);


        fab_usuarios.setVisibility(View.GONE);
        fab_gps.setVisibility(View.GONE);

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                boolean MostrarMensaje = true;
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
//                    if (estado.equalsIgnoreCase("0")) {
//                        fab_gps.setVisibility(View.INVISIBLE);
//                        fab_usuarios.setVisibility(View.INVISIBLE);
//                    } else {
//                        fab_gps.setVisibility(View.INVISIBLE);
//                        fab_usuarios.setVisibility(View.INVISIBLE);
//                    }
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

                Snackbar.make(findViewById(R.id.xml_gestion_departamento),
                        mensaje, Snackbar.LENGTH_SHORT).show();
                mostrarProgreso(false);
            }
        };


        receptorTelefonos = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET-BROAD RECIBIDO", "GestionArrendatarioCliente Numeros");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean resultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (resultado) {
                    ArrayList listNumerosEnlazados = intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST);

                    telefonos = new String[listNumerosEnlazados.size()-1][((ArrayList) listNumerosEnlazados).size()-1];
//                    telefonos = new String[listNumerosEnlazados.size()][((ArrayList) listNumerosEnlazados).size()];
                    ArrayList nombres = new ArrayList();
                    String telUsuario = "",telGps = "";

                    Log.v("tam",""+listNumerosEnlazados.size());
                    Log.v("tam",""+((ArrayList) listNumerosEnlazados).size());
                    Log.v("tam",""+telefonos.length);
//                    for (int i = 0; i < listNumerosEnlazados.size() - 1; i++) {
                    for (int i = 0; i < listNumerosEnlazados.size()-1; i++) {
//                        for (int j = 0; j < ((ArrayList) listNumerosEnlazados).size()-1; j++){
                        Log.v("pasa i ",""+i);
                        for (int j = 0; j < ((ArrayList) listNumerosEnlazados).size()-2; j++){
                            Log.v("pasa j ",""+j);
                            //nombres.add((String) ((ArrayList) datosLista.get(i)).get(0) + " - " + ((ArrayList) datosLista.get(i)).get(1));
                            telUsuario = (String) ((ArrayList) listNumerosEnlazados.get(i)).get(j);
                            if (telUsuario != null)
                                telefonos[i][j] = (String) ((ArrayList) listNumerosEnlazados.get(i)).get(j);
                        }
                    }
                    for (int i = 0; i < telefonos.length; i++) {
                        Log.v("AGET-NUMEROUSUARIO", "msj: " + telefonos[i][0]+" num: "+telefonos[i][1]);
                    }
                    desvincular(telefonos);
                }
                eliminar(true);
                mostrarProgreso(false);
            }
        };

}

    private void desvincular(String[][]telefonos){
        Mensajes sms = new Mensajes(this,telefonos.length);

        for(int i = 0 ; i < telefonos.length ; i++){
            sms.enviarSMSDesvincularUsuario(telefonos[i][0],telefonos[i][1]);
        }

    }


    public  void eliminar(boolean smsEnviados){
        if(smsEnviados)
            Log.v("AGET-ELIMINADO","SI Eliminado");
        else
            Log.v("AGET-ELIMINADO","NO Eliminado");

//NOTA: LAS DOS LINEAS COMENTADAS DEBEN DE EJECUTARCE DEPUES DE MANDAS EL MENSAJE A LOS NUMEROS
         new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_DEPARTAMENTO, null, null, tabla, null, false)
                 .execute(Configuracion.PETICION_DEPARTAMENTO_MODIFICAR_ELIMINAR + ID, "delete");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GESTION_DEPARTAMENTO);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);

        IntentFilter filtroTelefonos = new IntentFilter(Configuracion.INTENT_DEPARTAMENTO_ENLACE_TELEFONOS_ENLAZADOS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorTelefonos, filtroTelefonos);


        //IntentFilter filtroTelefonos = new IntentFilter(Configuracion.INTENT_EMPRESA_CLIENTE_ENLACE_TELEFONOS_ENLAZADOS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorTelefonos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_insercion_departamento, menu);
        this.menuEliminar = menu.findItem(R.id.accion_eliminar);
        this.menuEditar = menu.findItem(R.id.accion_editar);
        this.menuOk = menu.findItem(R.id.accion_confirmar);
        // Verificación de visibilidad acción eliminar

        if (idDepartamento!= null) {
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
        String telefono = edtTelefono.getText().toString();
        String correo = edtCorreo.getText().toString();
        String direccion= edtDireccion.getText().toString();
        // Validaciones
        if (!esNombreValido(nombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_nombre_departamento);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            mostrarProgreso(true);
            String[] columnasFiltro = {Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE, Configuracion.COLUMNA_DEPARTAMENTO_TELEFONO
                    , Configuracion.COLUMNA_DEPARTAMENTO_CORREO, Configuracion.COLUMNA_DEPARTAMENTO_DIRECCION,Configuracion.COLUMNA_DEPARTAMENTO_EMPRESA_ID};
            String[] valorFiltro = {nombre, telefono, correo, direccion,idEmpresa};
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_DEPARTAMENTO, columnasFiltro, valorFiltro, tabla, null, false);
            if (idDepartamento == null) {
                resultado.execute(Configuracion.PETICION_DEPARTAMENTO_REGISTRO, tipoPeticion);
            } else {
                resultado.execute(Configuracion.PETICION_DEPARTAMENTO_MODIFICAR_ELIMINAR + ID, tipoPeticion);
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
        edtTelefono.setText(String.valueOf(data.get(1)));
        edtCorreo.setText(String.valueOf(data.get(2)));
        edtDireccion.setText(String.valueOf(data.get(3)));

        ID = String.valueOf(data.get(data.size() - 1));
        if(empresaStatus.equalsIgnoreCase("1")) {
            txtEstadoEmpresa.setText("Habilitada");
            fab_usuarios.setVisibility(View.VISIBLE);
            fab_gps.setVisibility(View.VISIBLE);
        }else {
            txtEstadoEmpresa.setText("Deshabilitada");
        }
        habilitarComponentes(false);

        setTitle(String.valueOf(data.get(0)));
    }

    void habilitarComponentes(Boolean habilitado) {
        edtNombre.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtCorreo.setEnabled(habilitado);
        edtDireccion.setEnabled(habilitado);
    }

    private void preparaEliminacion() {
        if (idDepartamento != null) {

            //Datos de busqueda
            String[] columnasFiltro = {Configuracion.COLUMNA_DEPARTAMENTO_ID};
            String[] valorFiltro = {idDepartamento};

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
        if (idDepartamento != null) {
            edtNombre.setText(String.valueOf(data.get(0)));
            edtTelefono.setText(String.valueOf(data.get(1)));
            edtCorreo.setText(String.valueOf(data.get(2)));
            edtDireccion.setText(String.valueOf(data.get(2)));

            habilitarComponentes(false);

            setTitle(String.valueOf(data.get(0)));
            menuOk.setVisible(false);

        } else {
            finish();
        }
    }

    void lanzarGpss() {
        Intent inten = new Intent(this, GpsDepartamento.class);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, empresaNombre);
        inten.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_ID, idDepartamento);
        inten.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE, departamentoNombre);
        startActivity(inten);
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    void lanzaUsuarios() {
        Intent inten = new Intent(this, ListaUsuarios.class);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, empresaNombre);
        inten.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_ID, idDepartamento);
        inten.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE, departamentoNombre);
        //Departamento
        // inten.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, empresaNombre);
        startActivity(inten);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}