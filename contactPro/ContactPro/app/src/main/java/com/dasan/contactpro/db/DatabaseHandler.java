package com.dasan.contactpro.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.StrictMode;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/insert.php";
                String data  = URLEncoder.encode("saving_name", "UTF-8") + "=" +
                        URLEncoder.encode(contact.getAsString("contact_name"), "UTF-8");
                data += "&" + URLEncoder.encode("saving", "UTF-8") + "=" +
                        URLEncoder.encode(contact.getAsString("contact_number"), "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());
            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }

    }


    // Getting All Contacts
    public double getSavings() {
        double savings =0;


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/sum.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());
                savings = Double.parseDouble(sb.toString());
            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }

        return savings;
    }

    public List<ContentValues> getAllContacts(){

        List<ContentValues> contactList = new ArrayList<ContentValues>();
        JSONArray jarray=null;


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/getAll.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());

                String json = sb.toString();

                try {

                    jarray = new JSONArray(json);

                    Log.d("Savings plus", jarray.toString());

                } catch (Throwable t) {
                    Log.e("Savings Plus", "Could not parse malformed JSON: \"" + json + "\"");
                }

            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();

                Log.e("Savings Plus", "Error while connecting to server");

            }

        }

        if (jarray != null) {
            for (int i=0;i<jarray.length();i++){

                ContentValues cvalues = new ContentValues();

                try {
                    cvalues.put("contact_name",jarray.getJSONObject(i).getString("name"));
                    cvalues.put("contact_number",jarray.getJSONObject(i).getString("saving"));
                    cvalues.put("contact_id",Integer.parseInt(jarray.getJSONObject(i).getString("id")));
                    contactList.add(cvalues);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        return contactList;
    }


    // Getting Single Contacts
    public ContentValues getContact(String contactId) {
        ContentValues contact = new ContentValues();
        JSONArray jsonarray = null;



        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/single.php";
                String data  = URLEncoder.encode("id", "UTF-8") + "=" +
                        URLEncoder.encode(contactId, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());

                String json = sb.toString();

                try {

                 jsonarray = new JSONArray(json);
                    Log.d("Savings plus", jsonarray.toString());

                } catch (Throwable t) {
                    Log.e("Savings Plus", "Could not parse malformed JSON: \"" + json + "\"");
                }
            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }



        if (jsonarray != null) {



                try {
                    contact.put("contact_name",jsonarray.getJSONObject(0).getString("name"));
                    contact.put("contact_number",jsonarray.getJSONObject(0).getString("saving"));
                    contact.put("contact_id",Integer.parseInt(jsonarray.getJSONObject(0).getString("id")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


        }


        // return contact list
        return contact;
    }


    // Updating single contact
    public void updateContact(ContentValues contact, String contact_id) {



        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/update.php";
                String data  = URLEncoder.encode("saving_name", "UTF-8") + "=" +
                        URLEncoder.encode(contact.getAsString("contact_name"), "UTF-8");
                data += "&" + URLEncoder.encode("saving", "UTF-8") + "=" +
                        URLEncoder.encode(contact.getAsString("contact_number"), "UTF-8");

                data += "&" + URLEncoder.encode("id", "UTF-8") + "=" +
                        URLEncoder.encode(contact_id, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());
            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }





    }


    // Deleting single contact
    public void deleteContact(String contact_id) {


        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

            try{

                String link="http://savingsplus.srishops.info/delete.php";
                String data  = URLEncoder.encode("id", "UTF-8") + "=" +
                        URLEncoder.encode(contact_id, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write( data );
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                System.out.println(sb.toString());
            } catch(Exception e){
                //  return new String("Exception: " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    public void deleteallContact() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, "", null);
        db.close();
    }


}