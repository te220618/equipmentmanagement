package com.example.equipmentmanagement.controller;

import com.example.equipmentmanagement.dto.CategoryOption;
import com.example.equipmentmanagement.dto.EquipmentDto;
import com.example.equipmentmanagement.entity.*;
import com.example.equipmentmanagement.repository.*;
import com.example.equipmentmanagement.service.DepreciationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 設備管理コントローラークラス
 * 
 * 設備管理システムのWebコントローラーです。
 * 設備の一覧表示、登録、編集、削除等の画面処理とHTTPリクエストの処理を行います。
 * 
 * 主な機能：
 * - 設備検索
 * - 設備一覧表示（減価償却計算込み）
 * - 設備の新規登録
 * - 設備の編集・更新
 * - 設備の削除（単体・複数）
 * - 設置場所の変更
 * - カテゴリー選択時の品目動的更新（Ajax）
 * 
 * @author Equipment Management Team
 * @version 1.0
 * @since 2024
 */
@Controller
public class EquipmentController {

    /** 設備リポジトリ */
    @Autowired
    private EquipmentRepository equipmentRepository;

    /** 設備寿命リポジトリ 
    @Autowired
    private EquipmentLifespanRepository equipmentLifespanRepository;*/

    /** 設置場所リポジトリ */
    @Autowired
    private LocationRepository locationRepository;

    /** 減価償却計算サービス */
    @Autowired
    private DepreciationService depreciationService;
    
    /** メインカテゴリーリポジトリ */
    @Autowired
    private CategoryRepository categoryRepository;
    
    /** サブカテゴリーリポジトリ */
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    
    /** 耐用年数リポジトリ 
    @Autowired
    private UsefulLifeRepository usefulLifeRepository;*/

    /**
     * ルートパスへのアクセスを検索画面にリダイレクト
     * 
     * @return 検索画面へのリダイレクト
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/equipment/list";
    }

    /**
     * 検索画面表示
     * 
     * 設備検索画面を表示します。
     * 
     * @param model SpringのModelオブジェクト
     * @return 検索画面のテンプレート名
     */
    @GetMapping("/equipment/search")
    public String showSearchForm(Model model) {
        model.addAttribute("locationOptions", getLocationOptions());
        return "equipment_search";
    }

