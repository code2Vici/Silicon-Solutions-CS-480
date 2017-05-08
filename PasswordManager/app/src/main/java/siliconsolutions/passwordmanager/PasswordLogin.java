package siliconsolutions.passwordmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import Encryption.Credentials;
import Encryption.Encryptor;

public class PasswordLogin extends AppCompatActivity {
    private EditText mPasswordView;
    private Credentials credentials;
    private Button loginButton;
    private Button forgotPasswordButton;
    private Button newVaultButton;

    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Intent myIntent = this.getIntent();
        Bundle bundle = myIntent.getExtras();

        credentials = (Credentials) bundle.getSerializable("credentials");

        mPasswordView = (EditText) findViewById(R.id.password);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });


        forgotPasswordButton = (Button) findViewById(R.id.forgot_password_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndShowAlertDialog();
//                forgotPassword();
            }
        });

        /*newVaultButton = (Button) findViewById(R.id.newVaultButton);
        newVaultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(PasswordLogin.this, RegistrationActivity.class);
                startActivity(loginIntent);
            }
        });*/
    }
    private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PasswordLogin.this);
        builder.setTitle("This will erase all data. Continue?");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                forgotPassword();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void attemptLogin(){
        String password = mPasswordView.getText().toString();
        if(Encryptor.checkPassword(password,credentials.getHash())){
            displayToast("Success");
            Intent myIntent = new Intent(PasswordLogin.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            myIntent.putExtra("password",password);
            startActivity(myIntent);

        }
        else{
            displayToast("Invalid password");
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
    private void forgotPassword(){
        credentials.setHash("");
        saveCredentials(CREDENTIAL_FILE_NAME);
        Intent myIntent = new Intent(PasswordLogin.this, RegistrationActivity.class);
        startActivity(myIntent);

    }

    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }


}
