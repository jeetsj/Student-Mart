package com.gwu.studentservicesapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gwu.studentservicesapp.model.Item;
import com.gwu.studentservicesapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class ItemDB extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "Items2.db";


    //Table name
    public static final String TABLE_NAME = "Items";

    //TAble Columns
    public static final String COLUMN_ITEM_ID = "item_id";
    public static final String COLUMN_ITEM_NAME = "item_name";
    public static final String COLUMN_ITEM_DESCRIPTION = "item_description";
    public static final String COLUMN_ITEM_PRICE = "item_price";
    public static final String COLUMN_ITEM_LOCATION = "item_location";
    public static final String COLUMN_ITEM_CATEGORY ="item_category";
    public static final String COLUMN_ITEM_PICTURE="picture";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";

    // Creating table query
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ITEM_NAME + " TEXT,"
            + COLUMN_ITEM_CATEGORY + " TEXT,"
            + COLUMN_ITEM_PICTURE + " BLOB,"
            + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT,"
            + COLUMN_ITEM_DESCRIPTION + " TEXT," + COLUMN_ITEM_PRICE + " REAL," + COLUMN_ITEM_LOCATION + " TEXT " + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    /**
     * Constructor
     *
     * @param context
     */
    public ItemDB(Context context) {
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
     * @param item
     */
    public void addItem(Item item, User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getProductName());
        values.put(COLUMN_ITEM_DESCRIPTION, item.getProductDescription());
        values.put(COLUMN_ITEM_PRICE, Double.valueOf(item.getProductPrice()));
        values.put(COLUMN_ITEM_LOCATION, item.getProductLocation());
        values.put(COLUMN_ITEM_CATEGORY,item.getProductCategory());
        values.put(COLUMN_ITEM_PICTURE,item.getItemPicture());
        values.put(COLUMN_USER_NAME,user.getUsername());
        values.put(COLUMN_USER_EMAIL,user.getEmail());
        // Inserting Row
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public List<Item> getAllItems() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_ITEM_ID,
                COLUMN_ITEM_NAME,
                COLUMN_ITEM_CATEGORY,
                COLUMN_ITEM_DESCRIPTION,
                COLUMN_ITEM_PRICE,
                COLUMN_ITEM_LOCATION,
                COLUMN_ITEM_PICTURE
        };
        // sorting orders
        String sortOrder =
                COLUMN_ITEM_NAME + " ASC";
        List<Item> itemList = new ArrayList<Item>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order
        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setProductId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_ID))));
                item.setProductName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
                item.setProductCategory(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY)));
                item.setProductDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION)));
                item.setProductLocation(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_LOCATION)));
                item.setItemPicture(cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE)));
                // Adding user record to list
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    /**
     * This method to update user record
     *
     * @param item
     */
    public void updateItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_NAME, item.getProductName());
        values.put(COLUMN_ITEM_DESCRIPTION, item.getProductDescription());
        values.put(COLUMN_ITEM_PRICE, Double.valueOf(item.getProductPrice()));
        values.put(COLUMN_ITEM_LOCATION, item.getProductLocation());
        values.put(COLUMN_ITEM_CATEGORY,item.getProductCategory());
        values.put(COLUMN_ITEM_PICTURE,item.getItemPicture());

        // updating row
        db.update(TABLE_NAME, values, COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getProductId())});
        db.close();
    }

    /**
     * This method is to delete item record
     *
     * @param item
     */
    public void deleteItem(Item item) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_NAME, COLUMN_ITEM_ID + " = ?",
                new String[]{String.valueOf(item.getProductId())});
        db.close();
    }

    public List<Item> getItems(String item_category){
        List<Item> ans = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ITEM_CATEGORY + " = ?" ;
        String[] selectionArgs = {item_category};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_ID));
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME));
            String description = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION));
            Double price = cursor.getDouble(cursor.getColumnIndex(COLUMN_ITEM_ID));
            String location = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_LOCATION));
            String category = cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_CATEGORY));
            byte[] picture = cursor.getBlob(cursor.getColumnIndex(COLUMN_ITEM_PICTURE));
            Item item = new Item(id,name,description,price,location,category,picture);
            ans.add(item);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return ans;
    }

    public Item getItemsbyID(String item_id){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_ITEM_ID + " = '" + item_id +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            Item item = new Item();
            item.setProductName(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_NAME)));
            item.setProductDescription(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_DESCRIPTION)));
            item.setProductPrice(cursor.getInt(cursor.getColumnIndex(COLUMN_ITEM_PRICE)));
            item.setProductLocation(cursor.getString(cursor.getColumnIndex(COLUMN_ITEM_LOCATION)));
            return item;
        }
        else{
            Log.d("getUser(" + String.valueOf(item_id) + ")", "null");
            return null;
        }
    }

    public void updateItembyItemID(Item item , int itemID) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_DESCRIPTION, item.getProductDescription());
        values.put(COLUMN_ITEM_PRICE, Double.valueOf(item.getProductPrice()));
        values.put(COLUMN_ITEM_LOCATION, item.getProductLocation());
        db.update(TABLE_NAME, values, COLUMN_ITEM_ID + " = ?", new String[]{String.valueOf(itemID)});
        db.close();
    }

    public void updateItemPictureByID(Item item, int itemID ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ITEM_PICTURE,item.getItemPicture());
        db.update(TABLE_NAME, values, COLUMN_ITEM_ID + " = ?", new String[]{String.valueOf(itemID)});
        db.close();
    }

}
