package com.aldo.aget.rsadmin.Vistas;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.aldo.aget.rsadmin.R;

public class ActivityEmpresa extends AppCompatActivity {
String[] x = {"1","3"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_empresa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.GPS);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "EMpresa", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String g = x[5];
                try {

                    this.finalize();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });


        FloatingActionButton usuarios = (FloatingActionButton) findViewById(R.id.usuarios);
        usuarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Empresa .....", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                String g = x[5];
                try {
                    this.finalize();

                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        });
    }

}
