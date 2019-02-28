package com.example.imwageesha.made;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Other.Constants;

public class MainActivity extends AppCompatActivity {

    private Button btlogin;
    private EditText etun,etpw;
    private TextView tvcrtnewacc, tvfgt;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences(Constants.USER_PREF,Context.MODE_PRIVATE);
        if(sharedPref.contains(Constants.USER_EMAIL) && sharedPref.contains(Constants.USER_FNAME)){
            Intent inte = new Intent(this,NavBar.class);
            inte.putExtra("FROM_LOGIN",true);
            startActivity(inte);
            finish();
        }
        btlogin = (Button) findViewById(R.id.btlogin);
        tvcrtnewacc = (TextView) findViewById(R.id.tvcrtnewacc);
        tvfgt = (TextView) findViewById(R.id.tvfgt);
        etpw = (EditText) findViewById(R.id.etpw);
        etun= (EditText)findViewById(R.id.etun);

        tvcrtnewacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });

        tvfgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, fgtpw.class);
                startActivity(intent);
            }
        });



        btlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInput()){
                    sendLoginData();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Username is not matched with Email format",Toast.LENGTH_SHORT).show();
                }
            }

        });
}

    private void sendLoginData() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = Constants.BASE_URL + "/api/authenticate";

        JSONObject obj = new JSONObject();

        try {
            obj.put("email",etun.getText().toString()); //get user email after validation
            obj.put("password",etpw.getText().toString()); // get user password after validation
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Success",response.toString()); //create session with this
                        try {
                            if(response.get("status").equals("success")){
                                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();

                                SharedPreferences.Editor editor = sharedPref.edit();
                                JSONObject user = response.getJSONObject("user");
                                Log.e("USER",user.toString());
                                editor.putString(Constants.USER_FNAME, user.get("fname").toString());
                                editor.putString(Constants.USER_LNAME, user.get("lname").toString());
                                editor.putString(Constants.USER_EMAIL, user.get("email").toString());
                                editor.putString(Constants.USER_REGCODE, user.get("regcode").toString());
                                editor.putString(Constants.USER_REGISTERED, user.get("registered").toString());
                                editor.apply();

                                if(user.get("registered").toString().equals("false")){
                                    Intent intent = new Intent(MainActivity.this, Profile.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Intent intent = new Intent(MainActivity.this, NavBar.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(),"Login Failed! Check your Username & Password again!",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        String password = etpw.getText().toString().trim();
        String email = etun.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if ((email.matches(emailPattern)) && !(password.isEmpty()) )
        {
            ret = true;

        }

        return ret;
    }
}
