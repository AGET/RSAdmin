package com.aldo.aget.rsadmin.Modelo;

import android.content.Context;

import com.aldo.aget.rsadmin.Control.SQLHelper;

import java.util.ArrayList;

/**
 * Created by Work on 13/12/16.
 */

public class DB_Departamento {

    Context contexto;
    String nombre;
    int id;

    public DB_Departamento(Context ctx, int id) {
        contexto = ctx;
        this.id = id;
    }

    public void reCuperarDatos() {
        ManagerDB bd = new ManagerDB(contexto);
        ArrayList datos = bd.obtenerDatos(SQLHelper.TABLA_DEPARTAMENTO, new String[]{SQLHelper.COLUMNA_DEPARTAMENTO_NOMBRE},
                SQLHelper.COLUMNA_GENERICA_ID, new String[]{String.valueOf(id)});
        nombre = (String) datos.get(0);
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean guardarDatos() {
        ManagerDB bd = new ManagerDB(contexto);
        long res = bd.insercionMultiple(SQLHelper.TABLA_DEPARTAMENTO, new String[]{SQLHelper.COLUMNA_DEPARTAMENTO_NOMBRE},
                new String[]{nombre});
        return res != -1 ? true : false;
    }
}
