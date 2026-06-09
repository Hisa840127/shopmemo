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
	
	// 再投入編集画面を表示
	@GetMapping("/readd/edit")
	public String showReaddEdit(@RequestParam Long id, Model model) {
	    Optional<HistoryItem> opt = historyItemRepository.findById(id);
	    if (opt.isEmpty()) {
	        return "redirect:/history";
	    }

	    model.addAttribute("historyItem", opt.get());
	    return "readd-edit";
	}
	/*
	 * <重要>
	 *
	 * 【このメソッドは何をしている？】
	 *
	 * 再投入編集画面(readd-edit.html)を表示する処理。
	 *
	 * 購入履歴(HistoryItem)を取得し、
	 * 画面へ渡している。
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
	 * model.addAttribute(...)
	 *
	 * ↓
	 *
	 * HTMLへデータを渡す
	 *
	 *
	 * return "readd-edit"
	 *
	 * ↓
	 *
	 * readd-edit.html を表示
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
	 * Modelへ格納
	 * ↓
	 * readd-edit.html表示
	 *
	 *
	 * 【覚えておけば十分なこと】
	 *
	 * ・再投入編集画面を表示する処理
	 *
	 * ・HistoryItemを取得して画面へ渡している
	 *
	 * ・ModelはHTMLへデータを渡す箱
	 *
	 * ・return "readd-edit" で
	 *   readd-edit.html を表示する
	 *
	 * ・細かい補足を忘れても、
	 *   「編集画面を開く処理」
	 *   と分かればOK
	 */
	/*
	 * <補足1>
	 * Model は、
	 * ControllerからHTMLへ
	 * データを渡すための箱。
	 *
	 * 今回の処理では、
	 *
	 * history.html
	 * ↓
	 * idだけ送信
	 *
	 * Controller
	 * ↓
	 * findById(id)
	 *
	 * ↓
	 * HistoryItem取得
	 *
	 * ↓
	 * Modelへ格納
	 *
	 * ↓
	 * readd-edit.html表示
	 *
	 * という流れになっている。
	 *
	 *
	 * 例：
	 *
	 * model.addAttribute(
	 *         "historyItem",
	 *         opt.get());
	 *
	 * ↓
	 *
	 * Model箱
	 * └─ historyItem
	 *     └─ HistoryItem
	 *
	 *
	 * その後、
	 *
	 * return "readd-edit";
	 *
	 * によって、
	 * readd-edit.html が表示される。
	 *
	 *
	 * HTML側では、
	 *
	 * ${historyItem.name}
	 *
	 * ${historyItem.quantity}
	 *
	 * のように参照できる。
	 *
	 *
	 * ポイント：
	 *
	 * history.html から送られてくるのは
	 * idだけ。
	 *
	 * Modelはブラウザから送られてくるものではなく、
	 * Springが用意してくれる箱。
	 *
	 * ControllerがDBから取得したデータを、
	 * HTMLへ渡すために使用する。
	 * 
	 * opt.get()は戻り値がHistoryItemクラスのインスタンス。
	 * optにはhistoryItemRepository.findById(id)が代入されているため。
	 */
	/*
	 * <補足2>
	 * addAttribute() の第1引数は、
	 * HTML側で使用する変数名。
	 *
	 * 例：
	 *
	 * model.addAttribute(
	 *         "historyItem",
	 *         opt.get());
	 *
	 * ↓
	 *
	 * HTML側では、
	 *
	 * ${historyItem}
	 *
	 * という名前で参照できるようになる。
	 *
	 *
	 * 今回の場合、
	 *
	 * opt.get()
	 *
	 * ↓
	 *
	 * HistoryItemインスタンス
	 *
	 * が格納されている。
	 *
	 *
	 * そのため、
	 *
	 * ${historyItem.name}
	 *
	 * ${historyItem.quantity}
	 *
	 * ${historyItem.unit}
	 *
	 * のように書ける。
	 *
	 *
	 * イメージ：
	 *
	 * model.addAttribute(
	 *         "historyItem",
	 *         opt.get());
	 *
	 * ↓
	 *
	 * Model箱
	 * └─ historyItem
	 *     └─ HistoryItem
	 *
	 *
	 * HTML
	 *
	 * ${historyItem.name}
	 *
	 * ↓
	 *
	 * HistoryItemのname取得
	 *
	 *
	 * ${historyItem.quantity}
	 *
	 * ↓
	 *
	 * HistoryItemのquantity取得
	 *
	 *
	 * Thymeleafが裏側で
	 * getter(getName(), getQuantity() など)
	 * を呼び出している。
	 *
	 *
	 * ポイント：
	 *
	 * addAttribute() の第1引数
	 * ↓
	 * HTML側の変数名
	 *
	 * addAttribute() の第2引数
	 * ↓
	 * 実際に渡すデータ
	 */
	
	// 再投入編集画面から買うものリストへ追加
	@PostMapping("/readd")
	public String readd(
	        @RequestParam String name,
	        @RequestParam int quantity,
	        @RequestParam String unit,
	        @RequestParam String shopName,
	        @RequestParam String memo) {

	    Item item = new Item(name, quantity, unit, shopName, memo);
	    itemRepository.save(item);

	    return "redirect:/list";
	}
	/*
	 * <重要>
	 *
	 * 【このメソッドは何をしている？】
	 *
	 * 再投入編集画面(readd-edit.html)から送信された内容を受け取り、
	 * 買うものリスト(Item)へ追加する処理。
	 *
	 *
	 * 【最低限どこを見る？】
	 *
	 * @RequestParam
	 *
	 * ↓
	 *
	 * HTMLから送られた値を受け取る
	 *
	 *
	 * new Item(...)
	 *
	 * ↓
	 *
	 * Itemインスタンス作成
	 *
	 *
	 * itemRepository.save(item)
	 *
	 * ↓
	 *
	 * DBへ保存
	 *
	 *
	 * return "redirect:/list"
	 *
	 * ↓
	 *
	 * 買うものリスト画面へ戻る
	 *
	 *
	 * 【ざっくり流れ】
	 *
	 * 再投入画面で入力
	 * ↓
	 * submit
	 * ↓
	 * @RequestParamで値受取
	 * ↓
	 * Item作成
	 * ↓
	 * saveでDB保存
	 * ↓
	 * /listへリダイレクト
	 *
	 *
	 * 【覚えておけば十分なこと】
	 *
	 * ・再投入内容をItemとして保存する処理
	 *
	 * ・@RequestParamは
	 *   HTMLから送られた値を受け取る
	 *
	 * ・new Item(...)で
	 *   Itemインスタンスを作成する
	 *
	 * ・save(...)でDBへ保存する
	 *
	 * ・return "redirect:/list" で
	 *   一覧画面へ戻る
	 *
	 * ・細かい補足を忘れても、
	 *   「入力内容をDBへ保存する処理」
	 *   と分かればOK
	 */
	/*
	 * <補足1>
	 *
	 * @RequestParam について。
	 *
	 * HTMLのformから送られた値を
	 * Javaの引数として受け取る。
	 *
	 *
	 * 例：
	 *
	 * HTML
	 *
	 * <input name="name">
	 *
	 * ↓
	 *
	 * Java
	 *
	 * @RequestParam String name
	 *
	 *
	 * 対応関係：
	 *
	 * HTML
	 * name="name"
	 *
	 * ↓
	 *
	 * Java
	 * @RequestParam String name
	 *
	 *
	 * HTML側の name と
	 * Java側の引数名が対応している。
	 */
	/*
	 * <補足2>
	 *
	 * submitからsaveまでの流れ。
	 *
	 * HTML入力
	 * ↓
	 * submit
	 * ↓
	 * form送信
	 * ↓
	 * @RequestParam
	 * ↓
	 * Javaで受取
	 * ↓
	 * new Item(...)
	 * ↓
	 * Item作成
	 * ↓
	 * save(...)
	 * ↓
	 * DB保存
	 *
	 *
	 * イメージ：
	 *
	 * HTML
	 * ↓
	 * form
	 * ↓
	 * submit
	 * ↓
	 * Java
	 * ↓
	 * Item
	 * ↓
	 * DB
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
