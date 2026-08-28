package com.example.monos.controller.restcontroller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.monos.domain.UserDetailsImpl;
import com.example.monos.dto.AvaliableInventoryDto;
import com.example.monos.dto.ApiResponse;
import com.example.monos.dto.InventoryDetailDto;
import com.example.monos.domain.Inventory;
import com.example.monos.form.InventoryUpdateForm;
import com.example.monos.service.InventoryService;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/inventories")
public class InventoryApiController {
    private final InventoryService inventoryService;
    private final MessageSource messageSource;

    public InventoryApiController(InventoryService inventoryService, MessageSource messageSource) {
        this.inventoryService = inventoryService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{inventoryId}")
    public ApiResponse<InventoryDetailDto> get(@AuthenticationPrincipal UserDetailsImpl signinUser,
                                                @PathVariable int inventoryId) {
        InventoryDetailDto inventory = inventoryService.findById(inventoryId, signinUser.getCompanyId());
        return inventory == null ? ApiResponse.errorMessage(messageSource.getMessage("dataNotExists", null, java.util.Locale.JAPAN))
                                 : ApiResponse.successData(inventory);
    }

    @PostMapping("/save")
    public ApiResponse<?> save(@AuthenticationPrincipal UserDetailsImpl signinUser,
                               @Valid @RequestBody InventoryUpdateForm form, BindingResult result) {
        if (result.hasErrors()) {
            return ApiResponse.validationError(result);
        }
        Inventory inventory = new Inventory();
        inventory.setInventoryId(form.getInventoryId());
        inventory.setCompanyId(signinUser.getCompanyId());
        inventory.setVersion(form.getVersion());
        inventory.setReorderPoint(form.getReorderPoint());
        inventory.setAppropriateStockQty(form.getAppropriateStockQty());
        inventoryService.update(inventory);
        return ApiResponse.successMessage(messageSource.getMessage("updateComplete", null, java.util.Locale.JAPAN));
    }

    /**
     * 指定された商品IDの在庫状況を取得する。
     * @param signinUser サインインユーザー
     * @param productId  商品ID
     * @param outboundScheduleId 出庫予定ID（引き当て可能数加算用）
     * @return <p>在庫状況を含むAPIレスポンス。</p>
     * <p>該当データが存在しない場合はエラーメッセージを返却する。</p> 
     */
    @GetMapping("/warehouse-availabilities")
    public List<AvaliableInventoryDto> getavailableInventories(@AuthenticationPrincipal UserDetailsImpl signinUser, 
                                                               @RequestParam int productId,
                                                               @RequestParam(required = false) Integer outboundScheduleId) {

        List<AvaliableInventoryDto> availabilities = 
            inventoryService.getAvailableInventories(signinUser.getCompanyId(), productId, outboundScheduleId);
        
        return availabilities;
    }
}
