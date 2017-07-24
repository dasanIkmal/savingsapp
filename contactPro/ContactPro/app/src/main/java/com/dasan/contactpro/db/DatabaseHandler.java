package com.dasan.contactpro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "contactsManager";


    // Contacts table name
    private static final String TABLE_CONTACTS = "contacts";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PH_NO = "phone_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PH_NO + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact
    public void addContact(ContentValues contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getAsString("contact_name")); // Contact Name
        values.put(KEY_PH_NO, contact.getAsString("contact_number")); // Contact Phone

        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }


    // Getting All Contacts
    public double getSavings() {
        double savings =0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, KEY_ID + " DESC ");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                savings += Double.parseDouble(cursor.getString(2));
            } while (cursor.moveToNext());
        }


        return savings;
    }

    public List<ContentValues> getAllContacts(){
        List<ContentValues> contactList = new ArrayList<ContentValues>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, null, null, null, null, KEY_ID + " DESC ");

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ContentValues contact = new ContentValues();
                contact.put("contact_id", Integer.parseInt(cursor.getString(0)));
                contact.put("contact_name", cursor.getString(1));
                contact.put("contact_number", cursor.getString(2));
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // Getting Single Contacts
    public ContentValues getContact(String contactId) {
        ContentValues contact = new ContentValues();
        String args[] = null;
        String whereClause = null;
        args = new String[1];
        args[0] = contactId;
        whereClause = KEY_ID + "=?";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_CONTACTS, null, whereClause, args, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                contact.put("contact_id", Integer.parseInt(cursor.getString(0)));
                contact.put("contact_name", cursor.getString(1));
                contact.put("contact_number", cursor.getString(2));
                // Adding contact to list
            } while (cursor.moveToNext());
        }

        // return contact list
        return contact;
    }


    // Updating single contact
    public void updateContact(ContentValues contact, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getAsString("contact_name")); // Contact Name
        values.put(KEY_PH_NO, contact.getAsString("contact_number")); // Contact Phone

        // updating row
        db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(contact_id)});
    }


    // Deleting single contact
    public void deleteContact(String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[]{String.valueOf(contact_id)});
        db.close();
    }

    public void deleteallContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, "", null);
        db.close();
    }


}