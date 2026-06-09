FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/shopmemo-0.0.1-SNAPSHOT.jar"]

# <重要>

#

# 【このDockerfileは何をしている？】

#

# ShopMemoをRender上で動かすための設定。

#

# GitHubから取得したソースコードをビルドし、

# Spring Bootアプリを起動する。

#

#

# 【最低限どこを見る？】

#

# FROM

#

# ↓

#

# Java21が入った環境を使う

#

#

# COPY

#

# ↓

#

# ShopMemoのソースコードをコピー

#

#

# RUN ./mvnw package

#

# ↓

#

# jarファイルを作る

#

#

# CMD

#

# ↓

#

# Spring Bootを起動する

#

#

# 【ざっくり流れ】

#

# GitHub

# ↓

# ソースコード取得

# ↓

# Mavenビルド

# ↓

# jar生成

# ↓

# Spring Boot起動

#

#

# 【覚えておけば十分なこと】

#

# ・RenderでShopMemoを動かす設定

#

# ・Mavenでビルドしている

#

# ・最後にjarを起動している

#

# ・細かい補足を忘れても、

# 「Render用の起動設定」

# と分かればOK
