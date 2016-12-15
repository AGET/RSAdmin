package com.aldo.aget.rsadmin.Receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Configuracion.Configuracion;
import com.aldo.aget.rsadmin.Configuracion.Utilidades;
import com.aldo.aget.rsadmin.Control.SQLHelper;

import static android.content.Context.NOTIFICATION_SERVICE;

import com.aldo.aget.rsadmin.Modelo.ManagerDB;
import com.aldo.aget.rsadmin.ServicioWeb.ObtencionDeResultadoBcst;

import java.util.ArrayList;

/**
 * Created by Work on 24/11/16.
 */

public class SMSReceiver extends BroadcastReceiver {
    public static final String TAGLOG = "AGET";

    Context contexto;

    private static String numero = "";
    private static String mensaje = "";

    Intent intentServicio;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAGLOG, "Deteccion de receptor SMS");

        contexto = context;

        ManagerDB db = new ManagerDB(contexto);

        // Obtenemos el contenido del mensaje SMS
        Bundle bundle = intent.getExtras();
        SmsMessage[] mensjs = null;
        String str = "";
        // Si el mensaje contiene algo
        if (bundle != null) {
            // Obtenemos la información del SMS recibido. El campo PDU significa “Protocol Description Unit”
            // y es el formato estándar de los mensajes cortos SMS
            Object[] pdus = (Object[]) bundle.get("pdus");
            // Pasamos todos los mensajes recibidos en formato pdu a una matriz del tipo SmsMessage que contendrá
            // los mensajes en un formato interno y accesible por Android.
            // Definimos el tamaño de la matriz con el no de mensaje recibidos
            mensjs = new SmsMessage[pdus.length];
            // Recorremos todos los mensajes recibidos
            for (int i = 0; i < mensjs.length; i++) {
                // Pasamos el elemento i a la matriz de mensajes
                mensjs[i] =
                        SmsMessage.createFromPdu((byte[]) pdus[i]);
                numero = mensjs[i].getOriginatingAddress();
                // Guardamos en texto plano el mensaje
                str += "SMS de" + numero;

                mensaje = mensjs[i].getMessageBody().toString();
                str += ".Mensaje:";
                str += mensaje;
                str += "\n";
            } // end for
            // Mostramos un mensaje indicando que se ha recibido el mensaje
            Log.d(TAGLOG, "info:" + str + "  tamanio: " + mensaje.length());

            numero = numero.replaceAll("\\s", "");
            numero = numero.replaceAll("-", "");
            if (numero.substring(0, 1).equalsIgnoreCase("+")) {
                numero = numero.substring(3);
            }

            if (db.existeDato(SQLHelper.TABLA_GPS, SQLHelper.COLUMNA_GPS_TELEFONO, numero)) {
                if (mensaje.substring(0, 4).equalsIgnoreCase("lat:")) {
                    parse();
                }
            }
        } // end if bundle != null
    }

    private void parse() {

        try {

            char caracteres[] = mensaje.toCharArray();
            double lat = 0;
            lat = getLatitud(caracteres);
            double lon = 0;
            lon = getLongitud(caracteres);
            if ((lat != 0 || lat != 0.0) && lon != 0 || lon != 0.0) {
                if(comporbarStatus()){
                    ManagerDB db = new ManagerDB(contexto);
                    String[][] datos;
//                    datos = db.obtenerDatos(SQLHelper.TABLA_ENLACE,new String[]{SQLHelper.COLUMNA_ENLACE_ID_REMOTO,
//                            SQLHelper.COLUMNA_ENLACE_USUARIO_ID,SQLHelper.COLUMNA_ENLACE_GPS_ID,
//                            SQLHelper.COLUMNA_ENLACE_USUARIO_NOMBRE},SQLHelper.COLUMNA_GPS_TELEFONO
//                            ,new String[]{numero});
                    datos = db.obtenerConInner(numero);

                    for (int i = 0; i < datos.length ;i++) {
//                        for(int j = 0 ; j < datos[i].length ; j ++){
//                            Log.v(Utilidades.TAGLOG,""+datos[i][j]);
//                        }
                        String enlace_id = datos[i][0];
                        String usuario_id = datos[i][1];
                        String gps_id = datos[i][2];


                        peticionRegistroCoordenadas(enlace_id,lon,lat);

                        Log.v(Utilidades.TAGLOG,"****");
                    }
                }
            }

            ManagerDB db = new ManagerDB(contexto);


        } catch (StringIndexOutOfBoundsException e) {
            Toast.makeText(contexto, "Error: " + e.getMessage() + "\nRebice el comando", Toast.LENGTH_SHORT).show();
        }

    }


    public double getLatitud(char[] caracteres) {
        char auxiliar = 0;
        String suma = "";
        double latid = 0;
        for (int i = 0; i < caracteres.length; i++) {
            auxiliar = caracteres[i];
            if (auxiliar == ':') {
                if (caracteres[i - 1] == 't' && caracteres[i - 2] == 'a') {
                    suma += caracteres[i + 1];
                    for (int j = i + 2; j < caracteres.length; j++) {
                        if (!esCoordenada(caracteres[j])) {
                            Log.v("NoCordenada", "" + caracteres[j]);
                            j = caracteres.length;
                        } else {
                            suma += caracteres[j];
                        }
                    }
                    i = caracteres.length;
                }
            }
        }
        Log.v("suma", "" + suma);
        try {
            latid = Double.parseDouble(suma);

        } catch (NumberFormatException ex) {
            latid = 0.0;
        }
        return latid;
    }

    public double getLongitud(char[] caracteres) {
        char auxiliar = 0;
        String suma = "";
        double longitud = 0;
        for (int i = 0; i < caracteres.length; i++) {
            auxiliar = caracteres[i];
            if (auxiliar == ':') {
                if (caracteres[i - 1] == 'n' && caracteres[i - 2] == 'o') {
                    suma += caracteres[i + 1];
                    for (int j = i + 2; j < caracteres.length; j++) {
                        if (!esCoordenada(caracteres[j])) {
                            Log.v("NoCordenada", "" + caracteres[j]);
                            j = caracteres.length;
                        } else {
                            suma += caracteres[j];
                        }
                    }
                    i = caracteres.length;
                }
            }
        }
        Log.v("suma", "" + suma);
        try {
            longitud = Double.parseDouble(suma);
        } catch (NumberFormatException ex) {
            longitud = 0.0;
        }

        return longitud;
    }

    public boolean esCoordenada(int caracter) {
        boolean coordenada = false;
        if (caracter == 46 || (caracter > 47 && caracter < 58)) {
            coordenada = true;
            Log.v("numero", "esNumero: " + caracter);
        } else {
            coordenada = false;
            Log.v("numero", "noEsNumero: " + caracter);
        }
        return coordenada;
    }

    public boolean comporbarStatus() {
        ArrayList dato;
        ManagerDB managerBD = new ManagerDB(contexto);
        String[] datos = {SQLHelper.COLUMNA_EMPRESA_ID_REMOTO, SQLHelper.COLUMNA_EMPRESA_STATUS};
        dato = managerBD.obtenerDatos(SQLHelper.TABLA_EMPRESA_CLIENTE, datos, null, null);
        if (dato.get(1) != null) {
            if (((String) dato.get(1)).equalsIgnoreCase("1"))
                return true;
            else
                return false;
        }
        return false;
    }

    private void getNotification(String tiker, String contentTitle, String contentText, String subText,
                                 String bigText, int icono) {
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        Uri defaultSound = RingtoneManager.getDefaultUri(R.raw.sound);
        long[] pattern = new long[]{1000, 500, 1000};
        NotificationManager nm = (NotificationManager) contexto.getSystemService(NOTIFICATION_SERVICE);
        ;
        final int ID_NOTIFICACION_CREAR = 1;
        Notification.Builder builder = new Notification.Builder(contexto);
        builder.setAutoCancel(false);
        builder.setTicker(tiker);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        builder.setSubText(subText);
        builder.setStyle(new Notification.BigTextStyle()
                .bigText(bigText));
        builder.setSmallIcon(icono);
        builder.setAutoCancel(true);
//        builder.setSound(defaultSound);        // Uso en API 11 o mayor
        builder.setVibrate(pattern);
        nm.notify(ID_NOTIFICACION_CREAR, builder.build());
    }


    public void peticionRegistroCoordenadas(String enlace, double longitud, double latitud) {

        final String columnasFiltroLista[] = {Configuracion.COLUMNA_ENLACE_ID,
                Configuracion.COLUMNA_COORDENADA_LONGITUD,Configuracion.COLUMNA_COORDENADA_LATITUD};


        String[] valorFiltroRemoto = {enlace, String.valueOf(longitud), String.valueOf(latitud)};

        new ObtencionDeResultadoBcst(contexto, Configuracion.INTENT_ADMIN_ENVIAR_COORDENADAS, columnasFiltroLista,
                valorFiltroRemoto, Configuracion.TABLA_USUARIOS, null, false)
                .execute(Configuracion.PETICION_COORDENADAS_REGISTRO, "post");
    }

}