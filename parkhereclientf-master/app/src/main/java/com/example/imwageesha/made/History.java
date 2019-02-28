package com.example.imwageesha.made;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Other.Constants;

public class History extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    SharedPreferences sharedPref;
    List<Booking> bookings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);


        sharedPref = getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);
        mRecyclerView = findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        bookings = new ArrayList<>();
        mAdapter = new BookingAdapter(bookings);
        mRecyclerView.setAdapter(mAdapter);

        getBookings();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemTouchListener(this,
                new RecyclerItemTouchListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(getApplicationContext(),BookingHistoryOptions.class);
                        intent.putExtra("BOOKING",bookings.get(position));
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongPress(int position) {
                        Log.e("Vehicles", "LongClicked"+position);
                    }
                }));
        getBookings();

    }

    public void getBookings(){
       // String url = "https://parkheresl.herokuapp.com/api/web/getBookedSlots?kid="+id+"&type="+booking.getVehicleType()+"&dep="+booking.getDepatureTime()+"&date="+booking.getDate()+"&arrival="+booking.getArivalTime();
        String url = Constants.BASE_URL + "/api/web/getUserBooking?DriverEmail="+sharedPref.getString(Constants.USER_EMAIL,"n/a");
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
                        Gson gson = new Gson();
                        try {
                            if(bookings.size()==0) {
                                JSONArray obj = new JSONArray(response);
                                for (int i = 0; i < obj.length(); i++) {
                                    Booking booking = gson.fromJson(obj.get(i).toString(), Booking.class);
                                    bookings.add(booking);
                                }
                                mAdapter.notifyDataSetChanged();
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
}
