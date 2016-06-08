package com.aldo.aget.rsadmin.Configuracion;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.aldo.aget.rsadmin.MainActivity;
/**
 * Created by Work on 22/05/16.
 */
public class Configuracion extends Activity {

    //Herramientas
    //https://materialdesignicons.com/icon/account-plus

    public static MainActivity context = null;
    public static final String SERVIDOR = "http://192.168.1.101/";
    public static String idActual = "";

/*    PETICIONES  */
//    gps
    public static final String PETICION_LISTAR_GPS_LIBRES = SERVIDOR + "/api.rs.com/v1/gps/listarLibres";
    public static final String PETICION_LISTAR_GPS_UNO = SERVIDOR + "/api.rs.com/v1/gps/listarUno_Id";
//    empresa_cliente
    public static final String PETICION_LISTAR_EMPRESAS_HABILITADAS = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarVarios";
    public static final String PETICION_LISTAR_EMPRESAS_POR_NOMBRE = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarPorNombre";
    public static final String PETICION_LISTAR_EMPRESAS_POR_ID = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarUno_Id";
    public static final String PETICION_EMPRESAS_REGISTRO = SERVIDOR + "/api.rs.com/v1/empresa_cliente/registro";
    public static final String PETICION_EMPRESAS_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/empresa_cliente/";



//    Tablas
    public static final String TABLA_GPS = "gps";
    public static final String TABLA_EMPRESA_CLIENTE = "empresa_cliente";


//    Columnas
//GPS
    public static final String COLUMNA_GPS_IMEI = "imei";
    public static final String COLUMNA_GPS_ENLACE = "enlace_id";
    public static final String COLUMNA_GPS_NUMERO = "numero";

//    EMPRESA CLIETNE
public static final String COLUMNA_EMPRESA_ID = "empresa_id";
    public static final String COLUMNA_EMPRESA_NOMBRE = "nombre";
    public static final String COLUMNA_EMPRESA_TELEFONO = "telefono";
    public static final String COLUMNA_EMPRESA_CORREO = "correo";
    public static final String COLUMNA_EMPRESA_STATUS = "status";

    //Broadcast
    public static final String INTENT_LISTA_EMPRESA = "com.aldo.aget.rsadmin.Vistas.ListaEmpresa";
    public static final String INTENT_LISTA_GPS = "com.aldo.aget.rsadmin.Vistas.ListaGps";
    public static final String INTENT_EMPRESA_CLIENTE = "com.aldo.aget.rsadmin.Vistas.ActivityEmpresaCliente";

    public static Boolean cambio = false;
}
