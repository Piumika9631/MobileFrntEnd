package com.example.imwageesha.made;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import Other.Constants;

public class MyPoints extends AppCompatActivity {
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_points);

        sharedPref = getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);

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
                                JSONArray obj = new JSONArray(response);
                            TextView tv = findViewById(R.id.tvPoints);
                                tv.setText("Current Points : "+obj.length());
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
