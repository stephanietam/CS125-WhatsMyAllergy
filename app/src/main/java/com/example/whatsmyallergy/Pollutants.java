package com.example.whatsmyallergy;

public class Pollutants {
    private String name;
    private int value;
    private String category;
    private String categoryValue;

    public Pollutants(String name, int value, String category, String categoryValue) {
        this.name = name;
        this.value = value;
        this.category = category;
        this.categoryValue = categoryValue;
    }

    public Pollutants(Pollutants p) {
        this.name = p.name;
        this.value = p.value;
        this.category = p.category;
        this.categoryValue = p.categoryValue;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public String getCategory() {
        return category;
    }

    public String getCategoryValue() {
        return categoryValue;
    }
}
