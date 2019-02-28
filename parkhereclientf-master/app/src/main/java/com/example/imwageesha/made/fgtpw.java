package com.example.imwageesha.made;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import Other.Constants;

public class fgtpw extends AppCompatActivity {

    Button btnsubmit;
    EditText etemail;

    private final boolean validate() {



        Boolean result = false;


        String email = etemail.getText().toString();



        if ( email.isEmpty()) {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }  else {
            result = true;
        }
        return result;
    }


    public void fgtpassword(){
        RequestQueue queue = Volley.newRequestQueue(fgtpw.this);
        String url = Constants.BASE_URL + "/api/rstpw";

        JSONObject obj = new JSONObject();

        try {

            obj.put("email",etemail.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.PUT, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getApplicationContext(),"Password reset link send to your email",Toast.LENGTH_SHORT).show();
                        Intent intent =  new Intent(fgtpw.this,MainActivity.class);
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
        setContentView(R.layout.activity_fgtpw);

        etemail= (EditText)findViewById(R.id.etemail);
        btnsubmit = (Button)findViewById(R.id.btnsubmit);


        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if( validate()){
                    fgtpassword();}
                else{

                }



            }
        });
    }
}
