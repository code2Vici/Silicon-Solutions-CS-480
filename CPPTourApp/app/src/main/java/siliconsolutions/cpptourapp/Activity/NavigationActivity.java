package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.Navigation;
import siliconsolutions.cpptourapp.R;

public class NavigationActivity extends AppCompatActivity implements OnMapReadyCallback,View.OnClickListener,
        GPSTrackerListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GPSTracker gpsTracker;
    private Marker destinationMarker;
    private String distance;
    private String time;
    private TextView distanceTv;
    private TextView timeTv;
    private View bottomSheet;
    private BottomSheetBehavior mBottomSheetBehavior;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getParcelableExtra("bundle");
        String header = bundle.getString("header");
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Hello world App");
        initBottomSheet();
        SupportMapFragment smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nav_map);
        smp.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initBottomSheet() {
        bottomSheet = findViewById(R.id.nav_display_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        timeTv = (TextView) findViewById(R.id.nav_display_time);
        distanceTv = (TextView) findViewById(R.id.nav_display_distance);
    }

    private void generateDestinationMarker() {
        LatLng toPosition = bundle.getParcelable("position");
        String title = bundle.getString("title");
        String snippet = bundle.getString("snippet");
        destinationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true).title(title).snippet(snippet).position(toPosition));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        gpsTracker = BaseMap.gpsTracker;
        initGPS();
        generateDestinationMarker();
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destinationMarker.getPosition(), 16));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
        builder.include(destinationMarker.getPosition());
        final LatLngBounds bounds = builder.build();
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
            }
        });
        Navigation navigation = Navigation.getInstance();
        navigation.setMap(mMap);
        navigation.setDestination(destinationMarker.getPosition());
        navigation.setGPSTracker(gpsTracker);
        navigation.setMarker(destinationMarker);
        navigation.start();
        /*
            need to display after JSON has been parsed.
         */
//        distance = navigation.getDistance();
//        time = navigation.getDuration();
//        distanceTv.setText(distance);
//        timeTv.setText(time);
    }

    private void initGPS() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            LatLng userLatLng = new LatLng(lat, lng);
            //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16));
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onGPSTrackerLocationChanged(Location location) {

    }

    @Override
    public void onGPSTrackerStatusChanged(String provider, int status, Bundle extra) {

    }
}
