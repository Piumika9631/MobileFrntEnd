package com.example.imwageesha.made;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import Other.Constants;
import lk.payhere.androidsdk.PHConfigs;
import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.PHMainActivity;
import lk.payhere.androidsdk.PHResponse;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;
import lk.payhere.androidsdk.model.StatusResponse;

import static Other.Constants.PAYHERE_REQUEST;
import static android.support.v4.app.ActivityCompat.startActivityForResult;

public class Payment extends AppCompatActivity {

    Booking booking;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button pay = findViewById(R.id.pay);

        booking = (Booking) getIntent().getSerializableExtra("BOOKING");
        pay();

    }

    public void pay(){
        PaymentGateway pg = new PaymentGateway();

        if(pg.cardIsAdded()){
            InitRequest req = new InitRequest();
            req.setMerchantId("1212156"); // Your Merchant ID
            req.setMerchantSecret("salalalaaa"); // Your Merchant secret

            req.setCurrency("LKR"); // Currency
            req.setOrderId("ItemNo12345"); // Unique ID for your payment transaction
            req.setItemsDescription("Door bell wireless");  // Item title or Order/Invoice number
            req.getCustomer().setFirstName("Saman");
            req.getCustomer().setLastName("Perera");
            req.getCustomer().setEmail("samanp@gmail.com");
            req.getCustomer().setPhone("");
            req.getCustomer().getAddress().setAddress("");
            req.getCustomer().getAddress().setCity("");
            req.getCustomer().getAddress().setCountry("");
            req.getCustomer().getDeliveryAddress().setAddress("");
            req.getCustomer().getDeliveryAddress().setCity("");
            req.getCustomer().getDeliveryAddress().setCountry("");
            req.getItems().add(new Item(null, "Door bell wireless", 1));

            req.setMerchantId("1212156"); // Your Merchant ID
            req.setMerchantSecret("salalalaaa"); // Your Merchant secret
            req.setAmount(booking.getCharge()*(booking.getDepatureTime()-booking.getArivalTime())); // Amount which the customer should pay
            req.setCurrency("LKR"); // Currency
            req.setOrderId(booking.getDriverId()+booking.getArivalTime()+booking.getKeeperId()); // Unique ID for your payment transaction
            req.setItemsDescription(booking.getVehicleType());  // Item title or Order/Invoice number
            req.getCustomer().setFirstName(booking.getDriverName());
            req.getCustomer().setLastName("");
            req.getCustomer().setEmail(booking.getDriverEmail());
            req.getItems().add(new Item(null, booking.getVehicleType() + " Parking", 1));

            Intent intent = new Intent(this, PHMainActivity.class);
            intent.putExtra(PHConstants.INTENT_EXTRA_DATA, req);
            PHConfigs.setBaseUrl(PHConfigs.SANDBOX_URL);
            //startActivity(intent);
            //finish();
            startActivityForResult(intent, 122);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO process response
        if (requestCode == 122 && data != null && data.hasExtra(PHConstants.INTENT_EXTRA_RESULT)) {
            PHResponse<StatusResponse> response = (PHResponse<StatusResponse>) data.getSerializableExtra(PHConstants.INTENT_EXTRA_RESULT);
            String msg;
            if (response.isSuccess()) {
                msg = "Activity result:" + response.getData().toString();
                Log.e("A", msg);
                setPaid();
                finish();
            } else {
                msg = "Result:" + response.toString();
                Log.e("A", msg);
                finish();
            }
            //textView.setText(msg);
        }
    }

    public void setPaid() {
        // String url = "https://parkheresl.herokuapp.com/api/web/getBookedSlots?kid="+id+"&type="+booking.getVehicleType()+"&dep="+booking.getDepatureTime()+"&date="+booking.getDate()+"&arrival="+booking.getArivalTime();
        String url = Constants.BASE_URL + "/api/web/bookingSetPaid?bookId="+booking.getBookId()+"&paid=true";
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.e("URL", url);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //mTextView.setText("Response is: "+ response.substring(0,500));
                        Log.e("ERR", response);
                        booking.setPaid(true);
                        /*Intent intent = new Intent(getApplicationContext(),History.class);
                        startActivity(intent);*/
                        finish();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //mTextView.setText("That didn't work!");
                Log.e("ERR", error.toString());
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}
