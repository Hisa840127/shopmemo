package com.example.shopmemo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.shopmemo.model.Item;

@Controller
public class ShopMemoController {

    // トップ画面（メニュー）
    @GetMapping("/")
    public String showIndex() {
        return "index"; // index.html
    }

 // 買うものリスト画面 
    @GetMapping("/list")
    public String showList(Model model) {

        // Mockデータ（DBから来たフリのデータ）
        List<Item> items = List.of(
            new Item("卵", 10, "個", "残り少ない気がする"),
            new Item("牛乳", 2, "本", "朝食用")
        );

        // HTML に渡すための詰め込み処理（昔の request.setAttribute に相当）
        model.addAttribute("items", items);

        // list.html を表示
        return "list";
    }


    // 購入履歴画面
    @GetMapping("/history")
    public String showHistory() {
        return "history"; // history.html
    }
}
