package e.utilisateur.easylife01;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 11f;
    private Location currentLocation;

    //widgets
    private String address;

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        address = getIntent().getStringExtra("ad");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();
    }

    @Override
        public void onMapReady (GoogleMap googleMap) {
            Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onMapReady: map is ready");
            mMap = googleMap;

            if (mLocationPermissionsGranted) {
                getDeviceLocation();

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(true);
            }
        }

        private void geoLocate(){
            Log.d(TAG, "geoLocate: geolocating");

            String searchString = address;

            Geocoder geocoder = new Geocoder(MapsActivity.this);
            List<Address> list = new ArrayList<>();
            try{
                list = geocoder.getFromLocationName(searchString, 1);
            }catch (IOException e){
                Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
            }

            if(list.size() > 0){
                Address address = list.get(0);

                Log.d(TAG, "geoLocate: found a location: " + address.toString());
                //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

                moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
                String url = getRequestUrl(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()) ,new LatLng(address.getLatitude(), address.getLongitude()));
                TaskRequestDirections taskRequestDirections = new TaskRequestDirections();
                taskRequestDirections.execute(url);
            }
        }

        private void getDeviceLocation(){
            Log.d(TAG, "getDeviceLocation: getting the devices current location");

            try{
                if(mLocationPermissionsGranted){

                    final Task<Location> location = mFusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if(task.isSuccessful()){
                                Log.d(TAG, "onComplete: found location!");
                                currentLocation = task.getResult();

                                if(currentLocation!=null){
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                            DEFAULT_ZOOM, "My Location");
                                    Log.d("Move","okay");
                                    geoLocate();
                                }

                            }else{
                                Log.d(TAG, "onComplete: current location is null");
                                Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }catch (SecurityException e){
                Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
            }
        }

        private void moveCamera(LatLng latLng, float zoom, String title){
            Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

            if(!title.equals("My Location")){
                MarkerOptions options = new MarkerOptions()
                        .position(latLng)
                        .title(title);
                mMap.addMarker(options);
            }

        }

        private void initMap(){
            Log.d(TAG, "initMap: initializing map");
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            mapFragment.getMapAsync(MapsActivity.this);
        }

        private void getLocationPermission(){
            Log.d(TAG, "getLocationPermission: getting location permissions");
            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION};

            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                        COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    mLocationPermissionsGranted = true;
                    initMap();
                }else{
                    ActivityCompat.requestPermissions(this,
                            permissions,
                            LOCATION_PERMISSION_REQUEST_CODE);
                }
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            Log.d(TAG, "onRequestPermissionsResult: called.");
            mLocationPermissionsGranted = false;

            switch(requestCode){
                case LOCATION_PERMISSION_REQUEST_CODE:{
                    if(grantResults.length > 0){
                        for(int i = 0; i < grantResults.length; i++){
                            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                                mLocationPermissionsGranted = false;
                                Log.d(TAG, "onRequestPermissionsResult: permission failed");
                                return;
                            }
                        }
                        Log.d(TAG, "onRequestPermissionsResult: permission granted");
                        mLocationPermissionsGranted = true;
                        //initialize our map
                        initMap();
                    }
                }
            }
        }

        private String getRequestUrl(LatLng origin, LatLng dest) {
            //Value of origin
            String str_org = "origin=" + origin.latitude +","+origin.longitude;
            //Value of destination
            String str_dest = "destination=" + dest.latitude+","+dest.longitude;
            //Set value enable the sensor
            String sensor = "sensor=false";
            //Mode for find direction
            String mode = "mode=walking";
            //Build the full param
            String param = str_org +"&" + str_dest + "&" +sensor+"&" +mode;
            //Output format
            String output = "json";
            //Create url to request
            String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + param;
            return url;
        }

        private String requestDirection(String reqUrl) throws IOException {
            String responseString = "";
            InputStream inputStream = null;
            HttpURLConnection httpURLConnection = null;
            try{
                URL url = new URL(reqUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.connect();

                //Get the response result
                inputStream = httpURLConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuffer stringBuffer = new StringBuffer();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                responseString = stringBuffer.toString();
                bufferedReader.close();
                inputStreamReader.close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                httpURLConnection.disconnect();
            }
            return responseString;
        }

        public class TaskRequestDirections extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                String responseString = "";
                try {
                    responseString = requestDirection(strings[0]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return  responseString;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Parse json here
                TaskParser taskParser = new TaskParser();
                taskParser.execute(s);
            }
        }

        public class TaskParser extends AsyncTask<String, Void, List<List<HashMap<String, String>>> > {

            @Override
            protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
                JSONObject jsonObject = null;
                List<List<HashMap<String, String>>> routes = null;
                try {
                    jsonObject = new JSONObject(strings[0]);
                    DirectionsParser directionsParser = new DirectionsParser();
                    routes = directionsParser.parse(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return routes;
            }

            @Override
            protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
                //Get list route and display it into the map

                ArrayList points = null;

                PolylineOptions polylineOptions = null;

                for (List<HashMap<String, String>> path : lists) {
                    points = new ArrayList();
                    polylineOptions = new PolylineOptions();

                    for (HashMap<String, String> point : path) {
                        double lat = Double.parseDouble(point.get("lat"));
                        double lon = Double.parseDouble(point.get("lon"));

                        points.add(new LatLng(lat, lon));
                    }

                    polylineOptions.addAll(points);
                    polylineOptions.width(15);
                    polylineOptions.color(Color.BLUE);
                    polylineOptions.geodesic(true);
                }

                if (polylineOptions != null) {
                    mMap.addPolyline(polylineOptions);
                } else {
                    Toast.makeText(getApplicationContext(), "Direction not found!", Toast.LENGTH_SHORT).show();
                }

            }
        }
}
