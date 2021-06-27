package com.example.android.contact.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.example.android.contact.constant.Key;
import com.example.android.contact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(Context context) {
        super(context, Key.DB_NAME, null, Key.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + Key.TABLE_NAME + "("
                + Key.KEY_ID + " INTEGER PRIMARY KEY," + Key.KEY_NAME
                + " TEXT, " + Key.KEY_PHONE + " TEXT," + Key.KEY_IMAGE + " BLOB);";
        Log.d("dbharry", "Query being run is : "+ create);
        db.execSQL(create);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Key.KEY_NAME, contact.getName());
        values.put(Key.KEY_PHONE, contact.getPhoneNumber());
        values.put(Key.KEY_IMAGE, contact.getImage());


        db.insert(Key.TABLE_NAME, null, values);
        Log.d("dev", "Successfully inserted");
        db.close();


    }

    public List<Contact> getAllContacts(){
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Generate the query to read from the database
        String select = "SELECT * FROM " + Key.TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);
        //cursor.moveToLast();
        //Loop through now
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNumber(cursor.getString(2));
                contact.setImage(cursor.getBlob(3));
                contactList.add(contact);
            }while(cursor.moveToNext());
        }
        return contactList;
    }

    public int updateContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Key.KEY_NAME, contact.getName());
        values.put(Key.KEY_PHONE, contact.getPhoneNumber());

        //Lets update now
        return db.update(Key.TABLE_NAME, values, Key.KEY_ID + "=?",
                new String[]{String.valueOf(contact.getId())});
    }

    public void deleteContactById(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Key.TABLE_NAME, Key.KEY_ID +"=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Key.TABLE_NAME, Key.KEY_ID +"=?", new String[]{String.valueOf(contact.getId())});
        db.close();
    }

    public int getCount(){
        String query = "SELECT  * FROM " + Key.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor.getCount();

    }

}