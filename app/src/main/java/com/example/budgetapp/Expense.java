package com.example.budgetapp;

public class Expense {
    // Fields from both classes
    private int id;
    private String date, startTime, endTime, description, category;
    private String minGoal, maxGoal;
    private byte[] photo;
    private String photoPath; // Path for the photo

    // Constructor with ID (used for database retrieval)
    public Expense(int id, String date, String startTime, String endTime, String description,
                   String category, String minGoal, String maxGoal, byte[] photo, String photoPath) {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
        this.category = category;
        this.minGoal = minGoal;
        this.maxGoal = maxGoal;
        this.photo = photo;
        this.photoPath = photoPath;
    }



    // Overloaded constructor without ID (used for inserts)
    public Expense(String date, String startTime, String endTime, String description,
                   String category, String minGoal, String maxGoal, byte[] photo, String photoPath) {
        this(-1, date, startTime, endTime, description, category, minGoal, maxGoal, photo, photoPath);
    }

    // Constructor without min/max goal (for simpler use cases)
    public Expense(String date, String startTime, String endTime, String description,
                   String category, byte[] photo, String photoPath) {
        this(-1, date, startTime, endTime, description, category, null, null, photo, photoPath);
    }

    // Getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDate() { return date; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public String getDescription() { return description; }
    public String getCategory() { return category; }
    public String getMinGoal() { return minGoal; }
    public String getMaxGoal() { return maxGoal; }
    public byte[] getPhoto() { return photo; }
    public void setPhoto(byte[] photo) { this.photo = photo; }
    public String getPhotoPath() { return photoPath; }

    // Optionally, add setters if needed for other fields
    public void setPhotoPath(String photoPath) { this.photoPath = photoPath; }
}
