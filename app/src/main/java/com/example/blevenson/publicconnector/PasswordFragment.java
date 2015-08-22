package com.example.blevenson.publicconnector;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.client.Firebase;

import org.w3c.dom.Text;


public class PasswordFragment extends DialogFragment {
    private Firebase myFirebaseRef;
    private EditText pass;

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        String chatRoom = getArguments().getString("roomName");
        myFirebaseRef = new Firebase("https://publicconnector.firebaseio.com/passwords").child(chatRoom);

        View view =getActivity().getLayoutInflater().inflate(R.layout.fragment_password, null);
        pass=(EditText)view.findViewById(R.id.fragment_password);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.fragment_password, null))
                // Add action buttons
                .setPositiveButton("signin", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(!pass.getText().toString().equals(myFirebaseRef.getKey())){
                            //Wrong password
                        }

                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PasswordFragment.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
