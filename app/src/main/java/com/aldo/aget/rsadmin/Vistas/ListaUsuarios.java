package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
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

import static android.support.v4.view.ViewCompat.animate;

public class ListaUsuarios extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView lista;
    ArrayAdapter adaptador;
    private ProgressBar progressBar;
    ArrayList datos;
    final long DURACION = 1300;

    final static String tabla = Configuracion.TABLA_USUARIOS;
    final static String columnas[] = {Configuracion.COLUMNA_USUARIO_ID, Configuracion.COLUMNA_USUARIO_NOMBRE,Configuracion.COLUMNA_USUARIO_AP_PATERNO,Configuracion.COLUMNA_USUARIO_AP_MATERNO};
    final static String columnasFiltro[] = {Configuracion.COLUMNA_DEPARTAMENTO_ID};

    private BroadcastReceiver receptorMensaje;

    FloatingActionButton fab_nuevo_usuario;

    ObtencionDeResultadoBcst resultado;

    String tipoPeticion = "post";

    String idEmpresa, empresaNombre, departamentoId,departamentoNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_usuarios);

        agregarToolbar();
        setTitle(R.string.titulo_actividad_lista_usuarios);

        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);
        empresaNombre = bundle.getString(Configuracion.COLUMNA_EMPRESA_NOMBRE);
        departamentoId= bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_ID);
        departamentoNombre = bundle.getString(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE);

        String[] valorFiltro = {departamentoId};

        fab_nuevo_usuario = (FloatingActionButton) findViewById(R.id.nuevo_usuario);
        fab_nuevo_usuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                mostrarGestionUsuario(null, null);
            }
        });

        lista = (ListView) findViewById(R.id.listausuario);
        progressBar = (ProgressBar) findViewById(R.id.barra);

        mostrarProgreso(true);
        resultado = new ObtencionDeResultadoBcst(this, Configuracion.INTENT_LISTA_USUARIOS, columnasFiltro, valorFiltro, tabla, columnas, true);
        resultado.execute(Configuracion.PETICION_USUARIO_LISTAR_VARIOS, tipoPeticion);

        ManejadorScroll.Action desplazamiento = new ManejadorScroll.Action() {

            private boolean ocultar = true;

            @Override
            public void up() {
                if (ocultar) {
                    ocultar = false;
                    animate(fab_nuevo_usuario)
                            .translationY(fab_nuevo_usuario.getHeight() +
                                    getResources().getDimension(R.dimen.fab_margin))
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(DURACION);
                    Log.v("AGET-POSICION", "UP");
                }
            }

            @Override
            public void down() {
                if (!ocultar) {
                    ocultar = true;
                    animate(fab_nuevo_usuario)
                            .translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(DURACION);
                    Log.v("AGET-POSICION", "DOWN");
                }
            }

        };

        lista.setOnScrollListener(new ManejadorScroll(lista, 8, desplazamiento));

        lista.setOnItemClickListener(this);

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO Usuarios");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean reultado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (reultado) {
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }

                Snackbar.make(findViewById(R.id.xml_lista_usuario),
                        mensaje, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_LISTA_USUARIOS);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtroSync);

        if (Configuracion.cambio) {
            mostrarProgreso(true);
            Configuracion.cambio = false;
            new ObtenerAsincrono(ListaUsuarios.this, Configuracion.INTENT_LISTA_USUARIOS, tabla, columnas)
                    .execute(Configuracion.PETICION_USUARIO_LISTAR_VARIOS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receptorMensaje);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String marcado = (String) lista.getItemAtPosition(position);
        String nombre;
        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        marcado = (String) ((ArrayList) datos.get(position)).get(0);
        nombre = (String) ((ArrayList) datos.get(position)).get(1);
        Log.v("AGET-Enviado", marcado + "+" + nombre);
        mostrarGestionUsuario(marcado, nombre);
    }

    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:", "" + datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datos.get(0)).size());

        for (int i = 0; i < datos.size() - 1; i++) {
            Log.v("AGET-include:", "" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) ((ArrayList) datos.get(i)).get(1)+" "+((ArrayList) datos.get(i)).get(2)+" "+((ArrayList) datos.get(i)).get(3));
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void mostrarGestionUsuario(String id, String nombre) {
        Intent actividad = new Intent(this, GestionUsuarios.class);
        actividad.putExtra(Configuracion.COLUMNA_USUARIO_ID, id);
        actividad.putExtra(Configuracion.COLUMNA_USUARIO_NOMBRE, nombre);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, empresaNombre);
        actividad.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_ID, departamentoId);
        actividad.putExtra(Configuracion.COLUMNA_DEPARTAMENTO_NOMBRE, departamentoNombre);
        startActivity(actividad);
    }

    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
