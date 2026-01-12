package com.ishii.shopmemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ishii.shopmemo.model.Item;
import com.ishii.shopmemo.repository.ItemRepository;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(ItemRepository itemRepository) {
        return args -> {
            itemRepository.save(new Item(
                "卵", 10, "個", "ヨークフーズ", "残り少ない気がする"
            ));
            itemRepository.save(new Item(
                "牛乳", 2, "本", "西友", "朝食用"
            ));
        };
    }
}

/*
 * 【学習メモ】
 * CommandLineRunner は関数型インターフェース。
 * run(String... args) 1つだけを持つ。
 *
 * ↓ これはreturn args -> の部分を匿名クラスで書いた場合
 *
 * return new CommandLineRunner() {
 *     @Override
 *     public void run(String... args) {
 *         itemRepository.save(...);
 *     }
 * };
 * itemRepository は @Bean メソッドの引数として Spring から注入されている
 * ※ args は受け取るが今回は使っていない
 */
