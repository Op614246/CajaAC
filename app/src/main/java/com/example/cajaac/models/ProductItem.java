package com.example.cajaac.models;

public class ProductItem {
    private String label;
    private String value;
    private int colorRes;

    public ProductItem(String label, String value, int colorRes) {
        this.label = label;
        this.value = value;
        this.colorRes = colorRes;
    }

    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public int getColorRes() {
        return colorRes;
    }
}

