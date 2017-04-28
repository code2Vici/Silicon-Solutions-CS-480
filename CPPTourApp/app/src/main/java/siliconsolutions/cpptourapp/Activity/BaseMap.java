package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import java.io.IOException;
import java.util.List;

import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.GlobalVars;
import siliconsolutions.cpptourapp.Model.MyLocation;
import siliconsolutions.cpptourapp.R;

public class BaseMap extends AppCompatActivity implements
        OnMapReadyCallback,
        View.OnClickListener,
        GPSTrackerListener {

    private GoogleMap mMap;
    private DrawerLayout drawer;

    private ImageView btnMyLocation;
    private ImageView btnOpenFavoriteDrawer;

    private GPSTracker gpsTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView leftNavigationView = (NavigationView) findViewById(R.id.nav_view_left);
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_left_start) {

                } else if (id == R.id.nav_left_discover) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_left_tools) {

                } else if (id == R.id.nav_left_check_1) {
                    if (item.isChecked()) item.setChecked(false);
                    else item.setChecked(true);
                    /*SpannableString spanString = new SpannableString(item.getTitle().toString());
                    spanString.setSpan(new ForegroundColorSpan(Color.RED), 0, spanString.length(), 0); // fix the color to white
                    item.setTitle(spanString);
                    return true;*/
                } else if (id == R.id.nav_left_check_2) {
                    if (item.isChecked())
                        item.setChecked(false);
                    else item.setChecked(true);
                    return true;
                } else if (id == R.id.nav_left_check_3) {
                    if (item.isChecked())
                        item.setChecked(false);
                    else item.setChecked(true);
                    return true;
                } else if (id == R.id.nav_left_settings) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }

        });
        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_view_right);

        initialize();

    }


    private void initialize() {
        initComponents();
        initGPS();
    }

    private void initGPS() {
        gpsTracker = new GPSTracker(this, this);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            Toast.makeText(getApplicationContext(), lat + " " + lng, Toast.LENGTH_SHORT).show();
            if (lat != 0 || lng != 0) {
                GlobalVars.location = new MyLocation(lat, lng);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    private void initComponents() {
        btnMyLocation = (ImageView) findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(this);

        btnOpenFavoriteDrawer = (ImageView) findViewById(R.id.btnOpenFavoriteDrawer);
        btnOpenFavoriteDrawer.setOnClickListener(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng cpp = new LatLng(34.056502, -117.821465);
        mMap.addMarker(new MarkerOptions().position(cpp).title("Cal Poly Pomona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cpp, 16));
    }

    public void setMarkers() {
        LatLng building8 = new LatLng(34.058664, -117.824796);
        //mMap.addMarker(new MarkerOptions().position(building8).title("Building 8"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //for creating the transparent effect of status bar
    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        gpsTracker = new GPSTracker(this, this);
        switch (v.getId()) {
            case R.id.btnOpenFavoriteDrawer:{

                break;
            }

            case R.id.btnMyLocation: {

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
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 16));
                }

            }
        }
    }


    @Override
    public void onGPSTrackerLocationChanged(Location location) {

    }

    @Override
    public void onGPSTrackerStatusChanged(String provider, int status, Bundle extra) {

    }
}