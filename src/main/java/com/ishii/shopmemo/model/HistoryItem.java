package com.ishii.shopmemo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate purchaseDate;
    private String name;
    private int quantity;
    private String unit;
    private String shopName;

    protected HistoryItem() {}

    public HistoryItem(LocalDate purchaseDate, String name, int quantity, String unit, String shopName) {
        this.purchaseDate = purchaseDate;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
    }

    public Long getId() { return id; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
}
