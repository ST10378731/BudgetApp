package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AccountC extends AppCompatActivity {

    EditText fullNameEditText, emailEditText, phoneEditText, passwordEditText;
    Button nextButton;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_c);

        // Initialize views
        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        nextButton = findViewById(R.id.nextButton);

        // Initialize DB Helper
        dbHelper = new DbHelper(this);

        // Handle sign-up button click
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = fullNameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Basic validation
                if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                    Toast.makeText(AccountC.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if user already exists
                if (dbHelper.checkUser(email)) {
                    Toast.makeText(AccountC.this, "Email already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Insert user
                long result = dbHelper.addUser(fullName, email, phone, password);

                if (result > 0) {
                    Toast.makeText(AccountC.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                    // Optionally clear inputs
                    fullNameEditText.setText("");
                    emailEditText.setText("");
                    phoneEditText.setText("");
                    passwordEditText.setText("");
                } else {
                    Toast.makeText(AccountC.this, "Failed to create account", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
