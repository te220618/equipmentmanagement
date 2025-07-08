package com.example.equipmentmanagement;

import com.example.equipmentmanagement.entity.*;
import com.example.equipmentmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Year;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * テストデータ生成クラス
 * 
 * アプリケーション起動時に1万件のランダムな設備データを生成します。
 * 本番環境では使用しないでください。
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Component
public class DataGenerator implements CommandLineRunner {

    @Autowired
    private EquipmentRepository equipmentRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    @Autowired
    private LocationRepository locationRepository;
    
    @Value("${app.data.generation.enabled:false}")
    private boolean dataGenerationEnabled;
    
    @Value("${app.data.generation.count:10000}")
    private int dataGenerationCount;

    // ランダムデータ生成用の配列
    private static final String[] MANUFACTURERS = {
        "パナソニック", "シャープ", "ソニー", "日立", "東芝", "三菱電機", "富士通", "NEC", 
        "リコー", "キヤノン", "エプソン", "ブラザー", "HP", "Dell", "Lenovo", "Apple",
        "サムスン", "LG", "オムロン", "横河電機", "アズビル", "山武", "富士電機", "安川電機"
    };
    
    private static final String[] EQUIPMENT_NAMES = {
        // 家具・電気機器
        "デスク", "チェア", "キャビネット", "本棚", "テーブル", "照明器具", "扇風機", "エアコン",
        "冷蔵庫", "電子レンジ", "洗濯機", "掃除機", "テレビ", "DVDプレーヤー", "ブルーレイプレーヤー",
        
        // 事務機器・通信機器
        "パソコン", "プリンター", "スキャナー", "コピー機", "ファックス", "電話機", "携帯電話",
        "タブレット", "プロジェクター", "ホワイトボード", "シュレッダー", "計算機", "タイムレコーダー",
        
        // 時計・試験機器・測定機器
        "デジタル時計", "アナログ時計", "ストップウォッチ", "温度計", "湿度計", "圧力計", "流量計",
        "pH計", "振動計", "騒音計", "照度計", "電力計", "オシロスコープ", "マルチメーター",
        
        // 光学機器・写真製作機器
        "カメラ", "ビデオカメラ", "顕微鏡", "双眼鏡", "望遠鏡", "レンズ", "フィルター", "三脚",
        "ストロボ", "ライトボックス", "スライドプロジェクター", "オーバーヘッドプロジェクター",
        
        // 看板・広告器具
        "看板", "サイン", "バナー", "ポスター", "ディスプレイ", "LED看板", "電光掲示板",
        "案内板", "標識", "ネオンサイン", "デジタルサイネージ",
        
        // 容器・金庫
        "金庫", "キャビネット", "ロッカー", "棚", "ボックス", "コンテナ", "タンク", "ドラム缶",
        "バケツ", "トレー", "ケース", "バッグ", "ファイルボックス",
        
        // 理容・美容機器
        "理容椅子", "美容椅子", "洗面台", "鏡", "ドライヤー", "コーム", "はさみ", "カミソリ",
        "マッサージ機", "エステ機器", "ネイルケア機器", "ヘアケア機器",
        
        // 医療機器
        "血圧計", "体温計", "体重計", "聴診器", "注射器", "点滴台", "ベッド", "車椅子",
        "松葉杖", "包帯", "ガーゼ", "消毒液", "医療用冷蔵庫", "X線装置", "超音波診断装置",
        
        // 娯楽・スポーツ器具
        "テニスラケット", "ゴルフクラブ", "野球バット", "サッカーボール", "バスケットボール",
        "卓球台", "ビリヤード台", "ゲーム機", "楽器", "スピーカー", "マイク", "アンプ"
    };
    
