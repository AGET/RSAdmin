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
    public static final String SERVIDOR = "http://192.168.0.100";
    //public static final String SERVIDOR = "http://192.168.2.140";
    //public static final String SERVIDOR = " http://aldogamaliel.byethost16.com";
    //http://aldogamaliel.byethost16.com/api.rs.com/v1/gps/listarVarios
    public static String idActual = "";

    /*PETICIONES  */
    //PETICIONES GPS
    public static final String PETICION_GPS_LISTAR_VARIOS = SERVIDOR + "/api.rs.com/v1/gps/listarVarios";
    public static final String PETICION_GPS_LISTAR_LIBRES = SERVIDOR + "/api.rs.com/v1/gps/listarLibres";
    public static final String PETICION_GPS_LISTAR_UNO = SERVIDOR + "/api.rs.com/v1/gps/listarUno_Id";
    public static final String PETICION_GPS_LISTAR_DEPARTAMENTO = SERVIDOR + "/api.rs.com/v1/gps/listarGpsDeDepartamento";
    public static final String PETICION_GPS_LISTAR_DIASPONIBLES_A_ENLAZAR = SERVIDOR + "/api.rs.com/v1/gps/listarGpsDeDepartamentoDisponiblesAEnlace";


    public static final String PETICION_GPS_LISTAR_DISPONOBLES_EMPRESA = SERVIDOR + "/api.rs.com/v1/gps/listarGpsDeDeparamentoDisponiblesParaEnlace";
    public static final String PETICION_GPS_LISTAR_ENLACES_USUARIO = SERVIDOR + "/api.rs.com/v1/gps/listarGpsUsuarioEnlazados";
    public static final String PETICION_GPS_SUSTITUIR = SERVIDOR + "/api.rs.com/v1/gps/sustituirGps";
    public static final String PETICION_GPS_REGISTRO = SERVIDOR + "/api.rs.com/v1/gps/registro";
    public static final String PETICION_GPS_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/gps/";


    //PETICION EMPRESA_CLIENTE
    public static final String PETICION_EMPRESA_LISTAR_VARIOS = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarVarios";
    public static final String PETICION_EMPRESA_LISTAR_HABILITADOS = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarHabilitado";
    public static final String PETICION_EMPRESA_LISTAR_DESHABILITADOS = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarDeshabilitado";
    public static final String PETICION_EMPRESA_LISTAR_POR_NOMBRE = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarPorNombre";
    public static final String PETICION_EMPRESA_LISTAR_POR_ID = SERVIDOR + "/api.rs.com/v1/empresa_cliente/listarUno_Id";
    public static final String PETICION_EMPRESA_REGISTRO = SERVIDOR + "/api.rs.com/v1/empresa_cliente/registro";
    public static final String PETICION_EMPRESA_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/empresa_cliente/";

    //PETICION DEPARTAMENTO
    public static final String PETICION_DEPARTAMENTO_LISTAR_VARIOS = SERVIDOR + "/api.rs.com/v1/departamento/listarVarios";
    public static final String PETICION_DEPARTAMENTO_LISTAR_POR_ID = SERVIDOR + "/api.rs.com/v1/departamento/listarUno_Id";
    public static final String PETICION_DEPARTAMENTO_REGISTRO = SERVIDOR + "/api.rs.com/v1/departamento/registro";
    public static final String PETICION_DEPARTAMENTO_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/departamento/";
    public static final String PETICION_DEPARTAMENTO_LISTAR_DE_EMPRESA = SERVIDOR + "/api.rs.com/v1/departamento/listarDeEmpresa";


    //PETICION USUARIOS
    public static final String PETICION_USUARIO_REGISTRO = SERVIDOR + "/api.rs.com/v1/usuarios/registro";
    public static final String PETICION_USUARIO_LISTAR_VARIOS = SERVIDOR + "/api.rs.com/v1/usuarios/listarVarios";
    public static final String PETICION_USUARIO_LISTAR_UNO = SERVIDOR + "/api.rs.com/v1/usuarios/listarUno_Id";
    public static final String PETICION_USUARIO_LISTAR_GPS = SERVIDOR + "/api.rs.com/v1/usuarios/listarGpsDeUsuario";//gps enlazados de un usuario
    public static final String PETICION_USUARIO_LISTAR_USUARIO_DEPARTAMENTO = SERVIDOR + "/api.rs.com/v1/usuarios/listarUsuariosDeDepartamento";
    public static final String PETICION_USUARIO_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/usuarios/";


    //PETICION ENLACE

    public static final String PETICION_ENLACE_REGISTRO = SERVIDOR + "/api.rs.com/v1/enlace/registro";
    public static final String PETICION_ENLACE_LISTAR_TELEFONOS = SERVIDOR + "/api.rs.com/v1/enlace/listarTelefonos";
    public static final String PETICION_ENLACE_MODIFICAR_ELIMINAR = SERVIDOR + "/api.rs.com/v1/enlace/";


    //PETICION



    //    Tablas
    public static final String TABLA_GPS = "gps";
    public static final String TABLA_EMPRESA_CLIENTE = "empresa_cliente";
    public static final String TABLA_ENLACE = "enlace";
    public static final String TABLA_DEPARTAMENTO = "departamento";
    public static final String TABLA_USUARIOS = "usuarios";

    //    Columnas
