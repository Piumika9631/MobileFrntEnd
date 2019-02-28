package com.example.imwageesha.made;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.List;

import Other.Constants;

public class Main2Activity extends AppCompatActivity {

    SharedPreferences sharedPref;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    List<Vehicle> vehicles;
    Spinner dropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(this,
                new RecyclerItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getApplicationContext(),VehicleEditor.class);
                        intent.putExtra("name",vehicles.get(position).name);
                        intent.putExtra("number",vehicles.get(position).number);
                        intent.putExtra("type",vehicles.get(position).type);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongPress(int position) {
                        Log.e("Vehicles", "LongClicked"+position);
                    }
                }));

        sharedPref = getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);

        //get the spinner from the xml.
        dropdown = findViewById(R.id.type);
        //create a list of items for the spinner.

        //create an adapter to describe how the items are displayed, adapters are used in several places in android.
        //There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Constants.VEHICLE_TYPES);
        //set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

        getVehicles();


        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
    }

    public void logout(View v) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        Intent inte = new Intent(this, MainActivity.class);
        startActivity(inte);
        finish();
    }

    public void showVehicles(JSONArray vehicles_) {
        if (vehicles_.length() == 0) {
            findViewById(R.id.no_vehicles).setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            return;
        }
        findViewById(R.id.no_vehicles).setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        vehicles = new ArrayList<>();
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(vehicles);
        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < vehicles_.length(); i++) {
            Vehicle vehicle_temp = new Vehicle();
            try {
                vehicle_temp.setName(vehicles_.getJSONObject(i).getString("name"));
                vehicle_temp.setNumber(vehicles_.getJSONObject(i).getString("number"));
                vehicle_temp.setType(vehicles_.getJSONObject(i).getString("type"));
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }
            vehicles.add(vehicle_temp);
        }


        mAdapter.notifyDataSetChanged();
    }

    public void addVehicle(View v) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/addUserVehicle";

        JSONObject obj = new JSONObject();
        JSONObject vehicle_json = new JSONObject();
        final EditText name = findViewById(R.id.name);
        final EditText number = findViewById(R.id.number);

        String number_s = number.getText().toString().toUpperCase().trim();
        String number_pattern1 = "[A-Z]{3}[0-9]{4}";
        String number_pattern2 = "[A-Z]{2}-[0-9]{4}";

        if (!(((number_s.matches(number_pattern1)) ||  (number_s.matches(number_pattern2)))&& !(name.getText().toString().isEmpty())))
        {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            obj.put("email", sharedPref.getString(Constants.USER_EMAIL, "N/A"));
            vehicle_json.put("name",name.getText().toString().toUpperCase());
            vehicle_json.put("number",number.getText().toString().toUpperCase());
            vehicle_json.put("type",dropdown.getSelectedItem().toString());
            obj.put("vehicle",vehicle_json);
        } catch (JSONException e) {
            Log.e("Json", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success", response.toString()); //create session with this
                        name.setText("");
                        number.setText("");
                        getVehicles();
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
        queue.add(jsonObjectRequest);
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
}
