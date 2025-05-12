package com.example.budgetapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ExpenseEntryActivity extends AppCompatActivity {

    EditText editTextStartTime, editTextEndTime, editTextDescription, editTextMinGoal, editTextMaxGoal;
    Spinner spinnerCategory;
    ImageView imagePreview;
    Button buttonAttachPhoto, buttonSaveExpense;
    CalendarView calendarViewDate;
    TextView textViewDate; // Added TextView to display selected date

    Bitmap selectedImageBitmap = null;
    static final int PICK_IMAGE_REQUEST = 1;
    DatabaseExpense dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        // Initialize views
        calendarViewDate = findViewById(R.id.calendarViewDate);
        editTextStartTime = findViewById(R.id.editTextStartTime);
        editTextEndTime = findViewById(R.id.editTextEndTime);
        editTextDescription = findViewById(R.id.editTextDescription);
        editTextMinGoal = findViewById(R.id.editTextMinGoal);
        editTextMaxGoal = findViewById(R.id.editTextMaxGoal);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        imagePreview = findViewById(R.id.imagePreview);
        buttonAttachPhoto = findViewById(R.id.buttonAttachPhoto);
        buttonSaveExpense = findViewById(R.id.buttonSaveExpense);
        textViewDate = findViewById(R.id.textViewDate); // Initialize the TextView

        // Set up spinner for category
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.expense_categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Handle image attachment
        buttonAttachPhoto.setOnClickListener(view -> {
            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickPhoto, PICK_IMAGE_REQUEST);
        });

        // Set up Date Picker for calendar view
        calendarViewDate.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            // Format the date properly for displaying
            String formattedDate = String.format(Locale.US, "%02d/%02d/%d", dayOfMonth, month + 1, year);
            textViewDate.setText(formattedDate);  // Set the selected date
        });

        // Handle start and end time pickers
        editTextStartTime.setOnClickListener(v -> showTimePickerDialog(editTextStartTime));
        editTextEndTime.setOnClickListener(v -> showTimePickerDialog(editTextEndTime));

        // Handle save button click
        dbHelper = new DatabaseExpense(this);
        buttonSaveExpense.setOnClickListener(view -> saveExpense());
    }

    private void showTimePickerDialog(final EditText timeField) {
        // Get current time to set as default
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
            // Format time and set it to the EditText field
            String time = String.format("%02d:%02d", hourOfDay, minute1);
            timeField.setText(time);
        }, hour, minute, true); // true for 24-hour format
        timePickerDialog.show();
    }

    private void saveExpense() {
        String date = textViewDate.getText().toString(); // Now using formatted date from the text view
        String startTime = editTextStartTime.getText().toString();
        String endTime = editTextEndTime.getText().toString();
        String description = editTextDescription.getText().toString();
        String minGoal = editTextMinGoal.getText().toString();
        String maxGoal = editTextMaxGoal.getText().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(date) || TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill all required fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        byte[] imageBytes = null;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imageBytes = stream.toByteArray();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("start_time", startTime);
        values.put("end_time", endTime);
        values.put("description", description);
        values.put("min_goal", minGoal);
        values.put("max_goal", maxGoal);
        values.put("category", category);
        values.put("photo", imageBytes);

        long result = db.insert("expenses", null, values);
        if (result != -1) {
            Toast.makeText(this, "Expense saved successfully", Toast.LENGTH_SHORT).show();
            finish(); // Close activity after saving
        } else {
            Toast.makeText(this, "Error saving expense", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagePreview.setImageBitmap(selectedImageBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
