package com.example.monos.controller.restcontroller;

import java.util.Locale;
import java.util.Optional;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.ApiResponse;
import com.example.monos.dto.OutboundScheduleDetailDto;
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

    /**
     * 指定された出庫予定IDの出庫予定情報を取得する。
     * @param signinUser サインインユーザー
     * @param outboundScheduleId 出庫予定ID
     * @return 出庫予定情報を含むAPIレスポンス。
     * <p>該当データが存在しない場合はエラーメッセージを返却する。</p>
     */
    @GetMapping("/outbound-schedules/{outboundScheduleId}")
    public ApiResponse<?> get(@AuthenticationPrincipal UserDetailsImpl signinUser,
                              @PathVariable Integer outboundScheduleId){
        
        Optional<OutboundScheduleDetailDto> outboundSchedule = 
            outboundScheduleService.findById(outboundScheduleId, signinUser.getCompanyId());

        if (outboundSchedule.isPresent()) {
            return ApiResponse.successData(outboundSchedule.get());
        } else {
            return ApiResponse.errorMessage(
                    messageSource.getMessage("dataNotExists", new String[] {}, LocaleContextHolder.getLocale()));
        }
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
