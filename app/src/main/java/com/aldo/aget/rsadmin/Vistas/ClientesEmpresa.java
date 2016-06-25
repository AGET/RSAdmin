package com.aldo.aget.rsadmin.Vistas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.R;

public class ClientesEmpresa extends AppCompatActivity {
    String idEmpresa, nombreEmpresa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes_empresa);
        agregarToolbar();


        Bundle bundle = getIntent().getExtras();
        idEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_ID);
        nombreEmpresa = bundle.getString(Configuracion.COLUMNA_EMPRESA_NOMBRE);
        setTitle("Usuarios de " + nombreEmpresa);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
