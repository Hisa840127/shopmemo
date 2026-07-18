# ShopMemo

買う物リストと購入履歴を管理するWebアプリです。

スマートフォンで店内で利用することを想定し、買う物の登録、
購入済み処理、購入履歴からの再投入、メモ表示などを行えるようにしました。

## URL

- アプリURL: https://shopmemo-24qi.onrender.com/
- GitHub: https://github.com/Hisa840127/shopmemo

## 使用技術

- Java 21
- HTML/CSS
- JavaScript
- Spring Boot
- Thymeleaf
- PostgreSQL（本番環境）
- H2 Database（ローカル環境）
- Render
- Git / GitHub

## 主な機能

- 買う物の登録
- 買う物リストの表示
- 数量の変更（＋・－ボタン、直接入力）
- 購入済み処理
- まとめて購入処理
- 購入履歴の表示
- 購入履歴からの再投入
- 購入履歴の削除
- メモのモーダル表示

## 工夫した点

- Renderへ公開し、スマートフォンから利用できるようにしました。
- スマートフォンで使いやすいよう、数量変更やメモ表示のUIを改善しました。
- 公開環境ではPostgreSQL、ローカル環境ではH2を利用し、Spring Profilesで環境ごとに設定を分離しました。

## 今後の予定

- ログイン機能
- ユーザーごとのデータ管理
- 購入履歴のページング
- 履歴の並び順変更