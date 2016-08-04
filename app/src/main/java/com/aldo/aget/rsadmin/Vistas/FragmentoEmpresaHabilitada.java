package com.aldo.aget.rsadmin.Vistas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.MainActivity;
import com.aldo.aget.rsadmin.R;
import com.aldo.aget.rsadmin.ServicioWeb.ObtenerAsincrono;

import java.util.ArrayList;

import static android.support.v4.view.ViewCompat.animate;

/**
 * Created by Work on 16/07/16.
 */
public class FragmentoEmpresaHabilitada extends Fragment implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayAdapter adaptadorEH;
    private static ProgressBar progressBar;
    ArrayList datos;
    final long DURACION = 1300;
    static String peticionlistar = "";
    static String tabla = "";
    static String[] columnas;
    ArrayList<String> col = new ArrayList<String>();
    private BroadcastReceiver receptorMensaje;
    public static boolean cargadoHab = false;

    public static FragmentoEmpresaHabilitada newInstance() {

        FragmentoEmpresaHabilitada fragment = new FragmentoEmpresaHabilitada();
        return fragment;
    }

    public FragmentoEmpresaHabilitada() {
        //peticionlistar = Configuracion.PETICION_EMPRESA_LISTAR_HABILITADAS;
        peticionlistar = Configuracion.PETICION_EMPRESA_LISTAR_HABILITADOS;
        tabla = "empresa_cliente";
        columnas = new String[]{"empresa_id", "nombre"};
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmento_eh, container, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        lista = (ListView) getView().findViewById(R.id.lista_eh);
        progressBar = (ProgressBar) getView().findViewById(R.id.barra_eh);

        mostrarProgreso(true);
        cargar();

        ManejadorScroll.Action desplazamiento = new ManejadorScroll.Action() {


            private boolean ocultar = true;

            @Override
            public void up() {
                if (ocultar) {
                    ocultar = false;
                    animate(MainActivity.fabmain)
                            .translationY(MainActivity.fabmain.getHeight() +
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
                    animate(MainActivity.fabmain)
                            .translationY(0)
                            .setInterpolator(new LinearInterpolator())
                            .setDuration(DURACION);
                    Log.v("AGET-POSICION", "DOWN");
                }
            }

        };

        lista.setOnScrollListener(new ManejadorScroll(lista, 8, desplazamiento));
        lista.setNestedScrollingEnabled(true);
        lista.setOnItemClickListener(this);

//        if (adaptadorEH != null)
//            cargadoHab = (adaptadorEH.getCount() > 0) ? true : false;
//        else
//            cargadoHab = false;
    }

    public static void cargar() {
        mostrarProgreso(true);
        new ObtenerAsincrono(Configuracion.context, Configuracion.INTENT_LISTA_EMPRESAHABILITADA, tabla, columnas)
                .execute(peticionlistar);
    }


    @Override
    public void onResume() {
        super.onResume();

        receptorMensaje = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                Log.v("AGET", "BROAD RECIBIDO empresa");
                mostrarProgreso(false);
                String mensaje = intent.getStringExtra(Utilidades.EXTRA_MENSAJE);
                Boolean restado = intent.getBooleanExtra(Utilidades.EXTRA_RESULTADO, false);
                if (restado) {
                    actualizar(intent.getStringArrayListExtra(Utilidades.EXTRA_DATOS_ALIST));
                }
                Snackbar.make(getView().findViewById(R.id.fragmento_ehxml),
                        mensaje, Snackbar.LENGTH_SHORT).show();
            }
        };
        // Registrar receptor
        IntentFilter filtroSync = new IntentFilter(Configuracion.INTENT_LISTA_EMPRESAHABILITADA);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receptorMensaje, filtroSync);

        if (Configuracion.cambio) {
            mostrarProgreso(true);
            Configuracion.cambio = false;
            new ObtenerAsincrono(getContext(), Configuracion.INTENT_LISTA_EMPRESAHABILITADA, tabla, columnas)
                    .execute(peticionlistar);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Desregistrar receptor
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(receptorMensaje);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String marcado = (String) lista.getItemAtPosition(position);
        Snackbar.make(view, "Ha marcado el item " + position + " " + marcado, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        marcado = (String) ((ArrayList) datos.get(position)).get(0);
        Log.v("AGET-Enviado", marcado);
        actividadEmpresa(marcado);
    }

    public static void actividadEmpresa(String id) {
        Intent actividad = new Intent(Configuracion.context, GestionArrendatarioCliente.class);
        actividad.putExtra(Configuracion.COLUMNA_EMPRESA_ID, id);
        Configuracion.context.startActivity(actividad);
    }

    protected void actualizar(ArrayList datosMultiples) {
        this.datos = datosMultiples;
        Log.v("AGET-LISTA:", "" + datos.size());
        ArrayList nombres = new ArrayList();
        Log.v("AGET-item:", "" + ((ArrayList) datos.get(0)).size());

        for (int i = 0; i < datos.size() - 1; i++) {
            Log.v("AGET-include:", "" + i);
            Log.v("AGET-valor:", "" + (String) ((ArrayList) datos.get(i)).get(0));
            nombres.add((String) ((ArrayList) datos.get(i)).get(1));
        }

        adaptadorEH = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, nombres);

        //Relacionando la lista con el adaptador
        lista.setAdapter(adaptadorEH);

        //adaptador.insert(grupo, 0);
        adaptadorEH.notifyDataSetChanged();
    }


    private static void mostrarProgreso(boolean mostrar) {
        progressBar.setVisibility(mostrar ? View.VISIBLE : View.GONE);
    }

}
