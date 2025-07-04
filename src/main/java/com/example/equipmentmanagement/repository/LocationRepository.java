package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 設置場所リポジトリインターフェース
 * 
 * 設置場所エンティティに対するデータアクセス操作を提供します。
 * Spring Data JPAのJpaRepositoryを継承することで、
 * 基本的なCRUD操作が自動的に実装されます。
 * 
 * 利用可能な操作：
 * - findAll() : 全設置場所の取得
 * - findById(id) : IDによる設置場所の取得
 * - save(location) : 設置場所の保存（新規登録・更新）
 * - deleteById(id) : IDによる設置場所の削除
 * - delete(location) : 設置場所エンティティによる削除
 * - count() : 設置場所数のカウント
 * - existsById(id) : IDによる設置場所存在確認
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public interface LocationRepository extends JpaRepository<Location, Integer> {
    // Spring Data JPAにより基本的なCRUD操作が自動実装される
    // 必要に応じてカスタムクエリメソッドを追加可能
}
