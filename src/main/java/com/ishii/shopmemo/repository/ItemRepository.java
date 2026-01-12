package com.ishii.shopmemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishii.shopmemo.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
