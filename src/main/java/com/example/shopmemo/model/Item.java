package com.example.shopmemo.model;

public class Item {

    private String name;
    private int quantity;
    private String unit;
    private String memo;

    public Item(String name, int quantity, String unit, String memo) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.memo = memo;
    }

    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getMemo() { return memo; }
}
