package com.example.imwageesha.made;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Datentime extends AppCompatActivity implements

            View.OnClickListener {

        Button btnDatePicker, btninTimePicker, btnoutTimePicker;
        TextView txtDate, txtinTime, txtoutTime;
        private int mYear, mMonth, mDay, minHour, minMinute=-1,moutHour,moutMinute;
        double lat,lng;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_datentime);

            btnDatePicker = (Button) findViewById(R.id.btn_date);
            btninTimePicker = (Button) findViewById(R.id.btn_time);
            btnoutTimePicker = (Button) findViewById(R.id.btnout_time);
            txtDate = findViewById(R.id.in_date);
            txtinTime = findViewById(R.id.in_time);
            txtoutTime = findViewById(R.id.out_time);

            btnDatePicker.setOnClickListener(this);
            btninTimePicker.setOnClickListener(this);
            btnoutTimePicker.setOnClickListener(this);

            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // TODO: Get info about the selected place.
                    LatLng latLng = place.getLatLng();
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                }

                @Override
                public void onError(Status status) {
                    // TODO: Handle the error.
                    //Log.i(TAG, "An error occurred: " + status);
                    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                }
            });


        }

        public void confirm(View v){
            String in = txtinTime.getText().toString();
            String out = txtoutTime.getText().toString();
            String date = txtDate.getText().toString();
            if(lat!=0 && lng!=0 && !in.equals("") && !out.equals("") && !date.equals("")){
                Intent intent = new Intent(this,Temp.class);
                intent.putExtra("VEHICLE",getIntent().getSerializableExtra("VEHICLE"));
                Booking booking = (Booking) getIntent().getSerializableExtra("BOOKING");
                booking.setArivalTime(Double.parseDouble(in));
                booking.setDepatureTime(Double.parseDouble(out));
                booking.setDate(date);
                intent.putExtra("LAT",lat);
                intent.putExtra("LNG",lng);
                intent.putExtra("PREBOOK",true);
                intent.putExtra("BOOKING",booking);
                startActivity(intent);
            }else{
                Toast.makeText(getApplicationContext(),"Invalid data",Toast.LENGTH_SHORT).show();
            }
        }




        @Override
        public void onClick(View v) {

            if (v == btnDatePicker) {

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                try {
                                    Date date1=new SimpleDateFormat("dd/MM/yyyy").parse(dayOfMonth+"/"+monthOfYear+"/"+year);
                                    Date date2=new SimpleDateFormat("dd/MM/yyyy").parse(mDay+"/"+mMonth+"/"+mYear);
                                    if(date2.compareTo(date1)<=0){
                                        txtDate.setText(String.format("%04d",year) + "-" + String.format("%02d",monthOfYear+1) + "-" + String.format("%02d",dayOfMonth));
                                    }else{
                                        Toast.makeText( getApplicationContext(), "Invalid date",Toast.LENGTH_LONG).show();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
            if (v == btninTimePicker) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                minHour = c.get(Calendar.HOUR_OF_DAY);
                minMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                txtinTime.setText(hourOfDay + "." + minute);
                            }
                        }, minHour, minMinute, false);
                timePickerDialog.show();
            }
            if (v == btnoutTimePicker) {

                // Get Current Time
                final Calendar c = Calendar.getInstance();
                moutHour = c.get(Calendar.HOUR_OF_DAY);
                moutMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                if(minMinute==-1){
                                    Toast.makeText( getApplicationContext(), "Insert check in first",Toast.LENGTH_LONG).show();
                                    return;
                                }



                                    //Date date1=new SimpleDateFormat("kk:mm", Locale.getDefault()).parse(String.format("%02d", hourOfDay)+":"+String.format("%02d", minute));
                                    //Date date2=new SimpleDateFormat("kk:mm",Locale.getDefault()).parse(String.format("%02d", minHour)+":"+String.format("%02d", minMinute));

                                    double in = minHour + minMinute*0.01;
                                    double out = hourOfDay + minute*0.01;

                                    Log.e("ERR",Double.toString(in)+" "+Double.toString(out));

                                    if(out>in){
                                        txtoutTime.setText(hourOfDay + "." + minute);
                                    }else{
                                        Toast.makeText( getApplicationContext(), "Invalid time",Toast.LENGTH_LONG).show();
                                    }



                            }
                        }, moutHour, moutMinute, false);
                timePickerDialog.show();

            }
        }


    }
