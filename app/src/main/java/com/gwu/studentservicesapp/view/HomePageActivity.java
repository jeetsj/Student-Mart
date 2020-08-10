package com.gwu.studentservicesapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.gwu.studentservicesapp.view.fragments.AlertDialogForApartment;
import com.gwu.studentservicesapp.view.fragments.Selection;
import com.gwu.studentservicesapp.R;
import androidx.appcompat.app.AppCompatActivity;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String dialog_single_choice_radioButton = "HomePageActivity.Selection";
    private static final String dialog_choice_apartment = "HomePageActivity.AlertDialogForApartment";
    private ImageButton textbookbtn;
    private ImageButton apartmentbtn;
    private ImageButton furniturebtn;
    private ImageButton clothbtn;
    private Selection selection = new Selection();
    private AlertDialogForApartment alertDialogForApartment = new AlertDialogForApartment();
    public static String category ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        initViews();
        initListeners();
    }

    public void initViews(){
        textbookbtn = findViewById(R.id.textbooksButton);
        apartmentbtn = findViewById(R.id.ApartmentBtn);
        furniturebtn = findViewById(R.id.furniturebutton);
        clothbtn = findViewById(R.id.clothBtn);
    }
    /**
     * This method is to initialize listeners
     */
    private void initListeners() {
        textbookbtn.setOnClickListener(this);
        apartmentbtn.setOnClickListener(this);
        furniturebtn.setOnClickListener(this);
        clothbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.textbooksButton:
                OnClickTextbook();
                category = (String) v.getContentDescription();
                break;
            case R.id.ApartmentBtn:
                category = (String) v.getContentDescription();
                onClickApartment();
                break;
            case R.id.furniturebutton:
                onClickFurniture();
                category = (String) v.getContentDescription();
                break;
            case R.id.clothBtn:
                onClickClothing();
                category = (String) v.getContentDescription();
                break;
        }
    }

    public String getCategory(){
        System.out.println("The catergory is-->"+category);
        return category;
    }

    public void OnClickTextbook(){
        selection.show(getSupportFragmentManager(),dialog_single_choice_radioButton);
    }
    public void onClickFurniture(){
        selection.show(getSupportFragmentManager(),dialog_single_choice_radioButton);
    }
    public void onClickClothing(){
        selection.show(getSupportFragmentManager(),dialog_single_choice_radioButton);
    }
    public void onClickApartment(){
        alertDialogForApartment.show(getSupportFragmentManager(),dialog_choice_apartment);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.logout_action) {
            signOut();
        }
        if(id == R.id.myprofile_action){
            Intent i = new Intent(getApplicationContext(), ProfileUpdateActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
    void signOut() {
        Intent in = getIntent();
        in.getStringExtra("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this,R.style.AlertDialog);
        builder.setTitle("Alert!").
                setMessage("You sure, that you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("finish", true); // if you are checking for this in your other Activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                Toast.makeText(HomePageActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder.create();
        alert11.show();
    }

}
