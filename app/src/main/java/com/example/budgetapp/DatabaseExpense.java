package com.example.budgetapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseExpense extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BudgetApp.db";
    private static final int DATABASE_VERSION = 2;

    public DatabaseExpense(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT NOT NULL," +
                "start_time TEXT NOT NULL," +
                "end_time TEXT NOT NULL," +
                "description TEXT NOT NULL," +
                "min_goal TEXT," +
                "max_goal TEXT," +
                "category TEXT," +
                "photo BLOB" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    // ✅ Added method to get expenses between two dates
    public List<Expense> getExpensesBetween(String startDate, String endDate) {
        List<Expense> expenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT date, start_time, end_time, description, category, min_goal, max_goal, photo " +
                        "FROM expenses WHERE date BETWEEN ? AND ?",
                new String[]{startDate, endDate}
        );

        if (cursor.moveToFirst()) {
            do {
                String date = cursor.getString(0);
                String startTime = cursor.getString(1);
                String endTime = cursor.getString(2);
                String description = cursor.getString(3);
                String category = cursor.getString(4);
                String minGoal = cursor.getString(5);
                String maxGoal = cursor.getString(6);
                byte[] photo = cursor.getBlob(7);

                Expense expense = new Expense(
                        cursor.getInt(0),  // Assuming the first column is the id
                        date,
                        startTime,
                        endTime,
                        description,
                        category,
                        minGoal,
                        maxGoal,
                        photo,
                        null  // photoPath can be null for now if not available
                );
                expenseList.add(expense);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return expenseList;
    }
}
