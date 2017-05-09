package siliconsolutions.passwordmanager;


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

public class SwipeRegistration extends AppCompatActivity {

    private MaterialLockView materialLockView;
    private String myPattern;
    private static final String CREDENTIAL_FILE_NAME = "Creds.ser";
    private Credentials credentials = new Credentials("Swipe", "");




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_registration);
        materialLockView = (MaterialLockView) findViewById(R.id.pattern);



        Button setPasswordButton = (Button) findViewById(R.id.set_password_swipe);
        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
     //           setPassword();
            }
        });

        materialLockView.setOnPatternListener(new MaterialLockView.OnPatternListener() {
            @Override
            public void onPatternDetected(List<MaterialLockView.Cell> pattern, String SimplePattern) {
                myPattern = SimplePattern;
                Log.e("SimplePattern", SimplePattern);

                super.onPatternDetected(pattern, SimplePattern);
            }
        });


        /*((EditText) findViewById(R.id.correct_pattern_edittext)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                CorrectPattern = "" + s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }


    public void displayToast(String s) {
        Toast.makeText(this, s,
                Toast.LENGTH_LONG).show();
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

        String password = myPattern;
            credentials.setHash(Encryptor.hash(password));
            saveCredentials(CREDENTIAL_FILE_NAME);
            displayToast("User has been Added!");
            Intent myIntent = new Intent(SwipeRegistration.this, SwipeLogin.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("credentials", credentials);
            myIntent.putExtras(bundle);
            startActivity(myIntent);
        }


    }

