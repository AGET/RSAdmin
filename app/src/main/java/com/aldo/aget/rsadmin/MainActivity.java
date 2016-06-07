package com.aldo.aget.rsadmin;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Vistas.ListaEmpresa;
import com.aldo.aget.rsadmin.Vistas.ListaGps;
//import com.aldo.aget.rsadmin.arecycler.Test;

public class MainActivity extends AppCompatActivity {
    Button btnDispositivos, btnEmpresas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Configuracion.context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabmain = (FloatingActionButton) findViewById(R.id.fabmain);
        fabmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, " OBR", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                obtener();
//                test();
            }
        });

        btnDispositivos = (Button) findViewById(R.id.btndispositivos);
        btnDispositivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadListaGps();
            }
        });
        btnEmpresas = (Button) findViewById(R.id.btnempresas);
        btnEmpresas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actividadListaEmpresa();
            }
        });
    }

    void actividadListaGps() {
        Intent actividad = new Intent(this, ListaGps.class);
        startActivity(actividad);
    }

    void actividadListaEmpresa() {
        Intent actividad = new Intent(this, ListaEmpresa.class);
        startActivity(actividad);
    }

//    void test() {
//        Intent obt = new Intent(this, Test.class);
//        startActivity(obt);
//    }
}