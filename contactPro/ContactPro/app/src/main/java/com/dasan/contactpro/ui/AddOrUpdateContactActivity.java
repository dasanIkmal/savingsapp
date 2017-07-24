package com.dasan.contactpro.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dasan.contactpro.R;
import com.dasan.contactpro.db.DatabaseHandler;

import java.util.List;

public class AddOrUpdateContactActivity extends AppCompatActivity {
    private String contactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_update_contact);
        final EditText contactName = (EditText) findViewById(R.id.contact_name);
        final EditText contactNumber = (EditText) findViewById(R.id.contact_number);
        final TextView totalSavings = (TextView) findViewById(R.id.totalSavings);
        Button addContactBtn = (Button) findViewById(R.id.add_contact_btn);
        Button updateContactBtn = (Button) findViewById(R.id.update_contact_btn);
        Button deleteContactBtn = (Button) findViewById(R.id.delete_contact_btn);
        addContactBtn.setVisibility(View.GONE);
        updateContactBtn.setVisibility(View.GONE);
        deleteContactBtn.setVisibility(View.GONE);
        final DatabaseHandler db = new DatabaseHandler(this);
        Intent intent = getIntent();
        contactId = intent.getStringExtra("contact_id");
        if (contactId != null) {

            updateContactBtn.setVisibility(View.VISIBLE);
            deleteContactBtn.setVisibility(View.VISIBLE);
            ContentValues contacts = db.getContact(contactId);
            contactName.setText(contacts.getAsString("contact_name"));
            contactNumber.setText(contacts.getAsString("contact_number"));
        } else {
            addContactBtn.setVisibility(View.VISIBLE);
        }
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues tempData = new ContentValues();
                tempData.put("contact_name", contactName.getText().toString());
                tempData.put("contact_number", contactNumber.getText().toString());
                db.addContact(tempData);

                finish();
            }
        });
        updateContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues tempData = new ContentValues();
                tempData.put("contact_name", contactName.getText().toString());
                tempData.put("contact_number", contactNumber.getText().toString());
                db.updateContact(tempData, contactId);
                finish();
            }
        });
        deleteContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteContact(contactId);
                finish();
            }
        });
    }
}
