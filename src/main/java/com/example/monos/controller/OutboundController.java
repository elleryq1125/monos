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
import com.example.monos.dto.OutboundScheduleListDto;
import com.example.monos.dto.OutboundScheduleSearchCondition;
import com.example.monos.form.OutboundScheduleSearchForm;
import com.example.monos.service.CodeMasterService;
import com.example.monos.service.OutboundScheduleService;


/**
 * <p>出庫関連画面への遷移を担当する。</p>
 * @author t.ueta
 */
@Controller
public class OutboundController {
    private final OutboundScheduleService outboundScheduleService;
    private final CodeMasterService codeMasterService;
    
    public OutboundController(OutboundScheduleService outboundScheduleService, CodeMasterService codeMasterService) {
    	this.outboundScheduleService = outboundScheduleService;
    	this.codeMasterService = codeMasterService;
    }

    /**
     * <p>出庫予定一覧画面を表示する。</p>
     * @return /outbounds/outbound-schedules.html
     */
    @GetMapping("/outbound-schedules")
    public String showOutboundSchedules(@AuthenticationPrincipal UserDetailsImpl signinUser,
    									@ModelAttribute OutboundScheduleSearchForm form,
    									Model model) {
    	
    	// 入力条件を検索用DTOに設定
    	var condition = new OutboundScheduleSearchCondition();
    	condition.setCompanyId(signinUser.getCompanyId());
    	condition.setProductCode(form.getProductCode());
    	condition.setProductName(form.getProductName());
    	condition.setWarehouseCode(form.getWarehouseCode());
    	condition.setWarehouseName(form.getWarehouseName());
    	condition.setStatus(form.getStatusValue());
    	
        List<OutboundScheduleListDto> outboundSchedules = outboundScheduleService.search(condition);
        model.addAttribute("outboundschedules", outboundSchedules);
    	
    	setSearchStatusSelectionValues(model);
        
        return "/outbounds/outbound-schedules";
    }
    
    /**
     * 検索項目「状態」に値を設定
     * @param model ビューに渡すモデル
     */
    private void setSearchStatusSelectionValues(Model model) {
    	List<CodeMaster> statuses = codeMasterService.findByCodeType(Const.CODE_TYPE_OUTBOUND_STATUS, true);
    	model.addAttribute("statuses", statuses);
    }
}
