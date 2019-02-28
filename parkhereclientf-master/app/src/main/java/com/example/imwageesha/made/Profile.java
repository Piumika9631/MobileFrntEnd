package com.example.imwageesha.made;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import Other.Constants;

public class Profile extends AppCompatActivity {

    TextView tvemail,tvfullname;
    SharedPreferences sharedPref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tvemail = (TextView) findViewById(R.id.tvemail);
        tvfullname = (TextView) findViewById(R.id.tvfullname);
        sharedPref = getSharedPreferences(Constants.USER_PREF,Context.MODE_PRIVATE);
        String user_reg = sharedPref.getString(Constants.USER_REGISTERED,"N/A");
        if(user_reg.equals("true") && getIntent().getBooleanExtra("FROM_LOGIN",false)){
            Intent intent = new Intent(this,NavBar.class);
            startActivity(intent);
            finish();
        }else{
            checkRegistered();
        }



    }

    private void checkRegistered() {
        String verified = sharedPref.getString(Constants.USER_REGISTERED,"false");
        EditText reg = findViewById(R.id.regcode);
        String fname = sharedPref.getString(Constants.USER_FNAME,"N/A");
        String lname = sharedPref.getString(Constants.USER_LNAME,"N/A");
        String email = sharedPref.getString(Constants.USER_EMAIL,"N/A");
        if(verified.equals("true")){
            findViewById(R.id.regcode).setVisibility(View.GONE);
            String nic_ = sharedPref.getString(Constants.USER_NIC,"N/A");
            String mob = sharedPref.getString(Constants.USER_MOBILE,"N/A");
            EditText nic = findViewById(R.id.nic);
            nic.setText(nic_);
            EditText mobileNo = findViewById(R.id.mobile);
            mobileNo.setText(mob);
            tvfullname.setText(fname+" "+lname);
            tvemail.setText(email);
            reg.setText(sharedPref.getString(Constants.USER_REGCODE,"N/A"));
            //Log.e("done","d");
        }else{
            findViewById(R.id.regcode).setVisibility(View.VISIBLE);
        }
    }

    public void logout(View v) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
        Intent inte = new Intent(Profile.this,MainActivity.class);
        startActivity(inte);
        finish();
    }

    public void sendData(View view) {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/updateUser";

        JSONObject obj = new JSONObject();
        EditText nic = findViewById(R.id.nic);
        EditText mobileNo = findViewById(R.id.mobile);
        EditText regcode = findViewById(R.id.regcode);

        if(!validInput()){
            Toast.makeText(this,"Invalid input",Toast.LENGTH_SHORT).show();
            return;
        }

        Log.e("CODE",sharedPref.getString(Constants.USER_REGCODE,"N/A"));
        Log.e("CODE_EDIT",regcode.getText().toString());
        if(!regcode.getText().toString().equals(sharedPref.getString(Constants.USER_REGCODE,"N/A"))){
            Toast.makeText(this,"Invalid registration code",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            obj.put("mobileNo",mobileNo.getText().toString());
            obj.put("nic",nic.getText().toString());
            obj.put("verified","true");
            obj.put("email",sharedPref.getString(Constants.USER_EMAIL,"N/A"));
        } catch (JSONException e) {
            Log.e("Json",e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success",response.toString()); //create session with this

                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedPref.edit();

                                editor.putString(Constants.USER_REGISTERED, "true");
                                EditText nic = findViewById(R.id.nic);
                                EditText mobileNo = findViewById(R.id.mobile);
                                editor.putString(Constants.USER_NIC, nic.getText().toString());
                                editor.putString(Constants.USER_MOBILE, mobileNo.getText().toString());
                                editor.apply();

                                Intent intent = new Intent(getApplicationContext(), NavBar.class);
                                startActivity(intent);
                                finish();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error",error.toString());
                        Toast.makeText(getApplicationContext(),"Request error",Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);
    }

    private boolean validInput() {
        boolean ret = false;
        EditText nic = findViewById(R.id.nic);
        EditText mobileNo = findViewById(R.id.mobile);
        String nic_s = nic.getText().toString().trim().toUpperCase();
        String mobile_s = mobileNo.getText().toString().trim();
        String mobile_pattern = "[0-9]{10}";
        String nic_pattern = "[0-9]{9}V";

        if ((mobile_s.matches(mobile_pattern)) && (nic_s.matches(nic_pattern)))
        {
            ret = true;

        }

        return ret;
    }
}


