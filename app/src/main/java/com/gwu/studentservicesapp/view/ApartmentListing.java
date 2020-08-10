package com.gwu.studentservicesapp.view;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.gwu.studentservicesapp.R;
import com.gwu.studentservicesapp.model.db.ApartmentDB;
import com.gwu.studentservicesapp.presenter.ApartmentListAdapter;
import java.util.ArrayList;

import static com.gwu.studentservicesapp.model.db.ItemDB.COLUMN_ITEM_PICTURE;

public class ApartmentListing extends AppCompatActivity {

    ApartmentDB apartmentDB;
    Cursor cursor;
    ApartmentListAdapter apartmentListAdapter;
    ListView listView;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<String> ID_Array;
    ArrayList<String> aptDescription_Array;
    ArrayList<String> aptPrice_Array;
    ArrayList<String> aptLocation_Array;
    ArrayList<byte[]> array_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apartment_main);
        listView = findViewById(R.id.aptlistview1);
        ID_Array = new ArrayList<String>();
        aptDescription_Array = new ArrayList<String>();
        aptPrice_Array = new ArrayList<String>();
        aptLocation_Array = new ArrayList<String>();
        array_image = new ArrayList<byte[]>();
        apartmentDB = new ApartmentDB(this);
    }
    @Override
    protected void onResume() {
        ShowSQLiteDBdata() ;
        super.onResume();
    }
    private void ShowSQLiteDBdata() {

        sqLiteDatabase = apartmentDB.getWritableDatabase();
        cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+ apartmentDB.TABLE_NAME +"", null);
        ID_Array.clear();
        aptDescription_Array.clear();
        aptLocation_Array.clear();
        aptPrice_Array.clear();
        if(cursor!=null && cursor.getCount()>0) {

            if (cursor.moveToFirst()) {
                do {
                    ID_Array.add(cursor.getString(cursor.getColumnIndex(apartmentDB.COLUMN_ITEM_ID)));
                    aptDescription_Array.add(cursor.getString(cursor.getColumnIndex(apartmentDB.COLUMN_APT_DESCRIPTION)));
                    aptPrice_Array.add(cursor.getString(cursor.getColumnIndex(apartmentDB.COLUMN_APT_PRICE)));
                    aptLocation_Array.add(cursor.getString(cursor.getColumnIndex(apartmentDB.COLUMN_APT_LOCATION)));
                    if(cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE))!=null){
                        array_image.add(cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE)));
                    }
                } while (cursor.moveToNext());
            }
        }

        apartmentListAdapter = new ApartmentListAdapter (ApartmentListing.this,
                ID_Array,
                aptDescription_Array,
                aptLocation_Array,
                aptPrice_Array,
                array_image
        );

        listView.setAdapter(apartmentListAdapter);

        if(cursor!= null){
            cursor.close();
        }
    }
}
