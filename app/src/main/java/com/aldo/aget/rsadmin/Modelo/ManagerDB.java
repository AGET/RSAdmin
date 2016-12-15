package com.aldo.aget.rsadmin.Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.SQLHelper;

import java.util.ArrayList;

/**
 * Created by AGET on 12/12/16.
 */

public class ManagerDB {

    SQLHelper sqlhelper;
    SQLiteDatabase db;

    public ManagerDB(Context contexto) {
        sqlhelper = new SQLHelper(contexto);
    }

    /**
     *
     * @param tabla
     * @param campos
     * @param where
     * @param datosWhere
     * @return Una lista con los datos de la tabla
     */
    public ArrayList obtenerDatos(String tabla, String[] campos, String where, String[] datosWhere) {
        abrirEscrituraBD();
        ArrayList datosCursor = new ArrayList();
        Cursor c;
        try {
            c = (where != null) ? db.query(tabla, campos, where + "=?", datosWhere, null, null,
                    null) : db.query(tabla, campos, null, null, null, null, null);
        } catch (SQLiteException e) {
            Log.v(Utilidades.TAGLOG, "Error SQLite" + e.getMessage());
            return null;
        }
        if (c.moveToFirst()) {
            do {
                for (int i = 0; i < c.getColumnCount(); i++) {
                    datosCursor.add(c.getString(i));
                }
            } while (c.moveToNext());
        } else
            datosCursor = null;
        return datosCursor;
    }

    //se comprueba que se ahia inseado != -1

    /**
     *
     * @param tabla
     * @param columna
     * @param datosColumnas
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insercionMultiple(String tabla, String columna[], String datosColumnas[]) {
        abrirEscrituraBD();
        ContentValues nuevoRegistro = new ContentValues();
        for (int i = 0; i < columna.length; i++) {
            nuevoRegistro.put(columna[i], datosColumnas[i]);
        }
        long respuesta = db.insert(tabla, null, nuevoRegistro);
        cerrarBD();
        return respuesta;
    }

    /**
     *
     * @param tabla
     * @param columna
     * @param dato
     * @param condicion
     * @param valorCondicion
     * @return the number of rows affected
     */
    public int actualizarUnDato(String tabla, String columna, String dato, String condicion, String valorCondicion) {
        abrirEscrituraBD();
        ContentValues valores = new ContentValues();
        valores.put(columna, dato);
        int num = db.update(tabla, valores, condicion + "=" + valorCondicion, null);
        return num;
    }

    /**
     *
     * @param tabla
     * @param columna
     * @param dato
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise. To remove all rows and get a count pass "1" as the whereClause.
     */
    public boolean eliminarItem(String tabla, String columna, String dato) {
        abrirEscrituraBD();
        db.delete(tabla, columna + "=" + dato, null);
        return true;
    }

    //se comprueba que se ahia eliminado 1 o no lo ahia hecho != 1

    /**
     *
     * @param tabla
     * @return numero de filas afectadas
     */
    public int eliminarTodo(String tabla) {
        abrirEscrituraBD();
        int codigo = db.delete(tabla, null, null);
        Log.v("DELIMINADO ", "DB CODE: " + codigo);
        cerrarBD();
        return codigo;
    }

    /**
     *
     * @param tabla
     * @param columna
     * @param dato
     * @return true si existe, false en caso contrario
     */
    public boolean existeDato(String tabla, String columna, String dato) {
        abrirEscrituraBD();

        Cursor c;
        c = db.query(tabla, new String[]{columna},
                columna + "=?", new String[]{dato}, null, null, null);

        return c.getCount() > 0;
    }

    /**
     *
     * @return true si se inserto, false en caso contrario
     */
//    public boolean autotrakEstablecido() {
//        abrirEscrituraBD();
//        Cursor c;
//        c = db.query(SQLHelper.TABLA_AUTOTRACK, new String[]{SQLHelper.COLUMNA_COMANDO}, null, null, null, null, null);

//        return c.getCount() > 0 ;
//    }

    public void abrirEscrituraBD() {
        db = sqlhelper.getWritableDatabase();
    }

    public void cerrarBD() {
        db.close();
    }
}