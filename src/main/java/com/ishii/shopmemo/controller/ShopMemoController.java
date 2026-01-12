package com.ishii.shopmemo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ishii.shopmemo.model.HistoryItem;
import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.repository.ItemRepository;

@Controller
public class ShopMemoController {
	private final ItemRepository itemRepository;

	public ShopMemoController(ItemRepository itemRepository) {
	    this.itemRepository = itemRepository;
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


    // 購入履歴画面
    @GetMapping("/history")
    public String showHistory(Model model) {
    		// Mockデータ（DBから来たフリのデータ）
        List<HistoryItem> historyItems = List.of(
            new HistoryItem("2025-11-15", "卵", 10, "個", "ヨークフーズ"),
            new HistoryItem("2025-11-10", "玉ねぎ", 5, "個", "マルエツ"),
            new HistoryItem("2025-11-03", "豚ひき肉", 500, "g", "イトーヨーカドー" )
        );
        
        // HTML に渡すための詰め込み処理
        model.addAttribute("historyItems", historyItems);
        
        // history.html を表示
        return "history";
    }

}
