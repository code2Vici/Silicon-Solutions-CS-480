package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.reflect.Type;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import siliconsolutions.cpptourapp.Adapters.BuildingsListAdapter;
import siliconsolutions.cpptourapp.Adapters.FavoritesListAdapter;
import siliconsolutions.cpptourapp.Adapters.LandmarksListAdapter;
import siliconsolutions.cpptourapp.Adapters.ParkingListAdapter;
import siliconsolutions.cpptourapp.Adapters.Utilities;
import siliconsolutions.cpptourapp.Directions.DirectionsService;
import siliconsolutions.cpptourapp.Directions.GeoCodeResponse;
import siliconsolutions.cpptourapp.Directions.Leg;
import siliconsolutions.cpptourapp.Directions.Route;
import siliconsolutions.cpptourapp.Directions.Step;
import siliconsolutions.cpptourapp.FavoritesFragment.FavoritesFragment;
import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.Model.GlobalVars;
import siliconsolutions.cpptourapp.Model.Landmarks;
import siliconsolutions.cpptourapp.Model.MyLocation;
import siliconsolutions.cpptourapp.Model.Navigation;
import siliconsolutions.cpptourapp.Model.ParkingLots;
import siliconsolutions.cpptourapp.R;

public class BaseMap extends AppCompatActivity implements
        OnMapReadyCallback,
        View.OnClickListener,
        GPSTrackerListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private DrawerLayout drawer;
    private ImageView btnMyLocation;
    private ImageView btnOpenFavoriteDrawer;
    public static GPSTracker gpsTracker;
    NavigationView leftNavigationView;
    private ProgressDialog progressDialog;
    private ArrayList<Building> buildingsArrayList;
    private ArrayList<Landmarks> landmarksArrayList;
    private ArrayList<ParkingLots> parkingLotsArrayList;
    private ArrayList<Marker> markersArrayList;
    private ArrayList<Marker> favoritesArrayList;
    private Marker myMarker;
    private StringBuffer postList;
    private StringBuffer landmarksPostList;
    private StringBuffer parkingPostList;
    private MenuItem buildingsMenuItem;
    private MenuItem landmarksMenuItem;
    private MenuItem parkingMenuItem;
    private CompoundButton buildingsCheckbox;
    private CompoundButton landmarksCheckbox;
    private CompoundButton parkingCheckbox;
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView bottomSheetHeading;
    private FloatingActionButton locationBtn;
    private FloatingActionButton favoritesBtn;
    private View bottomSheet;
    private ImageButton likeDetailBtn;
    private ImageButton navigationDetailBtn;
    private boolean markerSelected = false;

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


        likeDetailBtn = (ImageButton) findViewById(R.id.favorites_detail_selection);
        navigationDetailBtn = (ImageButton) findViewById(R.id.navigation_detail_selection);
        favoritesBtn = (FloatingActionButton) findViewById(R.id.btnOpenFavoriteDrawer);
        locationBtn = (FloatingActionButton) findViewById(R.id.btnMyLocation);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        setDetailView();

        leftNavigationView = (NavigationView) findViewById(R.id.nav_view_left);
        setFilters();
        leftNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_left_start) {

                } else if (id == R.id.nav_left_discover) {

                } else if (id == R.id.nav_slideshow) {

                } else if (id == R.id.nav_left_tools) {

                } else if (id == R.id.nav_left_check_1) {
                    buildingsCheckbox.performClick();
                    if(item.isChecked()){
                        item.setChecked(false);
                    }
                    else{
                      item.setChecked(true);
                    }
                } else if (id == R.id.nav_left_check_2) {
                    landmarksCheckbox.performClick();
                    if (item.isChecked()) {
                        item.setChecked(false);
                    }   else {
                        item.setChecked(true);
                    }
                } else if (id == R.id.nav_left_check_3) {
                    parkingCheckbox.performClick();
                    if (item.isChecked()) {
                        item.setChecked(false);
                    }   else {
                        item.setChecked(true);
                    }
                } else if (id == R.id.nav_left_settings) {

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        initialize();
        final NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_view_right);
        ListView buildingsListView = (ListView) findViewById(R.id.right_nav_building_listView);
        ListView landmarksListView = (ListView) findViewById(R.id.right_nav_landmarks_listView);
        ListView parkingListView = (ListView) findViewById(R.id.right_nav_parking_listView);

        BuildingsListAdapter buildingsListAdapter = new BuildingsListAdapter(this,R.id.right_nav_building_listView,buildingsArrayList);
        buildingsListView.setAdapter(buildingsListAdapter);
        buildingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                if(!markersArrayList.get(i).isVisible())
                    markersArrayList.get(i).setVisible(true);
                markersArrayList.get(i).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersArrayList.get(i).getPosition(),16));
                bottomSheetHeading.setText(markersArrayList.get(i).getTitle());
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if(btnMyLocation.getVisibility() == View.VISIBLE){
                    CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) favoritesBtn.getLayoutParams();
                    CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) locationBtn.getLayoutParams();
                    p1.setAnchorId(View.NO_ID);
                    p2.setAnchorId(View.NO_ID);
                    favoritesBtn.setLayoutParams(p1);
                    locationBtn.setLayoutParams(p2);
                    favoritesBtn.setVisibility(View.GONE);
                    locationBtn.setVisibility(View.GONE);
                }
            }
        });
        LandmarksListAdapter landmarksListAdapter = new LandmarksListAdapter(this,R.id.right_nav_landmarks_listView,landmarksArrayList);
        landmarksListView.setAdapter(landmarksListAdapter);
        landmarksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i += buildingsArrayList.size();
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                if(!markersArrayList.get(i).isVisible())
                    markersArrayList.get(i).setVisible(true);
                markersArrayList.get(i).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersArrayList.get(i).getPosition(),16));if(btnMyLocation.getVisibility() == View.VISIBLE){
                    CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) favoritesBtn.getLayoutParams();
                    CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) locationBtn.getLayoutParams();
                    p1.setAnchorId(View.NO_ID);
                    p2.setAnchorId(View.NO_ID);
                    favoritesBtn.setLayoutParams(p1);
                    locationBtn.setLayoutParams(p2);
                    favoritesBtn.setVisibility(View.GONE);
                    locationBtn.setVisibility(View.GONE);
                }
            }
        });
        ParkingListAdapter parkingListAdapter = new ParkingListAdapter(this,R.id.right_nav_parking_listView,parkingLotsArrayList);
        parkingListView.setAdapter(parkingListAdapter);
        parkingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i += buildingsArrayList.size() + landmarksArrayList.size();
                if (drawer.isDrawerOpen(GravityCompat.END))
                    drawer.closeDrawer(GravityCompat.END);
                if(!markersArrayList.get(i).isVisible())
                    markersArrayList.get(i).setVisible(true);
                markersArrayList.get(i).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersArrayList.get(i).getPosition(),16));
                if(btnMyLocation.getVisibility() == View.VISIBLE){
                    CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) favoritesBtn.getLayoutParams();
                    CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) locationBtn.getLayoutParams();
                    p1.setAnchorId(View.NO_ID);
                    p2.setAnchorId(View.NO_ID);
                    favoritesBtn.setLayoutParams(p1);
                    locationBtn.setLayoutParams(p2);
                    favoritesBtn.setVisibility(View.GONE);
                    locationBtn.setVisibility(View.GONE);
                }
            }
        });

        favoritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        Utilities.setListViewHeightBasedOnChildren(buildingsListView);
        Utilities.setListViewHeightBasedOnChildren(landmarksListView);
        Utilities.setListViewHeightBasedOnChildren(parkingListView);
    }

    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.favorites_dialog, null );
        RecyclerView rv;
        rv = (RecyclerView) view.findViewById(R.id.favoritesList);
        rv.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        FavoritesListAdapter adapter = new FavoritesListAdapter(favoritesArrayList);
        rv.setAdapter(adapter);
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setView(view);
        dialog.show();
    }

    private void setFilters() {
        buildingsMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_1);
        buildingsCheckbox = (CompoundButton) MenuItemCompat.getActionView(buildingsMenuItem);
        buildingsCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    for (int i = 0; i < buildingsArrayList.size();i++) {
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    for (int i = 0; i < buildingsArrayList.size();i++) {
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });
        landmarksMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_2);
        landmarksCheckbox = (CompoundButton) MenuItemCompat.getActionView(landmarksMenuItem);
        landmarksCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    for(int i = buildingsArrayList.size(); i < (buildingsArrayList.size() + landmarksArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    for(int i = buildingsArrayList.size(); i < (buildingsArrayList.size() + landmarksArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });
        parkingMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_3);
        parkingCheckbox = (CompoundButton) MenuItemCompat.getActionView(parkingMenuItem);
        parkingCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });
    }

    private void initialize() {
        initComponents();
        initLocations();
        initGPS();
    }

    private void initLocations() {
        Type listType = new TypeToken<ArrayList<Building>>() {
        }.getType();
        Type landmarksListType = new TypeToken<ArrayList<Landmarks>>() {
        }.getType();
        Type parkingListType = new TypeToken<ArrayList<ParkingLots>>() {
        }.getType();
        favoritesArrayList = new ArrayList<>();
        buildingsArrayList = new GsonBuilder().create().fromJson(loadBuildingJSONFromAsset(), listType);
        landmarksArrayList = new GsonBuilder().create().fromJson(loadLandmarksJSONFromAsset(), landmarksListType);
        parkingLotsArrayList = new GsonBuilder().create().fromJson(loadParkingLotsJSONFromAsset(), parkingListType);
    }

    private void initGPS() {
        gpsTracker = new GPSTracker(this, this);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            Toast.makeText(getApplicationContext(), "Latitude" +lat + " " + lng, Toast.LENGTH_SHORT).show();
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

    public void setMarkers() {
        markersArrayList = new ArrayList<>();
        postList = new StringBuffer();
        landmarksPostList = new StringBuffer();
        for (Building post : buildingsArrayList) {
            postList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getBuildingName() + "\n building number: " + post.getBuildingNumber() + "\n\n");
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).visible(false).title(post.getBuildingName()).snippet(post.getBuildingNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);
        }
        for (Landmarks post : landmarksArrayList) {
            landmarksPostList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getLandmarkName() + "\n building number: " + post.getLandmarkNumber() + "\n\n");

            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(post.getLandmarkName()).snippet(post.getLandmarkNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);
        }
        parkingPostList = new StringBuffer();
        for (ParkingLots post : parkingLotsArrayList) {
            parkingPostList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getParkingLotsName() + "\n building number: " + post.getParkingLotsNumber() + "\n\n");
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(false).title(post.getParkingLotsName()).snippet(post.getParkingLotsNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);

        }
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
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setOnMarkerClickListener(this);
        // Add a marker in Sydney and move the camera
        LatLng cpp = new LatLng(34.056502, -117.821465);
        setMarkers();
        //mMap.addMarker(new MarkerOptions().position(cpp).title("Cal Poly Pomona"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cpp, 16));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng latLng)
            {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
                {
                    @Override
                    public boolean onMarkerClick(Marker marker)
                    {
                        CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) favoritesBtn.getLayoutParams();
                        CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) locationBtn.getLayoutParams();
                        p1.setAnchorId(View.NO_ID);
                        p2.setAnchorId(View.NO_ID);
                        favoritesBtn.setLayoutParams(p1);
                        locationBtn.setLayoutParams(p2);
                        favoritesBtn.setVisibility(View.GONE);
                        locationBtn.setVisibility(View.GONE);
                        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        bottomSheetHeading.setText(marker.getTitle());
                        favoritesBtnListener(marker);
                        navigationBtnListener(marker);
                        return false;
                    }
                });
                if(favoritesBtn.getVisibility() == View.GONE){
                    CoordinatorLayout.LayoutParams p1 = (CoordinatorLayout.LayoutParams) favoritesBtn.getLayoutParams();
                    CoordinatorLayout.LayoutParams p2 = (CoordinatorLayout.LayoutParams) locationBtn.getLayoutParams();
                    p1.setAnchorId(R.id.include);
                    p2.setAnchorId(R.id.include);
                    likeDetailBtn.setImageResource(R.drawable.ic_like_clear);
                    favoritesBtn.setLayoutParams(p1);
                    locationBtn.setLayoutParams(p2);
                    favoritesBtn.setVisibility(View.VISIBLE);
                    locationBtn.setVisibility(View.VISIBLE);
                }
                if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN){
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }
        });

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

    public void onBackPressed() {
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

    private void setDetailView(){
        bottomSheet = findViewById(R.id.bottomSheetLayout);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
        Log.i("Peek Height", String.valueOf(mBottomSheetBehavior.getPeekHeight()));
    }


    public String loadBuildingJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("location.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public String loadLandmarksJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("landmark.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public String loadParkingLotsJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("parkinglot.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private void favoritesBtnListener(final Marker m){
        likeDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                likeDetailBtn.setImageResource(R.drawable.ic_like_filled);
                favoritesArrayList.add(m);
            }
        });
    }

    private void navigationBtnListener(final Marker m){
        navigationDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BaseMap.this, NavigationActivity.class);
                Bundle args = new Bundle();
                LatLng mLatLng = m.getPosition();
                args.putString("title",m.getTitle());
                args.putString("snippet",m.getSnippet());
                args.putParcelable("position", mLatLng);
                args.putString("header",m.getTitle());
                intent.putExtra("bundle", args);
                startActivity(intent);

                //initNavigation(m);
            }
        });
    }


    private void initNavigation(final Marker m) {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            Navigation navigation = Navigation.getInstance();
            navigation.setGPSTracker(gpsTracker);
            navigation.setDestination(m.getPosition());
            navigation.setMap(mMap);
            navigation.setMarker(m);
            navigation.setMarkersList(markersArrayList);
            navigation.start();
        }
        else{
            Toast.makeText(this, "Location services needed to access navigation.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);
            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + clickCount + " times.",
                    Toast.LENGTH_SHORT).show();
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}