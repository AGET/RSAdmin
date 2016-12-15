package com.aldo.aget.rsadmin;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import java.util.ArrayList;


/**
 * Created by Work on 06/12/16.
 */
public class TestingDialogos extends DialogFragment {
//    FireMissilesDialogFragment

    Context ctx;

    public Dialog onCreateDialog(final Bundle savedInstanceState, Context ctx) {
        this.ctx = ctx;
        final String msn = savedInstanceState.getString("msn");

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        builder.setMessage(R.string.dialog_fire_missiles)
                .setPositiveButton(R.string.fire, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                        Log.v("AGET", msn);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }


    public Dialog onCreateDialog2(Bundle bundle, Context ctx) {

        this.ctx = ctx;

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialogo_mensaje)
                .setTitle(R.string.titulo_dialogo);

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();


        return builder.create();
    }

    public Dialog onCreateDialog3(Bundle bundle, Context ctx) {
        this.ctx = ctx;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        return dialog;
    }


    public Dialog onCreateDialog4(Bundle savedInstanceState, Context ctx) {
        this.ctx = ctx;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);

        final String sr[] = {"item1", "item2", "item3"};
        builder.setTitle(R.string.pick_color)
//                .setItems(R.array.colors_array, new DialogInterface.OnClickListener() {
                .setItems(sr, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        Log.v("AGET", sr[which]);
                    }
                });
        return builder.create();
    }

    public Dialog onCreateDialog5(Bundle savedInstanceState, Context ctx) {
        this.ctx = ctx;
        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        // Set the dialog title
        builder.setTitle(R.string.pick_toppings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.toppings, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);
                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            Log.v("AGET", ""+ mSelectedItems.get(i));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("AGET", "cancelo");
                    }
                })
                .setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("AGET", "le valio");
                    }
                });

        return builder.create();
    }

    public Dialog onCreateDialog6(Bundle savedInstanceState,Context ctx) {
        this.ctx = ctx;
        final ArrayList mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        // Set the dialog title
        builder.setTitle(R.string.pick_toppings)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setSingleChoiceItems(R.array.toppings,1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.v("AGET", String.valueOf(which));
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        for (int i = 0; i < mSelectedItems.size(); i++) {
                            Log.v("AGET", (String) mSelectedItems.get(i));
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Log.v("AGET", "cancelo");
                    }
                })
                .setNeutralButton(R.string.neutral, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("AGET", "le valio");
                    }
                });

        return builder.create();
    }

    public Dialog onCreateDialog7(Bundle savedInstanceState, Context ctx, Activity activity) {
        this.ctx = ctx;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.ctx);
        // Get the layout inflater
//        LayoutInflater inflater = getActivity().getLayoutInflater();
        LayoutInflater inflater = activity.getLayoutInflater();

        // Inflate and set the layout for the dialog
        View view = inflater.inflate(R.layout.dialog_agregar_gps_dep, null);
//        final EditText edtuser = (EditText)view.findViewById(R.id.username);
//        final EditText edtpass = (EditText)view.findViewById(R.id.password);
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
//                        Log.v("AGET",edtuser.getText().toString());
//                        Log.v("AGET",edtpass.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
                        dialog.cancel();
                    }
                });
        return builder.create();
    }
}