package com.gwu.studentservicesapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.db.UserDB;
import com.gwu.studentservicesapp.model.User;
import com.gwu.studentservicesapp.presenter.InputValidation;


import static com.gwu.studentservicesapp.Utils.UIUtils.progressDialog;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = RegistrationActivity.this;

    private Button registerBtn;
    private EditText username,firstname,lastname,password,emailID,phoneNumber,confirmPassword;
    private Button cancelButton;
    private InputValidation inputValidation;
    private UserDB userDB;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_layout);
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        registerBtn = findViewById(R.id.btn_register);
        firstname = findViewById(R.id.ap_fname);
        lastname = findViewById(R.id.ap_lname);
        username = findViewById(R.id.ap_username);
        password = findViewById(R.id.ap_password);
        emailID = findViewById(R.id.ap_email);
        confirmPassword = findViewById(R.id.ap_confirmpassword);
        phoneNumber = findViewById(R.id.ap_phone);
        cancelButton = findViewById(R.id.cancel_button);
    }

    private void initListeners() {
        registerBtn.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        inputValidation = new InputValidation(activity);
        userDB = new UserDB(activity);
        user = new User();
    }

    public void onSignupSuccess() {
        registerBtn.setEnabled(true);
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {
        if (!inputValidation.isInputEditTextFilled(username, getString(R.string.emptyMessage))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(firstname, getString(R.string.emailValidation))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(lastname, getString(R.string.emailValidation))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(emailID, getString(R.string.emailValidation))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(password, getString(R.string.emptyMessage))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(confirmPassword, getString(R.string.emptyMessage))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(password, confirmPassword, getString(R.string.password_match_error))) {
            return;
        }
        if (!inputValidation.isEmailValid(emailID.getEditableText().toString().trim())) {
            emailID.setError(getString(R.string.emailValidation));
            return;
        }
        if(phoneNumber.getText().toString().isEmpty()){
            phoneNumber.requestFocus();
            phoneNumber.setError("Please Enter Phone Number");
        }
        if (!Patterns.PHONE.matcher(phoneNumber.getText().toString()).matches()) {
            phoneNumber.requestFocus();
            return;
        }
        if (!userDB.checkUser(emailID.getText().toString().trim()) && (!userDB.checkUsername(username.getText().toString().trim()))) {

            user.setUsername(username.getText().toString().trim());
            String name = firstname.getText().toString() +" "+lastname.getText().toString();
            user.setPname(name);
            user.setEmail(emailID.getText().toString().trim());
            user.setPassword(password.getText().toString().trim());
            user.setPhoneNo(phoneNumber.getText().toString().trim());
            userDB.addUser(user);
            progressDialog(this, "Creating Account...");
            //On Successful RegistrationActivity
            Toast toast = Toast.makeText(getApplicationContext(), "Registered successfully!!", Toast.LENGTH_LONG);
            toast.show();
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onSignupSuccess();
                        }
                    }, 3000);

        } else {
            //error message that record already exists
            Toast toast = Toast.makeText(getApplicationContext(), "User already exists!!", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                postDataToSQLite();
                break;
            case R.id.cancel_button:
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
        }
    }
}
