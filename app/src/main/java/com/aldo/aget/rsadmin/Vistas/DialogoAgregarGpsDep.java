package com.aldo.aget.rsadmin.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.aldo.aget.rsadmin.R;

/**
 * Created by Work on 05/12/16.
 */

public class DialogoAgregarGpsDep extends DialogFragment {

    Context ctx;
    Activity activity;
    View view;

    public DialogoAgregarGpsDep(Context ctx, Activity activity) {
        this.activity = activity;
        mListener = (DialogListener) activity;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogo();
    }


    public interface DialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    DialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    public View inflar() {
        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
        LayoutInflater inflater = activity.getLayoutInflater();

        // Inflate and set the layout for the dialog
        view = inflater.inflate(R.layout.dialog_agregar_gps_dep, null);
//        final EditText edtuser = (EditText)view.findViewById(R.id.username);
//        final EditText edtpass = (EditText)view.findViewById(R.id.password);

        return view;
    }

    public Dialog crearDialogo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        mListener.onDialogPositiveClick(DialogoAgregarGpsDep.this);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                        dialog.cancel();
                        mListener.onDialogNegativeClick(DialogoAgregarGpsDep.this);
                    }
                });
        return builder.create();
    }
}