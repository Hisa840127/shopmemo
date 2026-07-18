package com.ishii.shopmemo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	protected HistoryItem() {
	} // <補足4>

	public HistoryItem(LocalDate purchaseDate, String name,
			Integer quantity, String unit, String shopName) {
		this.purchaseDate = purchaseDate;
		this.name = name;
		this.quantity = quantity;
		this.unit = unit;
		this.shopName = shopName;
	}

	public Long getId() {
		return id;
	}
	
	public User getUser() {
	    return user;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public String getName() {
		return name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public String getUnit() {
		return unit;
	}

	public String getShopName() {
		return shopName;
	}

	public void setPurchaseDate(LocalDate now) {

	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public void setUser(User user) {
	    this.user = user;
	}
}
