package com.gwu.studentservicesapp.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.buyPage.BuyPage;
import com.gwu.studentservicesapp.view.HomePageActivity;
import com.gwu.studentservicesapp.view.ItemsListActivity;
import com.gwu.studentservicesapp.view.SellPageActivity;

public class Selection extends DialogFragment  {

    private int mSelectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.AlertDialog);
        builder.setTitle(R.string.pick_option)
                .setSingleChoiceItems(R.array.select_action, 0,
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
                        if( getResources().getStringArray(R.array.select_action)[mSelectedItem].equals("Sell")){
                            String cate = HomePageActivity.category;
                            System.out.println("Selling page->"+cate);
                            Intent i = new Intent(getActivity(), SellPageActivity.class);
                            i.putExtra("Item_category", cate);
                            startActivity(i);
                        }
                        else if(getResources().getStringArray(R.array.select_action)[mSelectedItem].equals("Buy")){
                            Intent i = new Intent(getActivity(), BuyPage.class);
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
