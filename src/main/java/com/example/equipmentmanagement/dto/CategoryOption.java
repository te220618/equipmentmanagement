package com.example.equipmentmanagement.dto;

/**
 * カテゴリーオプションクラス
 * 
 * 設備のカテゴリー選択用のデータ転送オブジェクトです。
 * プルダウンメニューやセレクトボックスでの表示に使用されます。
 * 
 * 使用例：
 * - code: "PC", label: "パソコン・周辺機器"
 * - code: "FURNITURE", label: "家具・什器"
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
public class CategoryOption {
    /** カテゴリーコード（内部識別用） */
    private String code;
    /** カテゴリーラベル（画面表示用） */
    private String label;

    /**
     * コンストラクタ
     * 
     * @param code カテゴリーコード
     * @param label カテゴリーラベル
     */
    public CategoryOption(String code, String label) {
        this.code = code;
        this.label = label;
    }

    /**
     * カテゴリーコードを取得
     * 
     * @return カテゴリーコード
     */
    public String getCode() {
        return code;
    }

    /**
     * カテゴリーラベルを取得
     * 
     * @return カテゴリーラベル
     */
    public String getLabel() {
        return label;
    }
}
