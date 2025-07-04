package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * メインカテゴリーリポジトリインターフェース
 * 
 * メインカテゴリーエンティティに対するデータアクセス操作を提供します。
 * Spring Data JPAのJpaRepositoryを継承することで、
 * 基本的なCRUD操作が自動的に実装されます。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    // Spring Data JPAにより基本的なCRUD操作が自動実装される
} 