    /**
     * 検索実行処理
     * 
     * 検索条件に基づいて設備を検索し、結果を一覧表示します。
     * 
     * @param searchType 検索タイプ（location: 設置場所のみ、name: 品名のみ、both: 両方）
     * @param location 設置場所コード（オプショナル）
     * @param name 品名（部分一致、オプショナル）
     * @param model SpringのModelオブジェクト
     * @return 設備一覧画面のテンプレート名
     */
    @GetMapping("/equipment/search/results")
    public String searchEquipment(
            @RequestParam("searchType") String searchType,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "name", required = false) String name,
            Model model) {
        
        List<Equipment> equipments = new ArrayList<>();
        
        // 検索タイプに応じて検索実行
        switch (searchType) {
            case "location":
                if (location != null && !location.isEmpty()) {
                    equipments = equipmentRepository.findByLocationCode(location);
                } else {
                    equipments = equipmentRepository.findAll();
                }
                break;
            case "name":
                if (name != null && !name.isEmpty()) {
                    equipments = equipmentRepository.findByNameContainingIgnoreCase(name);
                } else {
                    equipments = new ArrayList<>();
                }
                break;
            case "both":
                if (location != null && !location.isEmpty() && name != null && !name.isEmpty()) {
                    equipments = equipmentRepository.findByLocationCodeAndNameContainingIgnoreCase(location, name);
                } else if (location != null && !location.isEmpty()) {
                    equipments = equipmentRepository.findByLocationCode(location);
                } else if (name != null && !name.isEmpty()) {
                    equipments = equipmentRepository.findByNameContainingIgnoreCase(name);
                } else {
                    equipments = equipmentRepository.findAll();
                }
                break;
            default:
                equipments = equipmentRepository.findAll();
        }
        
        // エンティティをDTO（表示用）に変換
        List<EquipmentDto> equipmentDtoList = convertToDtoList(equipments);
        
        model.addAttribute("equipments", equipmentDtoList);
        model.addAttribute("searchType", searchType);
        model.addAttribute("searchLocation", location);
        model.addAttribute("searchName", name);
        
        return "equipment_list";
    }

    /**
     * 設備一覧表示
     * 
     * 全設備の一覧を取得し、減価償却計算を行った結果を表示用DTOに変換して画面に渡します。
     * 
     * @param model SpringのModelオブジェクト
     * @return 設備一覧画面のテンプレート名
     */
    @GetMapping("/equipment/list")
    public String getEquipmentList(Model model) {
        List<Equipment> equipments = equipmentRepository.findAll();

        // エンティティをDTO（表示用）に変換
        List<EquipmentDto> equipmentDtoList = convertToDtoList(equipments);

        model.addAttribute("equipments", equipmentDtoList);
        return "equipment_list";
    }

    /**
     * 設備エンティティリストをDTOリストに変換
     * 
     * @param equipments 設備エンティティリスト
     * @return 設備DTOリスト
     */
    private List<EquipmentDto> convertToDtoList(List<Equipment> equipments) {
        return equipments.stream().map(e -> {
            EquipmentDto dto = new EquipmentDto();
            // 基本情報をコピー
            dto.setId(e.getId());
            dto.setManagementNumber(e.getManagementNumber());
            dto.setCategoryCode(e.getCategoryCode());
            dto.setItemCode(e.getItemCode());
            dto.setName(e.getName());
            dto.setModelNumber(e.getModelNumber());
            dto.setManufacturer(e.getManufacturer());
            dto.setSpecification(e.getSpecification());
            dto.setCost(e.getCost());
            dto.setPurchaseDate(e.getPurchaseDate());
            dto.setQuantity(e.getQuantity());
            dto.setLocationCode(e.getLocationCode());
            dto.setIsBroken(e.getIsBroken());
            dto.setIsAvailableForLoan(e.getIsAvailableForLoan());
            dto.setUsageDeadline(e.getUsageDeadline());

            // 設置場所コードを表示用ラベルに変換
            dto.setLocationLabel(convertLocationCodeToLabel(e.getLocationCode()));
            
            // カテゴリー名とサブカテゴリー名を設定
            try {
                if (e.getCategoryCode() != null) {
                    Integer categoryId = Integer.parseInt(e.getCategoryCode());
                    categoryRepository.findById(categoryId).ifPresent(category -> {
                        dto.setCategoryName(category.getName());
                    });
                }
                
                if (e.getItemCode() != null) {
                    Integer subcategoryId = Integer.parseInt(e.getItemCode());
                    subcategoryRepository.findById(subcategoryId).ifPresent(subcategory -> {
                        dto.setSubcategoryName(subcategory.getName());
                    });
                }
            } catch (NumberFormatException ex) {
                // IDの変換に失敗した場合は名前を設定しない
            }

            // 耐用年数を取得
            int lifespan = depreciationService.getLifespanYears(e);
            dto.setLifespanYears(lifespan);

            // 減価償却計算
            if (e.getPurchaseDate() != null && lifespan > 0) {
                int elapsed = Math.min(java.time.Period.between(e.getPurchaseDate(), LocalDate.now()).getYears(),
                        lifespan);
                dto.setElapsedYears(elapsed);

                if (elapsed > lifespan) {
                    // 減価償却完了
                    dto.setDepreciationStatus("終了");
                    dto.setAnnualDepreciation(0.0);
                    dto.setBookValue(0.0);
                } else {
                    // 減価償却継続中
                    double annualDep = depreciationService.calculateAnnualDepreciation(e);
                    dto.setAnnualDepreciation(annualDep);
                    double bookValue = depreciationService.calculateBookValue(e, LocalDate.now());
                    dto.setBookValue(bookValue);
                    dto.setDepreciationStatus(String.format("%.2f", annualDep));
                }
            } else {
                // 購入日や耐用年数が不明な場合
                dto.setElapsedYears(0);
                dto.setAnnualDepreciation(0.0);
                dto.setBookValue(dto.getCost());
                dto.setDepreciationStatus("-");
            }

            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 設備登録フォーム表示
     * 
     * 新規設備登録画面を表示します。
     * カテゴリーオプションと設置場所オプションを取得してフォームに渡します。
     * 
     * @param model SpringのModelオブジェクト
     * @return 設備登録画面のテンプレート名
     */
    @GetMapping("/equipment/create-form")
    public String showCreateForm(Model model) {
        model.addAttribute("equipment", new Equipment());
        model.addAttribute("locations", locationRepository.findAll());
        model.addAttribute("categories", categoryRepository.findAll());

        return "equipment_create";
    }

    /**
     * 設備登録処理
     * 
     * フォームから送信された設備データを保存します。
     * 管理番号を自動生成し、購入日と使用期限を設定します。
     * 
     * @param year 購入年
     * @param month 購入月
     * @param day 購入日
     * @param usageYear 使用期限年（オプショナル）
     * @param usageMonth 使用期限月（オプショナル）
     * @param usageDay 使用期限日（オプショナル）
     * @param equipment 設備エンティティ
     * @return 設備一覧画面へのリダイレクト
     */
    @PostMapping("/equipment/create")
    public String createEquipment(
            @RequestParam("purchaseYear") int year,
            @RequestParam("purchaseMonth") int month,
            @RequestParam("purchaseDay") int day,
            @RequestParam(value = "usageDeadlineYear", required = false) Integer usageYear,
            @RequestParam(value = "usageDeadlineMonth", required = false) Integer usageMonth,
            @RequestParam(value = "usageDeadlineDay", required = false) Integer usageDay,
            @ModelAttribute Equipment equipment) {

        // 購入日を設定
        equipment.setPurchaseDate(LocalDate.of(year, month, day));

        // 使用期限を設定（任意）
        if (usageYear != null && usageMonth != null && usageDay != null) {
            equipment.setUsageDeadline(LocalDate.of(usageYear, usageMonth, usageDay));
        }

        // itemCodeをsubcategoryIdに設定
        if (equipment.getItemCode() != null) {
            try {
                equipment.setSubcategoryId(Integer.parseInt(equipment.getItemCode()));
            } catch (NumberFormatException e) {
                // itemCodeが数値でない場合はnullのまま
            }
        }

        // 管理番号を自動生成
        equipment.setManagementNumber(generateNextManagementNumber());
        equipmentRepository.save(equipment);

        return "redirect:/equipment/list";
    }

    /**
     * 設備編集画面表示
     * 
     * 指定されたIDの設備の編集画面を表示します。
     * 
     * @param id 編集対象の設備ID
     * @param model SpringのModelオブジェクト
     * @return 設備編集画面のテンプレート名
     */
    @GetMapping("/equipment/edit")
    public String editEquipment(@RequestParam("id") Integer id, Model model) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow();
        model.addAttribute("equipmentForm", equipment);
        model.addAttribute("locationOptions", getLocationOptions());

        // メインカテゴリーオプションを取得
        List<CategoryOption> categoryOptions = getCategoryOptionsFromDatabase();
        model.addAttribute("categoryOptions", categoryOptions);

        // データベースからカテゴリとサブカテゴリのマッピングを取得
        Map<String, List<String>> categoryItemMap = getCategoryItemMapFromDatabase();
        model.addAttribute("categoryItemMap", categoryItemMap);

        return "equipment_edit";
    }

    /**
     * 設備更新処理
     * 
     * 編集フォームから送信された設備データで既存の設備を更新します。
     * 
     * @param year 購入年
     * @param month 購入月
     * @param day 購入日
     * @param usageYear 使用期限年（オプショナル）
     * @param usageMonth 使用期限月（オプショナル）
     * @param usageDay 使用期限日（オプショナル）
     * @param equipment 設備エンティティ
     * @return 設備一覧画面へのリダイレクト
     */
    @PostMapping("/equipment/update")
    public String updateEquipment(
            @RequestParam("purchaseYear") int year,
            @RequestParam("purchaseMonth") int month,
            @RequestParam("purchaseDay") int day,
            @RequestParam(value = "usageDeadlineYear", required = false) Integer usageYear,
            @RequestParam(value = "usageDeadlineMonth", required = false) Integer usageMonth,
            @RequestParam(value = "usageDeadlineDay", required = false) Integer usageDay,
            @ModelAttribute Equipment equipment) {

        // 購入日を設定
        equipment.setPurchaseDate(LocalDate.of(year, month, day));

        // 使用期限を設定（任意）
        if (usageYear != null && usageMonth != null && usageDay != null) {
            equipment.setUsageDeadline(LocalDate.of(usageYear, usageMonth, usageDay));
        } else {
            equipment.setUsageDeadline(null);
        }

        // itemCodeをsubcategoryIdに設定
        if (equipment.getItemCode() != null) {
            try {
                equipment.setSubcategoryId(Integer.parseInt(equipment.getItemCode()));
            } catch (NumberFormatException e) {
                // itemCodeが数値でない場合はnullのまま
            }
        }

        equipmentRepository.save(equipment);
        return "redirect:/equipment/list";
    }

    /**
     * 設備削除処理（単体）
     * 
     * 指定されたIDの設備を削除します。
     * 
     * @param id 削除対象の設備ID
     * @return 設備一覧画面へのリダイレクト
     */
    @PostMapping("/equipment/delete")
    public String deleteEquipment(@RequestParam("id") Integer id) {
        equipmentRepository.deleteById(id);
        return "redirect:/equipment/list";
    }

    /**
     * 設置場所変更処理
     * 
     * プルダウンメニューからの即時更新で設備の設置場所を変更します。
     * 
     * @param id 対象設備のID
     * @param locationCode 新しい設置場所コード
     * @return 設備一覧画面へのリダイレクト
     */
    @PostMapping("/equipment/update-location")
    public String updateLocation(@RequestParam("id") Integer id, @RequestParam("locationCode") String locationCode) {
        Equipment equipment = equipmentRepository.findById(id).orElseThrow();
        equipment.setLocationCode(locationCode);
        equipmentRepository.save(equipment);
        return "redirect:/equipment/list";
    }

    /**
     * カテゴリー選択時のサブカテゴリー取得API（Ajax用）
     * 
     * 指定されたカテゴリーIDに対応するサブカテゴリー一覧をJSON形式で返します。
     * 設備登録・編集画面でカテゴリー選択時に動的にサブカテゴリーを更新するために使用されます。
     * 
     * @param categoryId カテゴリーID
     * @return サブカテゴリー一覧（JSON形式）
     */
    @GetMapping("/equipment/api/subcategories/{categoryId}")
    @ResponseBody
    public List<Map<String, String>> getSubcategoriesByCategory(@PathVariable Integer categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId)
                .stream()
                .map(subcategory -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", subcategory.getId().toString());
                    map.put("name", subcategory.getName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * データベースからカテゴリ・サブカテゴリーのマッピングを取得
     * 
     * @return カテゴリーIDをキーとし、サブカテゴリーIDリストを値とするマップ
     */
    private Map<String, List<String>> getCategoryItemMapFromDatabase() {
        List<Category> categories = categoryRepository.findAll();
        Map<String, List<String>> categoryItemMap = new HashMap<>();
        
        for (Category category : categories) {
            List<Subcategory> subcategories = subcategoryRepository.findByCategoryId(category.getId());
            List<String> subcategoryIds = subcategories.stream()
                    .map(subcategory -> subcategory.getId().toString())
                    .collect(Collectors.toList());
            categoryItemMap.put(category.getId().toString(), subcategoryIds);
        }
        
        return categoryItemMap;
    }

    /**
     * メインカテゴリーオプション（ID+名前）を取得
     * 
     * @return メインカテゴリーオプションのリスト
     */
    private List<CategoryOption> getCategoryOptionsFromDatabase() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream()
                .map(category -> new CategoryOption(category.getId().toString(), category.getName()))
                .sorted(Comparator.comparing(CategoryOption::getCode))
                .toList();
    }

    /**
     * 設置場所コードを表示用ラベルに変換
     * 
     * @param code 設置場所コード
     * @return 表示用ラベル
     */
    private String convertLocationCodeToLabel(String code) {
        if (code == null)
            return "不明";
            
        try {
            // 設置場所コードを数値に変換
            Integer locationId = Integer.parseInt(code);
            
            // データベースから設置場所を検索
            return locationRepository.findById(locationId)
                .map(Location::getName)
                .orElse("不明");
        } catch (NumberFormatException e) {
            return "不明";
        }
    }

    /**
     * 設置場所オプション一覧を取得
     * 
     * @return 設置場所のIDと名前のマップ
     */
    private List<String> getLocationOptions() {
        List<Location> locations = locationRepository.findAll();
        
        // 設置場所が空の場合は空のリストを返す
        if (locations.isEmpty()) {
            return new ArrayList<>();
        }
        
        return locations.stream()
            .map(location -> location.getId().toString())
            .collect(Collectors.toList());
    }
    
    /**
     * 管理番号の自動生成
     * 
     * 現在年を使用して「EQ年-連番」形式の管理番号を生成します。
     * 例：EQ2024-0001, EQ2024-0002...
     * 
     * @return 生成された管理番号
     */
    private String generateNextManagementNumber() {/**←-------------------------------ここ */
        int year = Year.now().getValue();
        String prefix = "EQ" + year + "-";

        // 既存の管理番号から最大番号を取得
        List<String> numbers = equipmentRepository.findAll().stream()
                .map(Equipment::getManagementNumber)
                .filter(num -> num != null && num.startsWith(prefix))
                .sorted()
                .toList();

        int nextNumber = 1;
        if (!numbers.isEmpty()) {
            String last = numbers.get(numbers.size() - 1);
            try {
                nextNumber = Integer.parseInt(last.substring(prefix.length())) + 1;
            } catch (NumberFormatException ignored) {
            }
        }

        return prefix + String.format("%04d", nextNumber);
    }

    /**
     * 削除モード画面表示
     * 
     * 複数の設備を選択して削除するための画面を表示します。
     * 
     * @param model SpringのModelオブジェクト
     * @return 削除モード画面のテンプレート名
     */
    @GetMapping("/equipment/delete-mode")
    public String showDeleteMode(Model model) {
        List<Equipment> equipments = equipmentRepository.findAll();

        // 設備一覧表示と同様の処理でDTOに変換
        List<EquipmentDto> equipmentDtoList = convertToDtoList(equipments);

        model.addAttribute("equipments", equipmentDtoList);
        return "equipment_delete";
    }

    /**
     * 複数設備削除処理
     * 
     * チェックボックスで選択された複数の設備を一括削除します。
     * 
     * @param selectedIds 削除対象の設備IDリスト
     * @return 設備一覧画面へのリダイレクト
     */
    @PostMapping("/equipment/delete-multiple")
    public String deleteMultipleEquipment(@RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds) {
        if (selectedIds != null && !selectedIds.isEmpty()) {
            equipmentRepository.deleteAllById(selectedIds);
        }
        return "redirect:/equipment/list";
    }
}