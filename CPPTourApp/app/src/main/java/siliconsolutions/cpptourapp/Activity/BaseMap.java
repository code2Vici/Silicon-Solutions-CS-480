package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import siliconsolutions.cpptourapp.Adapters.BuildingsListAdapter;
import siliconsolutions.cpptourapp.Adapters.EventsListAdapter;
import siliconsolutions.cpptourapp.Adapters.FavoritesListAdapter;
import siliconsolutions.cpptourapp.Adapters.HttpHandler;
import siliconsolutions.cpptourapp.Adapters.LandmarksListAdapter;
import siliconsolutions.cpptourapp.Adapters.MetersListAdapter;
import siliconsolutions.cpptourapp.Adapters.OfficesListAdapter;
import siliconsolutions.cpptourapp.Adapters.ParkingListAdapter;
import siliconsolutions.cpptourapp.Adapters.Utilities;
import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.Model.BusB;
import siliconsolutions.cpptourapp.Model.BusRouteA;
import siliconsolutions.cpptourapp.Model.GlobalVars;
import siliconsolutions.cpptourapp.Model.Landmarks;
import siliconsolutions.cpptourapp.Model.MyLocation;
import siliconsolutions.cpptourapp.Model.ParkingLots;
import siliconsolutions.cpptourapp.Model.Restaurants;
import siliconsolutions.cpptourapp.R;
import siliconsolutions.cpptourapp.Tour.StartTourFragment;
import siliconsolutions.cpptourapp.View.BottomSheetBehaviorGoogleMapsLike;
import siliconsolutions.cpptourapp.View.MergedAppBarLayoutBehavior;

