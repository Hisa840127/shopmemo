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

	@Transactional
	@PostMapping("/purchase")
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
	/*
	 * 【学習メモ】
	 * 1)について
	 * .findById(id) の戻り値は Item ではなく Optional<Item>
	 *
	 * ・Item が存在するか分からないため、Optional で包まれて返る
	 * ・Optional 自体は null ではないので、この時点では NPE は起きない
	 * ・ただし、中身が空のまま get() すると例外が発生する
	 *
	 * ・そのため isEmpty() で存在チェックしてから処理する
	 * ・存在しない場合は何もせず list 画面に戻す（redirect）
	 * 
	 * redirect:/list は「GET /list を新しく実行し直す」指示
	 * return "list" とは挙動が違う（POSTの文脈を引きずらない）
	 * ※redirect しないと POST の命令が残ったまま list 画面が表示されてしまう
	 * 
	 * .get() は Optional<T> の中身を取り出すメソッド
	 * T はここでは Item 型なので、戻り値は Item になる
	 * 
	 * 2) について
	 * 1) で取得した Item の値を使って、HistoryItem を新しく作成している
	 *
	 * ・Item → HistoryItem へ「値の詰め替え」をしているイメージ
	 * ・購入日が Item には存在しないため、ここで LocalDate.now() により
	 *   「購入した日」を新しく決定している
	 *
	 * ・HistoryItem の id はここでは渡していない
	 *   → @GeneratedValue が付いているため、
	 *     historyItemRepository.save(history) 実行時に
	 *     DB 側で自動的に ID が採番される
	 *
	 * ・Item の id と HistoryItem の id は別物
	 *   → それぞれのテーブル内での識別子
	 *   
	 * 3) について
	 * ここで引数として渡されたidはItemのidなので、それを使って
	 * DBのITEMテーブルから、該当する行（レコード）を削除している   
	 */

	// 購入履歴画面
	@GetMapping("/history")
	public String showHistory(Model model) {
		List<HistoryItem> historyItems = historyItemRepository.findAll();
		model.addAttribute("historyItems", historyItems);
		return "history";
	}

}
