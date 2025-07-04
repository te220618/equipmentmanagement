package com.example.equipmentmanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 設備管理システムのメインアプリケーションクラス
 * 
 * このクラスは設備管理システムのエントリーポイントです。
 * Spring Bootアプリケーションとして動作し、以下の機能を提供します：
 * - 設備の登録、編集、削除、一覧表示
 * - 設備の減価償却計算
 * - 設備の設置場所管理
 * - 設備の寿命管理
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
public class EquipmentManagementApplication {

    /**
     * アプリケーションのメインエントリーポイント
     * Spring Bootアプリケーションを起動します
     * 
     * @param args コマンドライン引数
     */
    public static void main(String[] args) {
        SpringApplication.run(EquipmentManagementApplication.class, args);
    }
}
