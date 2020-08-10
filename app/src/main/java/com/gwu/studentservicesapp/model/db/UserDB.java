package com.gwu.studentservicesapp.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gwu.studentservicesapp.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserDB extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    static final String DB_NAME = "UsersDB.db";


    //Table name
    public static final String TABLE_NAME = "USERS";

    //TAble Columns
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USER_PROFILENAME = "profile_name";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_USER_EMAIL = "user_email";
    public static final String COLUMN_USER_PASSWORD = "user_password";
    public static final String COLUMN_USER_LOCATION = "user_location";
    public static final String COLUMN_USER_PICTURE="picture";
    public static final String COLUMN_USER_PHONE = "user_phone";


    // Creating table query
    /*private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_LOCATION + " TEXT " + ")";*/
    private String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_PROFILENAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT," + COLUMN_USER_PHONE + " TEXT,"
            + COLUMN_USER_LOCATION + " TEXT, " + COLUMN_USER_PICTURE + " BLOB " + ")";

    private String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


    /**
     * Constructor
     *
     * @param context
     */
    public UserDB(Context context) {
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
     * This method is to create user record
     *
     * @param user
     */
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, user.getUsername());
        values.put(COLUMN_USER_EMAIL, user.getEmail());
        values.put(COLUMN_USER_PASSWORD, user.getPassword());
        values.put(COLUMN_USER_PROFILENAME,user.getPname());
        values.put(COLUMN_USER_PHONE,user.getPhoneNumber());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    /**
     * This method to update user record
     *
     * @param user
     */
    public void updateUser(User user,String userName ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PHONE,user.getPhoneNumber());
        values.put(COLUMN_USER_LOCATION,user.getLocation());
        values.put(COLUMN_USER_PICTURE,user.getProfilePicture());
        db.update(TABLE_NAME, values, COLUMN_USER_NAME + " = ?", new String[]{String.valueOf(userName)});
        db.close();
    }

    public void updateProfilePicture(User user,String userName ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_PICTURE,user.getProfilePicture());
        db.update(TABLE_NAME, values, COLUMN_USER_NAME + " = ?", new String[]{String.valueOf(userName)});
        db.close();
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_NAME, COLUMN_USER_ID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    public boolean checkUser(String email) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?";

        // selection argument
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @param password
     * @return true/false
     */
    public boolean checkUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_EMAIL + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }
    
    public boolean checkUsername(String username) {

        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_NAME
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = COLUMN_USER_NAME + " = ?";
        // selection argument
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean checkUsername(String username,String password) {
        String[] columns = {COLUMN_USER_NAME};
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_NAME + " = ?" + " AND " + COLUMN_USER_PASSWORD + " = ?";
        String[] selectionArgs = {username,password};
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public String getColumnUserName(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
        cursor.close();
        db.close();
        return "";
    }

    public String getColumnUserPhone(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USER_EMAIL + " = ?" ;
        String[] selectionArgs = {email};
        Cursor cursor = db.query(TABLE_NAME,null,selection,selectionArgs,null,null,null);
        cursor.moveToFirst();
        String phone = cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE));
        cursor.close();
        db.close();
        return phone;
    }



    public User getUser(String usrname) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT  * FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_NAME + " = '" + usrname + "' OR "
                + COLUMN_USER_EMAIL + " = '" + usrname+"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            User user = new User();
            user.setPname(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PROFILENAME)));
            user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
            user.setPhoneNo(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PHONE)));
            user.setLocation(cursor.getString(cursor.getColumnIndex(COLUMN_USER_LOCATION)));
            user.setProfilePicture(cursor.getBlob(cursor.getColumnIndex(COLUMN_USER_PICTURE)));
            return user;
        }
        else{
            Log.d("getUser(" + String.valueOf(usrname) + ")", "null");
            return null;
        }
    }
    public String getEmail(String usrname) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USER_EMAIL + " FROM " + TABLE_NAME + " WHERE " + COLUMN_USER_NAME + " = '" + usrname +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            //User user = new User();
            String email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL));
            return email;
        }
        else{
            Log.d("getEmail(" + String.valueOf(usrname) + ")", "null");
            return null;
        }
    }
    public String getUsername(String Email) {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT " + COLUMN_USER_NAME + " WHERE "
                + COLUMN_USER_EMAIL + " = '" + Email +"'";
        Cursor  cursor = db.rawQuery(query,null);
        if (cursor != null)
            cursor.moveToFirst();

        if (cursor.getCount() > 0) {
            //User user = new User();
            String username = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME));
            return username;
        }
        else{
            Log.d("getUsername(" + String.valueOf(Email) + ")", "null");
            return null;
        }
    }

    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    public List<User> getAllUser() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_USER_ID,
                COLUMN_USER_EMAIL,
                COLUMN_USER_NAME,
                COLUMN_USER_PASSWORD
        };
        // sorting orders
        String sortOrder =
                COLUMN_USER_NAME + " ASC";
        List<User> userList = new ArrayList<User>();

        SQLiteDatabase db = this.getReadableDatabase();
        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_NAME, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID))));
                user.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL)));
                user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

}

