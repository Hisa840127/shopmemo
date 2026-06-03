package com.ishii.shopmemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ShopmemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopmemoApplication.class, args);
	}

}
/*
 * <重要>
 *
 * 【このクラスは何をしている？】
 *
 * Shopmemoアプリの起動クラス。
 *
 * JVMが main() を実行し、
 * Spring Boot を起動する入口になっている。
 *
 *
 * 【最低限どこを見る？】
 *
 * @SpringBootApplication
 *
 * ↓
 *
 * Spring Bootの起点を示すタグ
 *
 *
 * SpringApplication.run(...)
 *
 * ↓
 *
 * Spring Boot起動命令
 *
 *
 * 【ざっくり流れ】
 *
 * JVM
 * ↓
 * main()
 * ↓
 * SpringApplication.run(...)
 * ↓
 * Spring起動
 * ↓
 * Controller
 * Configuration
 * Entity
 * Repository
 * などを探す
 *
 *
 * 【覚えておけば十分なこと】
 *
 * ・このクラスはアプリの入口
 *
 * ・@SpringBootApplication は
 *   「Spring Bootの起点」のタグ
 *
 * ・SpringApplication.run(...) が
 *   実際の起動命令
 *
 * ・細かい補足を忘れても、
 *   「Springを起動するクラス」
 *   と分かればOK
 */

/*
 * <補足1>
 * @SpringBootApplication
 *
 * Spring Bootアプリケーションの起点になることを示す
 * アノテーション（タグ）。
 *
 * Spring Boot起動時に、
 *
 * 「ここを起点にSpringの世界を作ろう」
 *
 * と判断される。
 *
 * イメージ：
 *
 * @SpringBootApplication
 * public class ShopmemoApplication
 *
 * ↓
 *
 * Spring
 * 「このクラスを起点に調査開始」
 *
 *
 * このクラスと同じパッケージ、
 * およびその配下のパッケージをスキャンする。
 *
 * 例：
 *
 * com.ishii.shopmemo
 * ├─ ShopmemoApplication
 * ├─ controller
 * ├─ repository
 * ├─ model
 * └─ service
 *
 * の場合、
 *
 * controller
 * repository
 * model
 * service
 *
 * などが自動的に調査対象になる。
 *
 *
 * Spring系のアノテーションは、
 * 「タグ」や「目印」と考えると理解しやすい。
 *
 * 例：
 *
 * @Controller
 * → Controllerタグ
 *
 * @Configuration
 * → 設定クラスタグ
 *
 * @Entity
 * → DB対応クラスタグ
 *
 * @SpringBootApplication
 * → Spring Boot起動クラスタグ
 *
 * SpringやHibernateは起動時に
 * これらのタグを探している。
 */