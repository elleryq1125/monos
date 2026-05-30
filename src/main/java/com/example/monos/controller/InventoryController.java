package com.example.monos.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.InventorySearchCondition;
import com.example.monos.form.InventorySearchForm;


/**
 * <p>在庫関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
@RequestMapping("/inventories")
public class InventoryController {

    /**
     * <p>在庫一覧画面を表示する。</p>
     * @return /inventories/inventories.html
     */
	@GetMapping
    public String showInventories(@AuthenticationPrincipal UserDetailsImpl signinUser,
    															@ModelAttribute InventorySearchForm form,
    															Model model) {
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new InventorySearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setProductCode(form.getProductCode());
    	condition.setProductName(form.getProductName());
    	condition.setWarehouseCode(form.getWarehouseCode());
    	condition.setWarehouseName(form.getWarehouseName());
    	condition.setStockOut(form.isStockOut());
    	
//        List<InboundScheduleListDto> inboundSchedules = inboundScheduleService.search(condition);
//        model.addAttribute("inboundschedules", inboundSchedules);
        
        return "/inventories/inventories";
    }
}
