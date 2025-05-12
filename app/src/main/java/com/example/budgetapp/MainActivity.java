package com.example.budgetapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nextButton = findViewById(R.id.Next);

        nextButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, Slide.class);
            startActivity(intent);
        });
    }
}