public class BaseMap extends AppCompatActivity implements
        OnMapReadyCallback,
        View.OnClickListener,
        GPSTrackerListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private Toolbar toolbar;
    private DrawerLayout drawer;
    private ImageView btnMyLocation;
    private ImageView btnOpenFavoriteDrawer;
    public static GPSTracker gpsTracker;
    NavigationView leftNavigationView;
    private ProgressDialog progressDialog;
    private ArrayList<Building> buildingsArrayList;
    private ArrayList<Landmarks> landmarksArrayList;
    private ArrayList<ParkingLots> parkingLotsArrayList;
    private ArrayList<Restaurants> restaurantsArrayList;
    private ArrayList<BusRouteA> busRouteAArrayList;
    private ArrayList<BusB> busBLocation;
    private ArrayList<Marker> markersArrayList;
    private ArrayList<Marker> favoritesArrayList;
    private Marker myMarker;
    private StringBuffer postList;
    private StringBuffer landmarksPostList;
    private StringBuffer parkingPostList;
    private StringBuffer restaurantPostList;
    private StringBuffer busRouteAPostList;
    private StringBuffer busBLocationPostList;
    private MenuItem buildingsMenuItem;
    private MenuItem landmarksMenuItem;
    private MenuItem parkingMenuItem;
    private MenuItem restaurantMenuItem;
    private MenuItem busRouteAMenuItem;
    private MenuItem busRouteBMenuItem;
    private MenuItem busRouteCMenuItem;
    private TextView bottomSheetDescriptionText;
    private LinearLayout bottomSheetPeekBar;
    private ImageView bottomImageHeader;
    private CompoundButton buildingsCheckbox;
    private CompoundButton landmarksCheckbox;
    private CompoundButton parkingCheckbox;
    private CompoundButton restaurantCheckbox;
    private CompoundButton busRouteACheckbox;
    private CompoundButton busRouteBCheckbox;
    private CompoundButton busRouteCCheckbox;
    private TextView bottomSheetHeading;
    private TextView bottomSheetSubHeading;
    private TextView bottomSheetHeadingDistance;
    private TextView bottomSheetListTitle;
    private TextView bottomSheetRestroomTitle;
    private RecyclerView bottomSheetRecycler;
    private FloatingActionButton locationBtn;
    private FloatingActionButton favoritesBtn;
    private ImageButton likeDetailBtn;
    private ImageButton navigationDetailBtn;
    private CoordinatorLayout coordinatorLayout;
    private View bottomSheet;
    private int mStackLevel = 0;
    private MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior;
    BottomSheetBehaviorGoogleMapsLike behavior;
    private String TAG = BaseMap.class.getSimpleName();
    ArrayList<HashMap<String, String>> contactList;
    private com.google.android.gms.maps.model.Polyline lineA;
    private com.google.android.gms.maps.model.Polyline lineB;

    public String busBLat;
    public String busBLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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

                if (id == R.id.nav_left_start){
                    startTourDialog();
                } else if (id == R.id.nav_left_discover) {

                }  else if (id == R.id.nav_left_check_1) {
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
                } else if (id == R.id.nav_left_check_4) {
                    restaurantCheckbox.performClick();
                    if (item.isChecked()) {
                        item.setChecked(false);
                    }   else {
                        item.setChecked(true);
                    }
                }else if (id == R.id.nav_left_check_5) {

                    if (item.isChecked()) {
                        item.setChecked(false);
                    }   else {
                        item.setChecked(true);
                    }
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

        final BuildingsListAdapter buildingsListAdapter = new BuildingsListAdapter(this,R.id.right_nav_building_listView,buildingsArrayList);
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
                bottomSheetUpdateFromBuilding(buildingsArrayList.get(i));
                bottomSheetUpdateFromMarker(markersArrayList.get(i));
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
                markersArrayList.get(i).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markersArrayList.get(i).getPosition(),16));
                bottomSheetUpdateFromLandmark(landmarksArrayList.get(i - buildingsArrayList.size()));
                bottomSheetUpdateFromMarker(markersArrayList.get(i));
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
                bottomSheetUpdateFromParking(parkingLotsArrayList.get(i - buildingsArrayList.size() - landmarksArrayList.size()));
                bottomSheetUpdateFromMarker(markersArrayList.get(i));
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

    private void initialize() {
        initComponents();
        initLocations();
        initGPS();

        new GetBusBLoction().execute();

    }


    private void initComponents() {
        btnMyLocation = (ImageView) findViewById(R.id.btnMyLocation);
        btnMyLocation.setOnClickListener(this);
        btnOpenFavoriteDrawer = (ImageView) findViewById(R.id.btnOpenFavoriteDrawer);
        btnOpenFavoriteDrawer.setOnClickListener(this);
    }

    private void initLocations() {
        Type listType = new TypeToken<ArrayList<Building>>() {
        }.getType();
        Type landmarksListType = new TypeToken<ArrayList<Landmarks>>() {
        }.getType();
        Type parkingListType = new TypeToken<ArrayList<ParkingLots>>() {
        }.getType();
        Type restaurantListType = new TypeToken<ArrayList<Restaurants>>() {
        }.getType();
        Type busRouteAListType = new TypeToken<ArrayList<BusRouteA>>(){
        }.getType();

        favoritesArrayList = new ArrayList<>();
        buildingsArrayList = new GsonBuilder().create().fromJson(loadBuildingJSONFromAsset(), listType);
        landmarksArrayList = new GsonBuilder().create().fromJson(loadLandmarksJSONFromAsset(), landmarksListType);
        parkingLotsArrayList = new GsonBuilder().create().fromJson(loadParkingLotsJSONFromAsset(), parkingListType);
        restaurantsArrayList = new GsonBuilder().create().fromJson(loadRestaurantJSONFromAsset(),restaurantListType);
        busRouteAArrayList = new GsonBuilder().create().fromJson(loadBusRouteAJSONFromAsset(), busRouteAListType);

    }

    private void initGPS() {
        gpsTracker = new GPSTracker(this, this);
        if (gpsTracker.canGetLocation()) {
            double lat = gpsTracker.getLatitude();
            double lng = gpsTracker.getLongitude();
            if (lat != 0 || lng != 0) {
                GlobalVars.location = new MyLocation(lat, lng);
            }
        } else {
            gpsTracker.showSettingsAlert();
        }
    }

    public void setMarkers() {
        markersArrayList = new ArrayList<>();
        postList = new StringBuffer();
        landmarksPostList = new StringBuffer();
        for (Building post : buildingsArrayList) {
            postList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getBuildingName() + "\n building number: " + post.getBuildingNumber() +
                    "\n imageurl: " + post.getImageUrl() + "\n floorplan: " + post.getFloorPlanUrl() + "\n offices: " + post.getOfficeList() + "\n description: " + post.getDescritption() + "\n\n");
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)).visible(false).title(post.getBuildingName()).snippet(post.getBuildingNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);
        }
        for (Landmarks post : landmarksArrayList) {
            landmarksPostList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getLandmarkName() + "\n building number: " + post.getLandmarkNumber() +
                    "\n imageurl: " + post.getImageUrl() +"\n description: " + post.getDescription() + "\n eventList: " + post.getEventList() + "\n\n");

            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)).title(post.getLandmarkName()).snippet(post.getLandmarkNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);
        }
        parkingPostList = new StringBuffer();
        for (ParkingLots post : parkingLotsArrayList) {
            parkingPostList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getParkingLotsName()+ "\n meters " + post.getMeters() + "\n description " + post.getDescription() + "\n meterlist: " + post.getMeters() + "\n building number: " + post.getParkingLotsNumber() + "\n imageUrl: " + post.getImageUrl() + "\n\n");
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).visible(false).title(post.getParkingLotsName()).snippet(post.getParkingLotsNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);

        }
        restaurantPostList = new StringBuffer();
        for (Restaurants post : restaurantsArrayList) {
            restaurantPostList.append("\n latitude: " + post.getLatitude() + "\n longtitude: " + post.getLongtitude() +
                    "\n building name: " + post.getRestaurantName()+ "\n meters " + post.getMeters() + "\n description " + post.getDescription() + "\n meterlist: " + post.getMeters() + "\n building number: " + post.getRestaurantNumber() + "\n imageUrl: " + post.getImageUrl() + "\n\n");
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)).visible(false).title(post.getRestaurantName()).snippet(post.getRestaurantNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);

        }

        busRouteAPostList = new StringBuffer();
        for (BusRouteA post : busRouteAArrayList) {
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).visible(false).title(post.getBusRouteName()).snippet(post.getBusRouteNumber()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongtitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);

        }
