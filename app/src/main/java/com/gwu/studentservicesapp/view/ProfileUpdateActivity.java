package com.gwu.studentservicesapp.view;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.Utils.ImageHelper;
import com.gwu.studentservicesapp.model.User;
import com.gwu.studentservicesapp.model.db.UserDB;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.gwu.studentservicesapp.Utils.UIUtils.progressDialog;
import static com.gwu.studentservicesapp.view.LoginActivity.Profile_USERNAME;
import static java.lang.Thread.sleep;

public class ProfileUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    private final AppCompatActivity activity = ProfileUpdateActivity.this;

    ImageButton Edit,Save;
    EditText editPhone,editLocation;
    LoginActivity loginActivity;
    UserDB userDB;
    User user;
    TextView FullName,pusername,pemail;
    ImageButton imageButton;
    CircleImageView circleImageView;
    Button viewItems;
    private byte[] profileImage = null;
    private final int Camera_Request=0;
    private final int Gallery_Request=1;
    SQLiteDatabase sqLiteDatabase;
    String name = Profile_USERNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        initViews();
        initListeners();
        loginActivity = new LoginActivity();
        userDB = new UserDB(this);
        user = new User();
        displayData();
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        FullName = findViewById(R.id.profile_fullname);
        pusername = findViewById(R.id.profile_username);
        pemail = findViewById(R.id.profile_email);
        editPhone = findViewById(R.id.profile_phone);
        editPhone.setEnabled(false);
        editLocation = findViewById(R.id.profile_location);
        editLocation.setEnabled(false);
        circleImageView = findViewById(R.id.profile_image);
        imageButton = findViewById(R.id.chooseimgbtn);
        viewItems = findViewById(R.id.view_Items);
        Edit = findViewById(R.id.btnEdit);
        Save = findViewById(R.id.btnSave);
    }

    private void initListeners() {
        Edit.setOnClickListener(this);
        Save.setOnClickListener(this);
        imageButton.setOnClickListener(this);
        viewItems.setOnClickListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                EditAction();
                break;
            case R.id.btnSave:
                SaveAction();
                break;
            case R.id.chooseimgbtn:
                Option_List();
                break;
            case R.id.view_Items:
                Intent vi = new Intent(getApplicationContext(), UserItemListActivity.class);
                startActivity(vi);
                break;
        }
    }

    public void EditAction(){
        editPhone = findViewById(R.id.profile_phone);
        editPhone.setEnabled(true);
        editLocation = findViewById(R.id.profile_location);
        editLocation.setEnabled(true);
        Edit.setVisibility(View.GONE);
        Save.setVisibility(View.VISIBLE);
    }

    public void SaveAction(){
        editPhone = findViewById(R.id.profile_phone);
        editPhone.setEnabled(false);
        editLocation = findViewById(R.id.profile_location);
        editLocation.setEnabled(false);
        user.setPhoneNo(editPhone.getText().toString());
        user.setLocation(editLocation.getText().toString().trim());
        userDB.updateUser(user,name);
        Toast.makeText(getApplicationContext(), "Updated Successfully!!", Toast.LENGTH_LONG).show();
        //this.recreate();
        Edit.setVisibility(View.VISIBLE);
        Save.setVisibility(View.GONE);
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
        if (resultCode == RESULT_OK) {
            if (requestCode == Camera_Request) {
                Bundle extras = data.getExtras();
                Uri selectedImageUri = data.getData();
                Bitmap bitmap = (Bitmap) extras.get("data");
                bitmap = ImageHelper.getRoundedCornerBitmap(bitmap,73);
                circleImageView.setImageBitmap(bitmap);
                setByteArray(bitmap);
                user.setProfilePicture(profileImage);
                userDB.updateProfilePicture(user,name);
            } else if (requestCode == Gallery_Request) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                thumbnail = ImageHelper.getRoundedCornerBitmap(thumbnail,73);
                circleImageView.setImageBitmap(thumbnail);
                setByteArray(thumbnail);
                user.setProfilePicture(profileImage);
                userDB.updateProfilePicture(user,name);
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
        private void setByteArray(Bitmap bitmap)
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
            profileImage = byteArrayOutputStream.toByteArray();
        }

    public void displayData(){
        sqLiteDatabase = userDB.getWritableDatabase();
        FullName.setText(userDB.getUser(name).getPname());
        pusername.setText(userDB.getUser(name).getUsername());
        pemail.setText(userDB.getUser(name).getEmail());
        editLocation.setText(userDB.getUser(name).getLocation());
        editPhone.setText(userDB.getUser(name).getPhoneNumber());
        if(userDB.getUser(name).getProfilePicture()!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(userDB.getUser(name).getProfilePicture(), 0, userDB.getUser(name).getProfilePicture().length);
            circleImageView.setImageBitmap(bmp);
        }
        else{
            circleImageView.setImageResource(R.drawable.avatar_default);
        }
    }
}
