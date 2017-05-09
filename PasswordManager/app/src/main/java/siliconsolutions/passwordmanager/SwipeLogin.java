package siliconsolutions.passwordmanager;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.amnix.materiallockview.MaterialLockView;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import Encryption.Credentials;
import Encryption.Encryptor;

public class SwipeLogin extends AppCompatActivity {
    private String CorrectPattern = "";
    private MaterialLockView materialLockView;
    private String myPattern;
    private Credentials credentials;
    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_login);
        materialLockView = (MaterialLockView) findViewById(R.id.pattern);

        Intent myIntent = this.getIntent();
        Bundle bundle = myIntent.getExtras();

        credentials = (Credentials) bundle.getSerializable("credentials");

        Button forgotPasswordButton = (Button) findViewById(R.id.forgot_password_swipe);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAndShowAlertDialog();
            }
        });

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                myPattern = SimplePattern;
                Log.e("SimplePattern", SimplePattern);
                if(Encryptor.checkPassword(myPattern,credentials.getHash())){
                    attemptLogin();
                }

                /*if (!SimplePattern.equals(CorrectPattern)) {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Wrong);
                    // materialLockView.clearPattern();

                } else {

                    materialLockView.setDisplayMode(MaterialLockView.DisplayMode.Correct);

                }*/
                super.onPatternDetected(pattern, SimplePattern);
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
    private void forgotPassword(){
        credentials.setHash("");
        saveCredentials(CREDENTIAL_FILE_NAME);
        Intent myIntent = new Intent(SwipeLogin.this, RegistrationActivity.class);
        startActivity(myIntent);

    }
    @Override
    public void onBackPressed() {
    }

    private void createAndShowAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SwipeLogin.this);
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
        String password = myPattern;
            displayToast("Success");
            Intent myIntent = new Intent(SwipeLogin.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            myIntent.putExtra("password",password);
            startActivity(myIntent);

    }

    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
    }

}
