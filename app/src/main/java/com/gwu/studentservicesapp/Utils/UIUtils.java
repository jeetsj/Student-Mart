package com.gwu.studentservicesapp.Utils;

import android.app.Activity;
import android.app.ProgressDialog;
import com.gwu.studentservicesapp.R;

public class UIUtils {

    public static void progressDialog(Activity activity,String message){
        final ProgressDialog progressDialog = new ProgressDialog(activity, ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(message);
        progressDialog.show();
    }



}
