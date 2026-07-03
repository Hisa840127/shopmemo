package com.ishii.shopmemo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ishii.shopmemo.model.HistoryItem;
import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.repository.HistoryItemRepository;
import com.ishii.shopmemo.repository.ItemRepository;

@Controller
public class ShopMemoController {
	private final ItemRepository itemRepository;
	private final HistoryItemRepository historyItemRepository;

	public ShopMemoController(ItemRepository itemRepository, HistoryItemRepository historyItemRepository) {
		this.itemRepository = itemRepository;
		this.historyItemRepository = historyItemRepository;
	}

	// トップ画面（メニュー）
	@GetMapping("/")
	public String showIndex() {
		return "index"; // index.html
	}

	// 買うものリスト画面 
	@GetMapping("/list")
	public String showList(Model model) {

		// DBから買うものリストを取得
		List<Item> items = itemRepository.findAll();

		// HTML に渡すための詰め込み処理（昔の request.setAttribute に相当）
		model.addAttribute("items", items);

		// list.html を表示
		return "list";
	}

	// 数量更新ボタン
	@PostMapping("/items/updateQuantity")
	public String updateQuantity(
	        @RequestParam Long id,
	        @RequestParam Integer quantity) {

	    Optional<Item> opt = itemRepository.findById(id);

	    if (opt.isPresent()) {
	        Item item = opt.get();
	        item.setQuantity(quantity);
	        itemRepository.save(item);
	    }

	    return "redirect:/list";
	}

	// 購入ボタン
	@Transactional
	@PostMapping("/items/purchase")
	public String purchase(@RequestParam Long id) {

		// 1) 対象のItemを取得
		Optional<Item> opt = itemRepository.findById(id);
		if (opt.isEmpty()) {
			return "redirect:/list"; // ないなら何もしない（雑に安全）
		}

		Item item = opt.get();

		// 2) 履歴を作って保存
		HistoryItem history = new HistoryItem(
				LocalDate.now(),
				item.getName(),
				item.getQuantity(),
				item.getUnit(),
				item.getShopName());
		historyItemRepository.save(history);

		// 3) 元のItemを削除
		itemRepository.deleteById(id);

		// 4) 画面を更新
		return "redirect:/list";
	}
	
	// まとめて購入処理ボタン
	@PostMapping("/items/purchase/bulk")
	public String bulkPurchase(@RequestParam List<Long> ids) {

	    for (Long id : ids) {

	        Item item = itemRepository.findById(id).orElseThrow();

	    		HistoryItem historyItem = new HistoryItem(
					LocalDate.now(),
					item.getName(),
					item.getQuantity(),
					item.getUnit(),
					item.getShopName());

	        historyItem.setPurchaseDate(LocalDate.now());
	        historyItem.setName(item.getName());
	        historyItem.setQuantity(item.getQuantity());
	        historyItem.setUnit(item.getUnit());
	        historyItem.setShopName(item.getShopName());

	        historyItemRepository.save(historyItem);

	        itemRepository.delete(item);
	    }

	    return "redirect:/list";
	}
	
	// 削除処理
	@PostMapping("/items/delete")
	public String deleteItem(@RequestParam Long id) {
		itemRepository.deleteById(id);
		return "redirect:/list";
	}

	// 新規追加
	@PostMapping("/items")
	public String addItem(Item item) {
		 if (item.getQuantity() == null) {
		        item.setQuantity(1);
		    }
		itemRepository.save(item);
		return "redirect:/list";
	}

	// 購入履歴画面
	@GetMapping("/history")
	public String showHistory(Model model) {
		List<HistoryItem> historyItems = historyItemRepository.findAll();
		model.addAttribute("historyItems", historyItems);
		return "history";
	}

	// 再投入編集画面
	@GetMapping("/readd/edit")
	public String showReaddEdit(@RequestParam Long id, Model model) {
		Optional<HistoryItem> opt = historyItemRepository.findById(id);
		if (opt.isEmpty()) {
			return "redirect:/history";
		}
		model.addAttribute("historyItem", opt.get());
		return "readd-edit";
	}

	// 再投入編集画面→再投入
	@PostMapping("/readd")
	public String readd(Item item) {
		if (item.getQuantity() == null) {
	        item.setQuantity(1);
	    }
		itemRepository.save(item);
		return "redirect:/list";
	}
	// 削除処理
	@PostMapping("/history/delete")
	public String deleteHistoryItem(@RequestParam Long id) {
		historyItemRepository.deleteById(id);
		return "redirect:/list";
	}


}

/*デプロイ動作確認用コメント6/30*/