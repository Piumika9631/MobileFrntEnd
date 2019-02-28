package com.example.imwageesha.made;

import Other.Constants;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Registration extends AppCompatActivity {

    Button btregister;
    EditText etfname,etlname,etpw,etcompw,etemail;


    private final boolean validate() {



        Boolean result = false;
        String firstname = etfname.getText().toString();
        String lastname = etlname.getText().toString();
        String email = etemail.getText().toString();
        String password = etpw.getText().toString();
        String compassword = etcompw.getText().toString();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || compassword.isEmpty()) {
            Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
        } else if (!compassword.equals(password)) {
            Toast.makeText(this, "Password Not Matched", Toast.LENGTH_SHORT).show();
        } else {
            result = true;
        }
        return result;
    }

    public void register(){
        RequestQueue queue = Volley.newRequestQueue(Registration.this);
        String url = Constants.BASE_URL + "/api/register";

        JSONObject obj = new JSONObject();
        int randomNum = 1111 + (int)(Math.random() * (9999 - 1111) );
        try {
            obj.put("firstName",etfname.getText().toString());
            obj.put("lastName",etlname.getText().toString());
            obj.put("email",etemail.getText().toString());
            obj.put("password",etpw.getText().toString());
            obj.put("regcode",Integer.toString(randomNum));
        } catch (JSONException e) {
            Log.e("JSON",e.toString());
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Toast.makeText(getApplicationContext(),"Registration Success",Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(Registration.this,MainActivity.class);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        queue.add(jsonObjectRequest);
        }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        btregister = (Button)findViewById(R.id.btreg);
        etfname = (EditText)findViewById(R.id.etfname);
        etlname = (EditText)findViewById(R.id.etlname);
        etemail= (EditText)findViewById(R.id.etemail);
        etpw = (EditText)findViewById(R.id.etpw);
        etcompw = (EditText)findViewById(R.id.etcompw);




        btregister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
               if( validate()){
                register();}
                else{

               }



            }
        });

    }


}
