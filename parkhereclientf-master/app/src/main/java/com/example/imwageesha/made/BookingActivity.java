package com.example.imwageesha.made;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Other.Constants;

public class BookingActivity extends AppCompatActivity {
    Booking booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        booking = (Booking) getIntent().getSerializableExtra("BOOKING");

        checkAvailable(booking.getKeeperId());
    }

    public void checkAvailable(final String id) {
        String url = "https://parkheresl.herokuapp.com/api/web/getBookedSlots?kid=" + id + "&type=" + booking.getVehicleType() + "&dep=" + booking.getDepatureTime() + "&date=" + booking.getDate() + "&arrival=" + booking.getArivalTime();

        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("URL", url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.e("ERROR", response);

                        try {
                            JSONObject obj = new JSONObject(response);

                                JSONArray bookings = obj.getJSONArray("bookings");
                                List<String> list = new ArrayList<String>();
                                for (int i = 0; i < bookings.length(); i++) {
                                    list.add(bookings.getString(i));
                                }
                                int i=1;
                                while (true) {
                                    if (!list.contains(Integer.toString(i))) {
                                        booking.setSlotNum(Integer.toString(i));
                                        booking.setCharge(Double.parseDouble(Constants.GET_TYPE_NUMBER(booking.getVehicleType()))*50);
                                        Log.e("BOOK", booking.toString());
                                        book();
                                        break;
                                    }
                                    i++;
                                }

                        } catch (Exception e) {
                            Log.e("ERROR", e.toString());
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

    public void book() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/web/setBook";

        Gson g = new Gson();
        String str = g.toJson(booking,Booking.class);
        Log.e("Json", str);
        JSONObject j = new JSONObject();
        try {
             j = new JSONObject(str);
        } catch (JSONException e) {
            Log.e("Json", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, j, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success", response.toString()); //create session with this
                        Intent intent = new Intent(getApplicationContext(),NavBar.class);
                        intent.putExtra("BOOKING",booking);
                        Toast.makeText(getApplicationContext(), "Booking done", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finishAffinity();

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
}