//GPS
    public static final String COLUMNA_GPS_ID = "gps_id";
    public static final String COLUMNA_GPS_IMEI = "imei";
    public static final String COLUMNA_GPS_NUMERO = "numero";
    public static final String COLUMNA_GPS_DESCRIPCION = "descripcion";
    public static final String COLUMNA_GPS_AUTORASTREO= "autorastreo";
    public static final String COLUMNA_GPS_DEPARTAMENTO = "departamento_id";


    //    EMPRESA CLIETNE
    public static final String COLUMNA_EMPRESA_ID = "empresa_id";
    public static final String COLUMNA_EMPRESA_NOMBRE = "nombre";
    public static final String COLUMNA_EMPRESA_TELEFONO = "telefono";
    public static final String COLUMNA_EMPRESA_CORREO = "correo";
    public static final String COLUMNA_EMPRESA_STATUS = "status";

    //DEPARTAMENTOS
    public static final String COLUMNA_DEPARTAMENTO_ID = "departamento_id";
    public static final String COLUMNA_DEPARTAMENTO_NOMBRE = "nombre";
    public static final String COLUMNA_DEPARTAMENTO_TELEFONO = "telefono";
    public static final String COLUMNA_DEPARTAMENTO_CORREO = "correo";
    public static final String COLUMNA_DEPARTAMENTO_DIRECCION = "direccion";
    public static final String COLUMNA_DEPARTAMENTO_EMPRESA_ID = "empresa_id";

    //USUARIOS
    public static final String COLUMNA_USUARIO_ID = "usuario_id";
    public static final String COLUMNA_USUARIO_NOMBRE = "nombre";
    public static final String COLUMNA_USUARIO_AP_PATERNO = "ap_paterno";
    public static final String COLUMNA_USUARIO_AP_MATERNO = "ap_materno";
    public static final String COLUMNA_USUARIO_TELEFONO = "telefono";
    public static final String COLUMNA_USUARIO_CORREO = "correo";
    public static final String COLUMNA_USUARIO_USUARIO = "usuario";
    public static final String COLUMNA_USUARIO_CONTRASE_NA = "contrase_na";
    public static final String COLUMNA_USUARIO_DEPARTAMENTO_ID = "departamento_id";

    //ENLACE
    public static final String COLUMNA_ENLACE_ID = "enlace_id";
    public static final String COLUMNA_ENLACE_USUARIO = "usuario_id";
    public static final String COLUMNA_ENLACE_GPS = "gps_id";

    //Broadcast
    public static final String INTENT_LISTA_EMPRESA = "com.aldo.aget.rsadmin.Vistas.ListaEmpresa";
    public static final String INTENT_LISTA_EMPRESAHABILITADA = "com.aldo.aget.rsadmin.Vistas.ListaEmpresaHabilitada";
    public static final String INTENT_LISTA_EMPRESADESHABILITADA = "com.aldo.aget.rsadmin.Vistas.ListaEmpresaDeshabilitada";
    public static final String INTENT_LISTA_GPS = "com.aldo.aget.rsadmin.Vistas.ListaGps";

    //RECEPTORES EN EMPRESA_CLIENTE
    public static final String INTENT_EMPRESA_CLIENTE = "com.aldo.aget.rsadmin.Vistas.GestionArrendatarioCliente";
    public static final String INTENT_EMPRESA_CLIENTE_ENLACE_TELEFONOS_ENLAZADOS = "com.aldo.aget.rsadmin.Vistas.GestionArrendatarioCliente.TelefonosEnlazados";

    //RECEPTORES DEPARTAMENTO
    public static final String INTENT_LISTA_DEPARTAMENTO = "com.aldo.aget.rsadmin.Vistas.ListaDepartamento";
    public static final String INTENT_GESTION_DEPARTAMENTO = "com.aldo.aget.rsadmin.Vistas.ListaDepartamento";
    public static final String INTENT_DEPARTAMENTO_ENLACE_TELEFONOS_ENLAZADOS = "com.aldo.aget.rsadmin.Vistas.Departamento.TelefonosEnlazados";

    //RECEPTORES
    public static final String INTENT_LISTA_USUARIOS = "com.aldo.aget.rsadmin.Vistas.ListaUsuarios";
    public static final String INTENT_GESTION_USUARIO = "com.aldo.aget.rsadmin.Vistas.GestionUsuario";
    public static final String INTENT_GESTION_USUARIO_LISTA = "com.aldo.aget.rsadmin.Vistas.GestionUsuario.Lista";
    public static final String INTENT_GESTION_USUARIO_GPS_EN_DEPARTAMENTO = "com.aldo.aget.rsadmin.Vistas.GestionUsuario.Gps.Departamento";
    public static final String INTENT_GESTION_USUARIO_REGISTRO_ENLACE = "com.aldo.aget.rsadmin.Vistas.GestionUsuario.Registro.Enlace";
    public static final String INTENT_GESTION_USUARIO_LISTA_GPS_DISPONIBLE = "com.aldo.aget.rsadmin.Vistas.GestionUsuario.Lista.Gps.Disponible";
    public static final String INTENT_GESTION_USUARIO_DESVINICULAR_UN_GPS = "com.aldo.aget.rsadmin.Vistas.GestionUsuario.Lista.Gps.Desvincular.Un.Gps";


    //RECEPTORES GPS
    public static final String INTENT_GPS= "com.aldo.aget.rsadmin.Vistas.Gps";

    public static final String INTENT_GPS_DEPARTAMENTO = "com.aldo.aget.rsadmin.Vistas.GpsDepartamento";
    public static final String INTENT_GPS_EMPRESA_AGREGADOS = "com.aldo.aget.rsadmin.Vistas.GpsEmpresa_Agregados";

    public static Boolean cambio = false;
}

/*{
  "estado": 1,
  "gps": [
    {
      "imei": "321654987456132",
      "numero": "1234567895",
      "descripcion": "soy un gps nuevo",
      "empresa_id": "2"
    }
  ]
}
*/

/*
{
        "estado": 1
        "empresa_cliente": {
        "empresa_id": "2"
        "nombre": "S.A. de R.L."
        "telefono": "4568971"
        "correo": "sa@rl.com"
        "status": "1"
        }-
        }

*/