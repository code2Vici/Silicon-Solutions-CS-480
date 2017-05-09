package siliconsolutions.passwordmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.LinkedList;

import Encryption.Credential;
import Encryption.Credentials;
import Encryption.Encryptor;

import static org.bouncycastle.crypto.tls.ContentType.alert;

public class MainActivity extends AppCompatActivity {

    private Credentials credentials;
    private String password;
    private LinkedList<Credential> credentialList;
    private String m_Website;
    private String m_Username;
    private String m_password;
    private String d_Website;
    private String d_Username;
    private String d_password;
    private ListView mListView;
    private boolean lazy;


    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        m_Website ="";
        m_Username ="";
        m_password ="";

        EditText mUsernameView;
        EditText mPasswordView;
        EditText mWebsiteView;
        mWebsiteView = (EditText) findViewById(R.id.d_website);
        mUsernameView = (EditText) findViewById(R.id.d_username);
        mPasswordView = (EditText) findViewById(R.id.d_password);

        mListView = (ListView) findViewById(R.id.password_list_view);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView <?> parentAdapter, View view, int position,
                                    long id) {
                    String s =(String) mListView.getItemAtPosition(position);
                    String[] splitWebsiteAndUsername = s.split("\n");
                    Credential reqCreds = null;
                for(Credential c : credentialList){
                        if(c.getUsername().equals(splitWebsiteAndUsername[1])){
                            if(c.getWebsite().equals(splitWebsiteAndUsername[0])){
                                reqCreds = c;
                            }
                        }
                    }

//                getAddInfo();
                displayFromList(reqCreds);
//                displayFromList();

//                Toast.makeText(getApplicationContext(), id, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getApplicationContext(), position, Toast.LENGTH_SHORT).show();


                //  Place code here with the action

            }
        });

        Intent myIntent = this.getIntent();
        Bundle bundle = myIntent.getExtras();

        credentials = (Credentials) bundle.getSerializable("credentials");
        password = bundle.getString("password");

        credentialList = credentials.getCredentials();

        //displayToast(password);

        if(credentials.getCredentials().size() >0){
            populateList();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
     //           getAddInfo(true);
                    getAddInfo();
            }
        });
    }

    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }
    /*@Override
    public void onBackPressed() {
    }*/

