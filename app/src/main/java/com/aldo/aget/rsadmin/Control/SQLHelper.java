package com.aldo.aget.rsadmin.Control;

/**
 * Created by Work on 12/12/16.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {
    Context contexto = null;

    public static final String DATABASE_NAME = "RSAdmin.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TIPO_DATO_TEXT = "TEXT";
    public static final String TIPO_DATO_INTEGER = "INTEGER";

    public static final String COLUMNA_GENERICA_ID = "_id";

    public static final String TABLA_EMPRESA_CLIENTE = "empresa_cliente";
    public static final String COLUMNA_EMPRESA_ID_REMOTO = "empresa_id";
    public static final String COLUMNA_EMPRESA_NOMBRE = "nombre";
    public static final String COLUMNA_EMPRESA_STATUS = "status";

    public static final String TABLA_DEPARTAMENTO = "departamento";
    public static final String COLUMNA_DEPARTAMENTO_ID_REMOTO = "departamento_id";
    public static final String COLUMNA_DEPARTAMENTO_NOMBRE = "nombre";

    public static final String TABLA_GPS = "gps";
    public static final String COLUMNA_GPS_ID_REMOTO = "gps_id";
    public static final String COLUMNA_GPS_IMEI = "imei";
    public static final String COLUMNA_GPS_TELEFONO = "numero";
    public static final String COLUMNA_GPS_DESCRIPCION = "descripcion";
    public static final String COLUMNA_GPS_AUTORASTREO = "autorastreo";
    public static final String COLUMNA_GPS_EMPRESA_ID = "empresa_id";
    public static final String COLUMNA_GPS_DEPARTAMENTO_ID = "departamento_id";


    public static final String TABLA_ENLACE = "enlace";
    public static final String COLUMNA_ENLACE_ID_REMOTO = "enlace_id";
    public static final String COLUMNA_ENLACE_GPS_ID = "gps_id";
    public static final String COLUMNA_ENLACE_USUARIO_ID = "usuario_id";
    public static final String COLUMNA_ENLACE_USUARIO_NOMBRE = "nombre";
    public static final String COLUMNA_ENLACE_USUARIO_AP_PATERNO = "ap_paterno";
    public static final String COLUMNA_ENLACE_USUARIO_AP_MATERNO = "ap_materno";


    public static final String TABLA_ADMINISTRADOR_SISTEMA = "administrador_sistema";
    public static final String COLUMNA_ADMINISTRADOR_ID_REMOTO = "administrador_id";
    public static final String COLUMNA_ADMINISTRADOR_NOMBRE = "nombre";
    public static final String COLUMNA_ADMINISTRADOR_AP_PATERNO = "ap_paterno";
    public static final String COLUMNA_ADMINISTRADOR_AP_MATERNO = "ap_materno";
    public static final String COLUMNA_ADMINISTRADOR_TELEFONO = "telefono";
    public static final String COLUMNA_ADMINISTRADOR_CORREO = "correo";
    public static final String COLUMNA_ADMINISTRADOR_CONTRASE_NA = "contrase_na";


    //Crear tabla empresa_cliente
    private static final String SQL_CREAR_TABLA_EMPRESA_CLIENTE = "CREATE TABLE IF NOT EXISTS "
            + TABLA_EMPRESA_CLIENTE + " ("
            + COLUMNA_GENERICA_ID + " " + TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_EMPRESA_ID_REMOTO + " " + TIPO_DATO_INTEGER + ", "
            + COLUMNA_EMPRESA_NOMBRE + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_EMPRESA_STATUS + " " + TIPO_DATO_TEXT + ")";

    //Crear tabla departamento
    private static final String SQL_CREAR_TABLA_DEPARTAMENTO = "CREATE TABLE IF NOT EXISTS "
            + TABLA_DEPARTAMENTO + " ("
            + COLUMNA_GENERICA_ID + " " + TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_DEPARTAMENTO_ID_REMOTO + ", "
            + COLUMNA_DEPARTAMENTO_NOMBRE + " " + TIPO_DATO_TEXT + " )";

    //Crear tabla GPS
    private static final String SQL_CREAR_TABLA_GPS = "CREATE TABLE IF NOT EXISTS "
            + TABLA_GPS + " ("
            + COLUMNA_GENERICA_ID + " " + TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_GPS_ID_REMOTO + " " + TIPO_DATO_INTEGER + ", "
            + COLUMNA_GPS_IMEI + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_GPS_TELEFONO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_GPS_DESCRIPCION + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_GPS_AUTORASTREO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_GPS_EMPRESA_ID + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_GPS_DEPARTAMENTO_ID + " " + TIPO_DATO_TEXT + ", "
            + "FOREIGN KEY ( " + COLUMNA_GPS_DEPARTAMENTO_ID + " ) REFERENCES " +
            TABLA_DEPARTAMENTO + "( " + COLUMNA_GENERICA_ID + "),"
            + "FOREIGN KEY ( " + COLUMNA_GPS_EMPRESA_ID + " ) REFERENCES " +
            TABLA_EMPRESA_CLIENTE + "( " + COLUMNA_GENERICA_ID + "))";

    public static final String SQL_CREAR_TABLA_ADMINISTRADOR_SISTEMA =  "CREATE TABLE IF NOT EXISTS "
            + TABLA_ADMINISTRADOR_SISTEMA + " ("
            + COLUMNA_GENERICA_ID + " " + TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_ADMINISTRADOR_ID_REMOTO + " " + TIPO_DATO_INTEGER + ", "
            + COLUMNA_ADMINISTRADOR_NOMBRE + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ADMINISTRADOR_AP_PATERNO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ADMINISTRADOR_AP_MATERNO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ADMINISTRADOR_TELEFONO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ADMINISTRADOR_CORREO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ADMINISTRADOR_CONTRASE_NA + " " + TIPO_DATO_TEXT + ") ";


    //Crear tabla enlace
    private static final String SQL_CREAR_TABLA_ENLACE = "CREATE TABLE IF NOT EXISTS "
            + TABLA_ENLACE + " ("
            + COLUMNA_GENERICA_ID + " " + TIPO_DATO_INTEGER + " PRIMARY KEY AUTOINCREMENT, "
            + COLUMNA_ENLACE_ID_REMOTO + " " + TIPO_DATO_INTEGER + ", "
            + COLUMNA_ENLACE_USUARIO_ID + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ENLACE_GPS_ID + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ENLACE_USUARIO_NOMBRE + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ENLACE_USUARIO_AP_PATERNO + " " + TIPO_DATO_TEXT + ", "
            + COLUMNA_ENLACE_USUARIO_AP_MATERNO + " " + TIPO_DATO_TEXT + ", "
            + "FOREIGN KEY ( " + COLUMNA_ENLACE_GPS_ID + " ) REFERENCES " + TABLA_GPS + "( " + COLUMNA_GENERICA_ID + " ))";

    public SQLHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        contexto = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREAR_TABLA_EMPRESA_CLIENTE);
        db.execSQL(SQL_CREAR_TABLA_DEPARTAMENTO);
        db.execSQL(SQL_CREAR_TABLA_GPS);
        db.execSQL(SQL_CREAR_TABLA_ENLACE);
        db.execSQL(SQL_CREAR_TABLA_ADMINISTRADOR_SISTEMA);

        /*Toast t1 = Toast.makeText(contexto, SQL_CREAR_TABLA_AUXILIAR, Toast.LENGTH_LONG);
        t1.setGravity(Gravity.TOP | Gravity.LEFT, 60, 110);
        t1.show();
        Toast t2 = Toast.makeText(contexto,SQL_INSERTAR_DATOS_DEFAULT_AUXILIAR,Toast.LENGTH_LONG);
        t2.setGravity(Gravity.BOTTOM | Gravity.RIGHT,60,100);*/
        // TODO Auto-generated method stub
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        // se elimina la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //db.execSQL("DROP TABLE IF EXISTS TablaPrueba");
        //db.execSQL(codeSQL);
    }
}
