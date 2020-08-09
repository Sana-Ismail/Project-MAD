package com.example.reminder_app;

import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialogboxx extends AppCompatDialogFragment {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    public AlertDialog.Builder getBuilder() {
        builder.setTitle("Warning...!")
                .setMessage("Please select a time and date")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        return builder;
    }
}
