package siliconsolutions.cpptourapp;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

public class HomeScreen extends AppCompatActivity {

    private Typeface typeF;
    private TextView homeTextview;
    //private SearchView searchHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        typeF = Typeface.createFromAsset(getAssets(), "fonts/Championship.ttf");
        homeTextview = (TextView) findViewById(R.id.broncoTxtView);
        homeTextview.setTypeface(typeF, typeF.ITALIC);

    }

}
