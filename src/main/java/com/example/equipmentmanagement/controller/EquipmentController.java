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
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;

/**
 * 設備管理コントローラークラス
 * 
 * 設備管理システムのWebコントローラーです。
 * 設備の一覧表示、登録、編集の画面処理とHTTPリクエストの処理を行います。
 * 
 * 主な機能：
 * - 設備一覧表示（減価償却計算込み）
 * - 設備の新規登録
 * - 設備の編集・更新
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

    /** 設備寿命リポジトリ */
    @Autowired
    private EquipmentLifespanRepository equipmentLifespanRepository;

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
    
    /** 耐用年数リポジトリ */
    @Autowired
    private UsefulLifeRepository usefulLifeRepository;

    /**
     * ルートパスへのアクセスを設備一覧にリダイレクト
     * 
     * @return 設備一覧へのリダイレクト
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/equipment/list";
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
                dto.setDepreciationStatus("不明");
                dto.setAnnualDepreciation(0.0);
                dto.setBookValue(e.getCost());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    /**
     * 設備登録フォーム表示
     * 
     * 設備登録画面を表示します。
     * 
     * @param model SpringのModelオブジェクト
     * @return 設備登録画面のテンプレート名
     */
    @GetMapping("/equipment/create-form")
    public String showCreateForm(Model model) {
        // 新しい設備エンティティを作成
        Equipment equipment = new Equipment();
        
        // カテゴリーとサブカテゴリーのオプションを取得
        List<Category> categories = categoryRepository.findAll();
        
        // モデルに追加
        model.addAttribute("equipment", equipment);
        model.addAttribute("categories", categories);
        model.addAttribute("locations", locationRepository.findAll());
        
        return "equipment_create";
    }

    /**
     * 設備登録処理
     * 
     * 新しい設備を登録します。
     * 
     * @param year 購入年
     * @param month 購入月
     * @param day 購入日
     * @param usageYear 使用期限年（オプション）
     * @param usageMonth 使用期限月（オプション）
     * @param usageDay 使用期限日（オプション）
     * @param equipment 登録する設備エンティティ
     * @return 設備一覧へのリダイレクト
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

        // 購入日をセット
        equipment.setPurchaseDate(LocalDate.of(year, month, day));
        
        // 使用期限をセット（値がある場合のみ）
        if (usageYear != null && usageMonth != null && usageDay != null) {
            equipment.setUsageDeadline(LocalDate.of(usageYear, usageMonth, usageDay));
        }
        
        // 設備を保存
        equipmentRepository.save(equipment);
        
        return "redirect:/equipment/list";
    }

    /**
     * 設備編集フォーム表示
     * 
     * 指定されたIDの設備編集画面を表示します。
     * 
     * @param id 編集する設備のID
     * @param model SpringのModelオブジェクト
     * @return 設備編集画面のテンプレート名
     */
    @GetMapping("/equipment/edit")
    public String editEquipment(@RequestParam("id") Integer id, Model model) {
        // 指定されたIDの設備を取得
        Equipment equipment = equipmentRepository.findById(id).orElse(null);
        
        // 設備が見つからない場合は一覧に戻る
        if (equipment == null) {
            return "redirect:/equipment/list";
        }
        
        // カテゴリーとサブカテゴリーのオプションを取得
        List<Category> categories = categoryRepository.findAll();
        
        // モデルに追加
        model.addAttribute("equipment", equipment);
        model.addAttribute("categories", categories);
        model.addAttribute("locations", locationRepository.findAll());
        
        return "equipment_edit";
    }

    /**
     * 設備更新処理
     * 
     * 設備情報を更新します。
     * 
     * @param year 購入年
     * @param month 購入月
     * @param day 購入日
     * @param usageYear 使用期限年（オプション）
     * @param usageMonth 使用期限月（オプション）
     * @param usageDay 使用期限日（オプション）
     * @param equipment 更新する設備エンティティ
     * @return 設備一覧へのリダイレクト
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

        // 購入日をセット
        equipment.setPurchaseDate(LocalDate.of(year, month, day));
        
        // 使用期限をセット（値がある場合のみ）
        if (usageYear != null && usageMonth != null && usageDay != null) {
            equipment.setUsageDeadline(LocalDate.of(usageYear, usageMonth, usageDay));
        } else {
            equipment.setUsageDeadline(null);
        }
        
        // 設備を保存
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
     * 設置場所コードをラベル（表示用名称）に変換
     * 
     * @param code 設置場所コード
     * @return 設置場所名（コードが見つからない場合は「不明」）
     */
    private String convertLocationCodeToLabel(String code) {
        if (code == null || code.isEmpty()) {
            return "未設定";
        }
        
        try {
            Integer locationId = Integer.parseInt(code);
            return locationRepository.findById(locationId)
                .map(Location::getName)
                .orElse("不明");
        } catch (NumberFormatException e) {
            return "不明";
        }
    }
}