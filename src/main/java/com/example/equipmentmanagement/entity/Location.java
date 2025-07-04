package com.example.equipmentmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * 設置場所エンティティクラス
 * 
 * 設備の設置場所を管理するエンティティです。
 * 階層構造（親子関係）を持つ場所管理をサポートします。
 * 
 * 例：
 * - 東京本店（親）
 *   - 1階（子）
 *   - 2階（子）
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Data
@Entity
@Table(name = "location")
public class Location {

    /** 場所ID（主キー） */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 場所名（表示用、例：東京本店、仙台支店等） */
    private String name;

    /** 場所コード */
    private String code;

    /** 親場所コード（階層構造用、オプショナル） */
    @Column(name = "parent_code")
    private String parentCode;
}
