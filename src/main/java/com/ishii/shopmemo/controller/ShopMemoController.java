package com.ishii.shopmemo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import jakarta.transaction.Transactional;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ishii.shopmemo.model.HistoryItem;
import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.model.User;
import com.ishii.shopmemo.repository.HistoryItemRepository;
import com.ishii.shopmemo.repository.ItemRepository;
import com.ishii.shopmemo.repository.UserRepository;

@Controller
public class ShopMemoController {
	private final ItemRepository itemRepository;
	private final HistoryItemRepository historyItemRepository;
	private final UserRepository userRepository;
	
	public ShopMemoController(ItemRepository itemRepository, 
			HistoryItemRepository historyItemRepository,
			UserRepository userRepository) {
		this.itemRepository = itemRepository;
		this.historyItemRepository = historyItemRepository;
		this.userRepository = userRepository;
	}

	@GetMapping("/")
	public String redirectToList() {
	    return "redirect:/list";
	}
	
	// 買うものリスト画面 
	@GetMapping("/list")
	public String showList(Model model,
	                       Authentication authentication) {

	    String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();

	    List<Item> items = itemRepository.findByUser(user);

	    model.addAttribute("items", items);
	    model.addAttribute("username", username);

	    return "list";
	}
	
	@PostMapping("/admin/restoreDemoData")
	public String restoreDemoData(Authentication authentication) {

	    if (!"admin".equals(authentication.getName())) {
	        throw new AccessDeniedException("adminのみ実行できます");
	    }

	    User admin = userRepository.findByUsername("admin")
	            .orElseThrow();

	    if (itemRepository.countByUser(admin) == 0) {

	        Item egg = new Item(
	                "卵",
	                10,
	                "個",
	                "ヨークフーズ",
	                "残り少ない気がする"
	        );
	        egg.setUser(admin);

	        Item milk = new Item(
	                "牛乳",
	                2,
	                "本",
	                "西友",
	                "朝食用"
	        );
	        milk.setUser(admin);

	        itemRepository.save(egg);
	        itemRepository.save(milk);
	    }

	    return "redirect:/list";
	}

	// 数量更新ボタン
	@PostMapping("/items/updateQuantity")
	public String updateQuantity(
	        @RequestParam Long id,
	        @RequestParam Integer quantity,
	        Authentication authentication) {
		
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();
	    
	    Optional<Item> opt = itemRepository.findById(id);

	    if (opt.isPresent()) {
	        Item item = opt.get();
	        if (!item.getUser().getId().equals(user.getId())) {
	            return "redirect:/list";
	        }

	        item.setQuantity(quantity);
	        itemRepository.save(item);
	    }

	    return "redirect:/list";
	}

	// 購入ボタン
	@Transactional
	@PostMapping("/items/purchase")
	public String purchase(@RequestParam Long id,
			Authentication authentication) {
		
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();
	    
		Optional<Item> opt = itemRepository.findById(id);
		if (opt.isEmpty()) {
			return "redirect:/list"; 
		}
		
		Item item = opt.get();
		
		if (!item.getUser().getId().equals(user.getId())) {
            return "redirect:/list";
        }
		
		HistoryItem history = new HistoryItem(
				LocalDate.now(),
				item.getName(),
				item.getQuantity(),
				item.getUnit(),
				item.getShopName());
		
		history.setUser(user);
		
		historyItemRepository.save(history);

		itemRepository.deleteById(id);

		return "redirect:/list";
	}
	
	// まとめて購入処理ボタン
	@PostMapping("/items/purchase/bulk")
	public String bulkPurchase(@RequestParam(required = false) List<Long> ids,
			Authentication authentication	) {
		
		
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();
	    
	    if (ids == null || ids.isEmpty()) {
	        return "redirect:/list";
	    }
	    
	    for (Long id : ids) {

	        Item item = itemRepository.findById(id).orElseThrow();
	        if (!item.getUser().getId().equals(user.getId())) {
	            return "redirect:/list";
	        }
	        
	    		HistoryItem historyItem = new HistoryItem(
					LocalDate.now(),
					item.getName(),
					item.getQuantity(),
					item.getUnit(),
					item.getShopName());
	    		
	    		historyItem.setUser(user);

	        historyItemRepository.save(historyItem);

	        itemRepository.delete(item);
	    }

	    return "redirect:/list";
	}
	
	// 削除処理
	@PostMapping("/items/delete")
	public String deleteItem(
			@RequestParam Long id,
			Authentication authentication) {
		
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();

	    Item item = itemRepository.findById(id)
	            .orElseThrow();

	    if (!item.getUser().getId().equals(user.getId())) {
	        return "redirect:/list";
	    }

	    itemRepository.delete(item);
	    
	    return "redirect:/list";
	}

	// 新規追加
	@PostMapping("/items")
	public String addItem(Item item, Authentication authentication) {
		
		if (item.getName().isBlank()) {
		    return "redirect:/list";
		}
		
	    if (item.getQuantity() == null) {
	        item.setQuantity(1);
	    }

	    String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();

	    item.setUser(user);

	    itemRepository.save(item);

	    return "redirect:/list";
	}

	// 購入履歴画面
	@GetMapping("/history")
	public String showHistory(
	        Model model,
	        Authentication authentication) {

	    String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();

	    List<HistoryItem> historyItems =
	            historyItemRepository.findByUser(user);

	    model.addAttribute("historyItems", historyItems);
	    model.addAttribute("username", username);
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
	public String readd(Item item,  
			Authentication authentication) {
		if (item.getQuantity() == null) {
	        item.setQuantity(1);
	    }
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();
	    
	    item.setUser(user);

	    itemRepository.save(item);

	    return "redirect:/list";
	}
	// 削除処理
	@PostMapping("/history/delete")
	public String deleteHistoryItem(
			@RequestParam Long id,
			Authentication authentication) {
		String username = authentication.getName();

	    User user = userRepository.findByUsername(username)
	            .orElseThrow();
	    
	    HistoryItem historyItem = historyItemRepository.findById(id)
	    						.orElseThrow();
	    if (!historyItem.getUser().getId().equals(user.getId())) {
	        return "redirect:/list";
	    }
	    
	    historyItemRepository.delete(historyItem);

		return "redirect:/history";
	}


}
