package siliconsolutions.passwordmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

import Encryption.Credentials;

public class RegistrationActivity extends AppCompatActivity {

    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";
    private Credentials credentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        if(loadCredentials(CREDENTIAL_FILE_NAME)){
            displayToast("File Found");
            Log.d("TAG", "Cred file exists");
//            loadCredentials(CREDENTIAL_FILE_NAME);
            if(credentials.getHash().equals("")){
                //No Hash, Create new Registration
                displayToast("No Hash");
            }
            else if(credentials.getAuthType().equals("Password")){
                Intent myIntent = new Intent(RegistrationActivity.this, PasswordLogin.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("credentials", credentials);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
            else if(credentials.getAuthType().equals("Pin")){
                Intent myIntent = new Intent(RegistrationActivity.this, PinLogin.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("credentials", credentials);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
            else if(credentials.getAuthType().equals("Swipe")){
                Intent myIntent = new Intent(RegistrationActivity.this, SwipeLogin.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("credentials", credentials);
                myIntent.putExtras(bundle);
                startActivity(myIntent);
            }
        }
        else{
            displayToast("No file");
        }


        Button passwordButton = (Button) findViewById(R.id.password_button);
        passwordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordRegister();
            }
        });
        Button pinButton = (Button) findViewById(R.id.pin_button);
        pinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinRegister();
            }
        });
        Button swipeButton = (Button) findViewById(R.id.swipe_button);
        swipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipeRegister();
            }
        });

    }
    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }
    private boolean loadCredentials(String fileName){
        FileInputStream fis = null;
        try {
            fis = this.openFileInput(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        ObjectInputStream is = null;

        try {
            is = new ObjectInputStream(fis);
            credentials = (Credentials) is.readObject();
            is.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean credFileExists(){
        File f = new File(CREDENTIAL_FILE_NAME);
        if(f.exists()){
            return true;
        }
        return false;
    }
    private void passwordRegister(){
        Intent myIntent = new Intent(RegistrationActivity.this,PasswordRegistration.class);
        Bundle bundle = new Bundle();
        startActivity(myIntent);
    }
    private void pinRegister(){
        Intent myIntent = new Intent(RegistrationActivity.this,PinRegistration.class);
        Bundle bundle = new Bundle();
        startActivity(myIntent);
    }
    private void swipeRegister(){
        Intent myIntent = new Intent(RegistrationActivity.this,SwipeRegistration.class);
        Bundle bundle = new Bundle();
        startActivity(myIntent);
    }


}