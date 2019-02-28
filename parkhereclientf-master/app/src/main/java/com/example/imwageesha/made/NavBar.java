package com.example.imwageesha.made;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Other.Constants;

public class NavBar extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button MapOn;
    private Button MapPreD;

    List<Vehicle> vehicles;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_bar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.btnOn).setVisibility(View.GONE);
        findViewById(R.id.btnPre).setVisibility(View.GONE);
        findViewById(R.id.type).setVisibility(View.GONE);
        sharedPref = getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        booking = new Booking();

        booking.setDriverId(sharedPref.getString(Constants.USER_NIC,"N/A"));
        booking.setDriverEmail(sharedPref.getString(Constants.USER_EMAIL,"N/A"));
        booking.setDriverName(sharedPref.getString(Constants.USER_FNAME,"N/A")+" "+sharedPref.getString(Constants.USER_LNAME,"N/A"));
        booking.setPaid(false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

   //     MapOn = (Button) findViewById(R.id.btnOn);

//        MapOn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                goToOnMap();
//            }
//        });
        vehicles = new ArrayList<>();
        getVehicles();



    }

    public void getVehicles() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/getUser";

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", sharedPref.getString(Constants.USER_EMAIL, "N/A"));
        } catch (JSONException e) {
            Log.e("Json", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success", response.toString()); //create session with this
                        try {
                            JSONArray vehicles = response.getJSONObject("user").getJSONArray("vehicles");
                            Log.e("Vehicles", vehicles.toString());
                            findViewById(R.id.btnOn).setVisibility(View.VISIBLE);
                            findViewById(R.id.btnPre).setVisibility(View.VISIBLE);
                            findViewById(R.id.type).setVisibility(View.VISIBLE);
                            showVehicles(vehicles);
                        } catch (JSONException e) {
                            Log.e("JSON", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_SHORT).show();
                    }
                });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                4000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjectRequest.setShouldCache(false);
        queue.add(jsonObjectRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getVehicles();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getVehicles();
    }
    Spinner dropdown;
    public void showVehicles(JSONArray vehicles_) {
        TextView tv = findViewById(R.id.tvVehicle);
        vehicles.clear();
        if (vehicles_.length() == 0) {
            tv.setText("No vehicles found");
            findViewById(R.id.btnOn).setVisibility(View.GONE);
            findViewById(R.id.btnPre).setVisibility(View.GONE);
            findViewById(R.id.type).setVisibility(View.GONE);

            return;
        }
        tv.setText("");
        findViewById(R.id.btnOn).setVisibility(View.VISIBLE);
        findViewById(R.id.btnPre).setVisibility(View.VISIBLE);
        findViewById(R.id.type).setVisibility(View.VISIBLE);

        dropdown = findViewById(R.id.type);
        //create a list of items for the spinner.

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        String[] arr = new String[vehicles_.length()];


        for (int i = 0; i < vehicles_.length(); i++) {
            Vehicle vehicle_temp = new Vehicle();
            try {
                vehicle_temp.setName(vehicles_.getJSONObject(i).getString("name"));
                vehicle_temp.setNumber(vehicles_.getJSONObject(i).getString("number"));
                vehicle_temp.setType(vehicles_.getJSONObject(i).getString("type"));

                arr[i] = vehicle_temp.getName() + " : " + vehicle_temp.getNumber();
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }
            vehicles.add(vehicle_temp);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, arr);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);
    }

    Booking booking;

    public void goToPreMapD(View view) {
        Intent intent = new Intent(this, Datentime.class);
        intent.putExtra("VEHICLE",vehicles.get(dropdown.getSelectedItemPosition()));
        Vehicle vehicle = vehicles.get(dropdown.getSelectedItemPosition());
        booking.setVehicleNum(vehicle.getNumber());
        booking.setVehicleType(vehicle.getType());
        intent.putExtra("BOOKING",booking);
        intent.putExtra("PREBOOK",true);
        startActivity(intent);
    }

    public void goToOnMap(View view) {
        Intent intent = new Intent(this, Temp.class);
        intent.putExtra("VEHICLE",vehicles.get(dropdown.getSelectedItemPosition()));
        Vehicle vehicle = vehicles.get(dropdown.getSelectedItemPosition());
        booking.setArivalTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+Calendar.getInstance().get(Calendar.MINUTE)*0.01);
        booking.setDepatureTime((Calendar.getInstance().get(Calendar.HOUR_OF_DAY)+1.0)+Calendar.getInstance().get(Calendar.MINUTE)*0.01);
        Log.e("DATE","lol");
        //booking.setDate();
        //booking.setArivalTime();
        booking.setDate(Calendar.getInstance().get(Calendar.YEAR)+"-"+String.format("%02d",(Calendar.getInstance().get(Calendar.MONTH)+1))+"-"+String.format("%02d",Calendar.getInstance().get(Calendar.DATE)));
        booking.setVehicleNum(vehicle.getNumber());
        booking.setVehicleType(vehicle.getType());
        intent.putExtra("BOOKING",booking);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camera) {
//            Intent show = new Intent(NavBar.this,Profile.class);
//            show.putExtra("FROM_LOGIN",false);
//            startActivity(show);
//        }
//
        if (id == R.id.nav_gallery) {
            Intent show = new Intent(NavBar.this, Main2Activity.class);
            startActivity(show);
        }  else if (id == R.id.nav_manage) {
            Intent show = new Intent(NavBar.this, History.class);
            startActivity(show);
        }  else if (id == R.id.nav_send) {
            Intent show = new Intent(NavBar.this, MyPoints.class);
            startActivity(show);
        } else if (id == R.id.nav_send1) {
            Intent show = new Intent(NavBar.this, Help.class);
            startActivity(show);
        } else if (id == R.id.nav_send2) {
            Intent show = new Intent(NavBar.this, About.class);
            startActivity(show);
        } else if (id == R.id.nav_send4) {
            //Intent show = new Intent(this, MainActivity.class);
            //startActivity(show);
            logout(null);
            finish();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void logout(View v) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        Intent inte = new Intent(this, MainActivity.class);
        startActivity(inte);
        finish();
    }
}
