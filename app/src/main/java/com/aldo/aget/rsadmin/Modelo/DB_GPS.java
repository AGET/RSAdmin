package com.aldo.aget.rsadmin.Modelo;

import android.content.Context;

import com.aldo.aget.rsadmin.Control.SQLHelper;

import java.util.ArrayList;

/**
 * Created by Work on 13/12/16.
 */

public class DB_GPS {

    Context contexto;
    String nombre;
    int id;
    String imei, numero, descripcion, autorastreo, empresa_id, departamento_id;

    public DB_GPS(Context ctx, int id) {
        contexto = ctx;
        this.id = id;
    }

    public void reCuperarDatos() {
        ManagerDB bd = new ManagerDB(contexto);
        ArrayList datos = bd.obtenerDatos(SQLHelper.TABLA_GPS, new String[]{SQLHelper.COLUMNA_GPS_IMEI, SQLHelper.COLUMNA_GPS_TELEFONO,
                        SQLHelper.COLUMNA_GPS_DESCRIPCION, SQLHelper.COLUMNA_GPS_AUTORASTREO, SQLHelper.COLUMNA_GPS_DEPARTAMENTO_ID,
                        SQLHelper.COLUMNA_GPS_EMPRESA_ID},
                SQLHelper.COLUMNA_GENERICA_ID, new String[]{String.valueOf(id)});
        imei = (String) datos.get(0);
        numero = (String) datos.get(1);
        descripcion = (String) datos.get(2);
        autorastreo = (String) datos.get(3);
        empresa_id = (String) datos.get(4);
        departamento_id = (String) datos.get(5);
    }

    public String[] getNombre() {
        return new String[]{imei,
                numero,
                descripcion,
                autorastreo,
                empresa_id,
                departamento_id};
    }

    public void setNombre(String imei, String numero, String descripcion,
                          String autorastreo, String empresa_id, String departamento_id) {
        this.imei = imei;
        this.numero = numero;
        this.descripcion = descripcion;
        this.autorastreo = autorastreo;
        this.empresa_id = empresa_id;
        this.departamento_id = departamento_id;
    }

    public boolean guardarDatos() {
        ManagerDB bd = new ManagerDB(contexto);
        long res = bd.insercionMultiple(SQLHelper.TABLA_GPS, new String[]{SQLHelper.COLUMNA_GPS_IMEI, SQLHelper.COLUMNA_GPS_TELEFONO,
                        SQLHelper.COLUMNA_GPS_DESCRIPCION, SQLHelper.COLUMNA_GPS_AUTORASTREO, SQLHelper.COLUMNA_GPS_DEPARTAMENTO_ID,
                        SQLHelper.COLUMNA_GPS_EMPRESA_ID},
                new String[]{imei, numero, descripcion, autorastreo, empresa_id, departamento_id});

        return res != -1 ? true : false;
    }
}
