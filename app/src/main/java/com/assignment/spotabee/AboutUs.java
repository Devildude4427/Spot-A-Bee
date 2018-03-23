package com.assignment.spotabee;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AboutUs extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);


        Button advanceToQuestionTwo = (Button) findViewById(R.id.button2);
        advanceToQuestionTwo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AboutUs.this, Home.class);
                startActivity(intent);

            }
        });




    }
}
