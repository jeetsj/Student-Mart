package com.gwu.studentservicesapp.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.db.ItemDB;
import com.gwu.studentservicesapp.presenter.ListAdapter;

import java.util.ArrayList;

public class ItemsListActivity extends AppCompatActivity {

    ItemDB itemDB;
    Cursor cursor;
    ListAdapter listAdapter;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;

    ArrayList<String> ID_Array;
    ArrayList<String> NAME_Array;
    ArrayList<String> Description_Array;
    ArrayList<String> Price_Array;
    ArrayList<String> Location_Array;
    ArrayList<String> Category_Array;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_list);

        listView = findViewById(R.id.listView1);

        ID_Array = new ArrayList<String>();
        NAME_Array = new ArrayList<String>();
        Description_Array = new ArrayList<String>();
        Price_Array = new ArrayList<String>();
        Location_Array = new ArrayList<String>();
        Category_Array = new ArrayList<String>();
        itemDB = new ItemDB(this);
    }
    @Override
    protected void onResume() {
        ShowSQLiteDBdata() ;
        super.onResume();
    }
    private void ShowSQLiteDBdata() {

        sqLiteDatabase = itemDB.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ itemDB.TABLE_NAME +"", null);
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
                } while (cursor.moveToNext());
            }
        }

        listAdapter = new ListAdapter(ItemsListActivity.this,
                ID_Array,
                NAME_Array,
                Description_Array,
                Price_Array,
                Location_Array,
                Category_Array
        );

        listView.setAdapter(listAdapter);

        if(cursor!= null){
            cursor.close();
        }
    }
}
