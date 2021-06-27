package com.example.android.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.example.android.contact.adapter.RecyclerViewAdapter;
import com.example.android.contact.data.MyDbHelper;
import com.example.android.contact.model.Contact;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private  ArrayList<Contact> contactArrayList;
    private ArrayAdapter<String> arrayAdapter;

    MyDbHelper db;
    Contact dev;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = new MyDbHelper(MainActivity.this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_View);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        contactArrayList = new ArrayList<>();
        List<Contact>  allContact = db.getAllContacts();
        for(Contact contact: allContact) {
            Log.d("dbDev","Id" + contact.getId() + "\n" + "Name"
                    + contact.getName() + "\n" + "PhoneNumber" + contact.getPhoneNumber()+"\n");

            contactArrayList.add(contact);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(MainActivity.this,contactArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);

        //db.updateContact(dev);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch(item.getItemId()){//==R.id.action_edit) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_edit:
                Intent intent = new Intent(MainActivity.this,EnterPersonDetail.class);
                startActivity(intent);
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_refresh:
                db.updateContact(dev);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

