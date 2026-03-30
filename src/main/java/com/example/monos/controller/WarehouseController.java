package com.example.monos.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.domain.Warehouse;
import com.example.monos.dto.WarehouseSearchCondition;
import com.example.monos.form.WarehouseSearchForm;
import com.example.monos.service.WarehouseService;


/**
 * <p>倉庫関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/warehouses")
public class WarehouseController {
    private final WarehouseService warehouseServise;
    
    public WarehouseController(WarehouseService warehouseServise) {
        this.warehouseServise = warehouseServise;
    }

    /**
     * <p>倉庫一覧画面を表示する。</p>
     * @return /products/products.html
     */
    @GetMapping
    public String showWarehouses(@AuthenticationPrincipal UserDetailsImpl signinUser,
    											@ModelAttribute WarehouseSearchForm form,
												Model model) {
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new WarehouseSearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setWarehouseCode(form.getWarehouseCode());
    	condition.setName(form.getName());
    	condition.setActive(form.isActive());
    	
        List<Warehouse> warehouses = warehouseServise.search(condition);
        model.addAttribute("warehouses", warehouses);
        
        return "/warehouses/warehouses";
    }
}
