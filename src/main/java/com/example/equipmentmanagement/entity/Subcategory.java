package com.example.equipmentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * サブカテゴリーエンティティクラス
 * 
 * 設備のサブカテゴリー情報を管理するエンティティです。
 * メインカテゴリーに紐づけられます。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@Table(name = "subcategory")
public class Subcategory {

    /** サブカテゴリーID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** サブカテゴリー名 */
    private String name;
    
    /** 親カテゴリーID（外部キー） */
    @Column(name = "category_id")
    private Integer categoryId;
} 