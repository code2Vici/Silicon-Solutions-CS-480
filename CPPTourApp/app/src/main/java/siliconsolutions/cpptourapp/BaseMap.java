package siliconsolutions.cpptourapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by naborp on 4/20/2017.
 */

public class BaseMap extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_map_activity);

    }
}
