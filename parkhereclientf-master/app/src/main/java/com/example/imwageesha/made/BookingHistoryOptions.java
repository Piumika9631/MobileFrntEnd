package com.example.imwageesha.made;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Other.Constants;

public class BookingHistoryOptions extends AppCompatActivity {

    Booking booking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history_options);

        booking = (Booking) getIntent().getSerializableExtra("BOOKING");
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.navigate:
                Uri gmmIntentUri = Uri.parse("google.navigation:q=" + booking.getLat() + "," + booking.getLng());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
                break;
            case R.id.rate:
                findViewById(R.id.comment).setVisibility(View.GONE);
                sendRate();
                findViewById(R.id.comment).setVisibility(View.VISIBLE);
                break;
            case R.id.pay:
                if (!booking.isPaid()) {
                    Intent intent = new Intent(getApplicationContext(), Payment.class);
                    intent.putExtra("BOOKING", booking);
                    startActivity(intent);
                    Log.e("GOT", "GOT BACK TO MENU");
                    //finishAffinity();
                }else{
                    Toast.makeText(getApplicationContext(), "Already PAID", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }



    public void sendRate() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/web/feedback";

        JSONObject obj = new JSONObject();

        try {
            EditText et = findViewById(R.id.comment);
            obj.put("senderEmail", booking.getDriverEmail()); //get user email after validation
            obj.put("receiverId", booking.getKeeperId()); // get user password after validation
            obj.put("parkName", booking.getParkName());
            obj.put("comment", et.getText());
        } catch (JSONException e) {
            Log.e("ERR",e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success", response.toString()); //create session with this
                        Toast.makeText(getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
                        EditText et = findViewById(R.id.comment);
                        et.setText("");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                        Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

}
