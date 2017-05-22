package siliconsolutions.cpp.phonecontactsbook;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by naborp on 5/15/2017.
 */

public class ContactsBookActivity extends AppCompatActivity {

    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText addressEditText;
    private EditText emailEditText;
    private EditText newEntryEditText;
    private EditText newEntryEditText1;
    private EditText newEntryEditText2;
    private EditText newEntryEditText3;
    private EditText newEntryEditText4;
    private TextView newEntryTextView;
    private TextView newEntryTextView1;
    private TextView newEntryTextView2;
    private TextView newEntryTextView3;
    private TextView newEntryTextView4;
    private Spinner newEntryLinkedIn;
    private Spinner newEntryInstagram;
    private Spinner newEntrySnapchat;
    private Spinner newEntryOther;
    private Spinner newEntryOther2;
    private boolean newEntryIsVisible;
    private boolean newEntry_1_IsVisible;
    private boolean newEntry_2_IsVisible;
    private boolean newEntry_3_IsVisible;
    private boolean newEntry_4_IsVisible;
    private int timesClicked = 0;
    private ListView contactInfoListView;
    private FloatingActionButton floatingActionButton;


    List<ContactInfo> contactInfoList;
    private DatabaseReference contactReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        contactInfoListView = (ListView) findViewById(R.id.contactListView);
        newEntryTextView = (TextView) findViewById(R.id.newEntryTextView);
        newEntryTextView1 = (TextView) findViewById(R.id.newEntryTextView1);
        newEntryTextView2 = (TextView) findViewById(R.id.newEntryTextView2);
        newEntryTextView3 = (TextView) findViewById(R.id.newEntryTextView3);
        newEntryTextView4 = (TextView) findViewById(R.id.newEntryTextView4);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.addFab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getContactInfo();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        contactReference = database.getReference("contacts");

        contactReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                contactInfoList = new ArrayList<>();

