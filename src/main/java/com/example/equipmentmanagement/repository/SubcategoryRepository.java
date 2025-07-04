package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.entity.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * サブカテゴリーリポジトリインターフェース
 * 
 * サブカテゴリーエンティティに対するデータアクセス操作を提供します。
 * Spring Data JPAのJpaRepositoryを継承することで、
 * 基本的なCRUD操作が自動的に実装されます。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
    /**
     * カテゴリーIDに基づいてサブカテゴリーを検索します
     * 
     * @param categoryId カテゴリーID
     * @return 該当するサブカテゴリーのリスト
     */
    List<Subcategory> findByCategoryId(Integer categoryId);
} 