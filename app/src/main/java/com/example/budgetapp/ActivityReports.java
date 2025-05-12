package com.example.budgetapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.*;

public class ActivityReports extends AppCompatActivity {

    private Button buttonStartDate, buttonEndDate;
    private RecyclerView recyclerViewExpenses;
    private LinearLayout layoutCategoryTotals;

    private String startDate = "", endDate = "";
    private final Calendar calendar = Calendar.getInstance();

    private DatabaseExpense databaseExpense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);

        buttonStartDate = findViewById(R.id.buttonStartDate);
        buttonEndDate = findViewById(R.id.buttonEndDate);
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses);
        layoutCategoryTotals = findViewById(R.id.layoutCategoryTotals);

        recyclerViewExpenses.setLayoutManager(new LinearLayoutManager(this));
        databaseExpense = new DatabaseExpense(this);

        buttonStartDate.setOnClickListener(v -> showDatePicker(true));
        buttonEndDate.setOnClickListener(v -> showDatePicker(false));
    }

    private void showDatePicker(boolean isStart) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, (view, y, m, d) -> {
            String selectedDate = y + "-" + String.format("%02d", m + 1) + "-" + String.format("%02d", d);
            if (isStart) {
                startDate = selectedDate;
                buttonStartDate.setText("Start: " + selectedDate);
            } else {
                endDate = selectedDate;
                buttonEndDate.setText("End: " + selectedDate);
            }

            if (!startDate.isEmpty() && !endDate.isEmpty()) {
                if (startDate.compareTo(endDate) > 0) {
                    Toast.makeText(this, "Start date must be before end date", Toast.LENGTH_SHORT).show();
                } else {
                    loadFilteredExpenses();
                }
            }
        }, year, month, day);

        dpd.show();
    }

    private void loadFilteredExpenses() {
        List<Expense> expenses = databaseExpense.getExpensesBetween(startDate, endDate);
        ExpenseAdapter adapter = new ExpenseAdapter(this, expenses);
        recyclerViewExpenses.setAdapter(adapter);
        displayCategoryTotals(expenses);
    }

    private void displayCategoryTotals(List<Expense> expenses) {
        layoutCategoryTotals.removeAllViews();

        if (expenses.isEmpty()) {
            TextView empty = new TextView(this);
            empty.setText("No expenses found.");
            layoutCategoryTotals.addView(empty);
            return;
        }

        Map<String, Integer> categoryCounts = new HashMap<>();
        for (Expense e : expenses) {
            String cat = e.getCategory();
            categoryCounts.put(cat, categoryCounts.getOrDefault(cat, 0) + 1);
        }

        int max = Collections.max(categoryCounts.values());

        for (String cat : categoryCounts.keySet()) {
            int count = categoryCounts.get(cat);

            TextView label = new TextView(this);
            label.setText(cat + " (" + count + ")");
            layoutCategoryTotals.addView(label);

            ProgressBar bar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
            bar.setMax(max);
            bar.setProgress(count);
            bar.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            layoutCategoryTotals.addView(bar);
        }
    }
}