                //contactReference.child(contactInfo.getId()).setValue(contactInfo);
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    ContactInfo c = ds.getValue(ContactInfo.class);
                    contactInfoList.add(c);
                }
                ContactBookArrayAdapter courseListViewAdapter = new ContactBookArrayAdapter(
                        ContactsBookActivity.this, R.layout.listiview_contact_item, contactInfoList); //Changed MainActivity.this to context = getActivity()
                contactInfoListView.setAdapter(courseListViewAdapter);
                Log.i("POSITION", "" + contactInfoListView.getSelectedItemPosition());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void getContactInfo() {

        final Dialog addContactDialog = new Dialog(ContactsBookActivity.this);
        addContactDialog.setContentView(R.layout.dialog_add_contact);
        addContactDialog.setTitle("New Contact");
        addContactDialog.setCancelable(true);

        newEntryEditText = (EditText) addContactDialog.findViewById(R.id.newEntryEditTextView);
        newEntryEditText1 = (EditText) addContactDialog.findViewById(R.id.newEntryEditTextView1);
        newEntryEditText2 = (EditText) addContactDialog.findViewById(R.id.newEntryEditTextView2);
        newEntryEditText3 = (EditText) addContactDialog.findViewById(R.id.newEntryEditTextView3);
        newEntryEditText4 = (EditText) addContactDialog.findViewById(R.id.newEntryEditTextView4);

        newEntryLinkedIn = (Spinner) addContactDialog.findViewById(R.id.newEntrySpinner);
        newEntryInstagram = (Spinner) addContactDialog.findViewById(R.id.newEntrySpinner1);
        newEntrySnapchat = (Spinner) addContactDialog.findViewById(R.id.newEntrySpinner2);
        newEntryOther = (Spinner) addContactDialog.findViewById(R.id.newEntrySpinner3);
        newEntryOther2 = (Spinner) addContactDialog.findViewById(R.id.newEntrySpinner4);
        final LinearLayout addEntry = (LinearLayout) addContactDialog.findViewById(R.id.addDialogLayout);
        final LinearLayout addEntry1 = (LinearLayout) addContactDialog.findViewById(R.id.addDialogLayout1);
        final LinearLayout addEntry2 = (LinearLayout) addContactDialog.findViewById(R.id.addDialogLayout2);
        final LinearLayout addEntry3 = (LinearLayout) addContactDialog.findViewById(R.id.addDialogLayout3);
        final LinearLayout addEntry4 = (LinearLayout) addContactDialog.findViewById(R.id.addDialogLayout4);

        addEntry.setVisibility(View.GONE);
        addEntry1.setVisibility(View.GONE);
        addEntry2.setVisibility(View.GONE);
        addEntry3.setVisibility(View.GONE);
        addEntry4.setVisibility(View.GONE);

        newEntryIsVisible = false;
        newEntry_1_IsVisible = false;
        newEntry_2_IsVisible = false;
        newEntry_3_IsVisible = false;
        newEntry_4_IsVisible = false;


        ImageButton closeButton = (ImageButton) addContactDialog.findViewById(R.id.dialog_close);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addContactDialog.cancel();
                timesClicked = 0;
            }
        });

        Button addContactButton = (Button) addContactDialog.findViewById(R.id.addContactButton);
        addContactButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ContactInfo contactData = new ContactInfo();

                nameEditText = (EditText) addContactDialog.findViewById(R.id.nameEditTextView);
                phoneEditText = (EditText) addContactDialog.findViewById(R.id.phoneEditTextView);
                addressEditText = (EditText) addContactDialog.findViewById(R.id.addressEditTextView);
                emailEditText = (EditText) addContactDialog.findViewById(R.id.emailEditTextView);

                contactData.setContactName(nameEditText.getText().toString());
                contactData.setPhoneNumber(phoneEditText.getText().toString());
                contactData.setPhysicalAddress(addressEditText.getText().toString());
                contactData.setEmailAddress(emailEditText.getText().toString());

                if(newEntryIsVisible) {
                    newEntryTextView.setVisibility(View.VISIBLE);
                    contactData.setLinkedInField(newEntryLinkedIn.getSelectedItem().toString() + ": " + newEntryEditText.getText().toString());
                }
                if(newEntry_1_IsVisible) {
                    newEntryTextView1.setVisibility(View.VISIBLE);
                    contactData.setInstagramField(newEntryInstagram.getSelectedItem().toString() + ": " + newEntryEditText1.getText().toString());
                }
                if(newEntry_2_IsVisible) {
                    newEntryTextView2.setVisibility(View.VISIBLE);
                    contactData.setSnapchatField(newEntrySnapchat.getSelectedItem().toString() + ": " + newEntryEditText2.getText().toString());
                }
                if(newEntry_3_IsVisible) {
                    newEntryTextView3.setVisibility(View.VISIBLE);
                    contactData.setOtherField(newEntryOther.getSelectedItem().toString() + ": " + newEntryEditText3.getText().toString());
                }
                if(newEntry_4_IsVisible) {
                    newEntryTextView4.setVisibility(View.VISIBLE);
                    contactData.setOtherField2(newEntryOther2.getSelectedItem().toString() + ": " + newEntryEditText4.getText().toString());
                }


                if(contactData.getContactName().equals("")){
                    displayToast("No Empty Contact Name Allowed");
                    addContactDialog.dismiss();
                    timesClicked = 0;
                }
                else {
                    contactReference.child(contactData.getContactName()).setValue(contactData);
                    addContactDialog.dismiss();
                    timesClicked = 0;
                }


            }
        });

        Button newEntryButton = (Button) addContactDialog.findViewById(R.id.addNewEntryButton);
        newEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timesClicked++;

                switch (timesClicked) {
                    case 1:
                        addEntry.setVisibility(View.VISIBLE);
                        newEntryIsVisible = true;
                        break;

                    case 2:
                        addEntry1.setVisibility(View.VISIBLE);
                        newEntry_1_IsVisible = true;
                        break;
                    case 3:
                        addEntry2.setVisibility(View.VISIBLE);
                        newEntry_2_IsVisible = true;
                        break;
                    case 4:
                        addEntry3.setVisibility(View.VISIBLE);
                        newEntry_3_IsVisible = true;
                        break;
                    case 5:
                        addEntry4.setVisibility(View.VISIBLE);
                        newEntry_4_IsVisible = true;
                        break;
                    default:
                        displayToast("Cannot Add More Entries");

                }

                ArrayAdapter<String> spinnerAdapter =  new ArrayAdapter<String>(ContactsBookActivity.this, android.R.layout.simple_spinner_dropdown_item) {

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {

                        View v = super.getView(position, convertView, parent);
                        if (position == getCount()) {
                            ((TextView)v.findViewById(android.R.id.text1)).setText("");
                            ((TextView)v.findViewById(android.R.id.text1)).setHint(getItem(getCount())); //"Hint to be displayed"
                        }

                        return v;
                    }

                    @Override
                    public int getCount() {
                        return super.getCount()-1; // you dont display last item. It is used as hint.
                    }

                };

                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAdapter.add("LinkedIn");
                spinnerAdapter.add("Instagram");
                spinnerAdapter.add("Snapchat");
                spinnerAdapter.add("Other");
                spinnerAdapter.add("Select type");

                newEntryLinkedIn.setAdapter(spinnerAdapter);
                newEntryLinkedIn.setSelection(spinnerAdapter.getCount()); //display hint

                newEntryInstagram.setAdapter(spinnerAdapter);
                newEntryInstagram.setSelection(spinnerAdapter.getCount());

                newEntrySnapchat.setAdapter(spinnerAdapter);
                newEntrySnapchat.setSelection(spinnerAdapter.getCount());

                newEntryOther.setAdapter(spinnerAdapter);
                newEntryOther.setSelection(spinnerAdapter.getCount());

                newEntryOther2.setAdapter(spinnerAdapter);
                newEntryOther2.setSelection(spinnerAdapter.getCount());
                }

        });

        addContactDialog.show();

    }

    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }
    
}
