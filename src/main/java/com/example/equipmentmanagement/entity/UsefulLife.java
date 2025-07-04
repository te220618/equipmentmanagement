package com.example.equipmentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 耐用年数エンティティクラス
 * 
 * サブカテゴリーごとの法定耐用年数を管理するエンティティです。
 * 減価償却計算の基準となる重要な情報を提供します。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@Table(name = "useful_life")
public class UsefulLife {

    /** ID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** サブカテゴリーID（外部キー） */
    @Column(name = "subcategory_id")
    private Integer subcategoryId;
    
    /** 耐用年数 */
    @Column(name = "useful_years")
    private Integer usefulYears;
} 