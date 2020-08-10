package com.gwu.studentservicesapp.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.view.ApartmentActivity;
import com.gwu.studentservicesapp.view.ApartmentListing;
import com.gwu.studentservicesapp.view.ItemsListActivity;

public class AlertDialogForApartment extends DialogFragment {
    private int mSelectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialog);
        builder.setTitle(R.string.pick_option)
                .setSingleChoiceItems(R.array.select_action_apartment, 0,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelectedItem = which;
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if( getResources().getStringArray(R.array.select_action_apartment)[mSelectedItem].equals("Post Vacancy")){
                            Intent i = new Intent(getActivity(), ApartmentActivity.class);
                            startActivity(i);
                        }
                        else if(getResources().getStringArray(R.array.select_action_apartment)[mSelectedItem].equals("Look for rent")){
                            Intent i = new Intent(getActivity(), ApartmentListing.class);
                            startActivity(i);
                        }
                        //Toast.makeText(getActivity(), "You selected !! \n " + getResources().getStringArray(R.array.select_action)[mSelectedItem], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }
}
