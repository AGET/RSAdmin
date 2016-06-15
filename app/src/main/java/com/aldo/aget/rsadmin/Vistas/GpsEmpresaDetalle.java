package com.aldo.aget.rsadmin.Vistas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.R;

public class GpsEmpresaDetalle extends AppCompatActivity {

    EditText edtImei, edtTelefono, edtDescripcion;

    String imei,numero,descripcion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps_empresa_detalle);
        agregarToolbar();
        setTitle("Detalle del Gps");

        Bundle bundle = getIntent().getExtras();
        imei  = bundle.getString(Configuracion.COLUMNA_GPS_IMEI);
        numero  = bundle.getString(Configuracion.COLUMNA_GPS_NUMERO);
        descripcion  = bundle.getString(Configuracion.COLUMNA_GPS_DESCRIPCION);

        edtImei = (EditText) findViewById(R.id.edt_imei_gps);
        edtTelefono = (EditText) findViewById(R.id.edt_telefono_gps);
        edtDescripcion = (EditText) findViewById(R.id.edt_descripcion_gps);

        cargarComponetntes();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void cargarComponetntes(){
        edtImei.setText(imei);
        edtTelefono.setText(numero);
        edtDescripcion.setText(descripcion);
    }

    private void agregarToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
}
