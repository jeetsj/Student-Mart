package com.gwu.studentservicesapp.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gwu.studentservicesapp.presenter.InputValidation;
import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.db.UserDB;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button loginBtn;
    private EditText email_id;
    private EditText password;
    private TextView signuplink;
    private InputValidation inputValidation;
    private UserDB userDB;
    private final AppCompatActivity activity = LoginActivity.this;
    public static String Profile_USERNAME;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        loginBtn = findViewById(R.id.btn_login);
        email_id = findViewById(R.id.ap_name);
        password = findViewById(R.id.ap_password);
        signuplink = findViewById(R.id.link_signup);
    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        loginBtn.setOnClickListener(this);
        signuplink.setOnClickListener(this);
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        userDB = new UserDB(activity);
        inputValidation = new InputValidation(activity);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                verifyFromSQLite();
                break;
            case R.id.link_signup:
                // Navigate to RegisterActivity
                Intent i = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(i);
                break;
        }
    }

        /**
         * This method is to validate the input text fields and verify login credentials from SQLite
         */
        private void verifyFromSQLite() {
            if (!inputValidation.isInputEditTextFilled(email_id, getString(R.string.emptyMessage))) {
                return;
            }
            if (!inputValidation.isInputEditTextFilled(password, getString(R.string.password))) {
                return;
            }

            if (userDB.checkUser(email_id.getText().toString().trim()
                    , password.getText().toString().trim()) || userDB.checkUsername(email_id.getText().toString(),password.getText().toString()))  {
                Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
                intent.putExtra("Name", email_id.getText().toString().trim());

               /* Bundle bundle = new Bundle();
                bundle.putString("user_name",userDB.getColumnUserName(email_id.getText().toString().trim()));
//              bundle.putString("user_phone",userDB.getColumnUserPhone(email_id.getText().toString().trim()));
                intent.putExtra("userData",bundle);*/

                startActivity(intent);
                Profile_USERNAME = intent.getStringExtra("Name");
                finish();

            }
            else {
                 //error message that record is wrong
                Toast toast = Toast.makeText(getApplicationContext(), "User does not exists!!", Toast.LENGTH_LONG);
                toast.show();
            }
        }

        /**
         * This method is to empty all input edit text
         */
        private void emptyInputEditText() {
            email_id.setText(null);
            password.setText(null);
        }
    }

