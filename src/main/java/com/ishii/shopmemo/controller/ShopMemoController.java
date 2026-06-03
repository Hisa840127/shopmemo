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
	
	// 購入ボタン
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
	
	// 新規追加
	@PostMapping("/items")
	public String addItem(
	        @RequestParam String name,
	        @RequestParam int quantity,
	        @RequestParam String unit,
	        @RequestParam String shopName,
	        @RequestParam String memo) {

	    Item item = new Item(name, quantity, unit, shopName, memo);
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
	
	// 購入履歴から買うものリストへ再投入
	@PostMapping("/readd")
	public String readd(@RequestParam Long id) {

		Optional<HistoryItem> opt = historyItemRepository.findById(id);
		if (opt.isEmpty()) {
			return "redirect:/history";
		}

		HistoryItem history = opt.get();

		Item item = new Item(
				history.getName(),
				history.getQuantity(),
				history.getUnit(),
				history.getShopName(),
				"");

		itemRepository.save(item);

		return "redirect:/list";
	}
	/*
	 * <重要>
	 *
	 * 【このメソッドは何をしている？】
	 *
	 * 購入履歴(HistoryItem)から、
	 * 買うものリスト(Item)へ再投入する処理。
	 *
	 *
	 * 【最低限どこを見る？】
	 *
	 * historyItemRepository.findById(id)
	 *
	 * ↓
	 *
	 * 購入履歴を取得
	 *
	 *
	 * new Item(...)
	 *
	 * ↓
	 *
	 * HistoryItemの内容から
	 * Itemを作成
	 *
	 *
	 * itemRepository.save(item)
	 *
	 * ↓
	 *
	 * 買うものリストへ保存
	 *
	 *
	 * 【ざっくり流れ】
	 *
	 * 再投入ボタン押下
	 * ↓
	 * 履歴ID受取
	 * ↓
	 * HistoryItem取得
	 * ↓
	 * Item作成
	 * ↓
	 * Item保存
	 * ↓
	 * 買うものリスト画面へ移動
	 *
	 *
	 * 【覚えておけば十分なこと】
	 *
	 * ・HistoryItem → Item の変換処理
	 *
	 * ・履歴から買うものリストへ戻す機能
	 *
	 * ・findById()で履歴取得
	 *
	 * ・save()で買うものリストへ保存
	 *
	 * ・細かい補足を忘れても、
	 *   「履歴から再投入する処理」
	 *   と分かればOK
	 */
	/*
	 * <補足1>
	 * Optional は
	 * 「中身があるかもしれないし、
	 * 無いかもしれない箱」。
	 *
	 * findById() は、
	 * データが見つからない可能性があるため、
	 *
	 * Optional<HistoryItem>
	 *
	 * を返す。
	 *
	 *
	 * 例：
	 *
	 * Optional<HistoryItem> opt
	 *
	 * ↓
	 *
	 * 中身あり
	 * └─ HistoryItem
	 *
	 * または
	 *
	 * 中身なし
	 * └─ 空
	 *
	 *
	 * そのため、
	 *
	 * if (opt.isEmpty())
	 *
	 * で空チェックを行う。
	 *
	 *
	 * 空でなければ、
	 *
	 * HistoryItem history = opt.get();
	 *
	 * で中身を取り出せる。
	 *
	 *
	 * イメージ：
	 *
	 * opt
	 * ↓
	 * Optionalの箱
	 *
	 * opt.get()
	 * ↓
	 * 箱から中身を取り出す
	 *
	 *
	 * 空の状態で get() を呼ぶと
	 * NoSuchElementException が発生するため、
	 * 通常は isEmpty() や isPresent() とセットで使用する。
	 */
}
/*
 * <重要>
 *
 * 【このクラスは何をしている？】
 *
 * Shopmemoの画面遷移とボタン処理を担当するControllerクラス。
 *
 * ブラウザから来たリクエストを受け取り、
 * DBからデータを取得したり保存したりして、
 * 表示するHTMLを決めている。
 *
 *
 * 【最低限どこを見る？】
 *
 * @Controller
 *
 * ↓
 *
 * 画面操作を受け取るControllerクラスのタグ
 *
 *
 * @GetMapping("/")
 *
 * ↓
 *
 * トップ画面 index.html を表示
 *
 *
 * @GetMapping("/list")
 *
 * ↓
 *
 * 買うものリストをDBから取得して
 * list.html を表示
 *
 *
 * @PostMapping("/purchase")
 *
 * ↓
 *
 * 購入ボタン処理。
 * ItemをHistoryItemへ移して、
 * 元のItemを削除する。
 *
 *
 * @PostMapping("/items")
 *
 * ↓
 *
 * 新規追加処理。
 * 入力された内容からItemを作り、
 * DBへ保存する。
 *
 *
 * @GetMapping("/history")
 *
 * ↓
 *
 * 購入履歴をDBから取得して
 * history.html を表示
 *
 *
 * 【ざっくり流れ】
 *
 * ブラウザからURLアクセス
 * ↓
 * @GetMapping / @PostMapping が受け取る
 * ↓
 * RepositoryでDB操作
 * ↓
 * Modelにデータを詰める
 * ↓
 * return "list" などでHTMLを表示
 *
 *
 * 【購入処理の流れ】
 *
 * 購入ボタン押下
 * ↓
 * /purchase にPOST
 * ↓
 * idでItemを検索
 * ↓
 * HistoryItemを作成して保存
 * ↓
 * 元のItemを削除
 * ↓
 * redirect:/list でリスト画面を再表示
 *
 *
 * 【覚えておけば十分なこと】
 *
 * ・このクラスはShopmemoのController
 *
 * ・画面表示とボタン処理を担当している
 *
 * ・Repositoryを使ってDB操作している
 *
 * ・/list は買うものリスト表示
 *
 * ・/purchase は購入済みにして履歴へ移す処理
 *
 * ・/items は買うものの新規追加
 *
 * ・/history は購入履歴表示
 *
 * ・細かい補足を忘れても、
 *   「画面とDB処理をつなぐ司令塔」
 *   と分かればOK
 */
