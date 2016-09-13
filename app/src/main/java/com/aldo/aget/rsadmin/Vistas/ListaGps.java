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

import java.util.ArrayList;

public class ListaGps extends AppCompatActivity implements OnItemClickListener{

    ListView lista;
    ArrayAdapter adaptador;
    ProgressBar progressBar;
    BroadcastReceiver receptorMensaje;
    ArrayList datos;

    final static String peticionlistarGps = Configuracion.PETICION_GPS_LISTAR_LIBRES;
    final static String tabla = "gps";
    final static String columnas[] = {"imei","numero"};
    //final static String columna = "imei";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
        lista = (ListView)findViewById(R.id.listagps);
        progressBar = (ProgressBar) findViewById(R.id.barra);

        mostrarProgreso(true);
        new ObtenerAsincrono(ListaGps.this,Configuracion.INTENT_LISTA_GPS,tabla,columnas)
                .execute(peticionlistarGps);

        lista.setOnItemClickListener(this);



        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO GPS");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if(restado){
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
                Snackbar.make(findViewById(R.id.xmllistagps),
                        mensaje, Snackbar.LENGTH_LONG).show();
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_LISTA_GPS);
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
//        Snackbar.make(view,  "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show();

        marcado = (String) ((ArrayList) datos.get(position)).get(0);
        Log.v("AGET-Enviado", marcado);

        actividadGps(marcado);
    }

    void actividadGps(String imei){
        Intent actividad = new Intent(this, GestionGps.class);
        actividad.putExtra(Configuracion.COLUMNA_GPS_IMEI, imei);
        startActivity(actividad);
    }

    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        ArrayList datosEnLista = new ArrayList();
        for(int i = 0 ; i < datos.size()-1; i++){
            datosEnLista.add((String) ((ArrayList) datos.get(i)).get(1));
        }

        adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datosEnLista);

        lista.setAdapter(adaptador);

        adaptador.notifyDataSetChanged();
    }


    private void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

}
