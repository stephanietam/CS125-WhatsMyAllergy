package com.example.whatsmyallergy;

public class Pollen {
    private String name;
    private int value;
    private String category;
    private String categoryValue;

    public Pollen(String name, int value, String category, String categoryValue) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.categoryValue = categoryValue;
    }

    public Pollen(Pollen p) {
        this.name = p.name;
        this.value = p.value;
        this.category = p.category;
        this.categoryValue = p.categoryValue;
    }

    public String getName() {
        return name;
    }

    public int getValue() { return value; }

    public String getCategory() {
        return category;
    }

    public String getCategoryValue() {
        return categoryValue;
    }

    public String getSeverity() {
        if (value <= 50) {
            return "LOW";
        } else if (value <= 1000) {
            return "MODERATE";
        } else {
            return "HIGH";
        }
    }
}
