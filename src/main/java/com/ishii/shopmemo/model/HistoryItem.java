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

    private LocalDate purchaseDate; // <補足5>
    private String name;
    private Integer quantity;
    private String unit;
    private String shopName;

    protected HistoryItem() {} // <補足4>

    public HistoryItem(LocalDate purchaseDate, String name,
    		Integer quantity, String unit, String shopName) {
        this.purchaseDate = purchaseDate;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
    }

    public Long getId() { return id; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getName() { return name; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
}
