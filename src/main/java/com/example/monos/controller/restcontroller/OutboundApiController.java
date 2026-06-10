package com.example.monos.controller.restcontroller;

import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ApiResponse;
import com.example.monos.form.OutboundScheduleInputForm;
import com.example.monos.form.OutboundScheduleSaveDto;
import com.example.monos.service.OutboundScheduleService;

import jakarta.validation.Valid;

/**
 * <p>出庫関連の非同期処理を担当する。</p>
 * @author t.ueta
 */
@RestController
@RequestMapping("/api")
public class OutboundApiController {
    private final MessageSource messageSource;
    private final OutboundScheduleService outboundScheduleService;

    public OutboundApiController(MessageSource messageSource, OutboundScheduleService outboundScheduleService) {
        this.messageSource = messageSource;
        this.outboundScheduleService = outboundScheduleService;
    }


    @PostMapping("/outbound-schedules/save")
    public ApiResponse<?> save(@AuthenticationPrincipal UserDetailsImpl signinUser,
                               @Valid @RequestBody OutboundScheduleInputForm form,
                               BindingResult result){
        
        if (result.hasErrors()) {
            return ApiResponse.validationError(result);
        }

        var saveDto = new OutboundScheduleSaveDto();
        saveDto.setCompanyId(signinUser.getCompanyId());
        saveDto.setOutboundScheduleId(form.getOutboundScheduleId());
        saveDto.setOutboundScheduleVersion(form.getOutboundScheduleVersion());
        saveDto.setInventoryId(form.getInventoryId());
        saveDto.setInventoryVersion(form.getInventoryVersion());
        saveDto.setScheduleQty(form.getScheduleQty());
        saveDto.setScheduleDate(form.getScheduleDate());

        String resultMessage = outboundScheduleService.save(saveDto);
                            
        return ApiResponse.successMessage(resultMessage);
    }
}
