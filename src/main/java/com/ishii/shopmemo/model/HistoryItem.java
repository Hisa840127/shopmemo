package com.ishii.shopmemo.model;

public class HistoryItem {

    private String purchaseDate;
    private String name;
    private int quantity;
    private String unit;
    private String shopName;

    public HistoryItem(String purchaseDate, String name, int quantity, String unit, String shopName) {
        this.purchaseDate = purchaseDate;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
    }

    public String getPurchaseDate() { return purchaseDate; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    	public String getShopName() { return shopName; }
}
