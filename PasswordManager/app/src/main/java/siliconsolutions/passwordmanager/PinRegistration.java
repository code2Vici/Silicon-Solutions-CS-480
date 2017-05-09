package siliconsolutions.passwordmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import Encryption.Credentials;
import Encryption.Encryptor;

public class PinRegistration extends AppCompatActivity {

    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";

    private Credentials credentials = new Credentials("Pin", "");

    private EditText mPinView;
    private EditText mPinConfirmView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_registration);

        mPinView = (EditText) findViewById(R.id.passwordPin);
        mPinConfirmView = (EditText) findViewById(R.id.pin_Confirm);

        Button registerButton = (Button) findViewById(R.id.register_button_pin);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

    }
    private void attemptRegister() {

        String password = mPinView.getText().toString();
        String passwordVerify = mPinConfirmView.getText().toString();

        if (password.equals(passwordVerify)) {
            credentials.setHash(Encryptor.hash(password));
            saveCredentials(CREDENTIAL_FILE_NAME);
            displayToast("User has been Added!");
            Intent myIntent = new Intent(PinRegistration.this, PinLogin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        } else {
            displayToast("Pin do not match");
        }

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
    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }


}
