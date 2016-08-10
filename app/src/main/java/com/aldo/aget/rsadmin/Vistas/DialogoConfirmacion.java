package com.aldo.aget.rsadmin.Vistas;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by Work on 09/08/16.
 */

public class DialogoConfirmacion extends DialogFragment {
String idAEliminar;
    public DialogoConfirmacion() {

    }

    public interface OnConfirmacionDialogListener {
        void onPossitiveButtonClick();
        void onNegativeButtonClick();
    }

    // Interfaz de comunicación
    OnConfirmacionDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createSimpleDialog();
    }

    /**
     * Crea un diálogo de alerta sencillo
     * @return Nuevo diálogo
     */
    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Titulo")
                .setMessage("El Mensaje para el usuario")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onPossitiveButtonClick();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listener.onNegativeButtonClick();
                            }
                        });

        return builder.create();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            listener = (OnConfirmacionDialogListener) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    activity.toString() +
                            " no implementó OnSimpleDialogListener");

        }
    }

}

