package com.example.equipmentmanagement.dto;

import java.time.LocalDate;

/**
 * 設備データ転送オブジェクト（DTO）クラス
 * 
 * このクラスは設備エンティティの情報を画面表示用に変換するためのDTOです。
 * エンティティの基本情報に加え、以下の計算済みフィールドを含みます：
 * - 減価償却関連の計算値
 * - 表示用のラベル情報
 * - 使用年数等の派生データ
 * 
 * 主な用途：
 * - 設備一覧画面での表示
 * - 設備詳細画面での表示
 * - JSON API レスポンス
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public class EquipmentDto {
    /** 設備ID */
    private Integer id;
    /** 管理番号 */
    private String managementNumber;
    /** メインカテゴリーコード */
    private String categoryCode;
    /** サブカテゴリーコード */
    private String itemCode;
    /** 品名 */
    private String name;
    /** 購入価格 */
    private Double cost;
    /** 購入日 */
    private LocalDate purchaseDate;
    /** 数量 */
    private Integer quantity;
    /** 設置場所コード */
    private String locationCode;

    /** 耐用年数 */
    private Integer lifespanYears;
    /** 経過年数 */
    private Integer elapsedYears;
    /** 年間減価償却額 */
    private Double annualDepreciation;
    /** 帳簿価額 */
    private Double bookValue;

    // 追加項目
    /** 型番 */
    private String modelNumber;
    /** メーカー名 */
    private String manufacturer;
    /** 仕様 */
    private String specification;
    /** 減価償却状況（例：「終了」） */
    private String depreciationStatus;
    /** 使用不能フラグ */
    private Boolean isBroken;
    /** 貸出可能フラグ */
    private Boolean isAvailableForLoan;
    /** 設置場所表示用ラベル */
    private String locationLabel;
    /** 使用期限 */
    private LocalDate usageDeadline;
    /** カテゴリー名（表示用） */
    private String categoryName;
    /** サブカテゴリー名（表示用） */
    private String subcategoryName;

    // ========== Getter/Setter メソッド ==========

    /** IDのゲッター */
    public Integer getId() { return id; }
    /** IDのセッター */
    public void setId(Integer id) { this.id = id; }

    /** 管理番号のゲッター */
    public String getManagementNumber() { return managementNumber; }
    /** 管理番号のセッター */
    public void setManagementNumber(String managementNumber) { this.managementNumber = managementNumber; }

    /** メインカテゴリーコードのゲッター */
    public String getCategoryCode() { return categoryCode; }
    /** メインカテゴリーコードのセッター */
    public void setCategoryCode(String categoryCode) { this.categoryCode = categoryCode; }

    /** サブカテゴリーコードのゲッター */
    public String getItemCode() { return itemCode; }
    /** サブカテゴリーコードのセッター */
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    /** 品名のゲッター */
    public String getName() { return name; }
    /** 品名のセッター */
    public void setName(String name) { this.name = name; }

    /** 購入価格のゲッター */
    public Double getCost() { return cost; }
    /** 購入価格のセッター */
    public void setCost(Double cost) { this.cost = cost; }

    /** 購入日のゲッター */
    public LocalDate getPurchaseDate() { return purchaseDate; }
    /** 購入日のセッター */
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    /** 数量のゲッター */
    public Integer getQuantity() { return quantity; }
    /** 数量のセッター */
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    /** 設置場所コードのゲッター */
    public String getLocationCode() { return locationCode; }
    /** 設置場所コードのセッター */
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }

    /** 耐用年数のゲッター */
    public Integer getLifespanYears() { return lifespanYears; }
    /** 耐用年数のセッター */
    public void setLifespanYears(Integer lifespanYears) { this.lifespanYears = lifespanYears; }

    /** 経過年数のゲッター */
    public Integer getElapsedYears() { return elapsedYears; }
    /** 経過年数のセッター */
    public void setElapsedYears(Integer elapsedYears) { this.elapsedYears = elapsedYears; }

    /** 年間減価償却額のゲッター */
    public Double getAnnualDepreciation() { return annualDepreciation; }
    /** 年間減価償却額のセッター */
    public void setAnnualDepreciation(Double annualDepreciation) { this.annualDepreciation = annualDepreciation; }

    /** 帳簿価額のゲッター */
    public Double getBookValue() { return bookValue; }
    /** 帳簿価額のセッター */
    public void setBookValue(Double bookValue) { this.bookValue = bookValue; }

    // 追加分のgetter/setter

    /** 型番のゲッター */
    public String getModelNumber() { return modelNumber; }
    /** 型番のセッター */
    public void setModelNumber(String modelNumber) { this.modelNumber = modelNumber; }

    /** メーカー名のゲッター */
    public String getManufacturer() { return manufacturer; }
    /** メーカー名のセッター */
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    /** 仕様のゲッター */
    public String getSpecification() { return specification; }
    /** 仕様のセッター */
    public void setSpecification(String specification) { this.specification = specification; }

    /** 減価償却状況のゲッター */
    public String getDepreciationStatus() { return depreciationStatus; }
    /** 減価償却状況のセッター */
    public void setDepreciationStatus(String depreciationStatus) { this.depreciationStatus = depreciationStatus; }

    /** 使用不能フラグのゲッター */
    public Boolean getIsBroken() { return isBroken; }
    /** 使用不能フラグのセッター */
    public void setIsBroken(Boolean isBroken) { this.isBroken = isBroken; }

    /** 貸出可能フラグのゲッター */
    public Boolean getIsAvailableForLoan() { return isAvailableForLoan; }
    /** 貸出可能フラグのセッター */
    public void setIsAvailableForLoan(Boolean isAvailableForLoan) { this.isAvailableForLoan = isAvailableForLoan; }

    /** 設置場所表示用ラベルのゲッター */
    public String getLocationLabel() { return locationLabel; }
    /** 設置場所表示用ラベルのセッター */
    public void setLocationLabel(String locationLabel) { this.locationLabel = locationLabel; }

    /** 使用期限のゲッター */
    public LocalDate getUsageDeadline() { return usageDeadline; }
    /** 使用期限のセッター */
    public void setUsageDeadline(LocalDate usageDeadline) { this.usageDeadline = usageDeadline; }

    /** カテゴリー名のゲッター */
    public String getCategoryName() { return categoryName; }
    /** カテゴリー名のセッター */
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    /** サブカテゴリー名のゲッター */
    public String getSubcategoryName() { return subcategoryName; }
    /** サブカテゴリー名のセッター */
    public void setSubcategoryName(String subcategoryName) { this.subcategoryName = subcategoryName; }

    /**
     * カテゴリーオプション内部クラス
     * プルダウンメニュー等でカテゴリー選択に使用
     * 
     * @deprecated この内部クラスは使用されていません。
     *             com.example.equipmentmanagement.dto.CategoryOption を使用してください。
     */
    public class CategoryOption {
        /** カテゴリーコード */
        private String code;
        /** カテゴリー表示名 */
        private String label;
    
        /**
         * コンストラクタ
         * @param code カテゴリーコード
         * @param label カテゴリー表示名
         */
        public CategoryOption(String code, String label) {
            this.code = code;
            this.label = label;
        }
    
        /**
         * カテゴリーコードのゲッター
         * @return カテゴリーコード
         */
        public String getCode() {
            return code;
        }
    
        /**
         * カテゴリー表示名のゲッター
         * @return カテゴリー表示名
         */
        public String getLabel() {
            return label;
        }
    }
}

