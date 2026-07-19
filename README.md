# ShopMemo

買う物リストと購入履歴を管理するWebアプリです。

スマートフォンで店内で利用することを想定し、買う物の登録、
購入済み処理、購入履歴からの再投入、メモ表示などを行えるようにしました。

## URL

- アプリURL (Render): https://shopmemo-24qi.onrender.com/
- GitHub (ソースコード): https://github.com/Hisa840127/shopmemo

## デモアカウント

ユーザー名：admin

パスワード：1234

## 使用技術

- Java 21
- HTML/CSS
- JavaScript
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- PostgreSQL（本番環境）
- H2 Database（ローカル環境）
- Render
- Git / GitHub

## 主な機能

- ログイン機能（ユーザーごとのデータ管理）
- 買う物リストの登録・編集・削除
- 数量変更（＋・－ボタン、直接入力）
- 購入処理（単体・まとめて購入）
- 購入履歴の管理（表示・再投入・削除）
- メモのモーダル表示

## 工夫した点

- Renderへ公開し、スマートフォンから利用できるようにしました。
- スマートフォンで使いやすいよう、数量変更やメモ表示のUIを改善しました。
- 公開環境ではPostgreSQL、ローカル環境ではH2を利用し、Spring Profilesで環境ごとに設定を分離しました。
- Spring Securityを利用してログイン機能を実装し、ユーザーごとに買う物リストと購入履歴を管理できるようにしました。
- 公開環境ではログイン用パスワードを環境変数から読み込む構成にし、ソースコードへ公開用パスワードを書かないようにしました。

## 今後の予定

- 購入履歴のページング
- 履歴の並び順変更