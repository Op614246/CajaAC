package com.example.cajaac;

public class TransactionItem {
    private String label;
    private String amount;

    public TransactionItem(String label, String amount) {
        this.label = label;
        this.amount = amount;
    }

    public String getLabel() {
        return label;
    }

    public String getAmount() {
        return amount;
    }
}

