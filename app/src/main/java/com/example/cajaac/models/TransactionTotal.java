package com.example.cajaac.models;

public class TransactionTotal {
    private String label;
    private String quantity;
    private String amount;
    private int backgroundColor;

    // Constructor para totales con 2 columnas
    public TransactionTotal(String label, String amount, int backgroundColor) {
        this.label = label;
        this.amount = amount;
        this.quantity = "";
        this.backgroundColor = backgroundColor;
    }

    public String getLabel() {
        return label;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getAmount() {
        return amount;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }
}
