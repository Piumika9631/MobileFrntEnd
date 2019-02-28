package com.example.imwageesha.made;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import org.json.JSONException;
import org.json.JSONObject;

import Other.Constants;

public class VehicleEditor extends AppCompatActivity {
    Spinner dropdown;

    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_editor);
        sharedPref = getSharedPreferences(Constants.USER_PREF, Context.MODE_PRIVATE);


        //create a list of items for the spinner.
        EditText name = findViewById(R.id.name);
        TextView number = findViewById(R.id.number);
        name.setText(getIntent().getStringExtra("name"));
        number.setText(getIntent().getStringExtra("number"));


    }

    public void updateVehicle(View v) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/updateUserVehicle";

        JSONObject obj = new JSONObject();
        final EditText name = findViewById(R.id.name);
        final TextView number = findViewById(R.id.number);

        if (name.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            obj.put("email", sharedPref.getString(Constants.USER_EMAIL, "N/A"));
            obj.put("name",name.getText().toString().toUpperCase());
            obj.put("number",number.getText().toString().toUpperCase());
        } catch (JSONException e) {
            Log.e("Json", e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success", response.toString()); //create session with this
                        name.setText("");
                        finish();
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

    public void deleteVehicle(View v) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/deleteUserVehicle";

        JSONObject obj = new JSONObject();
        JSONObject vehicle_json = new JSONObject();
        final EditText name = findViewById(R.id.name);
        final TextView number = findViewById(R.id.number);


        try {
            obj.put("email", sharedPref.getString(Constants.USER_EMAIL, "N/A"));
            obj.put("number",number.getText().toString().toUpperCase());
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
                        finish();
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