//    private void displayFromList(Credential c){
    private void displayFromList(Credential c){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialogue_display_from_list);
        dialog.setTitle("View entry");
        dialog.setCancelable(true);

        final String prevUsername = c.getUsername();
        final String prevPassword = c.getPassword();
        final String prevWebsite = c.getWebsite();


        EditText editWebsite=(EditText)dialog.findViewById(R.id.dfl_website);
        editWebsite.setText(c.getWebsite());

        EditText editUsername=(EditText)dialog.findViewById(R.id.dfl_username);

        editUsername.setText(c.getUsername());
        EditText editPassword=(EditText)dialog.findViewById(R.id.dfl_password);
        try {
            editPassword.setText(Encryptor.decrypt(c.getPassword(),password));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Button okButton = (Button) dialog.findViewById(R.id.dfl_ok);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editWebsite=(EditText)dialog.findViewById(R.id.dfl_website);
                m_Website=editWebsite.getText().toString();
                EditText editUsername=(EditText)dialog.findViewById(R.id.dfl_username);
                m_Username=editUsername.getText().toString();
                EditText editPassword=(EditText)dialog.findViewById(R.id.dfl_password);
                m_password=editPassword.getText().toString();

                if((prevUsername.equals(m_Username) && prevWebsite.equals(m_Website)) || !entryExists(m_Website,m_Username)){
                    deleteEntry(prevWebsite,prevUsername);
                    addEntry();
                    dialog.dismiss();
                }
               else{
                    displayToast("Not updated!" + "\n Account already exists in another entry");
                }

            }
        });

        Button closeButton = (Button) dialog.findViewById(R.id.dfl_close);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });
        Button randButton = (Button) dialog.findViewById(R.id.dfl_generateRandom);
        randButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                EditText editPassword=(EditText)dialog.findViewById(R.id.dfl_password);
                editPassword.setText(generateRandom());


            }
        });
        Button deleteButton = (Button) dialog.findViewById(R.id.dfl_deleteEntry);
        deleteButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                //ARE YOU SURE?

                deleteEntry(prevWebsite,prevUsername);
                updateEntry();
                dialog.dismiss();
            }
        });


        dialog.show();


    }

    private boolean deleteEntry(String website, String username){

        for(Credential c: credentialList){
            if(c.getWebsite().toLowerCase().equals(website.toLowerCase()) && c.getUsername().toLowerCase().equals(username.toLowerCase())){
                credentialList.remove(c);
                return true;
            }
        }
        return false;
    }

    private void getAddInfo(){
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialogue_display);
        dialog.setTitle("New Entry");
        dialog.setCancelable(true);

        Button okButton = (Button) dialog.findViewById(R.id.ok);
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editWebsite=(EditText)dialog.findViewById(R.id.d_website);
                m_Website=editWebsite.getText().toString();
                EditText editUsername=(EditText)dialog.findViewById(R.id.d_username);
                m_Username=editUsername.getText().toString();
                EditText editPassword=(EditText)dialog.findViewById(R.id.d_password);
                m_password=editPassword.getText().toString();
                addEntry();
                dialog.dismiss();

            }
        });
        Button closeButton = (Button) dialog.findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        Button randButton = (Button) dialog.findViewById(R.id.generateRandom);
        randButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText editPassword=(EditText)dialog.findViewById(R.id.d_password);
                editPassword.setText(generateRandom());
            }
        });

        dialog.show();
    }

    private String generateRandom(){
        SecureRandom random = new SecureRandom();
        return new BigInteger(40, random).toString(32);
    }

    private boolean entryExists(String website, String username){
        boolean alreadyExists = false;
        for(Credential c: credentialList){
            if(c.getWebsite().toLowerCase().equals(website.toLowerCase()) && c.getUsername().toLowerCase().equals(username.toLowerCase())){
                alreadyExists = true;
            }
        }
        return alreadyExists;
    }

    private void addEntry(){
        if(m_Website.equals("") || m_Username.equals("") || m_password.equals("") || m_Website == null || m_Username == null || m_password == null){
            displayToast("Please fill in all fields!");
            return;
        }
        boolean alreadyExists = false;
        alreadyExists = entryExists(m_Website,m_Username);
        for(Credential c: credentialList){
            try {
                if(Encryptor.decrypt(c.getPassword(),password).equals(m_password)){
                    displayToast("Another website uses this same password! Consider changing it.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        if(alreadyExists){
            displayToast("This account already exists! Please update or delete the existing account!");
        }
        else {
            try {
                credentialList.add(new Credential(m_Username, Encryptor.encrypt(m_password, password), m_Website));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //update Data Struct
        credentials.setCredentials(credentialList);
        saveCredentials(CREDENTIAL_FILE_NAME);
        updateEntry();
    }
    private void saveCredentials(String fileName) {
        try {
            FileOutputStream fos = this.openFileOutput(fileName, this.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(credentials);
            os.close();
            fos.close();
        } catch (Exception e) {
            Log.d("TAG", e.getLocalizedMessage());
            Log.d("TAG", "save credentials failed");
            e.printStackTrace();
        }
    }
    private void updateEntry(){
        populateList();
    }
    private void populateList(){
        String[] listItems = new String[credentialList.size()];
        Collections.sort(credentialList);
        for(int i=0; i<listItems.length; i++){
            listItems[i] = credentialList.get(i).getWebsite() + "\n" + credentialList.get(i).getUsername();
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
        mListView.setAdapter(adapter);
    }

}
