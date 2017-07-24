package com.dasan.contactpro.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dasan.contactpro.R;
import com.dasan.contactpro.db.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {
    private ListView contactListView;
    private ContactListAdapter contactListAdapter;
    private ImageView addContact;
     private double savings = 0;
    private TextView totalSavings;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);
        addContact = (ImageView) findViewById(R.id.add_contact);
        totalSavings = (TextView) findViewById(R.id.totalSavings);
        contactListView = (ListView) findViewById(R.id.contact_list_view);
        contactListAdapter = new ContactListAdapter(getApplication());
        contactListView.setAdapter(contactListAdapter);
        db = new DatabaseHandler(this);

//        /**
//         * CRUD Operations
//         * */
//        // Inserting Contacts
//        Log.d("Insert: ", "Inserting ..");
//
//        ContentValues tempData = new ContentValues();
//        tempData.put("contact_name","numesh");
//        tempData.put("contact_number","0777285008");
//        db.addContact(tempData);
//        tempData = new ContentValues();
//        tempData.put("contact_name","dasan");
//        tempData.put("contact_number","0701234567");
//        db.addContact(tempData);

        List<ContentValues> contacts = db.getAllContacts();
        contactListAdapter.setData(contacts);
        contactListView.setAdapter(contactListAdapter);
        contactListAdapter.notifyDataSetChanged();
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(ContactListActivity.this, AddOrUpdateContactActivity.class);
//                myIntent.putExtra("key", value); //Optional parameters
                ContactListActivity.this.startActivity(myIntent);
            }
        });

    }

    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("test123 : 1");
        //db.deleteallContact();
        List<ContentValues> contacts = db.getAllContacts();
        contactListAdapter.setData(contacts);
        contactListView.setAdapter(contactListAdapter);
        contactListAdapter.notifyDataSetChanged();
    }

    public class ContactListAdapter extends BaseAdapter {
        private List<ContentValues> data;
        private Context context;
        private int selectedPosition;

        public ContactListAdapter(Context context) {
            this.context = context;
            data = new ArrayList<ContentValues>();

        }

        public void highlightStrategy(String strategy) {

            notifyDataSetChanged();
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        public void setData(List<ContentValues> data) {
            this.data = data;
        }

        // How many items are in the dataInside set represented by this Adapter.
        @Override
        public int getCount() {
            return data.size();
        }

        // Get the dataInside item associated with the specified position in the dataInside set.
        @Override
        public Object getItem(int position) {


            return data.get(position);
        }

        public void setSelectedPosition(int selectedPosition) {
            this.selectedPosition = selectedPosition;
            notifyDataSetChanged();
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;


            viewHolder = new ViewHolder();

            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.contact_list_item, null);
            viewHolder.contactName = (TextView) convertView.findViewById(R.id.contact_name);
            viewHolder.contactNumber = (TextView) convertView.findViewById(R.id.contact_number);

            convertView.setTag(viewHolder);
            if (data.size() > position) {


                final ContentValues contactDdata = this.data.get(position);
                double tempsav = contactDdata.getAsDouble("contact_number");
                totalSavings.setText(String.valueOf(db.getSavings()));
                final String contact_name = contactDdata.getAsString("contact_name");
                final String contact_number = contactDdata.getAsString("contact_number");

                viewHolder.contactName.setText(contact_name);
                viewHolder.contactNumber.setText(contact_number);


                contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        System.out.println("test123 : "+position);
                        final ContentValues contactDdata = data.get(position);
                        Intent myIntent = new Intent(ContactListActivity.this, AddOrUpdateContactActivity.class);
                        myIntent.putExtra("contact_id", contactDdata.getAsString("contact_id")); //Optional parameters
                        ContactListActivity.this.startActivity(myIntent);


                    }

                });

            }
            return convertView;
        }

        class ViewHolder {


            TextView contactName;
            TextView contactNumber;


        }


    }

}
