package pe.gdgopenlima.cooltura.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pe.gdgopenlima.cooltura.R;
import pe.gdgopenlima.cooltura.adapters.PlaceListAdapter;
import pe.gdgopenlima.cooltura.entities.Place;
import pe.gdgopenlima.cooltura.helpers.Database;

/**
 * Created by armando on 21/09/14.
 */
public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionsTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = mDrawerTitle = getTitle();
        mOptionsTitle = getResources().getStringArray(R.array.options_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionsTitle));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        Bundle args;
        switch (position) {
            case 0:
                fragment = new MapsFragment();
                args = new Bundle();
                args.putInt(MapsFragment.ARG_OPTION_NUMBER, position);
                fragment.setArguments(args);
                break;
            case 1:
                fragment = new PlaceListFragment();
                args = new Bundle();
                args.putInt(PlaceListFragment.ARG_PLACES_NUMBER, position);
                fragment.setArguments(args);

                break;
        }


        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        // update selected item and title, then close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mOptionsTitle[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public static class MapsFragment extends Fragment implements LocationListener {
        public static final String ARG_OPTION_NUMBER = "option_number";

        private GoogleMap googleMap;

        public MapsFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gmaps, container, false);

            int i = getArguments().getInt(ARG_OPTION_NUMBER);
            String option = getResources().getStringArray(R.array.options_array)[i];

            setUpMapIfNeeded();

            getActivity().setTitle(option);
            return rootView;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            MapFragment f = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.gmaps);
            if (f != null)
                getFragmentManager().beginTransaction().remove(f).commit();
        }

        private void setUpMapIfNeeded() {
            // Do a null check to confirm that we have not already instantiated the map.
            if (googleMap == null) {
                // Try to obtain the map from the SupportMapFragment.
                FragmentManager fragmentManager = getActivity().getFragmentManager();
                final MapFragment fragmentById = (MapFragment) fragmentManager.findFragmentById(R.id.gmaps);
                googleMap = fragmentById.getMap();
                // Check if we were successful in obtaining the map.
                if (googleMap != null) {
                    setUpMap();
                }
            }
        }

        private void setUpMap() {

            Database db = new Database(getActivity().getApplicationContext());
            List<Place> places = new ArrayList<Place>();
            try {
                Dao<Place, Integer> dao = db.getDao(Place.class);
                places = dao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setMyLocationEnabled(true);

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);


            for (Place place : places) {
                googleMap.addMarker(new MarkerOptions().position(new LatLng(place.longitude, place.latitude)).title(place.getName()));
            }

        }

        @Override
        public void onLocationChanged(Location location) {

            // Getting latitude of the current location
            double latitude = location.getLatitude();

            // Getting longitude of the current location
            double longitude = location.getLongitude();

            // Creating a LatLng object for the current location
            LatLng latLng = new LatLng(latitude, longitude);

            // Showing the current location in Google Map
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            // Zoom in the Google Map
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


    public static class PlaceListFragment extends ListFragment implements
            AdapterView.OnItemClickListener {
        public static final String ARG_PLACES_NUMBER = "places_number";

        public PlaceListFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_list, container, false);

            return rootView;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            Database db = new Database(getActivity().getApplicationContext());
            List<Place> places = new ArrayList<Place>();
            try {
                Dao<Place, Integer> dao = db.getDao(Place.class);
                places = dao.queryForAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // Establecemos el Adapter a la Lista del Fragment
            setListAdapter(new PlaceListAdapter(getActivity(), places));
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            TextView txtPlaceId = (TextView) v.findViewById(R.id.txtPlaceId);
            TextView txtPlaceName = (TextView) v.findViewById(R.id.txtPlaceTitle);
            TextView txtPlaceAddress = (TextView) v.findViewById(R.id.txtPlaceAddress);
            TextView txtPlaceDistrict = (TextView) v.findViewById(R.id.txtPlaceDistrict);
            TextView txtPlaceDescription = (TextView) v.findViewById(R.id.txtPlaceDescription);

            PlaceDetailtFragment fragment = new PlaceDetailtFragment(); //  object of next fragment
            Bundle bundle = new Bundle();
            bundle.putString("placeName", (String) txtPlaceName.getText());
            bundle.putString("placeAddress", (String) txtPlaceAddress.getText());
            bundle.putString("placeDistrict", (String) txtPlaceDistrict.getText());
            bundle.putString("placeDescription", (String) txtPlaceDescription.getText());
            fragment.setArguments(bundle);

            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }
    }

    public static class PlaceDetailtFragment extends Fragment {
        public static final String ARG_PLACE_NUMBER = "place_number";

        public PlaceDetailtFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_place_detail, container, false);
            int i = getArguments().getInt(ARG_PLACE_NUMBER);

            Bundle bundle = this.getArguments();

            TextView txtName = (TextView) rootView.findViewById(R.id.txtDetailName);
            TextView txtAddress = (TextView) rootView.findViewById(R.id.txtDetailAddress);
            TextView txtDistrict = (TextView) rootView.findViewById(R.id.txtDetailDistrict);
            TextView txtDescription = (TextView) rootView.findViewById(R.id.txtDetailDescription);

            txtName.setText(bundle.getString("placeName"));
            txtAddress.setText(bundle.getString("placeAddress"));
            txtDistrict.setText(bundle.getString("placeDistrict"));
            txtDescription.setText(bundle.getString("placeDescription"));

            return rootView;
        }
    }


}