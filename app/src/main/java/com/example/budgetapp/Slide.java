package com.example.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class Slide extends AppCompatActivity {

    Button getStartedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide);  // Make sure this matches your layout XML

        // Initialize the button
        getStartedButton = findViewById(R.id.button);

        // Set up the button click listener
        getStartedButton.setOnClickListener(view -> {
            // Create an Intent to navigate to LogInActivity
            Intent intent = new Intent(Slide.this, LogIn.class);
            startActivity(intent); // Start the LogInActivity
        });
    }
}
