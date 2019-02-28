package com.example.imwageesha.made;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreBookDetails extends AppCompatActivity {

    private Button MapPre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_book_details);

        MapPre = (Button) findViewById(R.id.btnPreB);

        MapPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMapPre();
            }
        });
    }

    public void goToMapPre(){
        Intent intent =new Intent(this,PreMap.class);
        startActivity(intent);
    }
}
