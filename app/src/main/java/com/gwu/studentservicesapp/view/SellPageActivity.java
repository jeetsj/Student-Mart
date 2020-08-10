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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.gwu.studentservicesapp.model.Item;
import com.gwu.studentservicesapp.model.User;
import com.gwu.studentservicesapp.model.db.ItemDB;
import com.gwu.studentservicesapp.model.db.UserDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.gwu.studentservicesapp.Utils.UIUtils.progressDialog;
import static com.gwu.studentservicesapp.view.LoginActivity.Profile_USERNAME;

public class SellPageActivity extends AppCompatActivity implements View.OnClickListener  {

    private final AppCompatActivity activity = SellPageActivity.this;
    String[] categories = {"Furniture", "Clothes", "Books", "Apartment"};
    private Button addProdcutBtn,cancelBtn;
    private EditText productName,productDescription,productPrice,street,zipcode;
    private ItemDB itemDB;
    private Item item;
    private UserDB userDB;
    private User user;
    private AutoCompleteTextView city,state;
    TextView ItemCategory;
    String catSelection,stateSelection,citySelection;
    String json_string;
    JSONObject jsonObj;
    String name,item_Category;
    private ImageButton addImage;
    private final int Camera_Request=0;
    private final int Gallery_Request=1;
    private byte[] profileImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_main);
        initViews();
        initListeners();
        initObjects();
        name = Profile_USERNAME;
    }

    /**
     * This method is to initialize views
     */
    private void initViews() {
        addProdcutBtn = findViewById(R.id.btn_add_new_product);
        addProdcutBtn.requestFocus();
        productName = findViewById(R.id.product_name);
        productDescription = findViewById(R.id.product_description);
        productPrice = findViewById(R.id.product_price);
        street = findViewById(R.id.street_address);
        city = findViewById(R.id.city_address);
        state = findViewById(R.id.state_address);
        zipcode = findViewById(R.id.zip_address);
        ItemCategory = findViewById(R.id.Item_category);
        cancelBtn = findViewById(R.id.btn_cancel);
        addImage = findViewById(R.id.addImageButton);
        item_Category = getIntent().getExtras().getString("Item_category");
        System.out.println("Item category is-->"+item_Category);
        ItemCategory.setText(item_Category);
    }

    private void initListeners() {
        //autocomplete_Category();
        autocompleteAddress();
        addProdcutBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        addImage.setOnClickListener(this);
    }
   /* public void autocomplete_Category(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categories);
        catView.setThreshold(1); //will start working from first character
        catView.setAdapter(adapter);
        catView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                catSelection = (String) parent.getItemAtPosition(position);
            }
        });
    }*/
    public void autocompleteAddress(){
        json_string= loadJSONFromAsset();
        final ArrayList<String> citiesArr = new ArrayList<String>();
        final ArrayList<String> statesArr = new ArrayList<String>();
        {
            try {
                jsonObj =new JSONObject(json_string);
                JSONObject jsonObject = jsonObj.getJSONObject("State_Name");
                JSONObject keys = jsonObject.getJSONObject("Names");
                JSONArray states = keys.names();
                for(int i =0;i<states.length();i++){
                    statesArr.add(states.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, statesArr);
        state.setThreshold(2);//will start working from first character
        state.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                stateSelection = (String) parent.getItemAtPosition(position);
                try {
                    jsonObj =new JSONObject(json_string);
                    JSONObject jsonObject = jsonObj.getJSONObject("State_Name");
                    JSONObject keys = jsonObject.getJSONObject("Names");
                    JSONArray states = keys.names();
                    for(int i =0;i<states.length();i++){
                        statesArr.add(states.getString(i));
                    }
                    if(statesArr.contains(stateSelection)){
                        JSONArray cities = keys.getJSONArray(stateSelection);
                        citiesArr.clear();
                        for(int j=0;j<cities.length();j++){
                            citiesArr.add(cities.getString(j));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, citiesArr);
        city.setThreshold(2);//will start working from first character
        city.setAdapter(cityAdapter);
        city.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                citySelection= (String) parent.getItemAtPosition(position);
            }
        });
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("States.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * This method is to initialize objects to be used
     */
    private void initObjects() {
        itemDB = new ItemDB(activity);
        item = new Item();
        user = new User();
        userDB = new UserDB(activity);
    }

    public void onItemSuccess() {
        addProdcutBtn.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }
    /**
     * This method is to validate the input text fields and post data to SQLite
     */
    private void postDataToSQLite() {

            item.setProductCategory(item_Category);
            item.setProductName(productName.getText().toString().trim());
            item.setProductDescription(productDescription.getText().toString().trim());
            item.setProductPrice(Integer.parseInt(productPrice.getText().toString().trim()));
            String location = street.getText().toString().trim()+","+ citySelection+ "," + stateSelection +","
                    +zipcode.getText().toString().trim();
            item.setProductLocation(location);
            if (profileImage == null )
            {
                setDrawableResource();
            }
            item.setItemPicture(profileImage);
            user.setUsername(name);
            String email = userDB.getEmail(name);
            user.setEmail(email);
            itemDB.addItem(item,user);
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
            case R.id.btn_add_new_product:
                postItem();
                //postDataToSQLite();
                break;
            case R.id.btn_cancel:
                Intent i = new Intent(getApplicationContext(), HomePageActivity.class);
                startActivity(i);
            case R.id.addImageButton:
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SellPageActivity.this,R.style.AlertDialog);
        builder.setTitle("Alert!").
                setMessage("You sure, that you want to sell this item?");
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
