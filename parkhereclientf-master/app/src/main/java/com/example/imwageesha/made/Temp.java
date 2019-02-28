package com.example.imwageesha.made;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Other.Constants;

public class Temp extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;

    LocationListener locationListener;
    LocationManager locationManager;
    FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    LocationRequest mlocationRequest;
    Booking booking;

    boolean mRequestingLocationUpdates = false, marksAdded = false;
    Vehicle vehicle;
    Marker myLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        vehicle = (Vehicle) getIntent().getSerializableExtra("VEHICLE");
        booking = (Booking) getIntent().getSerializableExtra("BOOKING");


        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                    if(!marksAdded) {
                        mMap.clear();
                        //myLoc = mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker"));
                        double lat,lng;
                        boolean prebook = getIntent().getBooleanExtra("PREBOOK",false);
                        Log.e("LOC",Boolean.toString(getIntent().getBooleanExtra("PREBOOK",false)));
                        if(!prebook) {
                            Log.e("LOC","GETTING LOCATIONS=======================>");
                            lat = location.getLatitude();
                            lng = location.getLongitude();
                            addMarks(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
                            marksAdded = true;
                        }else{
                            lat = getIntent().getDoubleExtra("LAT",0);
                            lng = getIntent().getDoubleExtra("LNG",0);
                            addMarks(Double.toString(getIntent().getDoubleExtra("LAT",0)),Double.toString(getIntent().getDoubleExtra("LNG",0)));
                            marksAdded = true;
                        }

                        LatLngBounds LOC = new LatLngBounds(new LatLng(lat - 0.05, lng - 0.05), new LatLng(lat + 0.05, lng + 0.05));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(LOC, 0));
                    }

                    Log.e("LAT",Double.toString(location.getLatitude()));
                    Log.e("LONG",Double.toString(location.getLongitude()));
                }
            }

        };




    }
    HashMap<Marker,String> markers = new HashMap<Marker,String>();
    private void addMarks(String lat,String lng){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url =Constants.BASE_URL+ "/api/getParks?lat="+lat+"&lng="+lng;
        Log.e("URL",url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.e("ERROR",response);
                        double lat,lng;
                        String title,id,pid;
                        int numberOfSlots;

                        try {
                            JSONArray obj = new JSONArray(response);
                            Log.e("len",Integer.toString(obj.length()));
                            for(int i=0;i<obj.length();i++){
                                String type = "4";
                                JSONObject obj1  = obj.getJSONObject(i);
                                lat = obj1.getDouble("lat");
                                lng = obj1.getDouble("lng");
                                title = obj1.getString("name");
                                id = obj1.getString("ownerid");
                                //pid = obj1.getJSONObject("_id").getString("$oid");
                                numberOfSlots = Integer.parseInt(obj1.getString("alocatedSlots"+type));
                                HashMap<String,String> map = new HashMap<>();

                                JSONArray arr = obj1.getJSONArray("type"+type);
                                for(int k=0;k<arr.length();k++){
                                    try {
                                        map.put(id, arr.getJSONObject(k).getString("slotId")+"==="+arr.getJSONObject(k).getString("slotNumber"));
                                    }catch (JSONException e) {
                                        Log.e("ERROR",e.toString());
                                    }
                                }


                                checkAvailable(lat,lng,title,id,numberOfSlots,map);


                            }

                        } catch (JSONException e) {
                            Log.e("ERROR",e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void checkAvailable(final double lat,final double lng,final String title,final String id,final int numberOfSlots, final HashMap<String,String> map){
        String url = "https://parkheresl.herokuapp.com/api/web/getBookedSlots?kid="+id+"&type="+booking.getVehicleType()+"&dep="+booking.getDepatureTime()+"&date="+booking.getDate()+"&arrival="+booking.getArivalTime();

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("URL",url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.e("ERROR",response);

                        try {
                            JSONObject obj = new JSONObject(response);
                            int taken = Integer.parseInt(obj.getString("takenCount"));
                            if(taken<numberOfSlots) {
                                JSONArray bookings = obj.getJSONArray("bookings");
                                List<String> list = new ArrayList<String>();
                                for(int i = 0; i < bookings.length(); i++){
                                    list.add(bookings.getString(i));
                                }
                                for(int i = 1;i<=numberOfSlots;i++){
                                    if(!list.contains(Integer.toString(i))){
                                        booking.setSlotNum(Integer.toString(i));
                                        booking.setSlotId(map.get(Integer.toString(i)));
                                    }
                                }
                                Marker m = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title(title));
                                markers.put(m, id);
                            }
                        } catch (Exception e) {
                            Log.e("ERROR",e.toString());
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    12);

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mlocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.mMap = map;

        mMap.setOnMarkerClickListener(this);

        mlocationRequest = LocationRequest.create();
        mlocationRequest.setInterval(5000);
        mlocationRequest.setFastestInterval(2000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mRequestingLocationUpdates = true;
        startLocationUpdates();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 12: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.e("Marker",markers.get(marker));
        booking.setKeeperId(markers.get(marker));
        booking.setParkName(marker.getTitle());
        booking.setLat(marker.getPosition().latitude);
        booking.setLng(marker.getPosition().longitude);
        Intent intent = new Intent(getApplicationContext(),BookingActivity.class);
        intent.putExtra("BOOKING",booking);
        startActivity(intent);
        return false;
    }
}
