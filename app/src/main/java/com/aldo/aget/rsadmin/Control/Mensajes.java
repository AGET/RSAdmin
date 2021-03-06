package com.aldo.aget.rsadmin.Control;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.aldo.aget.rsadmin.Vistas.GestionArrendatarioCliente;

/**
 * Created by Genexus on 21/06/2016.
 */
public class Mensajes  {
    boolean enviado;
    int totalAEnviar;
    int totalEnviados;
    Context contexto;
    public Mensajes(Context contexto, int cantidad){
        enviado = true;
        this.totalAEnviar = cantidad;
        totalEnviados = 0;
        this.contexto = contexto;
    }

    public void enviarSMSEstablecerIntervalo(String telefonoGps, String comando){
        String mensaje = comando;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(DELIVERED), 0);

        //al enviar el SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS enviado",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(contexto, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(contexto, "No hay servicio de red",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(contexto, "No hay datos de protocolo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(contexto, "Verifique que no este en modo avion \"Radio off\"",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Al entregar el  SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS entregado",
//                                Toast.LENGTH_SHORT).show();

                        totalEnviados++;
                        if(enviado) {
                            if (totalEnviados == totalAEnviar) {
//                                GestionArrendatarioCliente.eliminar(true);
                            }
                        }
                        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(contexto, "SMS no entregado",
//                                Toast.LENGTH_SHORT).show();
                        enviado = false;
                        //GestionArrendatarioCliente.eliminar(false);
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));

        // SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telefonoGps, null,mensaje,sentPI,deliveredPI);
        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

    }

    public void enviarSMSDesvincularUsuario(String telefonoAdesvincular, String numeroGps){
        String mensaje = "noadmin123456 " +telefonoAdesvincular;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(DELIVERED), 0);

        //al enviar el SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS enviado",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(contexto, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        Log.v("SMS","Generic failure");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(contexto, "No hay servicio de red",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(contexto, "No hay datos de protocolo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(contexto, "Verifique que no este en modo avion \"Radio off\"",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Al entregar el  SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS entregado",
//                                Toast.LENGTH_SHORT).show();

                        totalEnviados++;
                        if(enviado) {
                            if (totalEnviados == totalAEnviar) {
//                                GestionArrendatarioCliente.eliminar(true);
                            }
                        }
                        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(contexto, "SMS no entregado",
//                                Toast.LENGTH_SHORT).show();
                        enviado = false;
//                        GestionArrendatarioCliente.eliminar(false);
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));

       // SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numeroGps, null,mensaje,sentPI,deliveredPI);
        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

    }

    public void enviarSMSVincularUsuario(String telefonoAVincular, String numeroGps){
        String mensaje = "admin123456 " +telefonoAVincular;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(DELIVERED), 0);

        //al enviar el SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS enviado",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(contexto, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(contexto, "No hay servicio de red",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(contexto, "No hay datos de protocolo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(contexto, "Verifique que no este en modo avion \"Radio off\"",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Al entregar el  SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS entregado",
//                                Toast.LENGTH_SHORT).show();

                        totalEnviados++;
                        if(enviado) {
                            if (totalEnviados == totalAEnviar) {
//                                GestionArrendatarioCliente.eliminar(true);
                            }
                        }
                        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(contexto, "SMS no entregado",
//                                Toast.LENGTH_SHORT).show();
                        enviado = false;
                        //GestionArrendatarioCliente.eliminar(false);
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));

        // SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(numeroGps, null,mensaje,sentPI,deliveredPI);
        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

    }

    public void enviarRestaurarGps(String telefonoARestaurar){
        String mensaje = "begin123456" ;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(DELIVERED), 0);

        //al enviar el SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS enviado",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(contexto, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(contexto, "No hay servicio de red",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(contexto, "No hay datos de protocolo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(contexto, "Verifique que no este en modo avion \"Radio off\"",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Al entregar el  SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS entregado",
//                                Toast.LENGTH_SHORT).show();

                        totalEnviados++;
                        if(enviado) {
                            if (totalEnviados == totalAEnviar) {
//                                GestionArrendatarioCliente.eliminar(true);
                            }
                        }
                        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(contexto, "SMS no entregado",
//                                Toast.LENGTH_SHORT).show();
                        enviado = false;
                        //GestionArrendatarioCliente.eliminar(false);
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));

        // SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telefonoARestaurar, null,mensaje,sentPI,deliveredPI);
        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

    }

    public void enviarSMSAgregarNuevoGPS(String telefonoAdministrador, String telefonoGpsAgregar){
        String mensaje = "admin123456 " + telefonoAdministrador;

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(contexto, 0,
                new Intent(DELIVERED), 0);

        //al enviar el SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS enviado",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
//                        Toast.makeText(contexto, "Generic failure",
//                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(contexto, "No hay servicio de red",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(contexto, "No hay datos de protocolo",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(contexto, "Verifique que no este en modo avion \"Radio off\"",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //Al entregar el  SMS
        contexto.registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
//                        Toast.makeText(contexto, "SMS entregado",
//                                Toast.LENGTH_SHORT).show();

                        totalEnviados++;
                        if(enviado) {
                            if (totalEnviados == totalAEnviar) {
//                                GestionArrendatarioCliente.eliminar(true);
                            }
                        }
                        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

                        break;
                    case Activity.RESULT_CANCELED:
//                        Toast.makeText(contexto, "SMS no entregado",
//                                Toast.LENGTH_SHORT).show();
                        enviado = false;
                        //GestionArrendatarioCliente.eliminar(false);
                        break;

                }
            }
        }, new IntentFilter(DELIVERED));

        // SmsManager sms = SmsManager.getDefault();
        //sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(telefonoGpsAgregar, null,mensaje,sentPI,deliveredPI);
        Log.v("AGET-SMS","Cantidad A Enviar: "+totalAEnviar+" Enviados: " +totalEnviados);

    }
}