    private static final String[] MODEL_PREFIXES = {
        "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };
    
    private static final String[] SPECIFICATIONS = {
        "高性能モデル", "省エネタイプ", "コンパクトサイズ", "大型モデル", "軽量タイプ", "防水仕様",
        "静音設計", "高精度", "多機能", "シンプル設計", "カスタマイズ可能", "メンテナンスフリー",
        "長寿命", "高効率", "低消費電力", "環境対応", "安全設計", "使いやすい", "高品質"
    };

    @Override
    public void run(String... args) throws Exception {
        if (!dataGenerationEnabled) {
            return;
        }
        
        System.out.println("テストデータ生成を開始します...");
        System.out.println("生成対象件数: " + dataGenerationCount + " 件");
        
        // 既存データ数を確認
        long existingCount = equipmentRepository.count();
        if (existingCount > 0) {
            System.out.println("既存データが " + existingCount + " 件存在します。データ生成をスキップします。");
            return;
        }
        
        // 必要なマスタデータを確認
        List<Category> categories = categoryRepository.findAll();
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        List<Location> locations = locationRepository.findAll();
        
        if (categories.isEmpty() || subcategories.isEmpty() || locations.isEmpty()) {
            System.out.println("マスタデータが不足しています。データ生成をスキップします。");
            return;
        }
        
        // 指定された件数のデータを生成
        int targetCount = dataGenerationCount;
        List<Equipment> equipments = new ArrayList<>();
        
        for (int i = 0; i < targetCount; i++) {
            Equipment equipment = generateRandomEquipment(categories, subcategories, locations, i + 1);
            equipments.add(equipment);
            
            // 1000件ごとに進捗を表示
            if ((i + 1) % 1000 == 0) {
                System.out.println((i + 1) + " 件のデータを生成しました。");
            }
        }
        
        // データベースに一括保存
        equipmentRepository.saveAll(equipments);
        
        System.out.println("テストデータ生成が完了しました。合計 " + targetCount + " 件のデータを生成しました。");
    }
    
    /**
     * ランダムな設備データを生成
     */
    private Equipment generateRandomEquipment(List<Category> categories, List<Subcategory> subcategories, 
                                            List<Location> locations, int sequenceNumber) {
        Equipment equipment = new Equipment();
        
        // カテゴリーとサブカテゴリーをランダムに選択
        Category category = getRandomElement(categories);
        List<Subcategory> categorySubcategories = subcategories.stream()
                .filter(sub -> sub.getCategoryId().equals(category.getId()))
                .toList();
        
        Subcategory subcategory = categorySubcategories.isEmpty() ? 
                getRandomElement(subcategories) : getRandomElement(categorySubcategories);
        
        // 管理番号を生成（カテゴリーコード + 年 + 連番）
        String categoryCode = category.getCode() != null ? category.getCode() : "EQ";
        int year = Year.now().getValue();
        String managementNumber = String.format("%s%d-%04d", categoryCode, year, sequenceNumber);
        
        // 基本情報を設定
        equipment.setManagementNumber(managementNumber);
        equipment.setCategoryCode(category.getId().toString());
        equipment.setItemCode(subcategory.getId().toString());
        equipment.setSubcategoryId(subcategory.getId());
        equipment.setName(getRandomElement(EQUIPMENT_NAMES));
        equipment.setModelNumber(generateModelNumber());
        equipment.setManufacturer(getRandomElement(MANUFACTURERS));
        equipment.setSpecification(getRandomElement(SPECIFICATIONS));
        
        // 価格をランダムに設定（1万円〜1000万円）
        equipment.setCost(ThreadLocalRandom.current().nextDouble(10000, 10000000));
        
        // 購入日をランダムに設定（過去10年以内）
        equipment.setPurchaseDate(generateRandomDate());
        
        // 数量をランダムに設定（1〜10個）
        equipment.setQuantity(ThreadLocalRandom.current().nextInt(1, 11));
        
        // 設置場所をランダムに設定
        Location location = getRandomElement(locations);
        equipment.setLocationCode(location.getId().toString());
        
        // 耐用年数をランダムに設定（3〜15年）
        equipment.setLifespanYears(ThreadLocalRandom.current().nextInt(3, 16));
        
        // 状態フラグをランダムに設定
        equipment.setIsBroken(ThreadLocalRandom.current().nextDouble() < 0.05); // 5%の確率で故障
        equipment.setIsAvailableForLoan(ThreadLocalRandom.current().nextDouble() < 0.3); // 30%の確率で貸出可能
        
        // 使用期限をランダムに設定（20%の確率で設定）
        if (ThreadLocalRandom.current().nextDouble() < 0.2) {
            equipment.setUsageDeadline(generateRandomFutureDate());
        }
        
        return equipment;
    }
    
    /**
     * 配列からランダムな要素を取得
     */
    private <T> T getRandomElement(List<T> list) {
        return list.get(ThreadLocalRandom.current().nextInt(list.size()));
    }
    
    private <T> T getRandomElement(T[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
    
    /**
     * ランダムな型番を生成
     */
    private String generateModelNumber() {
        String prefix = getRandomElement(MODEL_PREFIXES);
        int number = ThreadLocalRandom.current().nextInt(100, 1000);
        return prefix + "-" + number;
    }
    
    /**
     * ランダムな過去の日付を生成（過去10年以内）
     */
    private LocalDate generateRandomDate() {
        LocalDate now = LocalDate.now();
        int daysAgo = ThreadLocalRandom.current().nextInt(1, 3650); // 1日〜10年前
        return now.minusDays(daysAgo);
    }
    
    /**
     * ランダムな将来の日付を生成（1年〜10年後）
     */
    private LocalDate generateRandomFutureDate() {
        LocalDate now = LocalDate.now();
        int daysLater = ThreadLocalRandom.current().nextInt(365, 3650); // 1年〜10年後
        return now.plusDays(daysLater);
    }
} 