/*
        busBLocationPostList = new StringBuffer();
        for (BusB post : busBLocation) {
            myMarker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)).visible(false).title(post.getBusName()).snippet(post.getRouteID()).position(new LatLng(Double.parseDouble(post.getLatitude()), Double.parseDouble(post.getLongitude()))));
            myMarker.setTag(markersArrayList.size());
            markersArrayList.add(myMarker);

        }*/
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
        restaurantMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_4);
        restaurantCheckbox = (CompoundButton) MenuItemCompat.getActionView(restaurantMenuItem);

        restaurantCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });


        busRouteAMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_5);

        busRouteACheckbox = (CompoundButton) MenuItemCompat.getActionView(busRouteAMenuItem);
        busRouteACheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    busARoute();
                    mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).position(new LatLng(Double.parseDouble(busBLat), Double.parseDouble(busBLong))));

                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size() + busRouteAArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    lineA.remove();
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size() + busRouteAArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });

        busRouteBMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_6);//TODO:
        //busRouteAMenuItem = leftNavigationView.getMenu().findItem(R.id.nav_left_check_5);
        busRouteBCheckbox = (CompoundButton) MenuItemCompat.getActionView(busRouteBMenuItem);
        busRouteBCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    busBRoute();
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size() + busRouteAArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(true);
                    }
                }
                else{
                    lineB.remove();
                    for(int i = (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size()); i < (buildingsArrayList.size() + landmarksArrayList.size() + parkingLotsArrayList.size() + restaurantsArrayList.size() + busRouteAArrayList.size()); i++){
                        markersArrayList.get(i).setVisible(false);
                    }
                }
            }
        });
    }



    private void showDialog() {
        View view = getLayoutInflater().inflate(R.layout.favorites_dialog, null );
        RelativeLayout relative = (RelativeLayout) view.findViewById(R.id.favorites_dialog_emptyView);
        ListView lv = (ListView) view.findViewById(R.id.favoritesList);
        FavoritesListAdapter adapter = new FavoritesListAdapter(this,R.id.favoritesList,favoritesArrayList);
        lv.setAdapter(adapter);

        if(favoritesArrayList.size() > 0 && relative.getVisibility() != View.GONE){
            relative.setVisibility(View.GONE);
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder( this );
        dialog.setView(view);
        final AlertDialog d = dialog.show();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int val = Integer.parseInt(favoritesArrayList.get(i).getTag().toString());
                favoritesArrayList.get(i).showInfoWindow();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(favoritesArrayList.get(i).getPosition(),16));
                bottomSheetUpdateFromMarker(favoritesArrayList.get(i));
                detailViewAssignment(val);
                d.dismiss();
            }
        });
    }

    public void detailViewAssignment(int val){
        if(val < buildingsArrayList.size()){
            bottomSheetUpdateFromBuilding(buildingsArrayList.get(val));
        }
        else if(val < (landmarksArrayList.size() + buildingsArrayList.size())){
            bottomSheetUpdateFromLandmark(landmarksArrayList.get(val - buildingsArrayList.size()));
        }
        else if (val < (landmarksArrayList.size() + buildingsArrayList.size() + landmarksArrayList.size())){
            bottomSheetUpdateFromParking(parkingLotsArrayList.get(val - buildingsArrayList.size() - landmarksArrayList.size()));
        }
        else{
            bottomSheetUpdateFromRestaurants(restaurantsArrayList.get(val - buildingsArrayList.size() -
                    landmarksArrayList.size() - parkingLotsArrayList.size()));
        }
    }


    private void setDetailView(){
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehaviorGoogleMapsLike.from(bottomSheet);
        behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
        bottomSheetHeading = (TextView) findViewById(R.id.bottomSheetHeading);
        bottomSheetSubHeading = (TextView) findViewById(R.id.bottomSheetSubheader);
        bottomSheetHeadingDistance = (TextView) findViewById(R.id.bottomSheetHeadingDistance);
        bottomSheetPeekBar = (LinearLayout) findViewById(R.id.bottom_sheet_peek_bar_container);
        bottomImageHeader = (ImageView) findViewById(R.id.bottomSheetImage);
        bottomSheetDescriptionText = (TextView) findViewById(R.id.description_bottom_sheet);
        bottomSheetListTitle = (TextView) findViewById(R.id.bottom_sheet_list_title);
        bottomSheetRecycler = (RecyclerView) findViewById(R.id.bottom_sheet_recycler);
        bottomSheetRestroomTitle = (TextView) findViewById(R.id.bottom_sheet_restroom_text);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        bottomSheetRecycler.setLayoutManager(mLayoutManager);
        AppBarLayout mergedAppBarLayout = (AppBarLayout) findViewById(R.id.merged_appbarlayout);
        mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        behavior.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback() {
            @Override
            public void onStateChanged(View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                        Log.d("bottomsheet-", "STATE_COLLAPSED");
                        toolbar.getNavigationIcon().setAlpha(255);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                        if(bottomSheetPeekBar.getVisibility() == View.INVISIBLE){
                            bottomSheetPeekBar.setVisibility(View.VISIBLE);
                        }
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                        Log.d("bottomsheet-", "STATE_EXPANDED");
                        bottomSheetPeekBar.setVisibility(View.INVISIBLE);

                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                        Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                        toolbar.getNavigationIcon().setAlpha(0);
                        break;
                    case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                        Log.d("bottomsheet-", "STATE_HIDDEN");
                        break;
                    default:
                        Log.d("bottomsheet-", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        mergedAppBarLayoutBehavior.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
            }
        });

    }

    public void bottomSheetUpdateFromBuilding(Building b){
        bottomSheetDescriptionText.setText(b.getDescritption());
        if(!b.getImageUrl().equals("")){
            Picasso.with(getApplicationContext()).load(b.getImageUrl()).into(bottomImageHeader);
        }
        else{
            Picasso.with(getApplicationContext()).load(R.drawable.notavailableimg).into(bottomImageHeader);
        }
        OfficesListAdapter adapter = new OfficesListAdapter(b.getOfficeList());
        bottomSheetRecycler.setAdapter(adapter);
        if(bottomSheetRestroomTitle.getVisibility() == View.INVISIBLE){
            bottomSheetRestroomTitle.setVisibility(View.VISIBLE);
        }
        if(b.getOfficeList().size() != 0){
            if(bottomSheetListTitle.getVisibility() == View.GONE)
                bottomSheetListTitle.setVisibility(View.VISIBLE);
            bottomSheetListTitle.setText("OFFICES");
        }
        else{
            bottomSheetListTitle.setVisibility(View.GONE);
        }
    }

    public void bottomSheetUpdateFromLandmark(Landmarks l){
        bottomSheetDescriptionText.setText(l.getDescription());
        if(!l.getImageUrl().equals("")){
            Picasso.with(getApplicationContext()).load(l.getImageUrl()).into(bottomImageHeader);
        }
        else{
            Picasso.with(getApplicationContext()).load(R.drawable.notavailableimg).into(bottomImageHeader);
        }
        EventsListAdapter adapter = new EventsListAdapter(l.getEventList());
        bottomSheetRecycler.setAdapter(adapter);
        if(bottomSheetRestroomTitle.getVisibility() == View.VISIBLE){
            bottomSheetRestroomTitle.setVisibility(View.INVISIBLE);
        }
        if(l.getEventList().size() != 0){
            if(bottomSheetListTitle.getVisibility() == View.GONE)
                bottomSheetListTitle.setVisibility(View.VISIBLE);
            bottomSheetListTitle.setText("EVENTS");
        }
        else{
            bottomSheetListTitle.setVisibility(View.GONE);
        }
    }

    public void bottomSheetUpdateFromParking(ParkingLots p){
        bottomSheetDescriptionText.setText(p.getDescription());
        if(!p.getImageUrl().equals("")){
            Picasso.with(getApplicationContext()).load(p.getImageUrl()).into(bottomImageHeader);
        }
        else{
            Picasso.with(getApplicationContext()).load(R.drawable.notavailableimg).into(bottomImageHeader);
        }
        MetersListAdapter adapter = new MetersListAdapter(p.getMeters());
        bottomSheetRecycler.setAdapter(adapter);
        if(bottomSheetRestroomTitle.getVisibility() == View.VISIBLE){
            bottomSheetRestroomTitle.setVisibility(View.INVISIBLE);
        }
        if(p.getMeters().size() != 0){
            if(bottomSheetListTitle.getVisibility() == View.GONE)
                bottomSheetListTitle.setVisibility(View.VISIBLE);
            bottomSheetListTitle.setText("METERS");
        }
        else{
            bottomSheetListTitle.setVisibility(View.GONE);
        }
    }

    private void bottomSheetUpdateFromRestaurants(Restaurants restaurants) {
        //bottomSheetDescriptionText.setText();
    }


    public void bottomSheetUpdateFromMarker(Marker m){

        bottomSheetHeading.setText(m.getTitle());
        if(!(m.getSnippet().length() > 4) && !(m.getSnippet()).equals("")){
            bottomSheetSubHeading.setText("Building " + m.getSnippet());
        }
        else{
            bottomSheetSubHeading.setText(m.getSnippet());
        }
        siliconsolutions.cpptourapp.Model.Location location = getFavoriteObjectByID(m);
        if(likeDetailBtn.getTag() == null){
            likeDetailBtn.setTag("unselected");
        }
        if(location.isFavorite()){
            likeDetailBtn.setTag("selected");
            likeDetailBtn.setImageResource(R.drawable.ic_like_filled);
        }
        else{
            likeDetailBtn.setTag("unselected");
            likeDetailBtn.setImageResource(R.drawable.ic_like_clear);
        }
        favoritesBtnListener(m,location);
        navigationBtnListener(m);
        float[] result = new float[1];
        Location.distanceBetween(gpsTracker.getLatitude(),gpsTracker.getLongitude(),m.getPosition().latitude,m.getPosition().longitude,result);
        String distance = Utilities.formatDistance(result[0]);
        bottomSheetHeadingDistance.setText(distance + " ft away");
        mergedAppBarLayoutBehavior.setToolbarTitle(m.getTitle());
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
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {
            @Override
            public boolean onMarkerClick(Marker marker)
            {
                behavior.setState(BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED);
                int val = Integer.valueOf(marker.getTag().toString());
                detailViewAssignment(val);
                bottomSheetUpdateFromMarker(marker);
                return false;
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

    public String loadRestaurantJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("restaurant.json");
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

    public String loadBusRouteAJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("busstopA.json");
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



    private class GetBusBLoction extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(BaseMap.this,"Json Data is downloading",Toast.LENGTH_LONG).show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String url = "https://broncoshuttle.com/Route/4512/Vehicles";
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    // Getting JSON Array node
                    JSONArray contacts = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < contacts.length(); i++) {
                        JSONObject c = contacts.getJSONObject(i);

                        // Phone node is JSON Object
                        JSONObject phone = c.getJSONObject("Coordinate");
                        busBLat = phone.getString("Latitude");
                        busBLong = phone.getString("Longitude");

                        Log.e("Abc:", busBLat+ " " + busBLong);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });

                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

    }

    private void favoritesBtnListener(final Marker m, final siliconsolutions.cpptourapp.Model.Location location){
        likeDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((likeDetailBtn.getTag().toString()).equals("unselected")){
                    location.setFavorite(true);
                    favoritesArrayList.add(m);
                    likeDetailBtn.setImageResource(R.drawable.ic_like_filled);
                    likeDetailBtn.setTag("selected");
                }
                else{
                    likeDetailBtn.setImageResource(R.drawable.ic_like_clear);
                    likeDetailBtn.setTag("unselected");
                    if(favoritesArrayList.contains(m)){
                        favoritesArrayList.remove(m);
                    }
                    location.setFavorite(false);
                }

            }
        });
    }

    private siliconsolutions.cpptourapp.Model.Location getFavoriteObjectByID(Marker m){
        int val = Integer.parseInt(m.getTag().toString());
        siliconsolutions.cpptourapp.Model.Location location;
        if(val < buildingsArrayList.size()){
            location = buildingsArrayList.get(val);
        }
        else if(val < (landmarksArrayList.size() + buildingsArrayList.size())){
            location = landmarksArrayList.get(val - buildingsArrayList.size());
        }
        else if(val < (landmarksArrayList.size() + buildingsArrayList.size() + parkingLotsArrayList.size())){
            location = parkingLotsArrayList.get(val - buildingsArrayList.size() - landmarksArrayList.size());
        } else {
            location = restaurantsArrayList.get(val - buildingsArrayList.size() - landmarksArrayList.size() - parkingLotsArrayList.size());
        }
        return location;
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
            }
        });
    }

    public void startTourDialog(){
            mStackLevel++;

            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            android.app.Fragment prev = getFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            // Create and show the dialog.
            DialogFragment newFragment = StartTourFragment.newInstance(mStackLevel);
            newFragment.show(ft, "dialog");

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Integer clickCount = (Integer) marker.getTag();

        // Check if a click count was set, then display the click count.
        if (clickCount != null) {
            clickCount = clickCount + 1;
            marker.setTag(clickCount);

        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void busARoute(){
        lineA = mMap.addPolyline(new PolylineOptions().add(new LatLng(34.048406, -117.815238), new LatLng(34.048615, -117.815091),new LatLng(34.048837, -117.815116),
                new LatLng(34.049065, -117.815789),new LatLng(34.049102, -117.816195),new LatLng(34.049025, -117.816268),new LatLng(34.049495, -117.817084),
                new LatLng(34.052174, -117.815027),new LatLng(34.052884, -117.814440),new LatLng(34.053669, -117.813682),new LatLng(34.053105, -117.812839),
                new LatLng(34.052922, -117.812144),new LatLng(34.052795, -117.812058),new LatLng(34.052376, -117.812176),new LatLng(34.051886, -117.812487),
                new LatLng(34.049464, -117.814465),new LatLng(34.050347, -117.816838),new LatLng(34.050569, -117.818835),new LatLng(34.050589, -117.820644),
                new LatLng(34.051268, -117.820614),new LatLng(34.051708, -117.820697),new LatLng(34.053217, -117.821456),new LatLng(34.053443, -117.821691),
                new LatLng(34.053581, -117.822033), new LatLng(34.053572, -117.822728),new LatLng(34.053492, -117.823581),new LatLng(34.053737, -117.824188),
                new LatLng(34.054048, -117.824611),new LatLng(34.054119, -117.824955),new LatLng(34.054070, -117.825888),new LatLng(34.054088, -117.826151),
                new LatLng(34.054221, -117.826403),new LatLng(34.054719, -117.826746),new LatLng(34.055870, -117.827256), new LatLng(34.056146, -117.827417),
                new LatLng(34.057306, -117.827846),new LatLng(34.057506, -117.827803),new LatLng(34.057732, -117.827599),new LatLng(34.059661, -117.823243),
                new LatLng(34.060088, -117.822219),new LatLng(34.060279, -117.821945),new LatLng(34.061608, -117.820652),new LatLng(34.061750, -117.820341),
                new LatLng(34.061683, -117.818437),new LatLng(34.060919, -117.818335),new LatLng(34.060457, -117.818281),new LatLng(34.060434, -117.818592),
                new LatLng(34.060372, -117.818694), new LatLng(34.059790, -117.819188),new LatLng(34.059639, -117.819236),new LatLng(34.059519, -117.819113),
                new LatLng(34.059435, -117.818748),new LatLng(34.059488, -117.818136),new LatLng(34.059955, -117.818163),new LatLng(34.060839, -117.818254),
                new LatLng(34.061750, -117.818389),new LatLng(34.061737, -117.818963),new LatLng(34.061843, -117.820143),new LatLng(34.061781, -117.820454),
                new LatLng(34.061661, -117.820690),new LatLng(34.060354, -117.821940),new LatLng(34.060243, -117.822085),new LatLng(34.060079, -117.822342),
                new LatLng(34.059697, -117.823302),new LatLng(34.057764, -117.827642),new LatLng(34.057630, -117.827803),new LatLng(34.057399, -117.827916),
                new LatLng(34.057141, -117.827868),new LatLng(34.056190, -117.827481),new LatLng(34.055821, -117.827299),new LatLng(34.054781, -117.826844),
                new LatLng(34.054257, -117.826537), new LatLng(34.054075, -117.826290), new LatLng(34.054012, -117.826011),new LatLng(34.054052, -117.824922),
                new LatLng(34.053972, -117.824606), new LatLng(34.053972, -117.824606),new LatLng(34.053577, -117.824021),new LatLng(34.053444, -117.823635),
                new LatLng(34.053537, -117.822289), new LatLng(34.053452, -117.821843),new LatLng(34.053212, -117.821532),new LatLng(34.051777, -117.820786),
                new LatLng(34.051346, -117.820690), new LatLng(34.050412, -117.820706),new LatLng(34.050350, -117.817986),new LatLng(34.050252, -117.817278),
                new LatLng(34.050141, -117.816742), new LatLng(34.049559, -117.817219),new LatLng(34.049279, -117.816747),new LatLng(34.048994, -117.816291),
                new LatLng(34.048274, -117.816844), new LatLng(34.047772, -117.815529),new LatLng(34.048406, -117.815238)));lineA = mMap.addPolyline(new PolylineOptions().add(new LatLng(34.048406, -117.815238), new LatLng(34.048615, -117.815091),new LatLng(34.048837, -117.815116),
                new LatLng(34.049065, -117.815789),new LatLng(34.049102, -117.816195),new LatLng(34.049025, -117.816268),new LatLng(34.049495, -117.817084),
                new LatLng(34.052174, -117.815027),new LatLng(34.052884, -117.814440),new LatLng(34.053669, -117.813682),new LatLng(34.053105, -117.812839),
                new LatLng(34.052922, -117.812144),new LatLng(34.052795, -117.812058),new LatLng(34.052376, -117.812176),new LatLng(34.051886, -117.812487),
                new LatLng(34.049464, -117.814465),new LatLng(34.050347, -117.816838),new LatLng(34.050569, -117.818835),new LatLng(34.050589, -117.820644),
                new LatLng(34.051268, -117.820614),new LatLng(34.051708, -117.820697),new LatLng(34.053217, -117.821456),new LatLng(34.053443, -117.821691),
                new LatLng(34.053581, -117.822033), new LatLng(34.053572, -117.822728),new LatLng(34.053492, -117.823581),new LatLng(34.053737, -117.824188),
                new LatLng(34.054048, -117.824611),new LatLng(34.054119, -117.824955),new LatLng(34.054070, -117.825888),new LatLng(34.054088, -117.826151),
                new LatLng(34.054221, -117.826403),new LatLng(34.054719, -117.826746),new LatLng(34.055870, -117.827256), new LatLng(34.056146, -117.827417),
                new LatLng(34.057306, -117.827846),new LatLng(34.057506, -117.827803),new LatLng(34.057732, -117.827599),new LatLng(34.059661, -117.823243),
                new LatLng(34.060088, -117.822219),new LatLng(34.060279, -117.821945),new LatLng(34.061608, -117.820652),new LatLng(34.061750, -117.820341),
                new LatLng(34.061683, -117.818437),new LatLng(34.060919, -117.818335),new LatLng(34.060457, -117.818281),new LatLng(34.060434, -117.818592),
                new LatLng(34.060372, -117.818694), new LatLng(34.059790, -117.819188),new LatLng(34.059639, -117.819236),new LatLng(34.059519, -117.819113),
                new LatLng(34.059435, -117.818748),new LatLng(34.059488, -117.818136),new LatLng(34.059955, -117.818163),new LatLng(34.060839, -117.818254),
                new LatLng(34.061750, -117.818389),new LatLng(34.061737, -117.818963),new LatLng(34.061843, -117.820143),new LatLng(34.061781, -117.820454),
                new LatLng(34.061661, -117.820690),new LatLng(34.060354, -117.821940),new LatLng(34.060243, -117.822085),new LatLng(34.060079, -117.822342),
                new LatLng(34.059697, -117.823302),new LatLng(34.057764, -117.827642),new LatLng(34.057630, -117.827803),new LatLng(34.057399, -117.827916),
                new LatLng(34.057141, -117.827868),new LatLng(34.056190, -117.827481),new LatLng(34.055821, -117.827299),new LatLng(34.054781, -117.826844),
                new LatLng(34.054257, -117.826537), new LatLng(34.054075, -117.826290), new LatLng(34.054012, -117.826011),new LatLng(34.054052, -117.824922),
                new LatLng(34.053972, -117.824606), new LatLng(34.053972, -117.824606),new LatLng(34.053577, -117.824021),new LatLng(34.053444, -117.823635),
                new LatLng(34.053537, -117.822289), new LatLng(34.053452, -117.821843),new LatLng(34.053212, -117.821532),new LatLng(34.051777, -117.820786),
                new LatLng(34.051346, -117.820690), new LatLng(34.050412, -117.820706),new LatLng(34.050350, -117.817986),new LatLng(34.050252, -117.817278),
                new LatLng(34.050141, -117.816742), new LatLng(34.049559, -117.817219),new LatLng(34.049279, -117.816747),new LatLng(34.048994, -117.816291),
                new LatLng(34.048274, -117.816844), new LatLng(34.047772, -117.815529),new LatLng(34.048406, -117.815238)));
    }

    private void busBRoute() {
        lineB = mMap.addPolyline(new PolylineOptions().add(
                new LatLng(34.050257, -117.816496),new LatLng(34.050347, -117.816838),new LatLng(34.050569, -117.818835),new LatLng(34.050589, -117.820644),
                new LatLng(34.051268, -117.820614),new LatLng(34.051708, -117.820697),new LatLng(34.053217, -117.821456),new LatLng(34.053443, -117.821691),
                new LatLng(34.053581, -117.822033), new LatLng(34.053572, -117.822728),new LatLng(34.053492, -117.823581),new LatLng(34.053737, -117.824188),
                new LatLng(34.054048, -117.824611),new LatLng(34.054119, -117.824955),new LatLng(34.054070, -117.825888),new LatLng(34.054088, -117.826151),
                new LatLng(34.054221, -117.826403),new LatLng(34.054719, -117.826746),/*b route*/new LatLng(34.054768, -117.826770),new LatLng(34.054808, -117.826685),
                new LatLng(34.054860, -117.826601),new LatLng(34.054901, -117.826554), new LatLng(34.054956, -117.826531),new LatLng(34.055054, -117.826501),
                new LatLng(34.055116, -117.826510),new LatLng(34.055212, -117.826550), new LatLng(34.055325, -117.826617),new LatLng(34.055403, -117.826683),
                new LatLng(34.055468, -117.826717),new LatLng(34.055641, -117.826721), new LatLng(34.056025, -117.826591),new LatLng(34.055857, -117.825982),
                new LatLng(34.055450, -117.825366),new LatLng(34.055140, -117.825189), new LatLng(34.055171, -117.825030),new LatLng(34.055327, -117.824865),
                new LatLng(34.055438, -117.824810),new LatLng(34.055664, -117.824991), new LatLng(34.055893, -117.825237), new LatLng(34.056069, -117.825556),
                new LatLng(34.056021, -117.825813),new LatLng(34.055921, -117.825972), new LatLng(34.056096, -117.826670), new LatLng(34.055775, -117.826781),
                new LatLng(34.055649, -117.826806),new LatLng(34.055441, -117.826797), new LatLng(34.055367, -117.826766),new LatLng(34.055279, -117.826696),
                new LatLng(34.055085, -117.826600),new LatLng(34.055032, -117.826598), new LatLng(34.054939, -117.826632),new LatLng(34.054881, -117.826704),
                new LatLng(34.054837, -117.826799),/*b*/

                new LatLng(34.055870, -117.827256), new LatLng(34.056146, -117.827417),
                new LatLng(34.057306, -117.827846),new LatLng(34.057506, -117.827803),new LatLng(34.057732, -117.827599),new LatLng(34.059661, -117.823243),
                new LatLng(34.060088, -117.822219),new LatLng(34.060279, -117.821945),new LatLng(34.061608, -117.820652),new LatLng(34.061750, -117.820341),
                new LatLng(34.061683, -117.818437),

                new LatLng(34.061720, -117.818174),new LatLng(34.061764, -117.817945),new LatLng(34.061875, -117.817730),
                new LatLng(34.062027, -117.817543), new LatLng(34.060765, -117.815328),new LatLng(34.061982, -117.814722),new LatLng(34.061435, -117.813948),
                new LatLng(34.060703, -117.813216),new LatLng(34.061197, -117.812545),new LatLng(34.061303, -117.812294),new LatLng(34.061168, -117.812048),
                new LatLng(34.060345, -117.812780),new LatLng(34.060417, -117.812935),new LatLng(34.061027, -117.812389),
                new LatLng(34.060961, -117.812249), new LatLng(34.061137, -117.812089),new LatLng(34.061266, -117.812298),new LatLng(34.061172, -117.812507),
                new LatLng(34.060640, -117.813235),new LatLng(34.060417, -117.812935),new LatLng(34.061043, -117.813607),
                new LatLng(34.060813, -117.813978), new LatLng(34.060571, -117.814274),new LatLng(34.060298, -117.814525),new LatLng(34.060021, -117.814820),
                new LatLng(34.059754, -117.815181),new LatLng(34.059412, -117.815727),new LatLng(34.059613, -117.815966),new LatLng(34.059745, -117.816402),
                new LatLng(34.059880, -117.816543),new LatLng(34.060119, -117.816118),new LatLng(34.060430, -117.815708),new LatLng(34.060804, -117.815336),
                new LatLng(34.062089, -117.817533),new LatLng(34.061992, -117.817635),new LatLng(34.061933, -117.817714),new LatLng(34.061857, -117.817847),
                new LatLng(34.061807, -117.817979), new LatLng(34.061766, -117.818183),

                new LatLng(34.061750, -117.818389),new LatLng(34.061737, -117.818963),new LatLng(34.061843, -117.820143),new LatLng(34.061781, -117.820454),
                new LatLng(34.061661, -117.820690),new LatLng(34.060354, -117.821940),new LatLng(34.060243, -117.822085),new LatLng(34.060079, -117.822342),
                new LatLng(34.059697, -117.823302),new LatLng(34.057764, -117.827642),new LatLng(34.057630, -117.827803),new LatLng(34.057399, -117.827916),
                new LatLng(34.057141, -117.827868),new LatLng(34.056190, -117.827481),new LatLng(34.055821, -117.827299),new LatLng(34.054781, -117.826844),
                new LatLng(34.054920, -117.826610), new LatLng(34.054971, -117.826583), new LatLng(34.055071, -117.826568),new LatLng(34.055126, -117.826586),
                new LatLng(34.055287, -117.826671), new LatLng(34.055367, -117.826741),new LatLng(34.055478, -117.826780),new LatLng(34.055650, -117.826780),
                new LatLng(34.055836, -117.826732), new LatLng(34.056063, -117.826638),new LatLng(34.055943, -117.826167),new LatLng(34.055882, -117.825975),
                new LatLng(34.055774, -117.825804), new LatLng(34.055662, -117.825594),new LatLng(34.055574, -117.825453),new LatLng(34.055461, -117.825323),
                new LatLng(34.055319, -117.825235), new LatLng(34.055164, -117.825166),new LatLng(34.055191, -117.825063),new LatLng(34.055327, -117.824904),
                new LatLng(34.055430, -117.824843), new LatLng(34.055617, -117.824979), new LatLng(34.055875, -117.825257),new LatLng(34.055965, -117.825399),
                new LatLng(34.056047, -117.825562), new LatLng(34.056051, -117.825611),new LatLng(34.055999, -117.825807),new LatLng(34.055889, -117.825962),
                new LatLng(34.055952, -117.826154), new LatLng(34.056064, -117.826608),new LatLng(34.055766, -117.826725),new LatLng(34.055620, -117.826756),
                new LatLng(34.055451, -117.826748), new LatLng(34.055311, -117.826655),new LatLng(34.055097, -117.826547),new LatLng(34.055020, -117.826543),
                new LatLng(34.054934, -117.826572), new LatLng(34.054840, -117.826670), new LatLng(34.054765, -117.826826),


                new LatLng(34.054257, -117.826537), new LatLng(34.054075, -117.826290), new LatLng(34.054012, -117.826011),new LatLng(34.054052, -117.824922),
                new LatLng(34.053972, -117.824606), new LatLng(34.053972, -117.824606),new LatLng(34.053577, -117.824021),new LatLng(34.053444, -117.823635),
                new LatLng(34.053537, -117.822289), new LatLng(34.053452, -117.821843),new LatLng(34.053212, -117.821532),new LatLng(34.051777, -117.820786),
                new LatLng(34.051346, -117.820690), new LatLng(34.050412, -117.820706),new LatLng(34.050194, -117.820620),new LatLng(34.050070, -117.820331),
                new LatLng(34.049874, -117.820266), new LatLng(34.049457, -117.820298),new LatLng(34.049119, -117.820266),new LatLng(34.048830, -117.820390),
                new LatLng(34.048594, -117.820744), new LatLng(34.048162, -117.820653),new LatLng(34.047992, -117.820270),new LatLng(34.048046, -117.820160),
                new LatLng(34.048605, -117.819750), new LatLng(34.048960, -117.819621),new LatLng(34.049064, -117.819492),new LatLng(34.049030, -117.819386),
                new LatLng(34.048788, -117.819151), new LatLng(34.048533, -117.818506),new LatLng(34.048605, -117.818074),new LatLng(34.048822, -117.817721),
                new LatLng(34.049108, -117.817482), new LatLng(34.049627, -117.817019),new LatLng(34.050257, -117.816496)));
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