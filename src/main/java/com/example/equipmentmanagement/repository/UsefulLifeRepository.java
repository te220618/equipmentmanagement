package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.entity.UsefulLife;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 耐用年数リポジトリインターフェース
 * 
 * 耐用年数エンティティに対するデータアクセス操作を提供します。
 * Spring Data JPAのJpaRepositoryを継承することで、
 * 基本的なCRUD操作が自動的に実装されます。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public interface UsefulLifeRepository extends JpaRepository<UsefulLife, Integer> {
    /**
     * サブカテゴリーIDに基づいて耐用年数を検索します
     * 
     * @param subcategoryId サブカテゴリーID
     * @return 該当する耐用年数エンティティ（見つからない場合はEmpty）
     */
    Optional<UsefulLife> findBySubcategoryId(Integer subcategoryId);
} 