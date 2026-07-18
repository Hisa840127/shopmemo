package com.ishii.shopmemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer quantity;
    private String unit;
    private String shopName;
    private String memo;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; 

    protected Item() {} 
    public Item(String name, Integer quantity, String unit, String shopName,String memo) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
        this.memo = memo;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Integer getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
    public String getMemo() { return memo; }
    public User getUser() {
        return user;
    }

    
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public void setUser(User user) {
	    this.user = user;
	}
}

