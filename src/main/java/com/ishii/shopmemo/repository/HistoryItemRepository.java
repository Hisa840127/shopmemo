package com.ishii.shopmemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ishii.shopmemo.model.HistoryItem;

public interface HistoryItemRepository extends JpaRepository<HistoryItem, Long> {
}
