package com.gwu.studentservicesapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gwu.studentservicesapp.model.Apartment;
import com.gwu.studentservicesapp.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ApartmentDB extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "Apartments.db";


    //Table name
    public static final String TABLE_NAME = "APT";

    //TAble Columns
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_APT_DESCRIPTION = "item_description";
    public static final String COLUMN_APT_PRICE = "item_price";
    public static final String COLUMN_APT_LOCATION = "item_location";
    public static final String COLUMN_ITEM_PICTURE="picture";


    // Creating table query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_APT_DESCRIPTION + " TEXT,"
            + COLUMN_ITEM_PICTURE + " BLOB,"
            + COLUMN_APT_PRICE + " INTEGER," + COLUMN_APT_LOCATION + " TEXT " + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**
     * Constructor
     *
     * @param context
     */
    public ApartmentDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * This method is to create item record
     *
     * @param apartment
     */
    public void addItem(Apartment apartment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APT_DESCRIPTION, apartment.getAptDescription());
        values.put(COLUMN_APT_PRICE, apartment.getAptPrice());
        values.put(COLUMN_APT_LOCATION, apartment.getAptLocation());
        values.put(COLUMN_ITEM_PICTURE,apartment.getItemPicture());

        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

}
