package com.example.shopmemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShopMemoController {

    // トップ画面（メニュー）
    @GetMapping("/")
    public String showIndex() {
        return "index"; // index.html
    }

    // 買うものリスト画面
    @GetMapping("/list")
    public String showList() {
        return "list"; // list.html
    }

    // 購入履歴画面
    @GetMapping("/history")
    public String showHistory() {
        return "history"; // history.html
    }
}
