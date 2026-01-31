package com.example.cajaac;

public class TransactionItem {
    private String label;
    private String amount;
    private String serie;
    private String quantity;

    // Constructor para 2 columnas (label, amount)
    public TransactionItem(String label, String amount) {
        this.label = label;
        this.amount = amount;
        this.serie = amount;  // También se puede usar como serie
        this.quantity = "";
    }

    // Constructor para 3 columnas (label, quantity, amount)
    public TransactionItem(String label, String quantity, String amount) {
        this.label = label;
        this.quantity = quantity;
        this.amount = amount;
        this.serie = "";
    }

    public String getLabel() {
        return label;
    }

    public String getAmount() {
        return amount;
    }

    public String getSerie() {
        return serie;
    }

    public String getQuantity() {
        return quantity;
    }
}

