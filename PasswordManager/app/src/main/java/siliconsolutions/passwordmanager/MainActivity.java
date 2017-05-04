package siliconsolutions.passwordmanager;

import Encryption.*;
import Encryption.Credentials;

import android.content.Intent;
import android.net.*;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    String username;
    String password;
    LinkedList<Credentials> credentials;
    Credentials myCreds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                displayToast(username);
            }
        });

        Intent myIntent = this.getIntent();
        Bundle bundle = myIntent.getExtras();

        credentials = (LinkedList<Credentials>) bundle.getSerializable("credentialList");
        username = myIntent.getStringExtra("username");
        password = myIntent.getStringExtra("password");
        loadCreds(username);
    }
    public void loadCreds(String username){
        for(Credentials c: credentials){
            if(c.getUsername().equals(username)){
                myCreds = c;
            }
        }
    }
    public void displayToast(String s){
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }

    private void saveCredentials(String fileName){
        try {
            FileOutputStream fos = this.openFileOutput(fileName, this.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(credentials);
            os.close();
            fos.close();
        }
        catch(Exception e){
            Log.d("TAG",e.getLocalizedMessage());
            Log.d("TAG","save credentials failed");
            e.printStackTrace();
        }
    }
    public void checkPasswordComplexity(String password){

    }

}
