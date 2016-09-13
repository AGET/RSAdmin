package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ImageView;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.Mensajes;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import android.widget.LinearLayout;


import java.util.ArrayList;

import com.aldo.aget.rsadmin.Vistas.DialogoConfirmacion.*;

public class GestionUsuarios extends AppCompatActivity implements AdapterView.OnItemClickListener,
        OnConfirmacionDialogListener {

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    String tabla = Configuracion.TABLA_USUARIOS;

    BroadcastReceiver receptorMensaje, receptorTelefonos;

    ArrayList data;

    MenuItem menuOk, menuEditar, menuEliminar;

    TextInputLayout mcr_clave;

    ImageView iconoPass;

    EditText edtNombre, edtApPaterno, edtApMaterno, edtTelefono, edtCorreo, edtClave;

    String idUsuario, nombreUsuario, departamentoId, departamentoNombre, numeroGpsSpinner;

    //Datos de lista
    final static String tablaLista = Configuracion.TABLA_USUARIOS;
    final static String columnasLista[] = {
            Configuracion.COLUMNA_ENLACE_ID,
            Configuracion.COLUMNA_USUARIO_ID,
            Configuracion.COLUMNA_USUARIO_NOMBRE,
            Configuracion.COLUMNA_GPS_ID,
            Configuracion.COLUMNA_GPS_IMEI,
            Configuracion.COLUMNA_GPS_NUMERO,
            Configuracion.COLUMNA_GPS_DESCRIPCION,
            Configuracion.COLUMNA_GPS_DEPARTAMENTO
    };

    final static String columnasFiltroLista[] = {Configuracion.COLUMNA_USUARIO_ID};

    ListView lista;
    ArrayAdapter adaptador;
    ArrayList datos;
    BroadcastReceiver receptorLista, receptorListaGpsDesvinculado;
    AppCompatImageButton arrow;
    String nuevoEstado = "";
    BottomSheetBehavior btnsht;

    //Spinner
    AppCompatSpinner spinner;
    SpinnerAdapter adaptadorSpinner;
    ArrayList datosSpinner;
    String[] spinnerVacio = {"No hay dispositivos, agrege mas a su departamento"};
    Boolean vacio = false;

    boolean datosRecibidos = false;

    BroadcastReceiver receptorSpinner, receptorAsignadoDeSpinner;

    String[] desvincularDispositivo = new String[2];

    String[][] telefonos;

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
        setTitle(nombreUsuario);

        edtNombre = (EditText) findViewById(R.id.edt_nombre_usuario);
        edtApPaterno = (EditText) findViewById(R.id.edt_apellido_paterno);
        edtApMaterno = (EditText) findViewById(R.id.edt_apellido_materno);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_usuario);
        edtCorreo = (EditText) findViewById(R.id.edt_correo_usuario);
        edtClave = (EditText) findViewById(R.id.edt_pass_usuario);

        mcr_clave = (TextInputLayout) findViewById(R.id.mcr_edt_pass_usuario);
        iconoPass = (ImageView) findViewById(R.id.icono_pass);

        progressBar = (ProgressBar) findViewById(R.id.barra);

        lista = (ListView) findViewById(R.id.lista_gps_de_usuario);
        lista.setOnItemClickListener(this);

        arrow = (AppCompatImageButton) findViewById(R.id.img_btn_arrow);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nuevoEstado == "STATE_EXPANDED") {
                    btnsht.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    btnsht.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });


        LinearLayout listaBottomSheet = (LinearLayout) findViewById(R.id.bottomSheet);
        btnsht = BottomSheetBehavior.from(listaBottomSheet);

        btnsht.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {


                Drawable remplazo;

                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        nuevoEstado = "STATE_COLLAPSED";
                        remplazo = getResources().getDrawable(R.drawable.arrow_up);
                        arrow.setBackgroundDrawable(remplazo);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        nuevoEstado = "STATE_EXPANDED";
                        remplazo = getResources().getDrawable(R.drawable.arrow_down);
                        arrow.setBackgroundDrawable(remplazo);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        nuevoEstado = "STATE_HIDDEN";
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        nuevoEstado = "STATE_DRAGGING";
                        remplazo = getResources().getDrawable(R.drawable.arrow_right);
                        arrow.setBackgroundDrawable(remplazo);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        nuevoEstado = "STATE_SETTLING";
                        remplazo = getResources().getDrawable(R.drawable.arrow_left);
                        arrow.setBackgroundDrawable(remplazo);
                        break;
                }
                Log.i("BottomSheets", "Nuevo estado: " + nuevoEstado);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.i("BottomSheets", "Offset: " + slideOffset);
            }
        });

        //Spinner
        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcado = "";
                String idGps = "";
                numeroGpsSpinner = "";
                String descripcion = "";

                if (spinner.getSelectedItem() == "Elige un Gps a asociar" || spinner.getSelectedItem() == spinnerVacio
                        || datosSpinner == null) {
                } else {
//                    Toast.makeText(GestionUsuarios.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    if (adaptadorSpinner.getCount() == position) {

                    } else {
                        idGps = (String) ((ArrayList) datosSpinner.get(position)).get(0);
                        numeroGpsSpinner = (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        descripcion = (String) ((ArrayList) datosSpinner.get(position)).get(3);

                        Log.v("AGET-Enviado", marcado);
                        Log.v("AGET-NUMERO", numeroGpsSpinner);
                        Log.v("AGET-DESCRIPCION", descripcion);

                        mostrarProgreso(true);
                        tipoPeticion = "post";
                        if (!vacio) {
                            peticionRegistroEnlace(idGps);
                            spinner.setSelection(adaptadorSpinner.getCount());
                        }


                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if (idUsuario != null) {
            mostrarProgreso(true);

            peticionDatos();

            peticionLista();

            peticionSpinner();
//                mcr_clave.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
//                btnsht.setState(BottomSheetBehavior.STATE_COLLAPSED);
            btnsht.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            iconoPass.setVisibility(View.VISIBLE);
            edtClave.setEnabled(true);

        } else {
            setTitle(R.string.titulo_actividad_agregar_usuario);

//            mcr_clave.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
//                btnsht.setState(BottomSheetBehavior.STATE_HIDDEN);
            btnsht.setState(BottomSheetBehavior.STATE_HIDDEN);
//            iconoPass.setVisibility(View.GONE);
            edtClave.setEnabled(false);
            mcr_clave.setHint("123 por defecto");
        }

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

        receptorLista = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO Lista en Usuarios");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean reultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (reultado) {
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
            }
        };

        receptorListaGpsDesvinculado = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GTN-Usuarios GPS Desvinculado");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (mensaje.equalsIgnoreCase("Registro eliminado correctamente")) {
                    peticionSpinner();
                    peticionLista();
                }
                Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
                        mensaje, Snackbar.LENGTH_SHORT).show();
            }
        };

        receptorSpinner = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROADRECIBIDO GTN-Usuarios GPS LIBRES");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    vacio = false;
                    actualizarSpinner(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
                if (mensaje.equalsIgnoreCase("Sin datos")) {
                    vacio = true;
                    ArrayAdapter spinner_adapter = new ArrayAdapter(GestionUsuarios.this, android.R.layout.simple_spinner_item, spinnerVacio);
                    spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinner_adapter);
                    Log.v("AGET-SINDATOS", "SINDATOSENSPINNER");
                }
            }
        };

        receptorAsignadoDeSpinner = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GTN-Usuarios GPS LIBRES");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (mensaje.equalsIgnoreCase("Registro con exito!")) {

                    peticionSpinner();
                    peticionLista();
                }
                if (mensaje.equalsIgnoreCase("Ya existe el registro")) {
                    Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
                            mensaje, Snackbar.LENGTH_SHORT).show();
                }
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
                    ArrayList nombres = new ArrayList();
                    String telUsuario = "",telGps = "";

                    Log.v("tam",""+listNumerosEnlazados.size());
                    Log.v("tam",""+((ArrayList) listNumerosEnlazados).size());
                    Log.v("tam",""+telefonos.length);
                    for (int i = 0; i < listNumerosEnlazados.size() - 1; i++) {
                        for (int j = 0; j < ((ArrayList) listNumerosEnlazados).size()-1; j++){
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
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, null, null, tabla, null, false)
                .execute(Configuracion.PETICION_USUARIO_MODIFICAR_ELIMINAR + idUsuario, "delete");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor datos
        IntentFilter filtro = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtro);

        // Registrar receptor lista
        IntentFilter filtroLista = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO_LISTA);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorLista, filtroLista);

        // Registrar receptor spinner
        IntentFilter filtroSpinner = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO_LISTA_GPS_DISPONIBLE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorSpinner, filtroSpinner);

        // Registrar receptor spinnerSeleccionado
        IntentFilter filtroSpinnerSeleccionado = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO_REGISTRO_ENLACE);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorAsignadoDeSpinner, filtroSpinnerSeleccionado);

        //Reseptor lista gps desvinculado
        IntentFilter filtroListaDesvinculado = new IntentFilter(Configuracion.INTENT_GESTION_USUARIO_DESVINICULAR_UN_GPS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorListaGpsDesvinculado, filtroListaDesvinculado);

        IntentFilter filtroTelefonos = new IntentFilter(Configuracion.INTENT_USUARIO_ENLACE_TELEFONOS_ENLAZADOS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorTelefonos, filtroTelefonos);

        if (Configuracion.cambio) {
            mostrarProgreso(true);
            Configuracion.cambio = false;
            peticionLista();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorLista);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorSpinner);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorAsignadoDeSpinner);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorListaGpsDesvinculado);

        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorTelefonos);
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

    public void peticionDatos() {
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
                Configuracion.COLUMNA_USUARIO_CONTRASE_NA,
                Configuracion.COLUMNA_USUARIO_DEPARTAMENTO_ID,
                Configuracion.COLUMNA_USUARIO_ID};

        //Datos
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, columnasArecuperar, false);
        resultado.execute(Configuracion.PETICION_USUARIO_LISTAR_UNO, tipoPeticion);

        Log.v("AGET-Recuparar", "PeticionDatos:" + columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);
    }

    public void peticionLista() {
        String[] valorFiltro = {idUsuario};
        //Lista
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO_LISTA, columnasFiltroLista, valorFiltro, tablaLista, columnasLista, true)
                .execute(Configuracion.PETICION_USUARIO_LISTAR_GPS, tipoPeticion);
    }

    public void peticionSpinner() {
        //Spinner
        String[] valorFiltroSpinner = {departamentoId, idUsuario};
        String[] columnasFiltroSpinner = {Configuracion.COLUMNA_DEPARTAMENTO_ID, Configuracion.COLUMNA_USUARIO_ID};
        String[] columnasArecuperarSpinner = {Configuracion.COLUMNA_GPS_ID, Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION};
        new ObtencionDeResultadoBcst(GestionUsuarios.this, Configuracion.INTENT_GESTION_USUARIO_LISTA_GPS_DISPONIBLE, columnasFiltroSpinner, valorFiltroSpinner, Configuracion.TABLA_GPS, columnasArecuperarSpinner, true)
                .execute(Configuracion.PETICION_GPS_LISTAR_DIASPONIBLES_A_ENLAZAR, tipoPeticion);
    }

    public void peticionRegistroEnlace(String idGps) {
        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_GPS_ID};
        String[] valorFiltro = {idUsuario, idGps};
        new Mensajes(this, 1).enviarSMSVincularUsuario(edtTelefono.getText().toString(), numeroGpsSpinner);
        new ObtencionDeResultadoBcst(GestionUsuarios.this, Configuracion.INTENT_GESTION_USUARIO_REGISTRO_ENLACE, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, null, false)
                .execute(Configuracion.PETICION_ENLACE_REGISTRO, tipoPeticion);
    }

    private void insertar() {
        // Extraer datos de UI
        String nombre = edtNombre.getText().toString();
        String apPaterno = edtApPaterno.getText().toString();
        String apMaterno = edtApMaterno.getText().toString();
        String telefono = edtTelefono.getText().toString();
        String correo = edtCorreo.getText().toString();


        // Validaciones
        if (!esNombreValido(nombre)) {
            TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_nombre_usuario);
            mascaraCampoNombre.setError("Este campo no puede quedar vacío");
        } else {
            String[] columnasFiltro = {
                    Configuracion.COLUMNA_USUARIO_NOMBRE,
                    Configuracion.COLUMNA_USUARIO_AP_PATERNO,
                    Configuracion.COLUMNA_USUARIO_AP_MATERNO,
                    Configuracion.COLUMNA_USUARIO_TELEFONO,
                    Configuracion.COLUMNA_USUARIO_CORREO,
                    Configuracion.COLUMNA_USUARIO_CONTRASE_NA,
                    Configuracion.COLUMNA_USUARIO_DEPARTAMENTO_ID};


            if (idUsuario == null) {
                mostrarProgreso(true);
                String clave = "123";
                String[] valorFiltro = {nombre, apPaterno, apMaterno, telefono, correo, clave, departamentoId};
                resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, null, false);
                resultado.execute(Configuracion.PETICION_USUARIO_REGISTRO, tipoPeticion);
            } else {
                String clave = edtClave.getText().toString();
                if (!esNombreValido(clave)) {
                    TextInputLayout mascaraCampoNombre = (TextInputLayout) findViewById(R.id.mcr_edt_pass_usuario);
                    mascaraCampoNombre.setError("Este campo no puede quedar vacío");
                } else {
                    mostrarProgreso(true);
                    String[] valorFiltro = {nombre, apPaterno, apMaterno, telefono, correo, clave, departamentoId};
                    resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, null, false);
                    resultado.execute(Configuracion.PETICION_USUARIO_MODIFICAR_ELIMINAR + idUsuario, tipoPeticion);
                    Log.v("AGET-ID", "Editar/eliminar:" + idUsuario);
                }
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

        setTitle(String.valueOf(data.get(0)));
    }

    void habilitarComponentes(Boolean habilitado) {

        edtNombre.setEnabled(habilitado);
        edtApPaterno.setEnabled(habilitado);
        edtApMaterno.setEnabled(habilitado);
        edtTelefono.setEnabled(habilitado);
        edtCorreo.setEnabled(habilitado);
//        edtClave.setEnabled(habilitado);
        if (idUsuario == null) {
//            mcr_clave.setVisibility(View.GONE);
//            spinner.setVisibility(View.GONE);
//            btnsht.setState(BottomSheetBehavior.STATE_HIDDEN);
            edtClave.setEnabled(false);
        } else {
//            mcr_clave.setVisibility(View.VISIBLE);
//            spinner.setVisibility(View.VISIBLE);
//            btnsht.setState(BottomSheetBehavior.STATE_COLLAPSED);

            edtClave.setEnabled(habilitado);
        }

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

//            mostrarProgreso(true);
            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_USUARIO_ENLACE_TELEFONOS_ENLAZADOS, columnasFiltro, valorFiltro, Configuracion.TABLA_ENLACE, columnasArecuperar, true);
            resultado.execute(Configuracion.PETICION_ENLACE_LISTAR_TELEFONOS_USUARIO, "post");
        }
    }

    //    Spinner
    protected void actualizarSpinner(ArrayList datosMultiples) {
        this.datosSpinner = datosMultiples;
        ArrayList descripciones = new ArrayList();

        for (int i = 0; i < datosSpinner.size() - 1; i++) {
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datosSpinner.get(i)).get(0));
            descripciones.add((String) ((ArrayList) datosSpinner.get(i)).get(2) + "  " + ((ArrayList) datosSpinner.get(i)).get(3));
        }

        adaptadorSpinner = new SpinnerAdapter(GestionUsuarios.this, android.R.layout.simple_list_item_1);
        adaptadorSpinner.addAll(descripciones);
        adaptadorSpinner.add("Elige un Gps a asociar");
        spinner.setAdapter(adaptadorSpinner);
        spinner.setSelection(adaptadorSpinner.getCount());

        // adaptador.notifyDataSetChanged();
    }

    //    Lista
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String marcado = (String) lista.getItemAtPosition(position);
        String nombre;
        marcado = (String) ((ArrayList) datos.get(position)).get(3);
        nombre = (String) ((ArrayList) datos.get(position)).get(5);

        desvincularDispositivo[0] = (String) ((ArrayList) datos.get(position)).get(0);
        desvincularDispositivo[1] = (String) ((ArrayList) datos.get(position)).get(5);

        // En algún lugar de tu actividad
        new DialogoConfirmacion("Desvincular", "Desea quitar este dispositivo a el usuario?", "Desvincular", "Cancelar").show(getSupportFragmentManager(), "SimpleDialog");
    }

    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:", "" + datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datos.get(0)).size());

        for (int i = 0; i < datos.size() - 1; i++) {
            Log.v("AGET-include:", "" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) "   " + ((ArrayList) datos.get(i)).get(5) + " || " + ((ArrayList) datos.get(i)).get(6));
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
    }
    //FinLista

    public void peticionDesvincularGps() {
        Log.v("AGET_Desvincular 1", desvincularDispositivo[0]);

        mostrarProgreso(true);
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO_DESVINICULAR_UN_GPS, null, null, Configuracion.TABLA_ENLACE, null, false);
        resultado.execute(Configuracion.PETICION_ENLACE_MODIFICAR_ELIMINAR + desvincularDispositivo[0], "delete");
    }

    public void smsPreDesvinculacion() {
        Log.v("AGET_Desvincular 0 ", desvincularDispositivo[1]);

        new Mensajes(this, 1).enviarSMSDesvincularUsuario(edtTelefono.getText().toString(), desvincularDispositivo[1]);
    }

    void regresar() {
        if (idUsuario != null) {
            edtNombre.setText(String.valueOf(data.get(0)));
            edtApPaterno.setText(String.valueOf(data.get(1)));
            edtApMaterno.setText(String.valueOf(data.get(2)));
            edtTelefono.setText(String.valueOf(data.get(3)));
            edtCorreo.setText(String.valueOf(data.get(4)));
            edtClave.setText(String.valueOf(data.get(5)));


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

    @Override
    public void onPossitiveButtonClick() {
        Log.v("AGET-DIALOGO", "ACEPTAR");
        smsPreDesvinculacion();
        peticionDesvincularGps();
    }

    @Override
    public void onNegativeButtonClick() {
        Log.v("AGET-DIALOGO", "CANCELAR");
    }
}