package com.aldo.aget.rsadmin.Vistas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.R;

public class GestionDepartamento extends AppCompatActivity {

    FloatingActionButton fab_usuarios, fab_gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_departamento);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab_usuarios = (FloatingActionButton) findViewById(R.id.fab_departamentos);
        fab_usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                lanzaUsuarios();
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
    }


    void lanzarGpss() {
        Intent inten = new Intent(this, GpsEmpresa.class);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        startActivity(inten);
    }

    void lanzaUsuarios() {
        Intent inten = new Intent(this, ClientesEmpresa.class);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_ID, idEmpresa);
        inten.putExtra(Configuracion.COLUMNA_EMPRESA_NOMBRE, edtNombre.getText().toString());
        startActivity(inten);
    }
}