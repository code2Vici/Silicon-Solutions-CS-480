package siliconsolutions.cpptourapp.Activity;

import android.Manifest;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
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
import android.util.TypedValue;
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
import java.lang.reflect.Type;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import siliconsolutions.cpptourapp.Adapters.BuildingsListAdapter;
import siliconsolutions.cpptourapp.Adapters.EventsListAdapter;
import siliconsolutions.cpptourapp.Adapters.FavoritesListAdapter;
import siliconsolutions.cpptourapp.Adapters.LandmarksListAdapter;
import siliconsolutions.cpptourapp.Adapters.MetersListAdapter;
import siliconsolutions.cpptourapp.Adapters.OfficesListAdapter;
import siliconsolutions.cpptourapp.Adapters.ParkingListAdapter;
import siliconsolutions.cpptourapp.Adapters.Utilities;
import siliconsolutions.cpptourapp.GPS.GPSTracker;
import siliconsolutions.cpptourapp.GPS.GPSTrackerListener;
import siliconsolutions.cpptourapp.Model.Building;
import siliconsolutions.cpptourapp.Model.GlobalVars;
import siliconsolutions.cpptourapp.Model.Landmarks;
import siliconsolutions.cpptourapp.Model.MyLocation;
import siliconsolutions.cpptourapp.Model.ParkingLots;
import siliconsolutions.cpptourapp.R;
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
    private ArrayList<Marker> markersArrayList;
    private ArrayList<Marker> favoritesArrayList;
    private Marker myMarker;
    private StringBuffer postList;
    private StringBuffer landmarksPostList;
    private StringBuffer parkingPostList;
    private MenuItem buildingsMenuItem;
    private MenuItem landmarksMenuItem;
    private MenuItem parkingMenuItem;
    private TextView bottomSheetDescriptionText;
    private LinearLayout bottomSheetPeekBar;
    private ImageView bottomImageHeader;
    private CompoundButton buildingsCheckbox;
    private CompoundButton landmarksCheckbox;
    private CompoundButton parkingCheckbox;
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
    private MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior;
    BottomSheetBehaviorGoogleMapsLike behavior;

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

                if (id == R.id.nav_left_start) {

                } else if (id == R.id.nav_left_discover) {

                } else if (id == R.id.nav_slideshow) {

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
        else{
            bottomSheetUpdateFromParking(parkingLotsArrayList.get(val - buildingsArrayList.size() - landmarksArrayList.size()));
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

    public void bottomSheetUpdateFromMarker(Marker m){

        bottomSheetHeading.setText(m.getTitle());
        if(!(m.getSnippet()).equals("")){
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
        else{
            location = parkingLotsArrayList.get(val - buildingsArrayList.size() - landmarksArrayList.size());
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}