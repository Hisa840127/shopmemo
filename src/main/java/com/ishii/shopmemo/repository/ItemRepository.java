package com.ishii.shopmemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.model.User;

public interface ItemRepository extends JpaRepository<Item, Long> {
	List<Item> findByUser(User user);}
