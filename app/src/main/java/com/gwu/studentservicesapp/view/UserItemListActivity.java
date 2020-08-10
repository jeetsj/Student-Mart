package com.gwu.studentservicesapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.Item;
import com.gwu.studentservicesapp.model.db.ItemDB;
import com.gwu.studentservicesapp.model.db.UserDB;
import com.gwu.studentservicesapp.presenter.UserItemListAdapter;

import java.util.ArrayList;

import static com.gwu.studentservicesapp.model.db.ItemDB.COLUMN_ITEM_PICTURE;
import static com.gwu.studentservicesapp.view.LoginActivity.Profile_USERNAME;

public class UserItemListActivity extends AppCompatActivity {

    ItemDB itemDB;
    Cursor cursor;
    UserItemListAdapter userItemListAdapter;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    UserDB userDB;
    Item item;
    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    ArrayList<String> Description_Array;
    ArrayList<String> Price_Array;
    ArrayList<String> Location_Array;
    ArrayList<String> Category_Array;
    ArrayList<byte[]> array_image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_main);
        listView = findViewById(R.id.listView1);
        ID_Array = new ArrayList<String>();
        NAME_Array = new ArrayList<String>();
        Description_Array = new ArrayList<String>();
        Price_Array = new ArrayList<String>();
        Location_Array = new ArrayList<String>();
        Category_Array = new ArrayList<String>();
        array_image = new ArrayList<byte[]>();
        itemDB = new ItemDB(this);
        userDB = new UserDB(this);
        item = new Item();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> arg0, View arg1,
                                    final int arg2, long arg3) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserItemListActivity.this,R.style.AlertDialog);
                builder.setTitle("Update/Delete " + NAME_Array.get(arg2));
                builder.setMessage("Do you want to update/delete the record?(Tap outside to cancel)");
                builder.setNegativeButton("UPDATE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                Intent i = new Intent(getApplicationContext(),
                                        UpdateRecordActivity.class);
                                i.putExtra("Item_ID", ID_Array.get(arg2));
                                i.putExtra("Item_Name",NAME_Array.get(arg2));
                                startActivity(i);
                                dialog.cancel();
                            }
                        });
                builder.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                sqLiteDatabase = itemDB.getWritableDatabase();
                                sqLiteDatabase.delete(itemDB.TABLE_NAME, itemDB.COLUMN_ITEM_ID + "="
                                        + ID_Array.get(arg2), null);
                                Toast.makeText(getApplicationContext(), NAME_Array.get(arg2)+ " has been deleted.", Toast.LENGTH_SHORT).show();
                                userItemListAdapter.notifyDataSetChanged();
                                ShowSQLiteDBdata();
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
    @Override
    protected void onResume() {
        ShowSQLiteDBdata() ;
        super.onResume();
    }
    private void ShowSQLiteDBdata() {
        String name = Profile_USERNAME;
        String email = userDB.getEmail(name);
        sqLiteDatabase = itemDB.getWritableDatabase();
        String query = "SELECT  * FROM " + itemDB.TABLE_NAME + " WHERE " + itemDB.COLUMN_USER_NAME + " = '" + name + "' UNION SELECT * FROM "
                + itemDB.TABLE_NAME + " WHERE " + itemDB.COLUMN_USER_EMAIL + " = '" + name + "' OR "
                + itemDB.COLUMN_USER_EMAIL + " = '" + email +"'";
        //cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+itemDB.TABLE_NAME+"", null);
        cursor = sqLiteDatabase.rawQuery(query,null);
        ID_Array.clear();
        NAME_Array.clear();
        Description_Array.clear();
        Price_Array.clear();
        Location_Array.clear();
        Category_Array.clear();
        if(cursor!=null && cursor.getCount()>0) {
            if (cursor.moveToFirst()) {
                do {
                    ID_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_ID)));
                    NAME_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_NAME)));
                    Description_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_DESCRIPTION)));
                    Price_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_PRICE)));
                    Location_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_LOCATION)));
                    Category_Array.add(cursor.getString(cursor.getColumnIndex(ItemDB.COLUMN_ITEM_CATEGORY)));
                    if(cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE))!=null){
                        array_image.add(cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE)));
                    }
                } while (cursor.moveToNext());
            }
        }

        userItemListAdapter = new UserItemListAdapter(UserItemListActivity.this,
                ID_Array,
                NAME_Array,
                Price_Array,
                Category_Array,
                array_image
        );

        listView.setAdapter(userItemListAdapter);

        if(cursor!= null){
            cursor.close();
        }
    }

}
