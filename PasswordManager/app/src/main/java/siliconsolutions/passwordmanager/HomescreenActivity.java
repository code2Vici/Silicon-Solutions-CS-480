package siliconsolutions.passwordmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by naborp on 5/7/2017.
 */

public class HomescreenActivity extends Activity {

    private Button beginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        beginButton = (Button) findViewById(R.id. beginButton);
        beginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent beginHomeIntent = new Intent(HomescreenActivity.this,RegistrationActivity.class);
                startActivity(beginHomeIntent);
            }
        });
    }
}
