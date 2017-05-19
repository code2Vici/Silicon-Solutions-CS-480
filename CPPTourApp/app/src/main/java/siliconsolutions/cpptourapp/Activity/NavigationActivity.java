package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import siliconsolutions.cpptourapp.Adapters.NavigationListAdapter;
import siliconsolutions.cpptourapp.Directions.APIService;
import siliconsolutions.cpptourapp.Directions.BusProvider;
import siliconsolutions.cpptourapp.Directions.DirectionEvent;
import siliconsolutions.cpptourapp.Directions.Step;
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
    private View dividerView;
    private ImageButton beginNavButton;
    private LinearLayout startContainer;
    private ListView stepListView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private APIService apiSerivce;
    private List<Marker> markers;
    private Button recenterButton;
    Bundle bundle;
    private Bus mBus;
    private CameraUpdate cameraUpdate;
    private final MyHandler mHandler = new MyHandler(this);
    public static final int MESSAGE_NOT_CONNECTED = 1;
    private final Runnable sRunnable = new Runnable() {
        @Override
        public void run() {
            Log.i("RUNNABLE WORKING","NOTHING INITIALIZED");
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLngBounds bounds;
            builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
            builder.include(markers.get(0).getPosition());
            bounds = builder.build();
            // define value for padding
            int padding =20;
            //This cameraupdate will zoom the map to a level where both location visible on map and also set the padding on four side.
            cameraUpdate =  CameraUpdateFactory.newLatLngBounds(bounds,padding);
            changeCameraAngle(markers.get(0));
            mHandler.postDelayed(sRunnable,60000);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getIntent().getParcelableExtra("bundle");
        String header = bundle.getString("header");
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Navigation to " + header);
        initViews();
        SupportMapFragment smp = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.nav_map);
        smp.getMapAsync(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initViews() {
        bottomSheet = findViewById(R.id.nav_display_bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        timeTv = (TextView) findViewById(R.id.nav_display_time);
        distanceTv = (TextView) findViewById(R.id.nav_display_distance);
        dividerView = findViewById(R.id.nav_dividerView);
        startContainer = (LinearLayout) findViewById(R.id.nav_start_container);
        stepListView = (ListView) findViewById(R.id.nav_step_list);
        beginNavButton = (ImageButton) findViewById(R.id.nav_beginButton);
        recenterButton = (Button) findViewById(R.id.nav_recenter);
    }



    private void generateDestinationMarker() {
        LatLng toPosition = bundle.getParcelable("position");
        String title = bundle.getString("title");
        String snippet = bundle.getString("snippet");
        destinationMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).visible(true).title(title).snippet(snippet).position(toPosition));
        destinationMarker.setTag("destination");
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
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        if(marker.getTag() != null && (marker.getTag().toString()).equals("destination")){
                            Log.i("SELECTED",destinationMarker.getTitle());
                            //makeInvisible
                            //changeLayout
                        }
                        else{
                            marker.showInfoWindow();
                        }
                        return true;
                    }
                });
            }
        });
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,30));
            }
        });
        String origin = gpsTracker.getLatitude() + "," + gpsTracker.getLongitude();
        String dest = destinationMarker.getPosition().latitude+ "," + destinationMarker.getPosition().longitude;
        String mode = "walking";
        apiSerivce.onLoadDirections(origin,dest,mode);
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

    @Override
    protected void onResume() {
        super.onResume();
        getBus().register(this);
        apiSerivce = new APIService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getBus().unregister(this);
    }

    private Bus getBus() {
        if (mBus == null) {
            mBus = BusProvider.getInstance();
        }
        return mBus;
    }


    @Subscribe
    public void onDirectionEvent(DirectionEvent event){
        final Navigation navigation = Navigation.getInstance();
        navigation.setResponse(event.response);
        navigation.setMap(mMap);
        navigation.setUp();
        markers = navigation.getMarkers();
        timeTv.setText(navigation.getDuration());
        distanceTv.setText(navigation.getDistance());
        startContainer.setVisibility(View.VISIBLE);
        dividerView.setVisibility(View.VISIBLE);
        setDirectionsList(navigation.getSteps(),markers);
        setButtonClicks(navigation);
    }

    public void setDirectionsList(List<Step> directionsList, final List<Marker> markers) {
        NavigationListAdapter navigationListAdapter = new NavigationListAdapter(this,R.id.nav_step_list,directionsList, markers);
        stepListView.setAdapter(navigationListAdapter);
        stepListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToMarker(markers.get(i));
            }
        });
    }

    public void setButtonClicks(final Navigation n){
        beginNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(markers.size() == 0){
                    mHandler.removeCallbacks(sRunnable);
                    Toast.makeText(getApplicationContext(), "YOU HAVE ARRIVED", Toast.LENGTH_SHORT).show();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(destinationMarker.getPosition()));
                    finish();
                }
                else{
                    startContainer.setVisibility(View.GONE);
                    dividerView.setVisibility(View.GONE);
                    recenterButton.setVisibility(View.VISIBLE);
                    n.start();
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    LatLngBounds bounds;
                    builder.include(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()));
                    builder.include(markers.get(0).getPosition());
                    bounds = builder.build();
                    // define value for padding
                    int padding =20;
                    //This cameraupdate will zoom the map to a level where both location visible on map and also set the padding on four side.
                    cameraUpdate =  CameraUpdateFactory.newLatLngBounds(bounds,padding);
                    mMap.moveCamera(cameraUpdate);
                    changeCameraAngle(markers.get(0));
                    mHandler.postDelayed(sRunnable,100);
                }
            }
        });

        recenterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeCameraAngle(markers.get(0));
            }
        });
    }

    public void changeCameraAngle(Marker marker) {
        Location startLocation = new Location("startLocation");
        startLocation.setLatitude(gpsTracker.getLatitude());
        startLocation.setLongitude(gpsTracker.getLongitude());
        Location endLocation = new Location("endLocation");
        endLocation.setLatitude(marker.getPosition().latitude);
        endLocation.setLongitude(marker.getPosition().longitude);
        float targetBearing = startLocation.bearingTo(endLocation);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(gpsTracker.getLatitude(),gpsTracker.getLongitude()))
                .zoom(mMap.getCameraPosition().zoom)
                .bearing(targetBearing).tilt(90)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(
                cameraPosition));
    }

    private void goToMarker(Marker m){
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),18));
        m.showInfoWindow();
    }

    @Override
    protected void onDestroy() {
        mHandler.removeCallbacks(sRunnable);
        super.onDestroy();
    }

    private static class MyHandler extends android.os.Handler {
        private final WeakReference<NavigationActivity> mActivity;

        public MyHandler(NavigationActivity activity) {
            mActivity = new WeakReference<NavigationActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NavigationActivity activity = mActivity.get();
            if (activity != null) {
                Log.e("ERROR:","HANDLER ACTIVITY FAILURE");
            }

            switch (msg.what) {
                case MESSAGE_NOT_CONNECTED:
                    //activity.changeCameraAngle();
                    Log.i("IN HANDLER","CHANGING CAMERA");
                    break;
            }
        }
    }
}
