package siliconsolutions.passwordmanager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import Encryption.Credentials;
import Encryption.Encryptor;

public class PinLogin extends AppCompatActivity {

    private EditText mPinView;
    private Credentials credentials;
    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_login);

        //((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

        Intent myIntent = this.getIntent();
        Bundle bundle = myIntent.getExtras();

        credentials = (Credentials) bundle.getSerializable("credentials");
        mPinView = (EditText) findViewById(R.id.pin_text);
        mPinView.setText("");
        if(mPinView.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        mPinView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(mPinView.getText().toString().length() ==4){
                    attemptLogin();
                }
            }
        });
        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password_pin_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndShowAlertDialog();
            }
        });

    }
    private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PinLogin.this);
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
    private void forgotPassword(){
        credentials.setHash("");
        saveCredentials(CREDENTIAL_FILE_NAME);
        Intent myIntent = new Intent(PinLogin.this, RegistrationActivity.class);
        startActivity(myIntent);

    }

    private void attemptLogin(){
        String password = mPinView.getText().toString();
        if(Encryptor.checkPassword(password,credentials.getHash())){
            displayToast("Success");
            Intent myIntent = new Intent(PinLogin.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            myIntent.putExtra("password",password);
            startActivity(myIntent);
        }
        else{
            displayToast("Invalid password");
            mPinView.setText("");
        }

    }
    @Override
    public void onBackPressed() {
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
