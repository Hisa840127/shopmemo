package com.ishii.shopmemo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class HistoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate purchaseDate; // <補足5>
    private String name;
    private int quantity;
    private String unit;
    private String shopName;

    protected HistoryItem() {} // <補足4>

    public HistoryItem(LocalDate purchaseDate, String name,
    		int quantity, String unit, String shopName) {
        this.purchaseDate = purchaseDate;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
    }

    public Long getId() { return id; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
}

/*
 * <重要>
 *
 * 【このクラスは何をしている？】
 *
 * 購入履歴(history)を1件分保持するEntityクラス。
 *
 * 商品名、購入日、個数、店舗名などを保持し、
 * Hibernate(JPA)によってDBのテーブルと対応付けられる。
 *
 *
 * 【最低限どこを見る？】
 *
 * @Entity
 *
 * ↓
 *
 * DBテーブル対応クラスのタグ
 *
 *
 * @Id
 * @GeneratedValue(...)
 * private Long id;
 *
 * ↓
 *
 * 主キー
 * ＋
 * 自動採番
 *
 *
 * HistoryItem(...)
 *
 * ↓
 *
 * 購入履歴データを作成するコンストラクタ
 *
 *
 * 【ざっくり流れ】
 *
 * HistoryItem作成
 * ↓
 * Repository.save(...)
 * ↓
 * Hibernate
 * ↓
 * DBへ保存
 *
 *
 * DBから取得
 * ↓
 * Hibernate
 * ↓
 * HistoryItem生成
 *
 *
 * 【覚えておけば十分なこと】
 *
 * ・購入履歴1件分を表すクラス
 *
 * ・@Entity は
 *   「DB対応クラス」のタグ
 *
 * ・id は主キーで自動採番
 *
 * ・フィールドが
 *   テーブルの列になる
 *
 * ・細かい補足を忘れても、
 *   「購入履歴を保存するEntity」
 *   と分かればOK
 */
/*
 * <補足1>
 * @Entity
 *
 * 「このクラスはDBのテーブルと対応するクラスですよ」
 * という目印。
 *
 * Spring Boot起動時にHibernate(JPA)が検知し、
 * テーブル情報として管理する。
 *
 * 例：
 *
 * @Entity
 * public class HistoryItem {
 * }
 *
 * ↓
 *
 * Hibernate
 * 「HistoryItemテーブルとして扱おう」
 *
 * のように認識する。
 */
/*
 * <補足2>
 * @Id
 *
 * 「このフィールドが主キーですよ」
 * という目印。
 *
 * 主キーは、
 * テーブル内のデータを一意に識別するための列。
 *
 * 例：
 *
 * @Id
 * private Long id;
 *
 * ↓
 *
 * Hibernate
 * 「id列が主キーだな」
 *
 * と認識する。
 */
/*
 * <補足3>
 * @GeneratedValue(strategy = GenerationType.IDENTITY)
 *
 * idの値を自動採番する設定。
 *
 * save()するたびに、
 * DBが連番を割り振る。
 *
 * 例：
 *
 * 1件目 → id=1
 * 2件目 → id=2
 * 3件目 → id=3
 *
 * のようになる。
 *
 * GenerationType.IDENTITY は、
 * DB側のAUTO_INCREMENT機能などを利用して
 * 採番する方式。
 */

/*
 * <補足4>
 * protected HistoryItem() {}
 *
 * JPA(Hibernate)がDBからデータを復元する時に
 * 使用するコンストラクタ。
 *
 * 開発者が直接使うためではなく、
 * Hibernate用の入口。
 *
 * Entityクラスには
 * 引数なしコンストラクタが必要になる。
 */

/*
 * <補足5>
 * private LocalDate purchaseDate;
 *
 * 購入日を保持するフィールド。
 *
 * HibernateはEntityのフィールドを見て
 * テーブルの列を作成する。
 *
 * この場合はDATE型の列として管理される。
 *
 * name
 * quantity
 * unit
 * shopName
 *
 * についても同様に、
 * Hibernateが対応する列を作成する。
 */

/*
 * <補足6>
 * アノテーション(@)は、
 * 基本的に「直後に書かれている要素」に対して付与される。
 *
 * そのため、
 *
 * @Entity
 * public class HistoryItem
 *
 * は、
 *
 * HistoryItemクラス
 *
 * に対して付いている。
 *
 *
 * @Bean
 * CommandLineRunner loadData(...)
 *
 * は、
 *
 * loadData()メソッド
 *
 * に対して付いている。
 *
 *
 * @Id
 * @GeneratedValue(strategy = GenerationType.IDENTITY)
 * private Long id;
 *
 * は、
 *
 * idフィールド
 *
 * に対して付いている。
 *
 * 今回のidには、
 *
 * ・@Id
 *   → 主キー
 *
 * ・@GeneratedValue
 *   → 自動採番
 *
 * の2つの情報が付与されている。
 *
 *
 * イメージ：
 *
 * @Id
 * ↓
 * @GeneratedValue(...)
 * ↓
 * private Long id;
 *
 * ↓
 *
 * idフィールドに
 * 複数のアノテーションが付いている状態。
 *
 *
 * アノテーションを見る時は、
 *
 * 「この@は何に付いているのか？」
 *
 * を確認すると理解しやすい。
 *
 * ・クラスに付いている
 *   → クラス全体への指示
 *
 * ・メソッドに付いている
 *   → メソッドへの指示
 *
 * ・フィールドに付いている
 *   → フィールドへの指示
 *
 * という見方ができる。
 */