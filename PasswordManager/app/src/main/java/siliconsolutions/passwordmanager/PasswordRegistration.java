package siliconsolutions.passwordmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import Encryption.*;

public class PasswordRegistration extends AppCompatActivity /*implements LoaderCallbacks<Cursor>*/ {

   /* private static final int REQUEST_READ_CONTACTS = 0;*/

    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";
    private Credentials credentials = new Credentials("Password", "");

    //private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mPasswordView;
    private EditText mPasswordConfirmView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_registration);


        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordConfirmView = (EditText) findViewById(R.id.passwordConfirm);

        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });


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

    private void attemptRegister() {

        String password = mPasswordView.getText().toString();
        String passwordVerify = mPasswordConfirmView.getText().toString();

        if (password.equals(passwordVerify)) {
            credentials.setHash(Encryptor.hash(password));
            saveCredentials(CREDENTIAL_FILE_NAME);
            displayToast("User has been Added!");
            Intent myIntent = new Intent(PasswordRegistration.this, PasswordLogin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        } else {
            displayToast("Passwords do not match");
        }

    }


    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }

}
