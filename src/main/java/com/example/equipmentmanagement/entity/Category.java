package com.example.equipmentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * メインカテゴリーエンティティクラス
 * 
 * 設備のメインカテゴリー情報を管理するエンティティです。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@Table(name = "category")
public class Category {

    /** カテゴリーID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** カテゴリー名 */
    private String name;
} 