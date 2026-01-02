package com.example.shopmemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private String shopName;
    private String memo;

    protected Item() {} // JPA用コンストラクタ（DBから復元するため）

    public Item(String name, int quantity, String unit, String shopName,String memo) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
        this.memo = memo;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
    public String getMemo() { return memo; }
}
