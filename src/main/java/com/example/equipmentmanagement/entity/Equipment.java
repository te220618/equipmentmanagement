package com.example.equipmentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 設備エンティティクラス
 * 
 * このクラスは設備管理システムの核となるエンティティで、
 * 設備の詳細情報を格納するデータベーステーブル（equipment）にマッピングされます。
 * 
 * 主な機能：
 * - 設備の基本情報（品名、型番、メーカー、仕様など）
 * - 購入情報（購入日、金額、数量）
 * - 設置場所情報
 * - 寿命・使用期限管理
 * - 貸出可能・故障状態管理
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@Table(name = "equipment")
public class Equipment {

    /** 設備の一意識別子（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 管理番号（ユニーク制約あり、例：EQ2024-0001） */
    @Column(name = "management_number", nullable = false, unique = true)
    private String managementNumber;

    /** メインカテゴリーコード（設備の大分類） */
    private String categoryCode;
    
    /** サブカテゴリーコード（設備の小分類） */
    private String itemCode;
    
    /** サブカテゴリーID */
    @Column(name = "subcategory_id")
    private Integer subcategoryId;
    
    /** 品名 */
    private String name;
    
    /** 型番 */
    private String modelNumber;
    
    /** メーカー名 */
    private String manufacturer;
    
    /** 仕様詳細 */
    private String specification;
    
    /** 購入価格 */
    private Double cost;
    
    /** 購入日 */
    private LocalDate purchaseDate;
    
    /** 数量（デフォルト：1） */
    private Integer quantity = 1;
    
    /** 設置場所コード */
    private String locationCode;
    
    /** 耐用年数 */
    private Integer lifespanYears;
    
    /** 使用不能フラグ（故障等、デフォルト：false） */
    private Boolean isBroken = false;
    
    /** 貸出可能フラグ（デフォルト：false） */
    private Boolean isAvailableForLoan = false;
    
    /** 使用期限 */
    private LocalDate usageDeadline;

    /** レコード作成日時（更新不可） */
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    /** レコード最終更新日時 */
    private LocalDateTime updatedAt;

    /**
     * エンティティ保存前の処理
     * 作成日時と更新日時を現在時刻に設定
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    /**
     * エンティティ更新前の処理
     * 更新日時を現在時刻に設定
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
