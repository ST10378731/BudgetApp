package com.example.budgetapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    private final List<Expense> expenses;
    private final Context context;

    public ExpenseAdapter(Context context, List<Expense> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textDescription, textDate, textCategory;
        ImageView imageReceipt;

        public ViewHolder(View itemView) {
            super(itemView);
            textDescription = itemView.findViewById(R.id.textDescription);
            textDate = itemView.findViewById(R.id.textDate);
            textCategory = itemView.findViewById(R.id.textCategory);
            imageReceipt = itemView.findViewById(R.id.imageReceipt);
        }
    }

    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_expenseitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExpenseAdapter.ViewHolder holder, int position) {
        Expense expense = expenses.get(position);

        holder.textDescription.setText(expense.getDescription());
        holder.textDate.setText(expense.getDate());
        holder.textCategory.setText(expense.getCategory());

        if (expense.getPhotoPath() != null) {
            holder.imageReceipt.setImageURI(Uri.parse(expense.getPhotoPath()));
        } else {
            // No placeholder or fallback image is set anymore
        }

        holder.imageReceipt.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(expense.getPhotoPath()), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }
}
