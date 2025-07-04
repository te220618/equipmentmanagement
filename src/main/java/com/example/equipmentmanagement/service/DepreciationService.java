package com.example.equipmentmanagement.service;

import com.example.equipmentmanagement.entity.Equipment;
import com.example.equipmentmanagement.entity.EquipmentLifespan;
import com.example.equipmentmanagement.entity.UsefulLife;
import com.example.equipmentmanagement.repository.EquipmentLifespanRepository;
import com.example.equipmentmanagement.repository.UsefulLifeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

/**
 * 減価償却計算サービスクラス
 * 
 * 設備の減価償却に関する計算を行うサービスクラスです。
 * 定額法による減価償却計算を実装しています。
 * 
 * 主な機能：
 * - 年間減価償却額の計算
 * - 累積減価償却額の計算
 * - 帳簿価額の計算
 * - 設備の耐用年数取得
 * 
 * 計算方式：定額法
 * 年間減価償却額 = 取得価額 ÷ 耐用年数
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Service
public class DepreciationService {

    /** 設備寿命リポジトリ */
    @Autowired
    private EquipmentLifespanRepository lifespanRepository;
    
    /** 耐用年数リポジトリ */
    @Autowired
    private UsefulLifeRepository usefulLifeRepository;

    /**
     * 年間減価償却額を計算します（定額法）
     * 
     * @param equipment 計算対象の設備
     * @return 年間減価償却額（耐用年数が0以下の場合は0を返す）
     */
    public double calculateAnnualDepreciation(Equipment equipment) {
        int lifespan = getLifespanYears(equipment);
        if (lifespan <= 0) return 0;
        return equipment.getCost() / lifespan;
    }

    /**
     * 累積減価償却額を計算します
     * 
     * @param equipment 計算対象の設備
     * @param today 計算基準日
     * @return 累積減価償却額（耐用年数を超えた場合は取得価額まで）
     */
    public double calculateAccumulatedDepreciation(Equipment equipment, LocalDate today) {
        int lifespan = getLifespanYears(equipment);
        if (lifespan <= 0) return 0;
        
        // 購入日がnullの場合は0を返す
        if (equipment.getPurchaseDate() == null) return 0;

        // 購入日から今日までの経過年数を計算
        int yearsUsed = Period.between(equipment.getPurchaseDate(), today).getYears();
        // 耐用年数を超えた場合は耐用年数で制限
        yearsUsed = Math.min(yearsUsed, lifespan);

        return calculateAnnualDepreciation(equipment) * yearsUsed;
    }

    /**
     * 帳簿価額を計算します
     * 
     * @param equipment 計算対象の設備
     * @param today 計算基準日
     * @return 帳簿価額（取得価額 - 累積減価償却額、負の値にはならない）
     */
    public double calculateBookValue(Equipment equipment, LocalDate today) {
        double accumulated = calculateAccumulatedDepreciation(equipment, today);
        double bookValue = equipment.getCost() - accumulated;
        return bookValue < 0 ? 0 : bookValue;
    }

    /**
     * 設備の耐用年数を取得します
     * 
     * 設備のカテゴリーコードと品目コードに基づいて、
     * 耐用年数を取得します。まずUsefulLifeから検索し、
     * 見つからない場合はEquipmentLifespanから検索します。
     * 
     * @param equipment 耐用年数を取得する設備
     * @return 耐用年数（該当するデータが見つからない場合は0）
     */
    public int getLifespanYears(Equipment equipment) {
        // サブカテゴリーIDから耐用年数を取得
        if (equipment.getItemCode() != null) {
            try {
                Integer subcategoryId = Integer.parseInt(equipment.getItemCode());
                Optional<UsefulLife> usefulLife = usefulLifeRepository.findBySubcategoryId(subcategoryId);
                if (usefulLife.isPresent() && usefulLife.get().getUsefulYears() != null) {
                    return usefulLife.get().getUsefulYears();
                }
            } catch (NumberFormatException e) {
                // IDの変換に失敗した場合は次の方法を試みる
            }
        }
        
        // 従来の方法でEquipmentLifespanから取得
        Optional<EquipmentLifespan> lifespanOpt = lifespanRepository
                .findByCategoryCodeAndItemCode(equipment.getCategoryCode(), equipment.getItemCode());
        return lifespanOpt.map(EquipmentLifespan::getLifespanYears).orElse(0);
    }
}
