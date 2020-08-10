package com.gwu.studentservicesapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Apartment;
import com.gwu.studentservicesapp.model.db.ApartmentDB;

import java.io.ByteArrayOutputStream;

import static com.gwu.studentservicesapp.Utils.UIUtils.progressDialog;

public class ApartmentActivity extends AppCompatActivity implements View.OnClickListener {
    private final AppCompatActivity activity = ApartmentActivity.this;


    private Button addPost;
    private Button cancelButton;
    private EditText aptDescription;
    private EditText aptPrice;
    private EditText aptLocation;
    private ApartmentDB apartmentDB;
    private Apartment apartment;
    private ImageButton addImage;
    private final int Camera_Request=0;
    private final int Gallery_Request=1;
    private byte[] profileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment_rent_post);
        initViews();
        initListeners();
        initObjects();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        addPost = findViewById(R.id.btn_add_rental_post);
        aptDescription = findViewById(R.id.apartment_description);
        aptPrice = findViewById(R.id.apartment_price);
        aptLocation = findViewById(R.id.apartment_location);
        cancelButton = findViewById(R.id.btn_apt_cancel);
        addImage = findViewById(R.id.apt_addImageButton);
    }

    private void initListeners() {
        addPost.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        addImage.setOnClickListener(this);
    }
    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        apartmentDB = new ApartmentDB(activity);
        apartment = new Apartment();
    }

    public void onItemSuccess() {
        addPost.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {

        apartment.setAptDescription(aptDescription.getText().toString().trim());
        apartment.setAptPrice(Integer.parseInt(aptPrice.getText().toString().trim()));
        apartment.setAptLocation(aptLocation.getText().toString().trim());
        if (profileImage == null )
        {
            setDrawableResource();
        }
        apartment.setItemPicture(profileImage);
        apartmentDB.addItem(apartment);
        progressDialog(this,"Adding Item...");
        //On Successful RegistrationActivity
        Toast toast = Toast.makeText(getApplicationContext(), "Added Item Successfully!!", Toast.LENGTH_LONG);
        toast.show();
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        onItemSuccess();
                    }
                }, 3000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_rental_post:
                postItem();
                //postDataToSQLite();
                break;
            case R.id.btn_apt_cancel:
                Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(i);
            case R.id.apt_addImageButton:
                Option_List();
        }
    }
    public void Option_List()
    {
        final  CharSequence[] Options={"Click Photo","Select From Gallery","Cancel"};
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Select Option");
        builder.setItems(Options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int selection) {
                if(Options[selection].equals("Click Photo"))
                {
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        CapturePhoto();
                    }
                    else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                            Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Camera_Request);
                    }
                }
                if(Options[selection].equals("Select From Gallery"))
                {
                    if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        selectFromGallery();
                    }
                    else{
                        if(shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)){
                            Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
                        }
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Gallery_Request);
                    }
                }
                if(Options[selection].equals("Cancel"))
                {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void setByteArray(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        profileImage = byteArrayOutputStream.toByteArray();
    }

    private void setDrawableResource()
    {
        Bitmap tempData = BitmapFactory.decodeResource(getResources(), R.drawable.no_image_available);
        setByteArray(tempData);
    }
    public void  CapturePhoto()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, Camera_Request);
        }
    }
    public void selectFromGallery(){
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Gallery_Request);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==Camera_Request)
            {
                Bundle extras = data.getExtras();
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = (Bitmap) extras.get("data");
                addImage.setImageBitmap(bitmap);
                setByteArray(bitmap);
            }
            else if(requestCode==Gallery_Request)
            {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                addImage.setImageBitmap(thumbnail);
                setByteArray(thumbnail);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == Camera_Request){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                CapturePhoto();
            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Needed.", Toast.LENGTH_LONG).show();
            }
        }
        else {

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    void postItem() {
        Intent in = getIntent();
        in.getStringExtra("message");
        AlertDialog.Builder builder = new AlertDialog.Builder(ApartmentActivity.this,R.style.AlertDialog);
        builder.setTitle("Alert!").
                setMessage("You sure, that you want to post this?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                postDataToSQLite();
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
