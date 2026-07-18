package com.ishii.shopmemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishii.shopmemo.model.HistoryItem;
import com.ishii.shopmemo.model.User;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
	List<HistoryItem> findByUser(User user);}
