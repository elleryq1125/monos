package com.example.monos.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.monos.common.Const;
import com.example.monos.domain.CodeMaster;
import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.domain.Warehouse;
import com.example.monos.dto.InboundResultListDto;
import com.example.monos.dto.InboundResultSearchCondition;
import com.example.monos.dto.InboundScheduleListDto;
import com.example.monos.dto.InboundScheduleSearchCondition;
import com.example.monos.form.InboundResultSearchForm;
import com.example.monos.form.InboundScheduleSearchForm;
import com.example.monos.service.CodeMasterService;
import com.example.monos.service.InboundResultService;
import com.example.monos.service.InboundScheduleService;
import com.example.monos.service.WarehouseService;


/**
 * <p>入庫関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
public class InboundController {
    private final InboundScheduleService inboundScheduleService;
    private final InboundResultService inboundResultService;
    private final WarehouseService warehouseService;
    private final CodeMasterService codeMasterService;
    
    public InboundController(InboundScheduleService inboundScheduleService, InboundResultService inboundResultService,
                             WarehouseService warehouseService, CodeMasterService codeMasterService) {
        this.inboundScheduleService = inboundScheduleService;
        this.inboundResultService = inboundResultService;
        this.warehouseService = warehouseService;
        this.codeMasterService = codeMasterService;
    }

    /**
     * <p>入庫実績一覧画面を表示する。</p>
     * @return /inbounds/inbound-results.html
     */
    @GetMapping("/inbound-results")
    public String showInboundResults(@AuthenticationPrincipal UserDetailsImpl signinUser,
                                     @ModelAttribute InboundResultSearchForm form,
                                     Model model) {
        var condition = new InboundResultSearchCondition();
        condition.setCompanyId(signinUser.getCompanyId());
        condition.setProductCode(form.getProductCode());
        condition.setProductName(form.getProductName());
        condition.setWarehouseCode(form.getWarehouseCode());
        condition.setWarehouseName(form.getWarehouseName());
        condition.setResultDateFrom(form.getResultDateFrom());
        condition.setResultDateTo(form.getResultDateTo());

        List<InboundResultListDto> inboundResults = inboundResultService.search(condition);
        model.addAttribute("inboundresults", inboundResults);

        return "inbounds/inbound-results";
    }

    /**
     * <p>入庫予定一覧画面を表示する。</p>
     * @return /inbounds/inbound-schedules.html
     */
    @GetMapping("/inboundschedules")
    public String showInboundSchedules(@AuthenticationPrincipal UserDetailsImpl signinUser,
    															@ModelAttribute InboundScheduleSearchForm form,
    															Model model) {
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new InboundScheduleSearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setProductCode(form.getProductCode());
    	condition.setProductName(form.getProductName());
    	condition.setWarehouseCode(form.getWarehouseCode());
    	condition.setWarehouseName(form.getWarehouseName());
    	condition.setStatus(form.getStatusValue());
    	
        List<InboundScheduleListDto> inboundSchedules = inboundScheduleService.search(condition);
        model.addAttribute("inboundschedules", inboundSchedules);
    	
    	setSearchStatusSelectionValues(model);
    	
    	setModalWarehouseSelectionValues(model, signinUser.getCompanyId());
        
        return "inbounds/inbound-schedules";
    }
    
    /**
     * 検索項目「状態」に値を設定
     * @param model ビューに渡すモデル
     */
    private void setSearchStatusSelectionValues(Model model) {
    	List<CodeMaster> statuses = codeMasterService.findByCodeType(Const.CODE_TYPE_INBOUND_STATUS, true);
    	model.addAttribute("statuses", statuses);
    }
    
    /**
     * モーダル項目「倉庫」に値を設定
     * @param model ビューに渡すモデル
     * @param companyId 企業ID
     */
    private void setModalWarehouseSelectionValues(Model model, int companyId) {
    	List<Warehouse> warehouses = warehouseService.findActiveByCompanyId(companyId);
    	model.addAttribute("warehouses", warehouses);
    }
}
