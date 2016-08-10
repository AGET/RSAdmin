package com.aldo.aget.rsadmin.Vistas;

import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
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
import android.widget.TextView;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;

import java.util.ArrayList;
import com.aldo.aget.rsadmin.Vistas.DialogoConfirmacion.*;
//AppCompatActivity
public class GestionUsuarios extends AppCompatActivity implements AdapterView.OnItemClickListener,
        OnConfirmacionDialogListener {

    ProgressBar progressBar;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    String tabla = Configuracion.TABLA_USUARIOS;

    BroadcastReceiver receptorMensaje;

    ArrayList data;

    MenuItem menuOk, menuEditar, menuEliminar;

    EditText edtNombre, edtApPaterno, edtApMaterno, edtTelefono, edtCorreo, edtUsuario, edtClave;

    String idUsuario, nombreUsuario, departamentoId, departamentoNombre;

    //Datos de lista
    final static String tablaLista = Configuracion.TABLA_USUARIOS;
    final static String columnasLista[] = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_USUARIO_NOMBRE,
            Configuracion.COLUMNA_GPS_IMEI, Configuracion.COLUMNA_GPS_NUMERO, Configuracion.COLUMNA_GPS_DESCRIPCION,
            Configuracion.COLUMNA_GPS_DEPARTAMENTO};
    final static String columnasFiltroLista[] = {Configuracion.COLUMNA_USUARIO_ID};

    ListView lista;
    ArrayAdapter adaptador;
    ArrayList datos;
    BroadcastReceiver receptorLista;

    //Spinner
    AppCompatSpinner spinner;
    SpinnerAdapter adaptadorSpinner;
    ArrayList datosSpinner;
    String[] spinnerVacio = {"No hay dispositivos, agrege mas a su departamento"};
    Boolean vacio = false;

    boolean datosRecibidos = false;

    BroadcastReceiver receptorSpinner, receptorAsignadoDeSpinner;

    String idGpsSeleccionadoAEliminar ="";

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

        lista = (ListView) findViewById(R.id.lista_gps_de_usuario);
        lista.setOnItemClickListener(this);

        //Spinner
        spinner = (AppCompatSpinner) findViewById(R.id.spinner_gps);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String marcado = "";
                String idGps =  "";
                String numero = "";
                String descripcion = "";

                if (spinner.getSelectedItem() == "Elige un Gps a asociar" || spinner.getSelectedItem() == spinnerVacio
                        || datosSpinner == null) {
                } else {
                    Toast.makeText(GestionUsuarios.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    Log.v("AGET-SPINNER-GPSS", "" + ((ArrayList) datosSpinner).size());
                    Log.v("AGET-SPINNER-ITEMS", "" + ((ArrayList) datosSpinner.get(0)).size());
                    Log.v("AGET-SPINNER-POSICION", "" + position);
                    Log.v("AGET-SPINNER-ADAPTADOR", "" + adaptadorSpinner.getCount());

                    if (adaptadorSpinner.getCount() == position) {

                    } else {
                        idGps = (String) ((ArrayList) datosSpinner.get(position)).get(0);
                        numero = (String) ((ArrayList) datosSpinner.get(position)).get(1);
                        descripcion = (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        //descripcion = (String) ((ArrayList) datosSpinner.get(position)).get(2);

                        //marcado = (String) ((ArrayList) datosSpinner.get(position)).get(0) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(1) + "-" + (String) ((ArrayList) datosSpinner.get(position)).get(2);
                        Log.v("AGET-Enviado", marcado);
                        Log.v("AGET-NUMERO", numero);
                        Log.v("AGET-DESCRIPCION", descripcion);

                        Log.v("AGET-0","posicion"+position);
                        Log.v("AGET-1","adaptadorSpinner"+adaptadorSpinner.getCount());
                        Log.v("AGET-2","contadordatosSpinner1"+((ArrayList) datosSpinner).size());
                        Log.v("AGET-3","contadordatosSpinner2"+datosSpinner.size());
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

        } else {
            setTitle(R.string.titulo_actividad_agregar_departamento);
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

//                Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
//                        mensaje, Snackbar.LENGTH_SHORT).show();
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

//                Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
//                        mensaje, Snackbar.LENGTH_SHORT).show();
            }
        };

        receptorSpinner = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS LIBRES");
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
                if(mensaje.equalsIgnoreCase("Sin datos")){
                    vacio = true;
                    ArrayAdapter spinner_adapter = new ArrayAdapter(GestionUsuarios.this, android.R.layout.simple_spinner_item,spinnerVacio);
                    spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(spinner_adapter);
                    Log.v("AGET-SINDATOS","SINDATOSENSPINNER");
                }
            }
        };

        receptorAsignadoDeSpinner = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS LIBRES");
                if (datosRecibidos) {
                    mostrarProgreso(false);
                } else {
                    datosRecibidos = true;
                }
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO,false);
                if(mensaje.equalsIgnoreCase("Registro con exito!")){
                    peticionSpinner();
                    peticionLista();
                }
                if(mensaje.equalsIgnoreCase("Ya existe el registro")){
                    Snackbar.make(findViewById(R.id.xml_gestion_usuarios),
                            mensaje, Snackbar.LENGTH_SHORT).show();
                }
            }
        };

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

        //IntentFilter filtroTelefonos = new IntentFilter(Configuracion.INTENT_EMPRESA_CLIENTE_ENLACE_TELEFONOS_ENLAZADOS);


        if (Configuracion.cambio) {
            mostrarProgreso(true);
            Configuracion.cambio = false;
//            new ObtenerAsincrono(GestionUsuarios.this, Configuracion.INTENT_GESTION_USUARIO_LISTA, tablaLista, columnasLista)
//                    .execute(Configuracion.PETICION_USUARIO_LISTAR_GPS);
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

    public void peticionDatos(){
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

        //Datos
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, columnasArecuperar, false);
        resultado.execute(Configuracion.PETICION_USUARIO_LISTAR_UNO, tipoPeticion);

        Log.v("AGET-Recuparar","PeticionDatos:"+ columnasArecuperar[columnasArecuperar.length - 1]);
        Log.v("AGET", Configuracion.idActual);
    }

    public void peticionLista(){
        String[] valorFiltro = {idUsuario};
        //Lista
        new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO_LISTA, columnasFiltroLista, valorFiltro, tablaLista, columnasLista, true)
                .execute(Configuracion.PETICION_USUARIO_LISTAR_GPS, tipoPeticion);

    }

    public void peticionSpinner(){
        //Spinner
        String[] valorFiltroSpinner = {departamentoId,idUsuario};
        String[] columnasFiltroSpinner = {Configuracion.COLUMNA_DEPARTAMENTO_ID,Configuracion.COLUMNA_USUARIO_ID};
        String[] columnasArecuperarSpinner = {Configuracion.COLUMNA_GPS_ID,Configuracion.COLUMNA_GPS_IMEI,Configuracion.COLUMNA_GPS_NUMERO};
        new ObtencionDeResultadoBcst(GestionUsuarios.this,Configuracion.INTENT_GESTION_USUARIO_LISTA_GPS_DISPONIBLE, columnasFiltroSpinner, valorFiltroSpinner, Configuracion.TABLA_GPS, columnasArecuperarSpinner, true)
                .execute(Configuracion.PETICION_GPS_LISTAR_DIASPONIBLES_A_ENLAZAR, tipoPeticion);
    }

    public void peticionRegistroEnlace(String idGps){
        String[] columnasFiltro = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_GPS_ID};
        String[] valorFiltro = {idUsuario,idGps};
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
                    Configuracion.COLUMNA_USUARIO_CORREO, Configuracion.COLUMNA_USUARIO_USUARIO, Configuracion.COLUMNA_USUARIO_CONTRASE_NA, Configuracion.COLUMNA_USUARIO_DEPARTAMENTO_ID};
            String[] valorFiltro = {nombre, apPaterno, apMaterno, telefono, correo, usuario, clave, departamentoId};

            resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_GESTION_USUARIO, columnasFiltro, valorFiltro, tabla, null, false);

            if (idUsuario == null) {
                resultado.execute(Configuracion.PETICION_USUARIO_REGISTRO, tipoPeticion);
            } else {
                resultado.execute(Configuracion.PETICION_USUARIO_MODIFICAR_ELIMINAR + idUsuario, tipoPeticion);
                Log.v("AGET-ID", "Editar/eliminar:" + idUsuario);
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

    //    Spinner
    protected void actualizarSpinner(ArrayList datosMultiples) {
        this.datosSpinner = datosMultiples;
        ArrayList descripciones = new ArrayList();

        for (int i = 0; i < datosSpinner.size() - 1; i++) {
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datosSpinner.get(i)).get(0));
            descripciones.add((String) ((ArrayList) datosSpinner.get(i)).get(1)+"  "+((ArrayList) datosSpinner.get(i)).get(2));
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
        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        marcado = (String) ((ArrayList) datos.get(position)).get(2);
        nombre = (String) ((ArrayList) datos.get(position)).get(4);
        Log.v("AGET-Enviado", marcado + "+" + nombre);
        Toast.makeText(GestionUsuarios.this, marcado + "+" + nombre, Toast.LENGTH_SHORT).show();
//        AlertDialog mostrar = dialogo((String) ((ArrayList) datos.get(position)).get(0));
  //      mostrar.show();
        idGpsSeleccionadoAEliminar = (String) ((ArrayList) datos.get(position)).get(0);
        // En algún lugar de tu actividad
        new DialogoConfirmacion().show(getSupportFragmentManager(), "SimpleDialog");
    }

    public AlertDialog dialogo(final String idGpsADesvincular){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Desea quitar el GPS a el usuario? ")
                .setMessage("El Mensaje para el usuario")
                .setPositiveButton("Desvincular GPS",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onPossitiveButtonClick();
                                desvincularGps(idGpsADesvincular);
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //listener.onNegativeButtonClick();
                            }
                        });

        return builder.create();
    }
    public void desvincularGps(String idGpsADesvincular){
        Log.v("AGET_Desvincular", idGpsADesvincular);
    }
    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:", "" + datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datos.get(0)).size());

        for (int i = 0; i < datos.size() - 1; i++) {
            Log.v("AGET-include:", "" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) "   " + ((ArrayList) datos.get(i)).get(3) + " || " + ((ArrayList) datos.get(i)).get(4));
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
    }

    //FinLista

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

    @Override
    public void onPossitiveButtonClick() {
        Log.v("AGET-DIALOGO","ACEPTAR");
        desvincularGps(idGpsSeleccionadoAEliminar);
    }

    @Override
    public void onNegativeButtonClick() {
        Log.v("AGET-DIALOGO","CANCELAR");
    }
}
