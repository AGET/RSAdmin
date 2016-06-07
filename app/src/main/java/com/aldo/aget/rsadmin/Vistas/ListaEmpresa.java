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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;
import android.widget.ProgressBar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListaEmpresa extends AppCompatActivity implements OnItemClickListener {

    ListView lista;
    ArrayAdapter adaptador;
    private ProgressBar progressBar;
    ArrayList datos;

    final static String peticionlistarEmpresaCliente = Configuracion.PETICION_LISTAR_EMPRESAS_HABILITADAS;
    final static String tabla = "empresa_cliente";
    final static String columnas[] = {"empresa_id","nombre"};

    private BroadcastReceiver receptorMensaje;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_empresa);
        agregarToolbar();
        setTitle(R.string.titulo_actividad_lista_empresa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                actividadEmpresa(null);
            }
        });

        lista = (ListView)findViewById(R.id.listaempresa);
        progressBar = (ProgressBar) findViewById(R.id.barra);

        mostrarProgreso(true);
        new ObtenerAsincrono(ListaEmpresa.this,tabla,columnas)
                .execute(peticionlistarEmpresaCliente);

        lista.setOnItemClickListener(this);

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO empresa");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if(restado){
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }

                Snackbar.make(findViewById(R.id.xmllistaempresa_cliente),
                        mensaje, Snackbar.LENGTH_SHORT).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.IntentListaEmpresa);
        LocalBroadcastManager.getInstance(this).registerReceiver(receptorMensaje, filtroSync);
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
        Snackbar.make(view,  "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        marcado = (String) ((ArrayList) datos.get(position)).get(0);
        Log.v("AGET-Enviado",marcado);
        actividadEmpresa(marcado);
    }

    void actividadEmpresa(String nombre){
        Intent actividad = new Intent(this, ActivityEmpresaCliente.class);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, nombre);
        startActivity(actividad);
    }

    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:",""+datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:",""+((ArrayList)datos.get(0)).size());

        for(int i = 0 ; i < datos.size()-1; i++){
            Log.v("AGET-include:","" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) ((ArrayList) datos.get(i)).get(1));
         }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptador);

        //adaptador.insert(grupo, 0);
        adaptador.notifyDataSetChanged();
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
