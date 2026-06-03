package com.ishii.shopmemo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int quantity;
    private String unit;
    private String shopName;
    private String memo;

    protected Item() {} 
    public Item(String name, int quantity, String unit, String shopName,String memo) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.shopName = shopName;
        this.memo = memo;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getShopName() { return shopName; }
    public String getMemo() { return memo; }
}

/*
 * <重要>
 *
 * 【このクラスは何をしている？】
 *
 * 買うものリスト(list)の1件分を保持するEntityクラス。
 *
 * 商品名、個数、店舗名、メモなどを保持し、
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
 * Item(...)
 *
 * ↓
 *
 * 買うものデータを作成するコンストラクタ
 *
 *
 * 【ざっくり流れ】
 *
 * Item作成
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
 * Item生成
 *
 *
 * 【覚えておけば十分なこと】
 *
 * ・買うものリスト1件分を表すクラス
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
 *   「買うものリストを保存するEntity」
 *   と分かればOK
 */

/*
 * <補足1>
 * ItemクラスはEntityクラス。
 *
 * 普通のJavaクラスに、
 *
 * @Entity
 *
 * というタグを付けることで、
 * Hibernate(JPA)がDBテーブルとして扱う。
 *
 * イメージ：
 *
 * Item
 * ↓
 * 買うものリストの1件
 *
 * DB
 * ↓
 * itemテーブルの1行
 */

/*
 * <補足2>
 * このクラスの大部分は普通のJava。
 *
 * 内容としては、
 *
 * ・フィールド定義
 * ・コンストラクタ
 * ・getter
 *
 * が中心。
 *
 * 特殊なのは主に、
 *
 * @Entity
 * @Id
 * @GeneratedValue
 *
 * などのJPA用アノテーション。
 *
 * Entityクラスだから特別というより、
 *
 * 「普通のJavaクラス
 *  ＋ JPA用タグ」
 *
 * と考えると理解しやすい。
 */

/*
 * <補足3>
 * protected Item() {}
 *
 * はHibernate(JPA)が
 * DBからデータを復元する時に使用する
 * 引数なしコンストラクタ。
 *
 * 開発者が直接使うためではなく、
 * Hibernate用の入口。
 */
