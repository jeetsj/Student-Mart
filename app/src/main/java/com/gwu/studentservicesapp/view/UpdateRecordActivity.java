package com.gwu.studentservicesapp.view;

import android.Manifest;
import android.app.AlertDialog;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.Utils.ImageHelper;
import com.gwu.studentservicesapp.model.Item;
import com.gwu.studentservicesapp.model.db.ItemDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UpdateRecordActivity extends AppCompatActivity implements View.OnClickListener {

    String itemID, itemName;
    private Button updateProductBtn,update_cancelBtn;
    private EditText uproductDescription,uproductPrice,ustreet,uzipcode;
    private TextView update_productName;
    private AutoCompleteTextView ucity,ustate;
    String json_string;
    JSONObject jsonObj;
    SQLiteDatabase sqLiteDatabase;
    String stateSelection,citySelection;
    ItemDB itemDB;
    Item item;
    private byte[] profileImage = null;
    private final int Camera_Request=0;
    private final int Gallery_Request=1;
    ImageButton itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_update_record);

        itemID = getIntent().getExtras().getString("Item_ID");
        itemName = getIntent().getExtras().getString("Item_Name");
        itemDB = new ItemDB(this);
        item = new Item();
        initViews();
        initListeners();
        displayData();
    }

    private void initViews() {
        updateProductBtn = findViewById(R.id.btn_update_product);
        update_productName = findViewById(R.id.update_product_name);
        uproductDescription = findViewById(R.id.update_product_description);
        uproductPrice = findViewById(R.id.update_product_price);
        ustreet = findViewById(R.id.update_street_address);
        ucity = findViewById(R.id.update_city_address);
        ustate = findViewById(R.id.update_state_address);
        uzipcode = findViewById(R.id.update_zip_address);
        itemImage = findViewById(R.id.updateImageButton);
        update_cancelBtn = findViewById(R.id.btn_update_cancel);
    }

    private void initListeners() {
        autocompleteAddress();
        updateProductBtn.setOnClickListener(this);
        update_cancelBtn.setOnClickListener(this);
        itemImage.setOnClickListener(this);
    }

    void displayData(){
        sqLiteDatabase = itemDB.getWritableDatabase();
        update_productName.setText(itemName);
        uproductDescription.setText(itemDB.getItemsbyID(itemID).getProductDescription());
        uproductPrice.setText(itemDB.getItemsbyID(itemID).getProductPrice());
        try     {
           String location = itemDB.getItemsbyID(itemID).getProductLocation();
           System.out.println("Location-->"+location);
           String[] parts = location.split(",");
           String street = parts[0];
           String City =  parts[1];
           String State = parts[2];
           String zipcode = parts[3];
           ustreet.setText(street);
           ustate.setText(State);
           ucity.setText(City);
           uzipcode.setText(zipcode);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    void updateData(){
        sqLiteDatabase = itemDB.getWritableDatabase();
        item.setProductDescription(uproductDescription.getText().toString().trim());
        item.setProductPrice(Double.parseDouble(uproductPrice.getText().toString().trim()));
        String updatedStreet = ustreet.getText().toString().trim();
        String updatedZipcode = uzipcode.getText().toString().trim();
        String updatedLocation = updatedStreet+","+ citySelection+ "," + stateSelection +","
                +updatedZipcode;
        item.setProductLocation(updatedLocation);
        itemDB.updateItembyItemID(item,Integer.parseInt(itemID));
        Toast.makeText(getApplicationContext(), "Updated Successfully!!", Toast.LENGTH_LONG).show();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update_product:
                updateData();
                break;
            case R.id.btn_update_cancel:
                Intent i = new Intent(getApplicationContext(), UserItemListActivity.class);
                startActivity(i);
                break;
            case R.id.updateImageButton:
                Option_List();
                break;
        }
    }

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
        ustate.setThreshold(2);//will start working from first character
        ustate.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        ustate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        ucity.setThreshold(2);//will start working from first character
        ucity.setAdapter(cityAdapter);
        ucity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                itemImage.setImageBitmap(bitmap);
                setByteArray(bitmap);
                item.setItemPicture(profileImage);
                itemDB.updateItemPictureByID(item,Integer.parseInt(itemID));
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
                itemImage.setImageBitmap(thumbnail);
                setByteArray(thumbnail);
                item.setItemPicture(profileImage);
                itemDB.updateItemPictureByID(item,Integer.parseInt(itemID));
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
}
