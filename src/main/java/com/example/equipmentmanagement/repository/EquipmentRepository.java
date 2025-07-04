package com.example.equipmentmanagement.repository;

import com.example.equipmentmanagement.entity.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * 設備リポジトリインターフェース
 * 
 * 設備エンティティに対するデータアクセス操作を提供します。
 * Spring Data JPAのJpaRepositoryを継承することで、
 * 基本的なCRUD操作が自動的に実装されます。
 * 
 * 利用可能な操作：
 * - findAll() : 全設備の取得
 * - findById(id) : IDによる設備の取得
 * - save(equipment) : 設備の保存（新規登録・更新）
 * - deleteById(id) : IDによる設備の削除
 * - delete(equipment) : 設備エンティティによる削除
 * - count() : 設備数のカウント
 * - existsById(id) : IDによる設備存在確認
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {
    /**
     * 設置場所コードで設備を検索
     * 
     * @param locationCode 設置場所コード
     * @return 該当する設備のリスト
     */
    List<Equipment> findByLocationCode(String locationCode);
    
    /**
     * 品名の部分一致で設備を検索（大文字小文字を区別しない）
     * 
     * @param name 検索する品名（部分一致）
     * @return 該当する設備のリスト
     */
    List<Equipment> findByNameContainingIgnoreCase(String name);
    
    /**
     * 設置場所コードと品名の部分一致で設備を検索（大文字小文字を区別しない）
     * 
     * @param locationCode 設置場所コード
     * @param name 検索する品名（部分一致）
     * @return 該当する設備のリスト
     */
    List<Equipment> findByLocationCodeAndNameContainingIgnoreCase(String locationCode, String name);
}
